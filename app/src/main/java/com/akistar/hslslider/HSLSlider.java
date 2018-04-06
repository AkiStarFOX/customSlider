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
import android.view.ViewTreeObserver;

/**
 * Created by AkiStar on 04.04.2018.
 */

public class HSLSlider extends View implements View.OnTouchListener {

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
    HSL color2;
    HSL color3;
    private float _paddingLeft;
    private float _paddingRight;


    public HSL getColor() {
        return color;
    }

    public void setColor(HSL color) {
        this.color.set(color); // this.color.set(color), не надо тут ссылки использовать
		// положение бегунка тоже должно меняться
    }


    Paint p;
    RectF _rect;
    Bitmap backImage;

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
        _sliderType = SliderType.values()[attributes.getInt(R.styleable.HSLSlider_type,1)];
        _paddingLeft = attPadding.getDimension(0, -1);
        _paddingRight = attPadding.getDimension(1, -1);

		// одного цвета достаточно + у color должен быть дефолтный цвет (0, 0.5, 0.5)
        color = new HSL(0f, 0.5f, 0.5f);

        if (_sliderType==SliderType.HUE) {
            int[] mass = new int[360];
            for (int i = 0; i < 360; i++) {
                color.set(i, 1f, 0.5f);
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 360, 1, Bitmap.Config.ARGB_8888);
            _thumbPos = _thumbRadius + _paddingLeft;
        }
        if (_sliderType==SliderType.SATURATION){
            _thumbPos = _thumbRadius + _paddingLeft+_widthV/2;
        }
        // дефолтное положение бегунка разное для каждого типа слайдера
        this.setOnTouchListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _heightV = canvas.getHeight();
        _widthV = canvas.getWidth();
		

        if (_sliderType==SliderType.HUE) {
			// незачем создавать соскейленный битмап посмотри в доках описание метода drawBitmap 
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int) ((_widthV - _thumbRadius * 2) - (_paddingLeft + _paddingRight)), (int) _sliderHeight, true), _thumbRadius + _paddingLeft, _heightV / 2 - _sliderHeight / 2, p);
            
			// числитель и знаменатель распихай по отдельным переменным, что красиво было. Типа sliderValue, maxSliderValue 
			// и значение value = sliderValue / maxSliderValue вынеси за switch, а то ты один и тот же кода юзаешь везде
			// + меняй всё это дело в методе onTouch
			color.h = ((_thumbPos - _thumbRadius - _paddingLeft) / (((_widthV - _thumbRadius) - _paddingRight) - _paddingLeft - _thumbRadius)) * 360;


        } else if (_sliderType==SliderType.SATURATION) {
			// для saturation и lightness незачем при каждой отрисовке создавать битмап делай это в setColor и в конструкторе с дефолтным цветом
            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color.set(color.h, i / 100f, color.l);
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int) ((_widthV - _thumbRadius * 2) - (_paddingLeft + _paddingRight)), (int) _sliderHeight, true), _thumbRadius + _paddingLeft, _heightV / 2 - _sliderHeight / 2, p);
            
			color.s = ((_thumbPos - _thumbRadius - _paddingLeft) / (((_widthV - _thumbRadius) - _paddingRight) - _paddingLeft - _thumbRadius)) * 100;

        } else if (_sliderType==SliderType.LIGHTNESS) {

            int[] mass = new int[100];
            for (int i = 0; i < 100; i++) {
                color.set(color.h, color.s, i / 100f);
                mass[i] = color.toColor();
            }
            backImage = Bitmap.createBitmap(mass, 100, 1, Bitmap.Config.ARGB_8888);
            canvas.drawBitmap(Bitmap.createScaledBitmap(backImage, (int) ((_widthV - _thumbRadius * 2) - (_paddingLeft + _paddingRight)), (int) _sliderHeight, true), _thumbRadius + _paddingLeft, _heightV / 2 - _sliderHeight / 2, p);
            color.l = ((_thumbPos - _thumbRadius - _paddingLeft) / (((_widthV - _thumbRadius) - _paddingRight) - _paddingLeft - _thumbRadius)) * 100;
        }
        p.setShader(null);
        p.setColor(Color.RED);
        canvas.drawCircle(_thumbPos, _heightV / 2, _thumbRadius, p);
        this.invalidate();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
				// y не нужно чекать, если ты сюда попал, то с y всё ок
                if (motionEvent.getY() >= (_heightV / 2 - _sliderHeight / 2) && motionEvent.getY() <= (_heightV / 2 + _sliderHeight / 2) && motionEvent.getX() >= _thumbRadius + _paddingLeft && motionEvent.getX() <= (_widthV - _thumbRadius) - _paddingRight) {
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
                if (_thumbPos <= _thumbRadius + _paddingLeft) {
                    _thumbPos = _thumbRadius + _paddingLeft;
                }
                if (_thumbPos >= (_widthV - _thumbRadius) - _paddingRight) {
                    _thumbPos = (_widthV - _thumbRadius) - _paddingRight;
                }
				
				
                if (_sliderType.equals("hue")) {
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.h);
                    }
                }
                if (_sliderType.equals("saturation")) {
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.s);
                    }
                }
                if (_sliderType.equals("lightness")) {
                    if (_valueChangeListener != null) {
                        _valueChangeListener.onValueChange(this, color.l);
                    }
                }
        }
        return true;
    }

	// это должно располагаться до полей
    public interface OnValueChangeListener {
        void onValueChange(HSLSlider slider, float value);
    }

	// расположи это вместе с остальными полями
    private OnValueChangeListener _valueChangeListener;

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        _valueChangeListener = listener;
    }


}
