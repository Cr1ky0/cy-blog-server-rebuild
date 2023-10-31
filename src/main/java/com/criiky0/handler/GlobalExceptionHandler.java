package com.criiky0.handler;

import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 参数NULL异常
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result<HashMap<String, String>>
        missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return Result.build(errorMap, ResultCodeEnum.PARAM_NULL_ERROR);
    }

    // 其他Exception
    @ExceptionHandler(Exception.class)
    public Result<HashMap<String, String>> exceptionHandler(Exception e) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return Result.build(errorMap, ResultCodeEnum.OCCUR_EXCEPTION);
    }

}
