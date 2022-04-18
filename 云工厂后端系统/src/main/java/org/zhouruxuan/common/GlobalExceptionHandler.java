package org.zhouruxuan.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public R error(Exception e){
		e.printStackTrace();
		return R.error();
	}

	@ExceptionHandler(MyExcption.class)
	@ResponseBody
	public R error(MyExcption e){
		e.printStackTrace();
		return R.error().message(e.getMsg()).code(e.getCode());
	}
}