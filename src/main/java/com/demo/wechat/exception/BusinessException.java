package com.demo.wechat.exception;

import com.demo.wechat.enums.ResponseCodeEnum;;

/**
 * @Description: 业务异常
 * @Author: KunSpireUp
 * @Date: 3/26/2024 11:25 PM
 */
public class BusinessException extends RuntimeException {

	private ResponseCodeEnum codeEnum;

	private Integer code;

	private String message;

	public BusinessException(String message, Throwable e) {
		super(message, e);
		this.message = message;
	}
	public BusinessException(String message){
		super(message);
        this.message = message;
	}

	public BusinessException(Throwable e) {
		super(e);
	}

	public BusinessException(ResponseCodeEnum codeEnum) {
		super(codeEnum.getMsg());
		this.codeEnum = codeEnum;
		this.code = codeEnum.getCode();
		this.message = codeEnum.getMsg();
	}

	public BusinessException(Integer code, String message){
		super(message);
		this.code = code;
		this.message = message;
	}

	public ResponseCodeEnum getCodeEnum() {
		return codeEnum;
	}

	public Integer getCode(){
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * 重写 fillInStackTrace 业务异常不需要堆栈信息，提高效率
	 */
	@Override
	public Throwable fillInStackTrace(){
		return this;
	}
}