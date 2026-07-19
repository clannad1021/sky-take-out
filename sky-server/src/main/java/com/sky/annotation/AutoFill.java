package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某些方法需要进行功能字段自动填充
 */
@Target(ElementType.METHOD) //标示作用在方法上
@Retention(RetentionPolicy.RUNTIME)//注解在程序运行时仍然存在，可以被反射或 AOP 读取到。
public @interface AutoFill {
    //指定数据库操作类型
    OperationType value();
}
