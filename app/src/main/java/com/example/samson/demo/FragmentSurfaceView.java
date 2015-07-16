package com.example.samson.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by samson on 14.07.15.
 */
public class FragmentSurfaceView extends Fragment implements View.OnClickListener {

    private MainActivity mCallingActivity;

    private CustomSurfaceView surfaceView;
    private Button btnExit;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallingActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_surface,null);

        findUI(view);
        setListener();
        return view;
    }

    private void findUI(View _view){
        surfaceView         = (CustomSurfaceView) _view.findViewById(R.id.surface);
        btnExit             = (Button) _view.findViewById(R.id.btnExitSave);
    }

    private void setListener(){
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnExitSave:
                mCallingActivity.setBitmapImage(surfaceView.getBitmap());
                mCallingActivity.setVisivilityViews(true);
                break;
        }
    }
}
