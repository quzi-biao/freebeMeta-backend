package com.freebe.code.common;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings({"rawtypes", "unchecked"})
@ApiModel("ResultBean 接口返回对象")
public class ResultBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "错误编码")
    int code;

    @ApiModelProperty(value = "描述")
    String message;

    @ApiModelProperty(value = "返回值")
    T data;

    @ApiModelProperty(value = "true为成功,false 为失败")
    boolean success = true;

    public ResultBean() {
        code = ErrorCode.RET_CODE_OK;
        message = "处理成功";
    }

    public ResultBean(int code) {
        this.code = code;
    }

    public ResultBean(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static ResultBean error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultBean error(String msg) {
        return error(500, msg);
    }

	public static ResultBean error(int code, String msg) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(code);
        resultBean.setMessage(msg);
        return resultBean;
    }

	public static ResultBean error(int code,Object data, String msg) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(code);
        resultBean.setMessage(msg);
        resultBean.setSuccess(false);
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean ok(String msg) {
        ResultBean resultBean = new ResultBean();
        resultBean.setMessage(msg);
        return resultBean;
    }

    public static <T> ResultBean<T> ok(T data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setData(data);
        return resultBean;
    }

    public static <T> ResultBean<T> ok(String msg, T data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setData(data);
        resultBean.setMessage(msg);
        return resultBean;
    }

    public static ResultBean ok() {
        return new ResultBean();
    }
}

