package com.freebe.code.web.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理运行异常
     */
    @ExceptionHandler(Exception.class)
    public ResultBean<?> handleShangChainException(Exception ex) {
    	ex.printStackTrace();
    	if(ex instanceof CustomException) {
    		return ResultBean.error(ex.getMessage());
    	}
    	return ResultBean.error("请求异常");
    }
}
