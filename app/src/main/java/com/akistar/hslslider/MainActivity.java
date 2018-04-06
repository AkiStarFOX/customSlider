package com.akistar.hslslider;

import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity  implements HSLSlider.OnValueChangeListener{
    ImageView img;
    HSL hsl;
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
        s.setColor(h.getColor());
        l.setColor(s.getColor());

        hsl = new HSL(h.getColor().h, s.getColor().s, l.getColor().l);
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        hsl.set(h.getColor());
        bitmap.eraseColor(hsl.toColor());
        h.setOnValueChangeListener(this);
        s.setOnValueChangeListener(this);
        l.setOnValueChangeListener(this);






    }


    @Override
    public void onValueChange(HSLSlider slider, float value) {
        if (slider==h){
            Log.d("TAG","value = " + value);
        }
        if (slider==s){
            Log.d("TAG","value = " + value);
        }
        if (slider==l){
            Log.d("TAG","value = " + value);
        }

    }
}
