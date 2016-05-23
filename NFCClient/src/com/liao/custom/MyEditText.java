package com.liao.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText{
	private Paint mPaint;
	
	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.parseColor("#99CC33"));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//画底线，参数为：起x,y	止x,y
		canvas.drawLine(0, this.getHeight()-1, this.getWidth()-1, this.getHeight()-1, mPaint);
	}
}
