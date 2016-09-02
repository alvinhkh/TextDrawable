package com.amulyakhare.td.sample.sample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * @author amulya
 * @datetime 17 Oct 2014, 4:02 PM
 */
public class DrawableProvider {

    public static final int SAMPLE_RECT = 1;
    public static final int SAMPLE_ROUND_RECT = 2;
    public static final int SAMPLE_ROUND = 3;
    public static final int SAMPLE_RECT_BORDER = 4;
    public static final int SAMPLE_ROUND_RECT_BORDER = 5;
    public static final int SAMPLE_ROUND_BORDER = 6;
    public static final int SAMPLE_ROUND_DRAWABLE = 7;
    public static final int SAMPLE_MULTIPLE_LETTERS = 8;
    public static final int SAMPLE_FONT = 9;
    public static final int SAMPLE_SIZE = 10;
    public static final int SAMPLE_ANIMATION = 11;
    public static final int SAMPLE_MISC = 12;

    private final ColorGenerator mGenerator;
    private final Context mContext;

    public DrawableProvider(Context context) {
        mGenerator = ColorGenerator.DEFAULT;
        mContext = context;
    }

    public TextDrawable getRect(String text) {
        return new TextDrawable.Builder()
                .setColor(mGenerator.getColor(text))
                .setShape(TextDrawable.SHAPE_RECT)
                .setText(text)
                .build();
    }

    public TextDrawable getRound(String text) {
        return new TextDrawable.Builder()
                .setColor(mGenerator.getColor(text))
                .setShape(TextDrawable.SHAPE_ROUND)
                .setText(text)
                .build();
    }

    public TextDrawable getRound(Drawable drawable) {
        return new TextDrawable.Builder()
                .setColor(mGenerator.getColor(drawable))
                .setDrawable(drawable)
                .setShape(TextDrawable.SHAPE_ROUND)
                .build();
    }

    public TextDrawable getRoundRect(String text) {
        return new TextDrawable.Builder()
                .setColor(mGenerator.getColor(text))
                .setRadius(toPx(10))
                .setShape(TextDrawable.SHAPE_ROUND_RECT)
                .setText(text)
                .build();
    }

    public TextDrawable getRectWithBorder(String text) {
        return new TextDrawable.Builder()
                .setBorder(toPx(2))
                .setColor(mGenerator.getColor(text))
                .setShape(TextDrawable.SHAPE_RECT)
                .setText(text)
                .build();
    }

    public TextDrawable getRoundWithBorder(String text) {
        return new TextDrawable.Builder()
                .setBorder(toPx(2))
                .setColor(mGenerator.getColor(text))
                .setShape(TextDrawable.SHAPE_ROUND)
                .setText(text)
                .build();
    }

    public TextDrawable getRoundRectWithBorder(String text) {
        return new TextDrawable.Builder()
                .setBorder(toPx(2))
                .setColor(mGenerator.getColor(text))
                .setRadius(toPx(10))
                .setShape(TextDrawable.SHAPE_ROUND_RECT)
                .setText(text)
                .build();
    }

    public TextDrawable getRectWithMultiLetter() {
        String text = "AK";
        return new TextDrawable.Builder()
                .setColor(mGenerator.getColor(text))
                .setFontSize(toPx(20))
                .setShape(TextDrawable.SHAPE_RECT)
                .setText(text)
                .build();
    }

    public TextDrawable getRoundWithCustomFont() {
        String text = "Bold";
        return new TextDrawable.Builder()
                .setBold()
                .setColor(mGenerator.getColor(text))
                .setFont(Typeface.DEFAULT)
                .setFontSize(toPx(15))
                .setShape(TextDrawable.SHAPE_RECT)
                .setText(text)
                .setTextColor(0xfff58559)
                .build();
    }

    public Drawable getRectWithCustomSize() {
        String leftText = "I";
        String rightText = "J";

        TextDrawable.Builder builder = new TextDrawable.Builder()
                .setWidth(toPx(29))
                .setBorder(toPx(2))
                .setShape(TextDrawable.SHAPE_RECT);

        TextDrawable left = builder
                .setColor(mGenerator.getColor(leftText))
                .setText(leftText)
                .build();

        TextDrawable right = builder
                .setColor(mGenerator.getColor(rightText))
                .setText(rightText)
                .build();

        Drawable[] layerList = {
                new InsetDrawable(left, 0, 0, toPx(31), 0),
                new InsetDrawable(right, toPx(31), 0, 0, 0)
        };
        return new LayerDrawable(layerList);
    }

    public Drawable getRectWithAnimation() {
        TextDrawable.Builder builder = new TextDrawable.Builder()
                .setShape(TextDrawable.SHAPE_RECT);

        AnimationDrawable animationDrawable = new AnimationDrawable();
        for (int i = 10; i > 0; i--) {
            TextDrawable frame = builder
                    .setColor(mGenerator.getRandomColor())
                    .setText(String.valueOf(i))
                    .build();
            animationDrawable.addFrame(frame, 1200);
        }
        animationDrawable.setOneShot(false);
        animationDrawable.start();

        return animationDrawable;
    }

    public int toPx(int dp) {
        Resources resources = mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
