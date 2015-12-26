package com.xiang.lu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xiang.lu.MyImageView;

import android.view.View;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType=MyImageView.OnViewClickListener.class,listenerSetter="setOnClickIntent",methodName = "onViewClick")
public @interface OnViewClick {
	int[] value();
}
