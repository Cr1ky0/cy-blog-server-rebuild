package com.criiky0.handler;

import com.alibaba.druid.util.StringUtils;
import com.criiky0.utils.EnvironmentChecker;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private EnvironmentChecker environmentChecker;

    // 参数NULL异常
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result<HashMap<String, String>>
        missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return Result.build(errorMap, ResultCodeEnum.PARAM_NULL_ERROR);
    }

    // 参数校验异常
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<HashMap<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        HashMap<String, String> errorMap = new HashMap<>();
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        if (!StringUtils.isEmpty(msg))
            errorMap.put("error", msg);
        else
            errorMap.put("error", "参数校验异常");
        return Result.build(errorMap, ResultCodeEnum.PARAM_ERROR);
    }

    // 约束验证异常
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<HashMap<String, String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("error", sb.toString());
        return Result.build(map, ResultCodeEnum.PARAM_ERROR);
    }

    // BindException
    @ExceptionHandler({BindException.class})
    public Result<HashMap<String, String>> bindExceptionHandler(BindException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isEmpty(msg))
            map.put("error", msg);
        else
            map.put("error", "数据绑定异常：BindException");
        return Result.build(map, ResultCodeEnum.PARAM_ERROR);
    }

    // 其他Exception
    @ExceptionHandler(Exception.class)
    public Result<HashMap<String, String>> exceptionHandler(Exception e) {
        HashMap<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        log.info("发生异常：" + e.getClass().getName());
        if (environmentChecker.isDevelopment()) {
            StackTraceElement[] traces = e.getStackTrace();
            for (StackTraceElement trace : traces) {
                log.info(trace.toString());
            }
        }
        return Result.build(errorMap, ResultCodeEnum.OCCUR_EXCEPTION);
    }

}
