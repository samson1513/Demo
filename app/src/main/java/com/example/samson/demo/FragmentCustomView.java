package com.example.samson.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by samson on 13.07.15.
 */
public class FragmentCustomView extends Fragment implements View.OnClickListener {

    private MainActivity mCallingActivity;
    private Button mCircle, mRect, mPoint, mExit, mText, mLine, mPath;
    private Button mFill, mStroke, mTest, mDraw;
    private CustomView paint;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallingActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_paint,null);

        findUI(view);
        setListeners();
        return view;
    }

    private void findUI(View _view){
        mCircle         = (Button) _view.findViewById(R.id.btnCircle);
        mLine           = (Button) _view.findViewById(R.id.btnLine);
        mPath           = (Button) _view.findViewById(R.id.btnPath);
        mPoint          = (Button) _view.findViewById(R.id.btnPoint);
        mRect           = (Button) _view.findViewById(R.id.btnRect);
        mText           = (Button) _view.findViewById(R.id.btnText);
        mFill           = (Button) _view.findViewById(R.id.btnFill);
        mStroke         = (Button) _view.findViewById(R.id.btnStroke);
        mTest           = (Button) _view.findViewById(R.id.btnDraw);
        mDraw           = (Button) _view.findViewById(R.id.btnOverdraw);
        mExit           = (Button) _view.findViewById(R.id.btnExit);

        paint           = (CustomView) _view.findViewById(R.id.paint);
    }

    private void setListeners(){
        mExit.setOnClickListener(this);
        mCircle.setOnClickListener(this);
        mLine.setOnClickListener(this);
        mPath.setOnClickListener(this);
        mPoint.setOnClickListener(this);
        mRect.setOnClickListener(this);
        mText.setOnClickListener(this);
        mFill.setOnClickListener(this);
        mStroke.setOnClickListener(this);
        mTest.setOnClickListener(this);
        mDraw.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCircle:
                paint.setFigure(Constants.FIGURE_CIRCLE);
                break;
            case R.id.btnLine:
                paint.setFigure(Constants.FIGURE_LINE);
                break;
            case R.id.btnPath:
                paint.setFigure(Constants.FIGURE_PATH);
                break;
            case R.id.btnPoint:
                paint.setFigure(Constants.FIGURE_POINT);
                break;
            case R.id.btnRect:
                paint.setFigure(Constants.FIGURE_RECTANGLE);
                break;
            case R.id.btnText:
                paint.setFigure(Constants.FIGURE_TEXT);
                break;
            case R.id.btnFill:
                paint.setFillPaint();
                break;
            case R.id.btnStroke:
                paint.setStrokePaint();
                break;
            case R.id.btnDraw:
                paint.setIsOverlay(false);
                break;
            case R.id.btnOverdraw:
                paint.setIsOverlay(true);
                break;
            case R.id.btnExit:
                mCallingActivity.setBitmapImage(paint.getBitmap());
                mCallingActivity.setVisivilityViews(true);
                break;
        }
    }
}
