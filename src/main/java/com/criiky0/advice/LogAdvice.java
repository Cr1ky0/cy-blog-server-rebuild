package com.criiky0.advice;

import java.util.Arrays;

import com.criiky0.utils.EnvironmentChecker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAdvice {
    @Autowired
    private EnvironmentChecker environmentChecker;

    @Around(value = "com.criiky0.pointcut.LogPointCut.cut()")
    public Object manageTransaction(

        // 通过在通知方法形参位置声明ProceedingJoinPoint类型的形参，
        // Spring会将这个类型的对象传给我们
        ProceedingJoinPoint joinPoint) {

        // 通过ProceedingJoinPoint对象获取外界调用目标方法时传入的实参数组
        Object[] args = joinPoint.getArgs();

        // 通过ProceedingJoinPoint对象获取目标方法的签名对象
        Signature signature = joinPoint.getSignature();

        // 通过签名对象获取目标方法的方法名
        String methodName = signature.getName();

        // 声明变量用来存储目标方法的返回值
        Object targetMethodReturnValue = null;

        // 获取目标service类名
        Object targetService = joinPoint.getTarget().getClass().getName();
        try {
            // 在目标方法执行前：开启事务（模拟）
            if (!targetService.equals("com.criiky0.service.impl.UserServiceImpl")) // 不要再log里记录敏感信息
                log.info("[AOP 环绕通知] 开启事务，方法名：" + methodName + "，参数列表：" + Arrays.asList(args));
            else
                log.info("[AOP 环绕通知] 开启事务，方法名：" + methodName);
            // 过ProceedingJoinPoint对象调用目标方法
            // 目标方法的返回值一定要返回给外界调用者
            targetMethodReturnValue = joinPoint.proceed(args);

            // 在目标方法成功返回后：提交事务（模拟）
            log.info("[AOP 环绕通知] 提交事务，方法名：" + methodName + "，方法返回值：" + targetMethodReturnValue);

        } catch (Throwable e) {
            // 在目标方法抛异常后：回滚事务（模拟）
            log.info("[AOP 环绕通知] 回滚事务，方法名：" + methodName + "，异常：" + e.getClass().getName());
            log.info("异常信息如下：");
            log.info(e.getMessage());
            if (environmentChecker.isDevelopment()) {
                StackTraceElement[] traces = e.getStackTrace();
                for (StackTraceElement trace : traces) {
                    log.info(trace.toString());
                }
            }

        }
        // finally {
        //
        // // 在目标方法最终结束后：释放数据库连接
        // log.info("[AOP 环绕通知] 释放数据库连接，方法名：" + methodName);
        //
        // }

        return targetMethodReturnValue;
    }
}
