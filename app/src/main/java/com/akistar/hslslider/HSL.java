package com.akistar.hslslider;

import android.support.v4.graphics.ColorUtils;



/**
 * Created by NERV on 22.12.2017.
 */

/**
 * HSL цвет (Hue Saturation Lightness)
 */
public class HSL implements Copyable<HSL> {
    private static float HUE_EPSILON = 0.001f; // для сравнения hue компонент
    private static float SL_EPSILON = 0.00001f; // для сравнения saturation, lightness компонент
    public float h; // hue, 0..360
    public float s; // saturation, 0..1
    public float l; // lightness, 0..1

    public HSL(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        h = hsl[0];
        s = hsl[1];
        l = hsl[2];
    }

    public HSL(float h, float s, float l) {
        this.h = h;
        this.s = s;
        this.l = l;
    }
    public HSL(HSL hsl){
        set(hsl);
    }

    /**
     * Сравнение с другим HSL цветом
     * @param c - HSL цвет
     * @return - true, если цвета совпадают
     */
    public boolean equals(HSL c) {
        return Math.abs(c.h - h) < HUE_EPSILON
                && Math.abs(c.s - s) < SL_EPSILON
                && Math.abs(c.l - l) < SL_EPSILON;
    }

    /**
     * Конвертировать из HSL модели в RGB модель
     * @return - RGB цвет
     */
    public RGB toRGB() {
        int color = ColorUtils.HSLToColor(new float[] {h, s, l});
        float r = (float)((color >> 16) & 0xFF) / 255f;
        float g = (float)((color >> 8) & 0xFF) / 255f;
        float b = (float)(color & 0xFF) / 255f;

        return new RGB(r, g, b);
    }

    public int toColor() {
        return ColorUtils.HSLToColor(new float[] {h, s, l});
    }

    /**
     * Конвертировать HSL в int
     * @return - int цвет
     */
    public static int toColor(float h, float s, float l) {
        return ColorUtils.HSLToColor(new float[] {h, s, l});
    }

    public void set(HSL hsl) {
        h = hsl.h;
        s = hsl.s;
        l = hsl.l;
    }

    public void set(float h, float s, float l) {
        this.h = h;
        this.s = s;
        this.l = l;
    }

    public void set(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        h = hsl[0];
        s = hsl[1];
        l = hsl[2];
    }

    @Override
    public HSL copy() {
        return new HSL(h, s, l);
    }
}