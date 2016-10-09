# ImgTxtCustomView
Android开发做到了一定程度，多少都会用到自定义控件，一方面是更加灵活，另一方面在大数据量的情况下自定义控件的效率比写布局文件更高。

    一个相对完善的自定义控件在布局文件中和java代码中都应能灵活设置属性。另外在普通的布局中和AdapterView中都应能正确绘制，这就要求合理设计onMeasure方法，下文中会做比较详细的讲解。

    接下来我就一步一步来讲解如何设计和编写一个比较完善的自定义控件。

    首先要来设计好我们要完成的效果，我们今天来实现下图所示的这样一个控件：


    用文字来描述一下：我们要定义的控件上方会显示一张图片，我们可以设置这张图片的内容，长宽比，透明度，伸缩模式，以及图片四周的填充空间大小。图片下方会显示一行文字，作为一级标题，我们可以设置文字的内容，大小，颜色，以及文字区域四周的填充空间的大小。一级标题下方显示一行二级标题，具体设置内容和一级标题相同。

    我们不妨先来直接看一下完成后的效果，这样可以更直观的了解要实现的控件的样子。

                   

    左图的样子是在常规的布局中自定义控件的样子，右图则是在大数据量的情况下自定义控件作为AdapterView的item的时候绘制出来的样子。

    上面我们大体完成了初步的控件设计，下面我们开始编写代码。

    第一步，我们写好自定义属性，根据我们上面所做的设计，我们的自定义属性涉及到三个方面，分别是图片相关的属性，一级标题相关的属性，二级标题相关的属性。

    按照惯例，我们首先在res/values文件目录下创建一个attrs.xml文件。

    然后我们在attrs.xml文件中完成我们对属性的定义，代码片段如下：

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="imageSrc" format="reference"/>
    <attr name="imageAspectRatio" format="float"/>
    <attr name="imageAlpha" format="float"/>
    <attr name="imagePaddingLeft" format="dimension"/>
    <attr name="imagePaddingTop" format="dimension"/>
    <attr name="imagePaddingRight" format="dimension"/>
    <attr name="imagePaddingBottom" format="dimension"/>
    <attr name="imageScaleType">
        <enum name="fillXY" value="0"/>
        <enum name="center" value="1"/>
    </attr>
    
    <attr name="titleText" format="string"/>
    <attr name="titleTextSize" format="dimension"/>
    <attr name="titleTextColor" format="color"/>
    <attr name="titlePaddingLeft" format="dimension"/>
    <attr name="titlePaddingTop" format="dimension"/>
    <attr name="titlePaddingRight" format="dimension"/>
    <attr name="titlePaddingBottom" format="dimension"/>
    
    <attr name="subTitleText" format="string"/>
    <attr name="subTitleTextSize" format="dimension"/>
    <attr name="subTitleTextColor" format="color"/>
    <attr name="subTitlePaddingLeft" format="dimension"/>
    <attr name="subTitlePaddingTop" format="dimension"/>
    <attr name="subTitlePaddingRight" format="dimension"/>
    <attr name="subTitlePaddingBottom" format="dimension"/>
    
    <declare-styleable name="CustomView">
        <attr name="imageSrc"/>
        <attr name="imageAspectRatio" />
        <attr name="imageAlpha" />
        <attr name="imagePaddingLeft" />
        <attr name="imagePaddingTop" />
        <attr name="imagePaddingRight" />
        <attr name="imagePaddingBottom" />
        <attr name="imageScaleType" />
        <attr name="titleText" />
        <attr name="titleTextSize" />
        <attr name="titleTextColor" />
        <attr name="titlePaddingLeft" />
        <attr name="titlePaddingTop" />
        <attr name="titlePaddingRight" />
        <attr name="titlePaddingBottom" />
        <attr name="subTitleText" />
        <attr name="subTitleTextSize" />
        <attr name="subTitleTextColor" />
        <attr name="subTitlePaddingLeft" />
        <attr name="subTitlePaddingTop" />
        <attr name="subTitlePaddingRight" />
        <attr name="subTitlePaddingBottom" />
    </declare-styleable>
</resources>

    这里需要说明几点：<attr>标签的format属性值代表属性的类型，这个类型值一共有10种，分别是：reference,float,color,dimension,boolean,string,enum,integer,fraction,flag

。但是我们作为开发者常用的基本上只有reference,float,color,dimension,boolean,string,enum这7种。在attrs.xml文件中的<declare-styleable>标签的name属性的值，按照惯例我们都是写成自定义控件类的名字。一个同名的<attr>在attrs.xml中只可以定义一次。

    除此之外，上面的代码都是针对前面的设计来定义了各种属性，相信各位同学都能看懂。

    第二步就是编写我们自定义控件的java类了，我们首先将之前做的自定义属性在自定义控件类中做好声明：

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

    然后我们要在构造方法中，将从布局文件中读取的自定义属性解析出来。

TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs, R.styleable.CustomView, defStyle, 0);
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


    这里需要说明几点，TypedArray对象在使用完毕后一定要调用recycle()方法。我之前曾在一篇文章中总结过 在java代码中进行px与dip(dp)、px与sp单位值的转换 。 实际上，android中也提供了单位转换的函数，我们也可以使用TypedValue.applyDimension(int unit, float value, DisplayMetrics metrics)方法来进行单位的互换，其中，第一个参数是你想要得到的单位，第二个参数是你想得到的单位的数值，比如：我要得到一个25sp，那么我就用TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25,getResources().getDisplayMetrics())，返回的就是25sp对应的px数值了。

    接下来我们要开始设计onMeasure方法，再设计onMeasure之前我们简单了解几个概念。

    MeasureSpec的三种模式：

    EXACTLY:表示我们设置了MATCH_PARENT或者一个准确的数值，含义是父布局要给子布局一个确切的大小。

    AT_MOST:表示子布局将被限制在一个最大值之内，通常是子布局设置了wrap_content。

    UNSPECIFIED:表示子布局想要多大就可以要多大，通常出现在AdapterView中item的heightMode中。

    了解了上面几个概念，我们就可以开始设计onMeasure了，具体代码如下：

@Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    
    int width;
    int height;
    
    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else {
      int desired = getPaddingLeft() + getPaddingRight() + 
          imagePaddingLeft + imagePaddingRight;
      desired += (imageBitmap != null) ? imageBitmap.getWidth() : 0;
      width = Math.max(MIN_SIZE, desired);
      if (widthMode == MeasureSpec.AT_MOST) {
        width = Math.min(desired, widthSize);
      }
    }
    
    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else {
      int rawWidth = width - getPaddingLeft() - getPaddingRight();
      int desired = (int) (getPaddingTop() + getPaddingBottom() + imageAspectRatio * rawWidth);
      
      if (titleText != null) {
        paint.setTextSize(titleTextSize);
        FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
        desired += (textHeight + titlePaddingTop + titlePaddingBottom);
      }
      
      if (subTitleText != null) {
        paint.setTextSize(subTitleTextSize);
        FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
        desired += (textHeight + subTitlePaddingTop + subTitlePaddingBottom);
      }
      
      height = Math.max(MIN_SIZE, desired);
      if (heightMode == MeasureSpec.AT_MOST) {
        height = Math.min(desired, heightSize);
      }
    }
    
    setMeasuredDimension(width, height);
  }


    思路是这样的：我们首先判断是不是EXACTLY模式，如果是，那就可以直接设置值了，如果不是，我们先按照UNSPECIFIED模式处理，让子布局得到自己想要的最大值，然后判断是否是AT_MOST模式，来做最后的限制。

    完成onMeasure过程之后，我们需要开始onDraw的设计，在onDraw中我们需要考虑各个部分设置的padding值，然后对应做出坐标的处理，整体的思路是从下向上绘制。具体的代码如下：

@Override
  protected void onDraw(Canvas canvas) {
    rect.left = getPaddingLeft();
    rect.top = getPaddingTop();
    rect.right = mViewWidth - getPaddingRight();
    rect.bottom = mViewHeight - getPaddingBottom();
    
    paint.setAlpha(255);
    if (subTitleText != null) {
      paint.setTextSize(subTitleTextSize);
      paint.setColor(subTitleTextColor);
      paint.setTextAlign(Paint.Align.LEFT);
      
      FontMetrics fm = paint.getFontMetrics();
      int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
      
      int left = getPaddingLeft() + subTitlePaddingLeft;
      int right = mViewWidth - getPaddingRight() - subTitlePaddingRight;
      int bottom = mViewHeight - getPaddingBottom() - subTitlePaddingBottom;
      
      String msg = TextUtils.ellipsize(subTitleText, textPaint, right - left, TextUtils.TruncateAt.END).toString();
      
      float textWidth = paint.measureText(msg);
      float x = textWidth < (right - left) ? left + (right - left - textWidth) / 2 : left;
      canvas.drawText(msg, x, bottom - fm.descent, paint);
      
      rect.bottom -= (textHeight + subTitlePaddingTop + subTitlePaddingBottom);
    }
    
    if (titleText != null) {
      paint.setTextSize(titleTextSize);
      paint.setColor(titleTextColor);
      paint.setTextAlign(Paint.Align.LEFT);
      
      FontMetrics fm = paint.getFontMetrics();
      int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
      
      float left = getPaddingLeft() + titlePaddingLeft;
      float right = mViewWidth - getPaddingRight() - titlePaddingRight;
      float bottom = rect.bottom - titlePaddingBottom;
      
      String msg = TextUtils.ellipsize(titleText, textPaint, right - left, TextUtils.TruncateAt.END).toString();
      
      float textWidth = paint.measureText(msg);
      float x = textWidth < right - left ? left + (right - left - textWidth) / 2 : left;
      canvas.drawText(msg, x, bottom - fm.descent, paint);
      
      rect.bottom -= (textHeight + titlePaddingTop + titlePaddingBottom);
    }
    
    if (imageBitmap != null) {
      paint.setAlpha((int) (255 * imageAlpha));
      rect.left += imagePaddingLeft;
      rect.top += imagePaddingTop;
      rect.right -= imagePaddingRight;
      rect.bottom -= imagePaddingBottom;
      if (imageScaleType == SCALE_TYPE_FILLXY) {
        canvas.drawBitmap(imageBitmap, null, rect, paint);
      } else if (imageScaleType == SCALE_TYPE_CENTER) {
        int bw = imageBitmap.getWidth();
        int bh = imageBitmap.getHeight();
        if (bw < rect.right - rect.left) {
          int delta = (rect.right - rect.left - bw) / 2;
          rect.left += delta;
          rect.right -= delta;
        }
        if (bh < rect.bottom - rect.top) {
          int delta = (rect.bottom - rect.top - bh) / 2;
          rect.top += delta;
          rect.bottom -= delta;
        }
        canvas.drawBitmap(imageBitmap, null, rect, paint);
      }
    }
  }


    当做完这一步的时候，我们的自定义控件已经能够在布局文件中进行使用了，但是我们还不能在AdapterView中用我们设计的布局文件，因为AdapterView中每一个item属性都是在java代码中动态设置的，因此我们就需要给我们的自定义控件开放属性设置的接口，我们这里暂时只开放了设置图片和文字内容的接口。

public void setImageBitmap(Bitmap bitmap) {
    imageBitmap = bitmap;
    requestLayout();
    invalidate();
  }
  
  public void setTitleText(String text) {
    titleText = text;
    requestLayout();
    invalidate();
  }
  
  public void setSubTitleText(String text) {
    subTitleText = text;
    requestLayout();
    invalidate();
  }


    做到这一步的时候，这个自定义控件基本就算完成了，后续的工作就是一些完善和修补了。

    接下来就是自定义控件的使用了，在布局文件中使用自定义控件的时候我们需要额外做一点工作，如下:

<RelativeLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:carrey="http://schemas.android.com/apk/res/com.carrey.customview"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity" >

    <com.carrey.customview.customview.CustomView 
        android:id="@+id/customview"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true"
        android:background="#FFD700"
        carrey:imageSrc="@drawable/clock"
        carrey:imageAspectRatio="1.0"
        carrey:imageAlpha="0.5"
        carrey:imagePaddingLeft="5dp"
        carrey:imagePaddingTop="5dp"
        carrey:imagePaddingRight="5dp"
        carrey:imagePaddingBottom="5dp"
        carrey:imageScaleType="center"
        carrey:titleText="这是一级标题"
        carrey:titleTextSize="30sp"
        carrey:titleTextColor="#1E90FF"
        carrey:titlePaddingLeft="4dp"
        carrey:titlePaddingTop="4dp"
        carrey:titlePaddingRight="4dp"
        carrey:titlePaddingBottom="4dp"
        carrey:subTitleText="这是二级子标题"
        carrey:subTitleTextSize="20sp"
        carrey:subTitleTextColor="#00FF7F"
        carrey:subTitlePaddingLeft="3dp"
        carrey:subTitlePaddingTop="3dp"
        carrey:subTitlePaddingRight="3dp"
        carrey:subTitlePaddingBottom="3dp"/>

    <Button 
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="next page"/>
</RelativeLayout>

我们需要添加一行xmlns: carrey ="http://schemas.android.com/apk/res/ com.carrey.customview "，其中 carrey 是一个前缀，你可以随意设置， com.carrey.customview 是我们的应用的包名，如果拿不准的可以打开 Manifest文件，在<manifest>节点中找到package属性值即可。 
