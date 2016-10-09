package sample.bakerj.imgtxtcustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author donghuiyang
 * @create time 2016/10/9 0009.
 */

public class CustomView extends View {
    /**
     * 图片Bitmap
     */
    private Bitmap imageBitmap;
    /**
     * 图片的长宽比
     */
    private float imageAspectRatio;
    /**
     * 图片的透明度
     */
    private float imageAlpha;
    /**
     * 图片的左padding
     */
    private int imagePaddingLeft;
    /**
     * 图片的上padding
     */
    private int imagePaddingTop;
    /**
     * 图片的右padding
     */
    private int imagePaddingRight;
    /**
     * 图片的下padding
     */
    private int imagePaddingBottom;
    /**
     * 图片伸缩模式
     */
    private int imageScaleType;
    /**
     * 图片伸缩模式常量 fillXY
     */
    private static final int SCALE_TYPE_FILLXY = 0;
    /**
     * 图片伸缩模式常量 center
     */
    private static final int SCALE_TYPE_CENTER = 1;

    /**
     * 标题文本内容
     */
    private String titleText;
    /**
     * 标题文本字体大小
     */
    private int titleTextSize;
    /**
     * 标题文本字体颜色
     */
    private int titleTextColor;
    /**
     * 标题文本区域左padding
     */
    private int titlePaddingLeft;
    /**
     * 标题文本区域上padding
     */
    private int titlePaddingTop;
    /**
     * 标题文本区域右padding
     */
    private int titlePaddingRight;
    /**
     * 标题文本区域下padding
     */
    private int titlePaddingBottom;

    /**
     * 子标题文本内容
     */
    private String subTitleText;
    /**
     * 子标题文本字体大小
     */
    private int subTitleTextSize;
    /**
     * 子标题文本字体颜色
     */
    private int subTitleTextColor;
    /**
     * 子标题文本区域左padding
     */
    private int subTitlePaddingLeft;
    /**
     * 子标题文本区域上padding
     */
    private int subTitlePaddingTop;
    /**
     * 子标题文本区域右padding
     */
    private int subTitlePaddingRight;
    /**
     * 子标题文本区域下padding
     */
    private int subTitlePaddingBottom;

    /**
     * 控件用的paint
     */
    private Paint paint;
    private TextPaint textPaint;
    /**
     * 用来界定控件中不同部分的绘制区域
     */
    private Rect rect;
    /**
     * 宽度和高度的最小值
     */
    private static final int MIN_SIZE = 12;
    /**
     * 控件的宽度
     */
    private int mViewWidth;
    /**
     * 控件的高度
     */
    private int mViewHeight;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomView_imageSrc:
                    imageBitmap = BitmapFactory.decodeResource(
                            getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomView_imageAspectRatio:
                    imageAspectRatio = a.getFloat(attr, 1.0f);//默认长宽相等
                    break;
                case R.styleable.CustomView_imageAlpha:
                    imageAlpha = a.getFloat(attr, 1.0f);//默认不透明
                    if (imageAlpha > 1.0f) imageAlpha = 1.0f;
                    if (imageAlpha < 0.0f) imageAlpha = 0.0f;
                    break;
                case R.styleable.CustomView_imagePaddingLeft:
                    imagePaddingLeft = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_imagePaddingTop:
                    imagePaddingTop = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_imagePaddingRight:
                    imagePaddingRight = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_imagePaddingBottom:
                    imagePaddingBottom = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_imageScaleType:
                    imageScaleType = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomView_titleText:
                    titleText = a.getString(attr);
                    break;
                case R.styleable.CustomView_titleTextSize:
                    titleTextSize = a.getDimensionPixelSize(
                            attr, (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics()));//默认标题字体大小25sp
                    break;
                case R.styleable.CustomView_titleTextColor:
                    titleTextColor = a.getColor(attr, 0x00000000);//默认黑色字体
                    break;
                case R.styleable.CustomView_titlePaddingLeft:
                    titlePaddingLeft = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_titlePaddingTop:
                    titlePaddingTop = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_titlePaddingRight:
                    titlePaddingRight = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_titlePaddingBottom:
                    titlePaddingBottom = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_subTitleText:
                    subTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomView_subTitleTextSize:
                    subTitleTextSize = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    20, TypedValue.COMPLEX_UNIT_SP, getResources().getDisplayMetrics()));//默认子标题字体大小20sp
                    break;
                case R.styleable.CustomView_subTitleTextColor:
                    subTitleTextColor = a.getColor(attr, 0x00000000);
                    break;
                case R.styleable.CustomView_subTitlePaddingLeft:
                    subTitlePaddingLeft = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_subTitlePaddingTop:
                    subTitlePaddingTop = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_subTitlePaddingRight:
                    subTitlePaddingRight = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomView_subTitlePaddingBottom:
                    subTitlePaddingBottom = a.getDimensionPixelSize(attr, 0);
                    break;
            }
        }
        a.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(paint);
        rect = new Rect();
    }

    /**
     * 思路是这样的：我们首先判断是不是EXACTLY模式，如果是，那就可以直接设置值了，
     * 如果不是，我们先按照UNSPECIFIED模式处理，让子布局得到自己想要的最大值，
     * 然后判断是否是AT_MOST模式，来做最后的限制。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY){
            //表示我们设置了MATCH_PARENT或者一个准确的数值，含义是父布局要给子布局一个确切的大小。
            width = widthSize;
        }else{
            int desired = getPaddingLeft() + getPaddingRight() + imagePaddingLeft + imagePaddingRight;
            desired += (imageBitmap!=null)?imageBitmap.getWidth():0;
            width = Math.max(MIN_SIZE, desired);
            if (widthMode == MeasureSpec.AT_MOST){
                //示子布局将被限制在一个最大值之内，通常是子布局设置了wrap_content。
                width = Math.min(desired, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            int rawWidth = width - getPaddingLeft() - getPaddingRight();
            int desired = (int)(getPaddingTop() + getPaddingBottom() + imageAspectRatio * rawWidth);

            if (titleText != null){
                paint.setTextSize(titleTextSize);
                Paint.FontMetrics fm = paint.getFontMetrics();
                //文字高度
                int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
                desired += (textHeight + titlePaddingTop + titlePaddingBottom);
            }

            if (subTitleText != null){
                paint.setTextSize(subTitleTextSize);
                Paint.FontMetrics fm = paint.getFontMetrics();
                //文字高度
                int textHeigt = (int) Math.ceil(fm.descent - fm.ascent);
                desired += (textHeigt + subTitlePaddingTop + subTitlePaddingBottom);
            }

            height =Math.max(MIN_SIZE, desired);
            if (heightMode == MeasureSpec.AT_MOST){
                height = Math.min(desired, heightSize);
            }
        }

        //设置view宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        System.out.println("mViewWidth:" + mViewWidth + ",mViewHeight:" + mViewHeight);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = mViewWidth - getPaddingRight();
        rect.bottom = mViewHeight - getPaddingBottom();

        paint.setAlpha(255);

        //二级标题
        if (subTitleText != null){
            paint.setTextSize(subTitleTextSize);
            paint.setColor(subTitleTextColor);
            paint.setTextAlign(Paint.Align.LEFT);

            Paint.FontMetrics fm = paint.getFontMetrics();
            int textHeight = (int) Math.ceil(fm.descent - fm.ascent);

            int left = getPaddingLeft() + subTitlePaddingLeft;
            int right = mViewWidth - getPaddingRight() - subTitlePaddingRight;
            int bottom = mViewHeight - getPaddingBottom() - subTitlePaddingBottom;

            String msg = TextUtils.ellipsize(subTitleText, textPaint, right - left, TextUtils.TruncateAt.END).toString();
            float textWidth = paint.measureText(msg);

            //起点坐标
            float x = textWidth < (right - left) ? left + (right - left - textWidth) / 2 : left;
            float y = bottom - fm.descent;

            canvas.drawText(msg, x, y, paint);

            rect.bottom -= textHeight + getPaddingBottom() + subTitlePaddingBottom;
        }

        //一级标题
        if (titleText != null){
            paint.setTextSize(titleTextSize);
            paint.setColor(titleTextColor);
            paint.setTextAlign(Paint.Align.LEFT);

            Paint.FontMetrics fm = paint.getFontMetrics();
            int textHeight = (int) Math.ceil(fm.descent - fm.ascent);

            int left = getPaddingLeft() + titlePaddingLeft;
            int right = mViewWidth - getPaddingRight() - titlePaddingRight;
            float bottom = rect.bottom - titlePaddingBottom;

            String msg = TextUtils.ellipsize(titleText, textPaint, right - left, TextUtils.TruncateAt.END).toString();
            float textWidth = paint.measureText(msg);

            //起点坐标
            float x = textWidth < (right - left) ? left + (right - left - textWidth) / 2 : left;
            float y = bottom - fm.descent;

            canvas.drawText(msg, x, y, paint);

            rect.bottom -= textHeight + getPaddingBottom() + titlePaddingBottom;
        }

        if (imageBitmap != null){
            paint.setAlpha((int)(255 * imageAlpha));

            rect.left += imagePaddingLeft;
            rect.right += imagePaddingRight;
            rect.top += imagePaddingTop;
            rect.bottom += imagePaddingBottom;

            if (imageScaleType == SCALE_TYPE_FILLXY){
                canvas.drawBitmap(imageBitmap, null, rect, paint);
            }else if (imageScaleType == SCALE_TYPE_CENTER){
                int bw = imageBitmap.getWidth();
                int bh = imageBitmap.getHeight();

                if (bw < (rect.right - rect.left)){
                    int delta = (rect.right - rect.left - bw) / 2;
                    rect.left += delta;
                    rect.right -= delta;
                }
                if (bh < (rect.bottom - rect.top)){
                    int delta = (rect.bottom - rect.top - bh) / 2;
                    rect.top += delta;
                    rect.bottom -= delta;
                }

                canvas.drawBitmap(imageBitmap, null, rect, paint);
            }
        }
    }

    public void setImageBitmap(Bitmap bitmap){
        imageBitmap = bitmap;
        requestLayout();
        invalidate();
    }

    public void setTitleText(String text){
        titleText = text;
        requestLayout();
        invalidate();
    }

    public void setSubTitleText(String text){
        subTitleText = text;
        requestLayout();
        invalidate();
    }
}
