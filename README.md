### ViewCreater使用说明
#### 1、简介：本工具通过annotationProcessor生成代码，在直接使用代码布局的情况下，使用本工具可以提高开发效率。
#### 2、使用说明
##### 1. 创建View
```
    @NewView()
    FrameLayout mainLayout = null;
```
通过注解NewView声明需要自动生成创建View的代码，此注解相关属性说明如下：
```
    int width() default ViewGroup.LayoutParams.WRAP_CONTENT;//宽
    int height() default ViewGroup.LayoutParams.WRAP_CONTENT;;//高
    int visible() default View.VISIBLE;//是否可显示,默认为可显示
    int bgId() default 0;//背景资源id
    int bgcolor() default -1;//背景颜色值
    int bgcolorId() default 0;//背景颜色id
    float alpha() default 0f;//透明度
    boolean focusable() default false;//是否可获取焦点
    boolean clip() default true;//是否clip
    boolean clickable() default false;//是否clickable
    boolean enable() default false;//是否enable
    boolean select() default false;//是否select
    String parent() default "";//父ViewGroup名称
    int layoutGravity() default -1;//布局位置,父View为FrameLayout时使用
    Margin margin() default @Margin;//Margin值注解
    Padding padding() default @Padding;//Padding值注解
    Listener listener() default @Listener;//监听注解
```
Margin注解：
```
    int left() default 0;//左
    int top() default 0;//上
    int right() default 0;//右
    int bottom() default 0;//下
```
Padding注解：
```
    int left() default 0;//左
    int top() default 0;//上
    int right() default 0;//右
    int bottom() default 0;//下
```
Listener注解：
```
    String keyListener() default "";//监听键值
    String focusListener() default "";//监听焦点变化
    String clickListener() default "";//监听点击事件
```
##### 2. 特定View配置
TextView、Button、EditText三个都可以使用注解Text，说明如下：
```
    String text() default "";//需要显示的字符串
    int textId() default 0;//需要显示的字符串id
    int textColor() default -1;//文字颜色值
    int textColorId() default 0;//文字颜色id
    int textSize() default 0;//文字大小
    int maxHeight() default 0;//最大高度
    int maxWidth() default 0;//最大宽度
    int minWidth() default 0;//最小宽度
    int minHeight() default 0;//最小高度
    int line() default 0;//行数
    String hint() default "";//默认文字
    int hintId() default 0;//默认文字id
    int hintColor() default -1;//默认文字颜色值
    int hintColorId() default 0;//默认文字颜色id
    boolean marquee() default false;//是否支持滚动
    int gravity() default Gravity.TOP | Gravity.START;//设置gravity
    CompoundDrawable compoundDrawable() default @CompoundDrawable;//文字四周图标
```
CompoundDrawable注解：
```
    int left() default 0;//左
    int top() default 0;//上
    int right() default 0;//右
    int bottom() default 0;//下
    int padding() default 0;//间距
```

ImageView可以使用Image注解，说明如下：
```
    int src() default 0;//需要显示的资源id
    ImageView.ScaleType scaleType() default ImageView.ScaleType.FIT_XY;//显示类型
```
#### 3、例子
```
    //创建一个布局
    @NewView(clip = false,
            bgId = R.drawable.bg)
    FrameLayout mainLayout = null;

    //创建一个ImageView
    @NewView(width = 20, height = 200,
            parent = "mainLayout",
            bgcolor = Color.RED,
            margin = @Margin(left = 50, top = 200),
            padding = @Padding(left = 20, right = 20),
            listener = @Listener(focusListener = "focusChangeListener"))
    @Image(scaleType = ImageView.ScaleType.CENTER)
    ImageView img = null;

    //创建一个TextView
    @NewView(width = 400, height = 80,
            parent = "mainLayout",
            select = true,
            bgId = R.drawable.bg,
            margin = @Margin(top = 300, left = 500),
            padding = @Padding(left = 40))
    @Text(textId = R.string.app_name,
            marquee = true,
            textColor = Color.BLUE,
            gravity = Gravity.CENTER_VERTICAL,
            compoundDrawable = @CompoundDrawable(left = R.drawable.ic_launcher_round, padding = 80))
    TextView textView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCreater.newView(this, this);//在使用相关View前初始化，第一个参数为Context,第二个参数为持有View的对象
        setContentView(mainLayout);
    }
```
所有使用NewView注解生成View的对象不能使用private