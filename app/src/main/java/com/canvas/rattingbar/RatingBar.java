package com.canvas.rattingbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RatingBar extends View {
    private int mNormalPic = 0;
    private int mSelectPic = 0;
    private Paint mPaintNormal,mPaintSelect;
    private Bitmap bitmap;
    private int position;
    private Bitmap seleBitmap;
    private int ratNum;
    private int currentPosition = 0;

    public RatingBar(Context context) {
        this(context,null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mNormalPic = typedArray.getResourceId(R.styleable.RatingBar_normalpic,mNormalPic);
        if (mNormalPic==0){
            throw new RuntimeException("必须设置图片哦");
        }
        mSelectPic = typedArray.getResourceId(R.styleable.RatingBar_selectpic,mSelectPic);
        if (mSelectPic==0){
            throw new RuntimeException("必须设置图片哦");
        }
        typedArray.recycle();
        bitmap = BitmapFactory.decodeResource(getResources(), mNormalPic);
        seleBitmap = BitmapFactory.decodeResource(getResources(), mSelectPic);
        mPaintNormal = new Paint();
        mPaintNormal.setAntiAlias(true);
        mPaintSelect = new Paint();
        mPaintSelect.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (bitmap.getWidth()+getPaddingLeft()+getPaddingRight())*ratNum;
        int height = bitmap.getHeight();
        setMeasuredDimension(width,height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = bitmap.getWidth()+getPaddingLeft()+getPaddingRight();
        if (position<0){
            position = -1;
        }
        if (position>ratNum){
            position = ratNum;
        }
        for (int i =0;i<ratNum;i++){
            int x = i*(width);
            if (i<position) {
                canvas.drawBitmap(seleBitmap, x, 0, mPaintSelect);
            }else{
                canvas.drawBitmap(bitmap, x, 0, mPaintNormal);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算位置  event.getX()  相对于当前控件的x  event.getRawx()相对于屏幕的x
                Log.e("TAG","getX"+event.getX());
                int width = bitmap.getWidth() + getPaddingRight() + getPaddingLeft();
                Log.e("TAG","width"+width);
                position = (int) (event.getX()/(width) + 1);
                if (currentPosition==position){
                    return true;
                }
                currentPosition = position;
                Log.e("TAG",position+"");
                invalidate();
                return true;
        }
        return true;
    }
    public synchronized void setRatnum(int num){
        ratNum = num;
    }
}
