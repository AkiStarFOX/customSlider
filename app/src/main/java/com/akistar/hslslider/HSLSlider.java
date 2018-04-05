package com.akistar.hslslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by AkiStar on 04.04.2018.
 */

public class HSLSlider extends View implements View.OnTouchListener {

    private float _heightSlider;
    private float _radiusPoint;
    private String _typeOfSlider;
    private final static float DEFAULT_HEIGHT = 10;
    private final static float DEFAULT_RADIUS = 10;
    private float _heightV;
    private float _widthV;
    private float _xCenter;
    private float _xDown;
    private boolean move = false;
    private HSL color;
    HSL color2;
    HSL color3;
    ImageView img;


    public HSL getColor() {
        return color;
    }

    public void setColor(HSL color) {
        this.color = color;
    }


    Paint p;
    RectF _rect;
    Bitmap backImage;

    public HSLSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        _rect = new RectF();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HSLSlider);
        _heightSlider = attributes.getDimension(R.styleable.HSLSlider_heigthSlider, DEFAULT_HEIGHT);
        _radiusPoint = attributes.getDimension(R.styleable.HSLSlider_radiusPoint, DEFAULT_RADIUS);
        _typeOfSlider = attributes.getString(R.styleable.HSLSlider_type);

        color = new HSL(150f, 1f, 0.5f);
        color2 = new HSL(150f, 1f, 0.5f);
        color3 = new HSL(150f, 1f, 0.5f);
        if (_typeOfSlider.equals("hue")) {
            int[] mass = new int[360];
            for (int i = 0; i < 360; i++) {
                color.set(i, 1f, 0.5f);
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 360, 1, Bitmap.Config.ARGB_8888);
        }
        _xCenter=_radiusPoint;
        this.setOnTouchListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _heightV = canvas.getHeight();
        _widthV = canvas.getWidth();
        if (_typeOfSlider.equals("hue")) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int)(_widthV - _radiusPoint * 2), (int) _heightSlider, false), _radiusPoint, _heightV / 2 - _heightSlider / 2, p);
            color.h = (_xCenter-_radiusPoint) / ((_widthV-_radiusPoint*2) / 360);

        } else if (_typeOfSlider.equals("saturation")) {
            color2.set(color);
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color2.set(color2.h, i / 100f, 0.5f);
                mass[i] = color2.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int) (_widthV - _radiusPoint * 2), (int) _heightSlider, false), _radiusPoint, _heightV / 2 - _heightSlider / 2, p);
            color.s = ((_xCenter-_radiusPoint) / ((_widthV-_radiusPoint*2) / 100)) / 100;

        } else if (_typeOfSlider.equals("lightness")) {
            color3.set(color);
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color3.set(color3.h, 1.0f, i / 100f);
                mass[i] = color3.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int) (_widthV - _radiusPoint * 2), (int) _heightSlider, false), _radiusPoint, _heightV / 2 - _heightSlider / 2, p);
            color.l = ((_xCenter-_radiusPoint) / ((_widthV-_radiusPoint*2) / 100)) / 100;
        }
        p.setShader(null);
        p.setColor(Color.RED);
        canvas.drawCircle(_xCenter, _heightV / 2, _radiusPoint, p);
        this.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (motionEvent.getY() >= (_heightV / 2 - _heightSlider / 2) && motionEvent.getY() <= (_heightV / 2 + _heightSlider / 2) && motionEvent.getX() >= _radiusPoint && motionEvent.getX() <= _widthV - _radiusPoint) {
                    _xCenter = motionEvent.getX();
                    move = true;
                    this.invalidate();
                    _xDown = motionEvent.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (move) {
                    _xCenter += motionEvent.getX() - _xDown;
                    this.invalidate();
                    _xDown = motionEvent.getX();
                }
                if (_xCenter <= _radiusPoint) {
                    _xCenter = _radiusPoint;
                }
                if (_xCenter >= _widthV - _radiusPoint) {
                    _xCenter = _widthV - _radiusPoint;
                }
        }
        return true;
    }
    public interface HSLSliderTouch {


    }

}