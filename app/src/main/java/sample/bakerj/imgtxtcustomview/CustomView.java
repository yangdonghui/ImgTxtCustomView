package sample.bakerj.imgtxtcustomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author donghuiyang
 * @create time 2016/10/9 0009.
 */

public class CustomView extends View {
    /** 图片Bitmap */
    private Bitmap imageBitmap;
    /** 图片的长宽比 */
    private float imageAspectRatio;
    /** 图片的透明度 */
    private float imageAlpha;
    /** 图片的左padding*/
    private int imagePaddingLeft;
    /** 图片的上padding */
    private int imagePaddingTop;
    /** 图片的右padding */
    private int imagePaddingRight;
    /** 图片的下padding */
    private int imagePaddingBottom;
    /** 图片伸缩模式 */
    private int imageScaleType;
    /** 图片伸缩模式常量 fillXY */
    private static final int SCALE_TYPE_FILLXY = 0;
    /** 图片伸缩模式常量 center */
    private static final int SCALE_TYPE_CENTER = 1;

    /** 标题文本内容 */
    private String titleText;
    /** 标题文本字体大小 */
    private int titleTextSize;
    /** 标题文本字体颜色 */
    private int titleTextColor;
    /** 标题文本区域左padding */
    private int titlePaddingLeft;
    /** 标题文本区域上padding */
    private int titlePaddingTop;
    /** 标题文本区域右padding */
    private int titlePaddingRight;
    /** 标题文本区域下padding */
    private int titlePaddingBottom;

    /** 子标题文本内容 */
    private String subTitleText;
    /** 子标题文本字体大小 */
    private int subTitleTextSize;
    /** 子标题文本字体颜色 */
    private int subTitleTextColor;
    /** 子标题文本区域左padding */
    private int subTitlePaddingLeft;
    /** 子标题文本区域上padding */
    private int subTitlePaddingTop;
    /** 子标题文本区域右padding */
    private int subTitlePaddingRight;
    /** 子标题文本区域下padding */
    private int subTitlePaddingBottom;

    /** 控件用的paint */
    private Paint paint;
    private TextPaint textPaint;
    /** 用来界定控件中不同部分的绘制区域 */
    private Rect rect;
    /** 宽度和高度的最小值 */
    private static final int MIN_SIZE = 12;
    /** 控件的宽度 */
    private int mViewWidth;
    /** 控件的高度 */
    private int mViewHeight;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
