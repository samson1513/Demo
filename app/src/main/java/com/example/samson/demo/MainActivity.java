package com.example.samson.demo;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView image;
    private LinearLayout toolbar;
    private FrameLayout container;
    private Button btnOnDraw, btnSurface, btnAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findUI();
        setListeners();
        setVisivilityViews(true);
    }

    private void findUI(){
        image           = (ImageView) findViewById(R.id.image);
        toolbar         = (LinearLayout) findViewById(R.id.toolbar);
        container       = (FrameLayout) findViewById(R.id.container);
        btnOnDraw       = (Button) findViewById(R.id.btnOnDraw);
        btnSurface      = (Button) findViewById(R.id.btnSurface);
        btnAnimation    = (Button) findViewById(R.id.btnAnim);
    }

    private void setListeners(){
        btnSurface.setOnClickListener(this);
        btnOnDraw.setOnClickListener(this);
        btnAnimation.setOnClickListener(this);
    }

    public void setVisivilityViews(boolean _isVisible){
        if(_isVisible){
            image.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
    }

    public void setBitmapImage(Bitmap _bitmap){
        image.setImageBitmap(_bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOnDraw:
                replaceFragment(new FragmentCustomView());
                break;
            case R.id.btnSurface:
                replaceFragment(new FragmentSurfaceView());
                break;
            case R.id.btnAnim:
                replaceFragment(new FragmentAnimationView());
                break;
        }
    }

    private void replaceFragment(Fragment _fragment){
        setVisivilityViews(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, _fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setVisivilityViews(true);
    }
}
