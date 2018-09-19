package com.mr.data.common.exception;


/**
 * @Author: zqzhou
 * @Description:
 * @Date: Created in 2018/9/19 17:16
 */
public class BusiException extends RuntimeException {
	private static final long serialVersionUID = 5251289579956583109L;

	public enum ExceptionType{
		WARN,ERROR;
	}
	private ExceptionType exceptionType;
	private String errorCode;
	private String[] errorParams;
	private Throwable cause;


	public BusiException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = ExceptionType.ERROR;
	}

	public BusiException(String errorCode,String[] errorParams) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = ExceptionType.ERROR;
		this.errorParams = errorParams;
	}

	public BusiException(String errorCode,String[] errorParams,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = ExceptionType.ERROR;;
		this.errorParams = errorParams;
		this.cause = cause;
	}

	public BusiException( String errorCode,String errorParam,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = ExceptionType.ERROR;
		this.errorParams = new String[]{errorParam};
		this.cause = cause;
	}


	public BusiException( String errorCode,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = ExceptionType.ERROR;
		this.cause = cause;
	}

	public BusiException(ExceptionType exceptionType, String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
	}

	public BusiException(ExceptionType exceptionType, String errorCode,String[] errorParams) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.errorParams = errorParams;
	}

	public BusiException(ExceptionType exceptionType, String errorCode,String[] errorParams,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.errorParams = errorParams;
		this.cause = cause;
	}

	public BusiException(ExceptionType exceptionType, String errorCode,String errorParam,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.errorParams = new String[]{errorParam};
		this.cause = cause;
	}


	public BusiException(ExceptionType exceptionType, String errorCode,Throwable cause) {
		super(errorCode);
		this.errorCode = errorCode;
		this.exceptionType = exceptionType;
		this.cause = cause;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getErrorParams() {
		return errorParams;
	}

	public void setErrorParams(String[] errorParams) {
		this.errorParams = errorParams;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}


}
