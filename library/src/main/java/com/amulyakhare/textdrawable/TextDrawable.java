package com.amulyakhare.textdrawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.amulyakhare.textdrawable.util.TypefaceHelper;

/**
 * @author amulya
 * @datetime 14 Oct 2014, 3:53 PM
 */
public class TextDrawable extends ShapeDrawable {

    private final Paint textPaint;
    private final Paint borderPaint;
    private static final float SHADE_FACTOR = 0.9f;
    private final String text;
    private final RectShape shape;
    private final int height;
    private final int width;
    private final int fontSize;
    private final float radius;
    private final int borderThickness;
    private final int borderColor;
    private Bitmap bitmap;

    private TextDrawable(Builder builder) {
        super(builder.shape);

        // shape properties
        shape = builder.shape;
        height = builder.height;
        width = builder.width;
        radius = builder.radius;

        // text and color
        text = builder.toUpperCase ? builder.text.toUpperCase() : builder.text;

        // text paint settings
        fontSize = builder.fontSize;
        textPaint = new Paint();
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(builder.borderThickness);

        // border paint settings
        borderThickness = builder.borderThickness;
        borderColor = builder.borderColor;
        borderPaint = new Paint();
        if (borderColor == -1) borderPaint.setColor(getDarkerShade(builder.color));
        else borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);

        // drawable paint color
        Paint paint = getPaint();
        paint.setColor(builder.color);

        //custom centre drawable
        if (builder.drawable != null) {
            if (builder.drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) builder.drawable).getBitmap();
            } else {
                bitmap = Bitmap.createBitmap(builder.drawable.getIntrinsicWidth(),
                        builder.drawable.getIntrinsicHeight(),
                        builder.drawable.getOpacity() != PixelFormat.OPAQUE ?
                                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                builder.drawable.setBounds(0, 0, builder.drawable.getIntrinsicWidth(),
                        builder.drawable.getIntrinsicHeight());
                builder.drawable.draw(canvas);
            }
        }

    }

    private int getDarkerShade(@ColorInt int color) {
        return Color.rgb((int) (SHADE_FACTOR * Color.red(color)),
                (int) (SHADE_FACTOR * Color.green(color)),
                (int) (SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();

        // draw border
        if (borderThickness > 0)
            drawBorder(canvas);

        int count = canvas.save();
        if (bitmap == null) {
            canvas.translate(r.left, r.top);
        }

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? (Math.min(width, height) / 2) : this.fontSize;
        textPaint.setTextSize(fontSize);
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, width / 2, height / 2 - textBounds.exactCenterY(), textPaint);

        if (bitmap == null) {
            textPaint.setTextSize(fontSize);
            canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        } else {
            canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2, (height - bitmap.getHeight()) / 2, null);
        }
        canvas.restoreToCount(count);

    }

    private void drawBorder(Canvas canvas) {
        RectF rect = new RectF(getBounds());
        rect.inset(borderThickness / 2, borderThickness / 2);

        if (shape instanceof OvalShape) {
            canvas.drawOval(rect, borderPaint);
        } else if (shape instanceof RoundRectShape) {
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        } else {
            canvas.drawRect(rect, borderPaint);
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap;
        if (getIntrinsicWidth() <= 0 || getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, getOpacity() != PixelFormat.OPAQUE ?
                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(getIntrinsicWidth(), getIntrinsicHeight(),
                    getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        draw(canvas);
        return bitmap;
    }

    public static IShapeBuilder builder(@NonNull Context context) {
        return new Builder(context);
    }

    public static class Builder implements IConfigBuilder, IShapeBuilder, IBuilder {

        private Context context;
        private String text;
        private int color;
        private int borderColor;
        private int borderThickness;
        private int width;
        private int height;
        private Typeface font;
        private RectShape shape;
        public int textColor;
        private int fontSize;
        private boolean isBold;
        private boolean toUpperCase;
        public float radius;
        public Drawable drawable;

        private Builder(@NonNull Context context) {
            this.context = context;
            text = "";
            color = Color.GRAY;
            textColor = Color.WHITE;
            borderColor = -1;
            borderThickness = 0;
            width = -1;
            height = -1;
            shape = new RectShape();
            fontSize = -1;
            isBold = false;
            toUpperCase = false;
        }

        @Override
        public IConfigBuilder width(@IntRange(from = 1, to = Integer.MAX_VALUE) int width) {
            this.width = width;
            return this;
        }

        @Override
        public IConfigBuilder widthRes(@DimenRes int widthRes) {
            return width((int) context.getResources().getDimension(widthRes));
        }

        @Override
        public IConfigBuilder height(@IntRange(from = 1, to = Integer.MAX_VALUE) int height) {
            this.height = height;
            return this;
        }

        @Override
        public IConfigBuilder heightRes(@DimenRes int heightRes) {
            return height((int) context.getResources().getDimension(heightRes));
        }

        @Override
        public IConfigBuilder textColor(@ColorInt int color) {
            this.textColor = color;
            return this;
        }

        @Override
        public IConfigBuilder withBorder(@IntRange(from = 1, to = Integer.MAX_VALUE) int thickness) {
            return withBorder(thickness, this.borderColor);
        }

        public IConfigBuilder withBorder(@IntRange(from = 1, to = Integer.MAX_VALUE) int thickness, @ColorInt int color) {
            this.borderThickness = thickness;
            this.borderColor = color;
            return this;
        }

        @Override
        public IConfigBuilder withBorderRes(@DimenRes int thicknessRes) {
            return withBorder((int) context.getResources().getDimension(thicknessRes));
        }

        @Override
        public IConfigBuilder withBorderRes(@DimenRes int thicknessRes, @ColorInt int color) {
            return withBorder((int) context.getResources().getDimension(thicknessRes), color);
        }

        @Override
        public IConfigBuilder useFont(@NonNull Typeface font) {
            this.font = font;
            return this;
        }

        @Override
        public IConfigBuilder useFont(@NonNull String name, int style) {
            this.font = TypefaceHelper.get(name, style);
            return this;
        }

        @Override
        public IConfigBuilder fontSize(@IntRange(from = 1, to = Integer.MAX_VALUE) int size) {
            this.fontSize = size;
            return this;
        }

        @Override
        public IConfigBuilder fontSizeRes(@DimenRes int sizeRes) {
            return fontSize((int) context.getResources().getDimension(sizeRes));
        }

        @Override
        public IConfigBuilder bold() {
            this.isBold = true;
            return this;
        }

        @Override
        public IConfigBuilder color(@ColorInt int color) {
            this.color = color;
            return this;
        }

        @Override
        public IConfigBuilder toUpperCase() {
            this.toUpperCase = true;
            return this;
        }

        @Override
        public IConfigBuilder beginConfig() {
            return this;
        }

        @Override
        public IShapeBuilder endConfig() {
            return this;
        }

        @Override
        public IBuilder rect() {
            this.shape = new RectShape();
            return this;
        }

        @Override
        public IBuilder round() {
            this.shape = new OvalShape();
            return this;
        }

        @Override
        public IBuilder roundRect(@IntRange(from = 1, to = Integer.MAX_VALUE) int radius) {
            this.radius = radius;
            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            this.shape = new RoundRectShape(radii, null, null);
            return this;
        }

        @Override
        public IBuilder roundRectRes(@IntRange(from = 1, to = Integer.MAX_VALUE) int radius) {
            return roundRect((int) context.getResources().getDimension(radius));
        }

        @Override
        public TextDrawable buildRect(@NonNull String text, @ColorInt int color) {
            rect();
            return build(text, color);
        }

        @Override
        public TextDrawable buildRect(@NonNull Drawable drawable, int color) {
            rect();
            return build(drawable, color);
        }

        @Override
        public TextDrawable buildRoundRect(@NonNull String text, @ColorInt int color, @IntRange(from = 1, to = Integer.MAX_VALUE) int radius) {
            roundRect(radius);
            return build(text, color);
        }

        @Override
        public TextDrawable buildRoundRect(@NonNull Drawable drawable, @ColorInt int color, @IntRange(from = 1, to = Integer.MAX_VALUE) int radius) {
            roundRect(radius);
            return build(drawable, color);
        }
        @Override
        public TextDrawable buildRound(@NonNull String text, @ColorInt int color) {
            round();
            return build(text, color);
        }

        @Override
        public TextDrawable buildRound(@NonNull Drawable drawable, @ColorInt int color) {
            round();
            return build(drawable, color);
        }

        @Override
        public TextDrawable build(@NonNull String text) {
            if (this.font == null)
                this.font = TypefaceHelper.get("sans-serif-light", Typeface.NORMAL);
            this.text = text;
            return new TextDrawable(this);
        }

        @Override
        public TextDrawable build(@NonNull String text, @ColorInt int color) {
            if (this.font == null)
                this.font = TypefaceHelper.get("sans-serif-light", Typeface.NORMAL);
            this.color = color;
            this.text = text;
            return new TextDrawable(this);
        }

        @Override
        public TextDrawable build(@NonNull Drawable drawable, @ColorInt int color) {
            this.drawable = drawable;
            this.color = color;
            return new TextDrawable(this);
        }
    }

    public interface IConfigBuilder {
        IConfigBuilder width(@IntRange(from = 1, to = Integer.MAX_VALUE) int width);

        IConfigBuilder widthRes(@DimenRes int width);

        IConfigBuilder height(@IntRange(from = 1, to = Integer.MAX_VALUE) int height);

        IConfigBuilder heightRes(@DimenRes int height);

        IConfigBuilder textColor(@ColorInt int color);

        IConfigBuilder withBorder(@IntRange(from = 1, to = Integer.MAX_VALUE) int thickness);

        IConfigBuilder withBorder(@IntRange(from = 1, to = Integer.MAX_VALUE) int thickness, @ColorInt int color);

        IConfigBuilder withBorderRes(@DimenRes int thickness);

        IConfigBuilder withBorderRes(@DimenRes int thickness, @ColorInt int color);

        IConfigBuilder useFont(@NonNull Typeface font);

        IConfigBuilder useFont(@NonNull String name, int style);

        IConfigBuilder fontSize(@IntRange(from = 1, to = Integer.MAX_VALUE) int size);

        IConfigBuilder fontSizeRes(@DimenRes int size);

        IConfigBuilder bold();

        IConfigBuilder color(@ColorInt int color);

        IConfigBuilder toUpperCase();

        IShapeBuilder endConfig();
    }

    public interface IBuilder {

        TextDrawable build(@NonNull String text);

        TextDrawable build(@NonNull String text, @ColorInt int color);

        TextDrawable build(@NonNull Drawable drawable, @ColorInt int color);

    }

    public interface IShapeBuilder {

        IConfigBuilder beginConfig();

        IBuilder rect();

        IBuilder round();

        IBuilder roundRect(@IntRange(from = 1, to = Integer.MAX_VALUE) int radius);

        IBuilder roundRectRes(@DimenRes int radius);

        TextDrawable buildRect(@NonNull String text, @ColorInt int color);

        TextDrawable buildRect(@NonNull Drawable drawable, @ColorInt int color);

        TextDrawable buildRoundRect(@NonNull String text, @ColorInt int color, @IntRange(from = 1, to = Integer.MAX_VALUE) int radius);

        TextDrawable buildRoundRect(@NonNull Drawable drawable, @ColorInt int color, @IntRange(from = 1, to = Integer.MAX_VALUE) int radius);

        TextDrawable buildRound(@NonNull String text, @ColorInt int color);

        TextDrawable buildRound(@NonNull Drawable drawable, @ColorInt int color);
    }
}