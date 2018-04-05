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

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener{
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
//        s.setColor(h.getColor());
//        l.setColor(s.getColor());

        hsl = new HSL(h.getColor().h, s.getColor().s, l.getColor().l);
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        hsl.set(l.getColor());
        bitmap.eraseColor(hsl.toColor());
        layout.setOnTouchListener(this);




    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d("TAG", "h = " + h.getColor().h + " s = " + h.getColor().s + " l = " + h.getColor().l);
        Log.d("TAG", "h2 = " + s.getColor().h + " s2 = " + s.getColor().s + " l2 = " + s.getColor().l);
        Log.d("TAG", "h3 = " + l.getColor().h + " s3 = " + l.getColor().s + " l3 = " + l.getColor().l);

        return true;
    }
}
