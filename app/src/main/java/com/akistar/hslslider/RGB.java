package com.akistar.hslslider;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by NERV on 22.12.2017.
 */

/**
 * RGB цвет
 */
public class RGB implements Copyable<Object> {
    //значения 0..1
    public float r; // красная компонента
    public float g; // зеленая компонента
    public float b; // синяя компонента

    public RGB() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public RGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set(RGB rgb) {
        this.r = rgb.r;
        this.g = rgb.g;
        this.b = rgb.b;
    }

    /**
     * Конвертировать из RGB модели в HSL модель
     * @return - RGB цвет
     */
    public HSL toHSL() {
        float[] hsl = new float[3];
        ColorUtils.RGBToHSL((int)(255f * r), (int)(255f * g), (int)(255f * b), hsl);
        return new HSL(hsl[0], hsl[1], hsl[2]);
    }

    /**
     * Конвертировать RGB в int
     * @return - int цвет
     */
    public int toColor() {
        return Color.rgb((int)(255f * r), (int)(255f * g), (int)(255f * b));
    }

    @Override
    public Object copy() {
        return new RGB(r, g, b);
    }
}