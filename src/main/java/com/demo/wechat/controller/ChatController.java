package com.demo.wechat.controller;

import cn.hutool.core.util.ArrayUtil;
import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.ChatMessage;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.enums.MessageTypeEnum;
import com.demo.wechat.enums.ResponseCodeEnum;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.service.ChatMessageService;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @Author ZXX
 * @Date 2024/12/28 17:37
 */
@Slf4j
public class ChatController extends ABaseController{
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private ChatSessionUserService chatSessionUserService;
    @Resource
    private AppConfig appConfig;
    @RequestMapping("/sendMessage")
    @GlobalInterceptor
    public ResponseVO sendMessage(HttpServletRequest request, @NotEmpty String contactId,
                                  @NotEmpty @Max(500) String messageContent,
                                  @NotNull Integer messageType,
                                  Long fileSize,
                                  String fileName,
                                  Integer fileType){
        MessageTypeEnum messageTypeEnum=MessageTypeEnum.getByType(messageType);
        if(null==messageTypeEnum||!ArrayUtil.contains(new Integer[]{MessageTypeEnum.CHAT.getType(),
                MessageTypeEnum.MEDIA_CHAT.getType()},messageType)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContactId(contactId);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setFileSize(fileSize);
        chatMessage.setFileName(fileName);
        chatMessage.setFileType(fileType);
        MessageSendDto messageSendDto= chatMessageService.saveMessage(chatMessage,tokenUserInfoDto);
        return getSuccessResponseVO(messageSendDto);
    }

    @RequestMapping("/uploadFile")
    @GlobalInterceptor
    public ResponseVO uploadFile(HttpServletRequest request, @NotNull Long messageId,
                                 @NotNull MultipartFile file,
                                 @NotNull MultipartFile cover){
        TokenUserInfoDto userInfoDto=getTokenUserInfo(request);
        chatMessageService.saveMessageFile(userInfoDto.getUserId(),messageId,file,cover);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/downloadFile")
    @GlobalInterceptor
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                                   @NotEmpty String fileId,@NotNull Boolean showCover){
        TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
        OutputStream out=null;
        FileInputStream in=null;
        try{
            File file=null;
            if(!StringTools.isNumber(fileId)){
                String avatarFolderName= Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
                String avatarPath=appConfig.getProjectFolder()+avatarFolderName+fileId+Constants.IMAGE_SUFFIX;
                if(showCover){
                    avatarPath=avatarPath+Constants.COVER_IMAGE_SUFFIX;
                }
                file=new File(avatarPath);
                if(!file.exists()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }

            }else{
                file=chatMessageService.downloadFile(tokenUserInfoDto,Long.parseLong(fileId),showCover);
            }
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Dispotion","attachment;");
            response.setContentLengthLong(file.length());
            in=new FileInputStream(file);
            byte[] byteData=new byte[1024];
            out=response.getOutputStream();
            int len;
            while((len=in.read(byteData))!=-1){
                out.write(byteData,0,len);
            }
            out.flush();
        }catch (Exception e){
            log.error("下载文件失败",e);
        }finally {
            if(out!=null){
                try{
                    out.close();
                }catch (Exception e){
                    log.error("IO异常",e);
                }
            }
            if(in!=null){
                try{
                    in.close();
                }catch (Exception e){
                    log.error("IO异常",e);
                }
            }
        }

    }
}
