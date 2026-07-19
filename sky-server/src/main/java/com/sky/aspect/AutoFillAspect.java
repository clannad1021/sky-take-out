package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.enumeration.OperationType;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 *  自定义切面，实现公共字段自动填充逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    //前置通知，通过前置通知对公共字段进行赋值操作
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("填充公共字段");
        //获取被拦截方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //获取方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取注解对象
        OperationType operationType = autoFill.value(); //获取数据库操作类型
        //获取实参对象
        Object entity = joinPoint.getArgs()[0];
        //准备要赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentTimeMillis = System.currentTimeMillis();
        //最后根据不同的数据库操作类型进行赋值操作
        try {       //获取entity对象的运行时Class对象-->然后通过(方法名,参数类型)获取setter方法-->最后通过反射给对象赋值
            entity.getClass()
                    .getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class)
                    .invoke(entity, now);
            entity.getClass()
                    .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class)
                    .invoke(entity, currentTimeMillis);
            if (operationType == OperationType.INSERT) {
                entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class)
                        .invoke(entity, now);
                entity.getClass()
                        .getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class)
                        .invoke(entity, currentTimeMillis);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
