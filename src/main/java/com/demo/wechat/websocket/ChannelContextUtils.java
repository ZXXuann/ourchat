package com.demo.wechat.websocket;

import cn.hutool.json.JSONUtil;
import cn.hutool.system.UserInfo;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.WsInitData;
import com.demo.wechat.entity.po.*;
import com.demo.wechat.entity.query.*;
import com.demo.wechat.enums.MessageTypeEnum;
import com.demo.wechat.enums.UserContactApplyStatusEnum;
import com.demo.wechat.enums.UserContactTypeEnum;
import com.demo.wechat.mappers.ChatMessageMapper;
import com.demo.wechat.mappers.ChatSessionUserMapper;
import com.demo.wechat.mappers.InfoMapper;
import com.demo.wechat.mappers.UserContactApplyMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.utils.StringTools;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author ZXX
 * @Date 2024/12/23 11:46
 */
@Component
public class ChannelContextUtils {
    @Resource
    private RedisComponent redisComponent;
    //存储用于与其WebSocket连接（channel）的映射关系，键是用户Id，值是channel
    private static final ConcurrentHashMap<String,Channel> USER_CONTEXT_MAP=new ConcurrentHashMap<>();
    //存储WebSocket连接组（ChannelGroup）的映射关系，键是分组的GroupId，值是一个ChannelGroup
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTEXT_MAP=new ConcurrentHashMap<>();
    @Autowired
    private InfoMapper<Info, InfoQuery> userInfoMapper;
    @Autowired
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;
    @Autowired
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;
    @Resource
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;
    public void addContext(String userId, Channel channel){
        //获取当前连接的唯一channelId
        String channelId=channel.id().toString();
        AttributeKey attributeKey=null;
        if(!AttributeKey.exists(channelId)){
            attributeKey=AttributeKey.newInstance(channelId);
        }else{
            attributeKey=AttributeKey.valueOf(channelId);
        }
        //通过AttributeKey来将用户信息与特定的链接绑定在一起
        //AttributeKey是Netty用于标识存储在Channel上的自定义属性的一个对象。
        //它允许你为每个Channel关联一些数据（例如用户Id，会话状态、连接信息等）
        //这些数据可以通过Channel的属性机制进行存储和访问
        //userId被绑定到Channel上，userId会存储在该channel的属性中，并且可以通过AttributeKey在后续处理中访问
        //attributeKey和USER_CONTEXT_MAP在存储userid上有些重叠，但作用不同：
        //USER_CONTEXT_MAP用于全局快速查找和管理userId与channel之间的关系
        //AttributeKey是将userid存储在特定channel上的机制，是channel的一个内部属性
        channel.attr(attributeKey).set(userId);
        //在redis获取所有联系人
        List<String> contactIdList=redisComponent.getUserContactList(userId);
        //循环遍历找出属于群组的联系人,并且将当前channel加入进去
        for(String groupId:contactIdList){
            if(groupId.startsWith(UserContactTypeEnum.GROUP.getPrefix())){
                add2Group(groupId,channel);
            }
        }
        //然后将channel放入USER_CONTEXT_MAP中，方便通过userId查找链接
        USER_CONTEXT_MAP.put(userId,channel);
        //保存心跳
        redisComponent.saveHeartBeat(userId);
        //更新用户最后的链接时间
        Info updateInfo=new Info();
        updateInfo.setLastLoginTime(new Date());
        userInfoMapper.updateByUserId(updateInfo,userId);
        //给用户发送消息,拉取最近的消息
        Info userInfo=userInfoMapper.selectByUserId(userId);
        Date sourceLastOffTime=userInfo.getLastOffTime();
        Long lastOffTime=null;
        if(sourceLastOffTime!=null) {
            lastOffTime=sourceLastOffTime.getTime();
        }
        //查询三天内的信息
        if(sourceLastOffTime!=null&&System.currentTimeMillis()- Constants.MILLISSECONDS_3DAYS_AGO>lastOffTime){
//            lastOffTime=Constants.MILLISSECONDS_3DAYS_AGO;
            lastOffTime=System.currentTimeMillis()- Constants.MILLISSECONDS_3DAYS_AGO;
        }
        //查询用户所有的会话信息
        ChatSessionUserQuery sessionUserQuery=new ChatSessionUserQuery();
        sessionUserQuery.setUserId(userId);
        sessionUserQuery.setOrderBy("last_receive_time desc");
        List<ChatSessionUser> chatSessionUserList=chatSessionUserMapper.selectList(sessionUserQuery);
        WsInitData wsInitData=new WsInitData();
        wsInitData.setChatSessionUserList(chatSessionUserList);
        //查询聊天信息
        List<String> groupIdList=contactIdList.stream().filter(item->item.startsWith(UserContactTypeEnum.GROUP.getPrefix())).collect(Collectors.toList());
        groupIdList.add(userId);
        ChatMessageQuery messageQuery=new ChatMessageQuery();
        messageQuery.setContactIdList(contactIdList);
        messageQuery.setLastReceiveTime(lastOffTime);
        List<ChatMessage> chatMessageList=chatMessageMapper.selectList(messageQuery);
        wsInitData.setChatMessageList(chatMessageList);
        //查询好友申请
        UserContactApplyQuery applyQuery=new UserContactApplyQuery();
        applyQuery.setReceiveUserId(userId);
        applyQuery.setLastApplyTimestamp(lastOffTime);
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        Integer applyCount=userContactApplyMapper.selectCount(applyQuery);
        wsInitData.setApplyCount(applyCount);

        //发送消息
        //---------------------有疑问--------------------------
        MessageSendDto messageSendDto=new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.INIT.getType());
        messageSendDto.setContactId(userId);
        messageSendDto.setExtendData(wsInitData);
        sendMsg(messageSendDto,userId);
    }

    private void add2Group(String groupId,Channel channel){
        //查看当前是否有这个分组
        ChannelGroup group=GROUP_CONTEXT_MAP.get(groupId);
        //如果为空
        if(group==null){
            //新建一个channel群组
            group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            //放入Groupmap中
            GROUP_CONTEXT_MAP.put(groupId,group);
        }
        if(channel==null){
            return;
        }
        group.add(channel);
    }
//    public void send2Group(MessageSendDto messageSendDto){
//        if(StringTools.isEmpty(messageSendDto.getContactId())){
//            return;
//        }
//
//    }
    public void removeContext(Channel channel){
        Attribute<String> attribute=channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId= attribute.get();
        if(!StringTools.isEmpty(userId)){
            USER_CONTEXT_MAP.remove(userId);
        }
        redisComponent.removeUserHeartBeat(userId);
        //更新用户的最后离线时间
        Info userInfo=new Info();
        userInfo.setLastOffTime(new Date());
        userInfoMapper.updateByUserId(userInfo,userId);
    }
    public void addUser2Group(String userId,String groupId){
        Channel channel=USER_CONTEXT_MAP.get(userId);
        add2Group(groupId,channel);
    }
    public void sendMessage(MessageSendDto messageSendDto){
        UserContactTypeEnum contactTypeEnum=UserContactTypeEnum.getByPrefix(messageSendDto.getContactId());
        switch (contactTypeEnum){
            case USER:
                send2User(messageSendDto);
                break;
            case GROUP:
                send2Group(messageSendDto);
                break;
        }
    }
    //发送给用户
    private void send2User(MessageSendDto messageSendDto) {
        String contactId = messageSendDto.getContactId();
        if (StringTools.isEmpty(contactId)) {
            return;
        }
        sendMsg(messageSendDto, contactId);
        //强制下线
        if (MessageTypeEnum.FORCE_OFF_LINE.getType().equals(messageSendDto.getMessageType())) {
            closeContact(contactId);
        }
    }
    public void closeContact(String userId){
        if(StringTools.isEmpty(userId)){
            return;
        }
        redisComponent.cleanUserTokenByUserId(userId);
        Channel channel=USER_CONTEXT_MAP.get(userId);
        if(channel==null){
            return;
        }
        channel.close();
    }
    //发送给群组
    private void send2Group(MessageSendDto messageSendDto){
        if(StringTools.isEmpty(messageSendDto.getContactId())){
            return;
        }
        ChannelGroup channelGroup=GROUP_CONTEXT_MAP.get(messageSendDto.getContactId());
        if(channelGroup==null){
            return;
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageSendDto)));

        //移除群聊
        MessageTypeEnum messageTypeEnum=MessageTypeEnum.getByType(messageSendDto.getMessageType());
        if(messageTypeEnum.LEAVE_GROUP==messageTypeEnum||MessageTypeEnum.REMOVE_GROUP==messageTypeEnum){
            String userId=(String) messageSendDto.getExtendData();
            redisComponent.removeUserContact(userId, messageSendDto.getContactId());
            Channel channel=USER_CONTEXT_MAP.get(userId);
            if(channel==null){
                return;
            }
            channelGroup.remove(channel);
        }
        if(MessageTypeEnum.DISSOLUTION_GROUP==messageTypeEnum){
            GROUP_CONTEXT_MAP.remove(messageSendDto.getContactId());
            channelGroup.close();
        }
    }

    public void sendMsg(MessageSendDto messageSendDto,String receiveId){

        Channel userChannel=USER_CONTEXT_MAP.get(receiveId);
        if(userChannel==null){
            return;
        }
        //相对于客户端而言，联系人就是发送人，所以这里转一下再发送
        if(MessageTypeEnum.ADD_FRIEND_SELF.getType().equals(messageSendDto.getMessageType())){
            Info userInfo=(Info) messageSendDto.getExtendData();
            messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
            messageSendDto.setContactId(userInfo.getUserId());
            messageSendDto.setContactName(userInfo.getNickName());
            messageSendDto.setExtendData(null);
        }else{
            messageSendDto.setContactId(messageSendDto.getSendUserId());
            messageSendDto.setContactName(messageSendDto.getSendUserNickName());
        }

        userChannel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageSendDto)));
    }
}
