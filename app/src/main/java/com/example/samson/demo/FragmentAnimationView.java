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
    private Button mThread, mAsync, mClose;
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
        mAsync = (Button) _view.findViewById(R.id.btnAnimSurface);
        mThread = (Button) _view.findViewById(R.id.btnAnimThread);
        mClose = (Button) _view.findViewById(R.id.btnClose);
        contAnim = (LinearLayout) _view.findViewById(R.id.contAnim);

        animationView = new AnimationView(mCallingActivity);
        animationView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        contAnim.addView(animationView);
    }

    private void setListener(){
        mClose.setOnClickListener(this);
        mAsync.setOnClickListener(this);
        mThread.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                if(!animationView.isAnimate())
                    mCallingActivity.setVisivilityViews(true);
                break;
            case R.id.btnAnimSurface:
                if(!animationView.isAnimate())
                    animationView.showAnim(Constants.ANIM_SURFACE);
                break;
            case R.id.btnAnimThread:
                if(!animationView.isAnimate())
                    animationView.showAnim(Constants.ANIM_THREAD);
                break;
        }
    }
}
