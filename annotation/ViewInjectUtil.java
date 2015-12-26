package com.xiang.lu.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.view.View;

public class ViewInjectUtil {
	private static final String METHOD_SET_CONTENEVIEW = "setContentView";
	private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

	private static void injectContentView(Activity activity){
		Class<? extends Activity> clazz=activity.getClass();
		
		
		ContentView contentView=clazz.getAnnotation(ContentView.class);
		if(contentView!=null){
			int contentViewLayoutId=contentView.value();
			
			try {
				
				Method method=clazz.getMethod(METHOD_SET_CONTENEVIEW, int.class);
				method.setAccessible(true);
				method.invoke(activity, contentViewLayoutId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	private static void injectViews(Activity activity){
		Class<? extends Activity> clazz=activity.getClass();
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields){
			ViewInject viewInject=field.getAnnotation(ViewInject.class);
			if(viewInject!=null){
				int viewId=viewInject.value();
				if(viewId!=-1){
					Method method;
					try {
						method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
						Object resViewObject=method.invoke(activity, viewId);
						field.setAccessible(true);
						field.set(activity, resViewObject);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	public static void inject(Activity activity){
		injectContentView(activity);
		injectViews(activity);
		injectEvents(activity);
	}
	private static void injectEvents(Activity activity){
		Class<? extends Activity> clazz=activity.getClass();
		Method[] methods=clazz.getMethods();
		for(Method method:methods){
			Annotation[] annotations=method.getAnnotations();
			for(Annotation annotation:annotations){
				Class<? extends Annotation> annotationType= annotation.annotationType();
				EventBase eventBase=annotationType.getAnnotation(EventBase.class);
				
				if(eventBase!=null){
					String listenerSetter=eventBase.listenerSetter();
					
					Class<?> listenerType=eventBase.listenerType();
					
					String methodName=eventBase.methodName();
					
					try {
						Method aMethod=annotationType.getDeclaredMethod("value");
						int[] viewIds=(int[])aMethod.invoke(annotation, null);
						DynamicHandler handler=new DynamicHandler(activity);
						
						handler.addMethod(methodName, method);
						Object listener=Proxy.newProxyInstance(listenerType.getClassLoader(),new Class<?>[]{listenerType},handler);
						for(int viewId:viewIds){
							View view=activity.findViewById(viewId);
							Method setEventListenerMethod=view.getClass().getMethod(listenerSetter, listenerType);
							
							setEventListenerMethod.invoke(view, listener);
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}
	}
}
