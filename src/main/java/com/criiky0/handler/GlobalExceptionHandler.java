package com.criiky0.handler;

import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<HashMap<String, String>> exceptionHandler(Exception e) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return Result.build(errorMap, ResultCodeEnum.OCCUR_EXCEPTION);
    }
}
