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
import android.view.MotionEvent;
import android.view.View;

import static com.akistar.hslslider.SliderType.HUE;

/**
 * Created by AkiStar on 04.04.2018.
 */

public class HSLSlider extends View implements View.OnTouchListener {
    public interface OnValueChangeListener {
        void onValueChange(HSLSlider slider, float value);
    }

    private final static float DEFAULT_HEIGHT = 5;
    private final static float DEFAULT_RADIUS = 5;
    private float _halfSliderHeight = DEFAULT_HEIGHT / 2f;// _sliderHeight
    private float _thumbRadius = DEFAULT_RADIUS; // _thumbRadius
    private SliderType _sliderType = SliderType.HUE; // _sliderType и сделать как enum
    private float _thumbPos = 0f; // thumbPos
    private HSL _color = new HSL(0f, 0.5f, 0.5f);;
    private Paint _paint = new Paint();
    private RectF _rect = new RectF();
    private Bitmap _backImage;


    private OnValueChangeListener _valueChangeListener;

    public HSLSlider (Context context){
        super(context);
        setOnTouchListener(this);
        _paint.setAntiAlias(true);
        createProgressBitmap();

    }

    public HSLSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HSLSlider);

        _halfSliderHeight= attributes.getDimension(R.styleable.HSLSlider_heigthSlider, DEFAULT_HEIGHT)/2f;
        _thumbRadius= attributes.getDimension(R.styleable.HSLSlider_radiusPoint, DEFAULT_RADIUS);
        _sliderType = SliderType.values()[attributes.getInt(R.styleable.HSLSlider_type, 1)];

        _thumbPos = _sliderType==SliderType.HUE ? 1.f : 0.5f;

        setOnTouchListener(this);
        createProgressBitmap();

        _paint.setAntiAlias(true);
    }

    private void createProgressBitmap(){
        int width = _sliderType==SliderType.HUE ? 361 : 101;
        int[] colors = new int[width];
        HSL c = _sliderType==SliderType.HUE ? new HSL (0,1f,0.5f) : new HSL(_color);

        for (int i=0;i<width;i++){
            float v = (float) i / (width - 1);

            switch (_sliderType){
                case HUE:
                    c.h = v * 360;
                    break;
                case LIGHTNESS:
                    c.l = v;
                    break;
                case SATURATION:
                    c.s = v;
                    break;
            }
            colors[i]=c.toColor();
        }
        _backImage = Bitmap.createBitmap(colors,width,1, Bitmap.Config.ARGB_8888);
    }


    public HSL getColor() {
        return _color;
    }

    public void setColor(HSL color) {
        _color.set(color);

        if (_sliderType != SliderType.HUE){
            createProgressBitmap();
            _thumbPos = _sliderType==SliderType.SATURATION ? _color.s: _color.l;
        } else {
            _thumbPos = color.s/360;
        }
        invalidate();
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        _valueChangeListener = listener;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        float halfHeight = getHeight()/2;
        float sliderStart = getPaddingLeft() + _thumbRadius;
        float sliderEnd = getWidth() - getPaddingRight() - _thumbRadius;
        float thumbCenterX = (sliderEnd-sliderStart) * _thumbPos + sliderStart;

        _rect.set(sliderStart,halfHeight-_halfSliderHeight,sliderEnd,halfHeight+_halfSliderHeight);

        canvas.drawBitmap(_backImage, null, _rect, _paint);

        _paint.setColor(Color.RED);
        canvas.drawCircle(thumbCenterX, halfHeight, _thumbRadius, _paint);

    }

    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float sliderStart = getPaddingLeft() + _thumbRadius;
        float sliderEnd = getWidth() - getPaddingRight() - _thumbRadius;
        float sliderWidth = sliderEnd - sliderStart;

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = clamp(motionEvent.getX() - sliderStart, 0, sliderWidth);
                _thumbPos = x / sliderWidth;

                if (_valueChangeListener != null) {
                    _valueChangeListener.onValueChange(this
                            , _sliderType == HUE ? _thumbPos * 360f : _thumbPos);
                }

                invalidate();
        }

        return true;
    }




}
