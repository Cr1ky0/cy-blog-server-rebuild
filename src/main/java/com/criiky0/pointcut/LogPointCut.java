package com.criiky0.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class LogPointCut {
    @Pointcut("execution(* com.criiky0.service.impl.*.*(..))")
    public void cut(){}
}
