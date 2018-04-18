package com.akistar.hslslider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements HSLSlider.OnValueChangeListener {
    ImageView img;
    HSL hsl = new HSL(Color.RED);
    HSLSlider h;
    HSLSlider s;
    HSLSlider l;
    Bitmap bitmap;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.testTouch);


        h = findViewById(R.id.h);
        s = findViewById(R.id.s);
        l = findViewById(R.id.l);

        h.setOnValueChangeListener(this);
        s.setOnValueChangeListener(this);
        l.setOnValueChangeListener(this);

        h.setColor(hsl);
        s.setColor(hsl);
        l.setColor(hsl);


    }


    @Override
    public void onValueChange(HSLSlider slider, float value) {
        switch (slider.getId()) {
            case R.id.h:
                hsl.h = value;
                s.setColor(hsl);
                l.setColor(hsl);
                break;
            case R.id.s:
                hsl.s = value;
                l.setColor(hsl);
                break;
            case R.id.l:
                hsl.l = value;
                s.setColor(hsl);

                break;

        }



    }
}
