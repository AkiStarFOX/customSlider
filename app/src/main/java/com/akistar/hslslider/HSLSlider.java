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

/**
 * Created by AkiStar on 04.04.2018.
 */

public class HSLSlider extends View implements View.OnTouchListener {
    public interface OnValueChangeListener {
        void onValueChange(HSLSlider slider, float value);
    }
    public void setOnValueChangeListener(OnValueChangeListener listener) {
        _valueChangeListener = listener;
    }

    private float _sliderHeight; // _sliderHeight
    private float _thumbRadius; // _thumbRadius
    private SliderType _sliderType; // _sliderType и сделать как enum
    private final static float DEFAULT_HEIGHT = 10;
    private final static float DEFAULT_RADIUS = 10;
    private float _heightV;
    private float _widthV;
    private float _thumbPos; // thumbPos
    private float _xDown;
    private boolean _move = false; // _move
    private HSL color;
    private float _paddingLeft;
    private float _paddingRight;
    private float _sliderMin;
    private float _sliderMax;
    private float _startThumbPos;
    private float _sliderValue;
    private float _sliderMaxValue;
    private float _sliderMinValue;
    private float _sliderMove;
    private OnValueChangeListener _valueChangeListener;
    Paint p;
    RectF _rect;
    Bitmap backImage;
    private boolean _firstTime;


    public HSL getColor() {
        return color;
    }

    public void setColor(HSL color) {

        this.color.set(color); // this.color.set(color), не надо тут ссылки использовать
        // положение бегунка тоже должно меняться

        if (_sliderType == SliderType.HUE) {


            _startThumbPos = color.h / 360;
        }
        if (_sliderType == SliderType.SATURATION) {
            HSL c = new HSL(color);

            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                c.s = i / 100f;
                mass[i] = c.toColor();
            }


            _startThumbPos = color.s;
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);


        }
        if (_sliderType == SliderType.LIGHTNESS) {
            HSL c = new HSL(color);
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                c.l = i / 100f;
                mass[i] = c.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);


            _startThumbPos = color.l;
        }

    }


    public HSLSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        _rect = new RectF();
        int[] attributesPadding = new int[]{android.R.attr.paddingLeft, android.R.attr.paddingRight};
        TypedArray attPadding = context.obtainStyledAttributes(attrs, attributesPadding);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HSLSlider);
        _sliderHeight = attributes.getDimension(R.styleable.HSLSlider_heigthSlider, DEFAULT_HEIGHT);
        _thumbRadius = attributes.getDimension(R.styleable.HSLSlider_radiusPoint, DEFAULT_RADIUS);
        _sliderType = SliderType.values()[attributes.getInt(R.styleable.HSLSlider_type, 1)];
        _paddingLeft = attPadding.getDimension(0, -1);
        _paddingRight = attPadding.getDimension(1, -1);

        // одного цвета достаточно + у color должен быть дефолтный цвет (0, 0.5, 0.5)
        color = new HSL(0f, 0.5f, 0.5f);

        if (_sliderType == SliderType.HUE) {
            int[] mass = new int[360];
            color.s = 1f;
            for (int i = 0; i < 360; i++) {
                color.h = i;
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 360, 1, Bitmap.Config.ARGB_8888);
            _startThumbPos = 0.01f;
            color.s = 0.5f;
        }
        if (_sliderType == SliderType.SATURATION) {
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color.s = i / 100f;
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            _startThumbPos = 0.5f;
        }
        if (_sliderType == SliderType.LIGHTNESS) {
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color.l = i / 100f;
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            _startThumbPos = 0.5f;
        }
        // дефолтное положение бегунка разное для каждого типа слайдера
        this.setOnTouchListener(this);
        _firstTime = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _heightV = canvas.getHeight();
        _widthV = canvas.getWidth();
        _sliderMin = _thumbRadius + _paddingLeft;
        _sliderMax = _widthV - _thumbRadius - _paddingRight;
        _rect.set(_sliderMin, (_heightV / 2 - _sliderHeight / 2), _sliderMax, (_heightV / 2 + _sliderHeight / 2));
        _sliderMinValue = (_thumbPos - _thumbRadius - _paddingLeft);
        _sliderMaxValue = (((_widthV - _thumbRadius) - _paddingRight) - _paddingLeft - _thumbRadius);

        // незачем создавать соскейленный битмап посмотри в доках описание метода drawBitmap

        // числитель и знаменатель распихай по отдельным переменным, что красиво было. Типа sliderValue, maxSliderValue
        // и значение value = sliderValue / maxSliderValue вынеси за switch, а то ты один и тот же кода юзаешь везде
        // + меняй всё это дело в методе onTouch
        if (_firstTime) {
            _thumbPos = (_startThumbPos * (_sliderMax - _sliderMin) + _sliderMin);
        }
        _firstTime = false;


        // для saturation и lightness незачем при каждой отрисовке создавать битмап делай это в setColor и в конструкторе с дефолтным цветом


        canvas.drawBitmap(backImage, null, _rect, p);
        p.setShader(null);
        p.setColor(Color.RED);
        if (_thumbPos <= _thumbRadius + _paddingLeft) {
            _thumbPos = _thumbRadius + _paddingLeft;
        }
        if (_thumbPos >= (_widthV - _thumbRadius) - _paddingRight) {
            _thumbPos = (_widthV - _thumbRadius) - _paddingRight;
        }
        canvas.drawCircle(_thumbPos, _heightV / 2, _thumbRadius, p);
        this.invalidate();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // y не нужно чекать, если ты сюда попал, то с y всё ок
                if (motionEvent.getX() >= _thumbRadius + _paddingLeft && motionEvent.getX() <= (_widthV - _thumbRadius) - _paddingRight) {
                    _thumbPos = motionEvent.getX();
                    _move = true;
                    this.invalidate();
                    _xDown = motionEvent.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (_move) {
                    _thumbPos += motionEvent.getX() - _xDown;
                    this.invalidate();
                    _xDown = motionEvent.getX();
                }

                if (_sliderType == SliderType.HUE) {
                    color.h = (_sliderMinValue / _sliderMaxValue) * 360;
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.h);
                    }
                }
                if (_sliderType == SliderType.SATURATION) {
                    color.s = (_sliderMinValue / _sliderMaxValue);
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.s);
                    }
                }
                if (_sliderType == SliderType.LIGHTNESS) {
                    color.l = (_sliderMinValue / _sliderMaxValue);
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.l);
                    }
                }
                break;


        }

        return true;
    }

    // это должно располагаться до полей


    // расположи это вместе с остальными полями



}
