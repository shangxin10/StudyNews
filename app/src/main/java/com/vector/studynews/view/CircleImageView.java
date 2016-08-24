package com.vector.studynews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vector.studynews.R;

import java.lang.ref.WeakReference;

/**
 * Created by zhang on 2016/8/16.
 */
public class CircleImageView extends ImageView {


    private Paint paint;
    private Bitmap mMaskBitmap;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private WeakReference<Bitmap> mWeakBitmap;

    //图片相关的属性
    private int type;                           //类型，圆形或者圆角
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 10;     //圆角默认大小值
    private int mBorderRadius;                  //圆角大小

    public CircleImageView(Context context) {
        this(context,null);
    }
    public CircleImageView(Context context, AttributeSet attrs){
       super(context,attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        //取出attrs中我们为View设置的相关值
//        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
//        mBorderRadius = tArray.getDimensionPixelSize(R.styleable.CircleImageView_Radius, BODER_RADIUS_DEFAULT);
//        type = tArray.getInt(R.styleable.CircleImageView_type, TYPE_CIRCLE);
//        tArray.recycle();
    }
    public CircleImageView(Context context,AttributeSet attributeSet,int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(type==TYPE_CIRCLE){
            int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
            setMeasuredDimension(width,width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mWeakBitmap==null?null:mWeakBitmap.get();
        if(bitmap == null || bitmap.isRecycled()) {
            //获取图片的宽高
            Drawable drawable = getDrawable();
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            if (drawable != null) {
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas drawCanvas = new Canvas(bitmap);
                float scale = 1.0f;
                if (type == TYPE_ROUND) {
                    scale = Math.max(getHeight() * 1.0F / height, getWidth() * 1.0F / width);
                } else {
                    scale = getWidth() * 1.0F / Math.min(width, height);
                }
                //根据缩放比例,设置bounds,相当于缩放图片了
                drawable.setBounds(0, 0, (int) (scale * width), (int) (scale * height));
                drawable.draw(drawCanvas);
                if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                    mMaskBitmap = getBitmap();
                }
                paint.reset();
                paint.setFilterBitmap(false);
                paint.setXfermode(xfermode);

                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, paint);
                //bitmap缓存起来，避免每次调用onDraw,分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);

                //绘制图片
                canvas.drawBitmap(bitmap, 0, 0, null);
                paint.setXfermode(null);
            }
        }
        if(bitmap!=null){
            paint.setXfermode(null);
            canvas.drawBitmap(bitmap,0.0f,0.0f,paint);
            return;
        }
    }

    @Override
    public void invalidate() {
        mWeakBitmap = null;
        if (mWeakBitmap != null) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
        super.invalidate();
    }
    private Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);   //抗锯齿
        paint.setColor(Color.BLACK);
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    mBorderRadius, mBorderRadius, paint);
        } else {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        }
        return bitmap;
    }
}
