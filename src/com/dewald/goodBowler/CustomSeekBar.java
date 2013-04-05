package com.dewald.goodBowler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class CustomSeekBar extends SeekBar{
	
	MyOnResizeListener orl = null;

	public CustomSeekBar(Context context) {
		super(context);
	}

	public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void SetOnResizeListener(MyOnResizeListener orlExt){
		orl = orlExt;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if(orl != null){
			orl.OnResize(this.getId(), w, h, oldw, oldh);
		}
		
	}
	
}
