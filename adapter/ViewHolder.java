package com.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;
	private ViewHolder(Context context,ViewGroup parent,int layoutId,int postion){
		mViews=new SparseArray<View>();
		this.mConvertView=LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	/**
	 * @param context
	 * @param converView
	 * @param parent
	 * @param layoutId
	 * @param postion
	 * @return
	 */
	public static ViewHolder get(Context context,View converView,ViewGroup parent,int layoutId,int postion){
		if(converView==null){
			return new ViewHolder(context, parent, layoutId, postion);
		}
		return (ViewHolder)converView.getTag();
	}
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view=mViews.get(viewId);
		if(view==null){
			view=mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	public View getConverView(){
		return mConvertView;
	}
	public void setString(int viewId,String value){
		TextView textView=getView(viewId);
		textView.setText(value);
	}
	public void setBackgroundResource(int viewId,int value){
		ImageView imageView=getView(viewId);
		imageView.setBackgroundResource(value);
	}
	public void setImageBitmap(int viewId,Bitmap bm){
		ImageView imageView=getView(viewId);
		imageView.setImageBitmap(bm);
	}
	
}
