package com.bnhp.network.annotations;

import com.bnhp.network.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerUrl {
    String serverUrl() default Constants.BASE_URL;
}
