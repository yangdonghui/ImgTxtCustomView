# ImgTxtCustomView

    Android开发做到了一定程度，多少都会用到自定义控件，一方面是更加灵活，另一方面在大数据量的情况下自定义控件的效率比写布局文件更高。一个相对完善的自定义控件在布局文件中和java代码中都应能灵活设置属性。另外在普通的布局中和AdapterView中都应能正确绘制，这就要求合理设计onMeasure方法，下文中会做比较详细的讲解。

    用文字来描述一下：我们要定义的控件上方会显示一张图片，我们可以设置这张图片的内容，长宽比，透明度，伸缩模式，以及图片四周的填充空间大小。图片下方会显示一行文字，作为一级标题，我们可以设置文字的内容，大小，颜色，以及文字区域四周的填充空间的大小。一级标题下方显示一行二级标题，具体设置内容和一级标题相同。

  
