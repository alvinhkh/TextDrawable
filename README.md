###TextDrawable 
This light-weight library provides images with letter/text like the Gmail app. It extends the `Drawable` class thus can be used with existing/custom/network `ImageView` classes. Also included is a [fluent interface](http://en.wikipedia.org/wiki/Fluent_interface) for creating drawables and a customizable `ColorGenerator`.

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen1-material.png" width="350"/>
<img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen2-material.png" width="350"/>
</p>

###How to use

#### Import with Gradle:

```groovy
repositories {
    // ...
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.alvinhkh:TextDrawable:c1c2b5b'
}
```

####1. Create simple tile:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen3.png"/>
</p>

```xml
<ImageView android:layout_width="60dp"
	       android:layout_height="60dp"
	       android:id="@+id/image_view"/>
```
**Note:** Specify width/height for the `ImageView` and the `drawable` will auto-scale to fit the size.
```java
TextDrawable drawable = new TextDrawable.Builder()
    .setColor(Color.RED)
    .setShape(TextDrawable.SHAPE_RECT)
    .setText("A")
    .build();

ImageView image = (ImageView) findViewById(R.id.image_view);
image.setImageDrawable(drawable);
```

####2. Create rounded corner or circular tiles:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen6.png"/>
</p>

```java
TextDrawable drawable1 = new TextDrawable.Builder()
    .setColor(Color.RED)
    .setRadius(2)
    .setShape(TextDrawable.SHAPE_ROUND_RECT)
    .setText("A")
    .build(); 

TextDrawable drawable2 = new TextDrawable.Builder()
    .setColor(Color.RED)
    .setShape(TextDrawable.SHAPE_ROUND)
    .setText("A")
    .build();
```

####3. Add border:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen5.png"/>
</p>

```java
TextDrawable drawable = new TextDrawable.Builder()
    .setBorder(1)
    .setBolderColor(Color.BLUE)
    .setColor(Color.RED)
    .setRadius(10)
    .setShape(TextDrawable.SHAPE_ROUND_RECT)
    .setText("A")
    .build();
```

####4. Modify font style:

```java
TextDrawable drawable = new TextDrawable.Builder()
    .setBold()
    .setColor(Color.RED)
    .setFont(Typeface.DEFAULT)
    .setFontSize(48) 
    .setText("A")
    .setTextColor(COLOR.BLACK) 
    .toUpperCase()
    .build()
```

####5. Built-in color generator:

```java
ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
int color2 = generator.getColor("user@gmail.com")

// declare the builder object once.
TextDrawable.IBuilder builder = new TextDrawable.Builder()
    .setBorder(1) 
    .setBorderColor(Color.BLUE)
    .setShape(TextDrawable.SHAPE_ROUND);

// reuse the builder specs to create multiple drawables
TextDrawable ic1 = builder.setColor(color1).setText("A").build();
TextDrawable ic2 = builder.setColor(color2).setText("B").build();
``` 

####6. Specify the width / height:

```xml
<ImageView android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/image_view" />
```
**Note:**  The `ImageView` could use `wrap_content` width/height. You could set the width/height of the `drawable` using code.

```java
TextDrawable drawable = new TextDrawable.Builder()
    .setWidth(48)
    .setHeight(48)
    .setColor(Color.RED)
    .setText("A")
    .build();  

ImageView image = (ImageView) findViewById(R.id.image_view);
image.setImageDrawable(drawable);
```

####7. Other features:

1. Mix-match with other drawables. Use it in conjunction with `LayerDrawable`, `InsetDrawable`, `AnimationDrawable`, `TransitionDrawable` etc.

2. Compatible with other views (not just `ImageView`). Use it as background drawable, compound drawable for `TextView`, `Button` etc.

3. Use multiple letters or `unicode` characters to create interesting tiles. 

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen7.png" width="350"/></p>
