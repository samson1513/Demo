package com.example.samson.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by samson on 15.07.15.
 */
public class FragmentAnimationView extends Fragment implements View.OnClickListener {

    private MainActivity mCallingActivity;

    private AnimationView animationView;
    private AnimationSurfaceView animationSurface;
    private Button mThread, mSurface, mClose;
    private LinearLayout contAnim;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallingActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation,null);

        findUI(view);
        setListener();
        return view;
    }

    private void findUI(View _view){
        mSurface            = (Button) _view.findViewById(R.id.btnAnimSurface);
        mThread             = (Button) _view.findViewById(R.id.btnAnimThread);
        mClose              = (Button) _view.findViewById(R.id.btnClose);

        animationView       = (AnimationView) _view.findViewById(R.id.animView);
        animationSurface    = (AnimationSurfaceView) _view.findViewById(R.id.animSurface);
    }

    private void setListener(){
        mClose.setOnClickListener(this);
        mSurface.setOnClickListener(this);
        mThread.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                if(animIsFinished())
                    mCallingActivity.setVisivilityViews(true);
                break;
            case R.id.btnAnimSurface:
                if(animIsFinished()) {
                    animationView.setVisibility(View.GONE);
                    animationSurface.setVisibility(View.VISIBLE);
                    animationSurface.showAnim();
                }
                break;
            case R.id.btnAnimThread:
                if(animIsFinished()) {
                    animationSurface.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.showAnim();
                }
                break;
        }
    }

    private boolean animIsFinished(){
        return !(animationView.isAnimate() || animationSurface.isAnimate());
    }
}
