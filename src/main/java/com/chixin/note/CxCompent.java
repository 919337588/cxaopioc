package com.chixin.note;

import java.lang.annotation.*;
// 自定义注解 注入到Spring容器
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CxCompent {
}
