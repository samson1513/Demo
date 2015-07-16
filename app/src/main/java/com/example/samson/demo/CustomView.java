package com.example.samson.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CustomView extends View {

    private int mFigure = -1;
    private boolean savingFlag = false;
    private boolean endDrawing = false;
    private boolean isSaved = false;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Coords startCoords, endCoords;
    private ArrayList<Path> paths;
    private Path path;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF00AAFF);
        mPaint.setStrokeWidth(5);
        mPaint.setTextSize(150f);

        startCoords = new Coords();
        endCoords = new Coords();
    }

    public void setStrokePaint() {
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setFillPaint() {
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * @param _typeFigure - type of figure for drawing
     */
    public void setFigure(int _typeFigure) {
        mFigure = _typeFigure;
    }

    /**
     * @param _saving - flag of mode: save or don't save result of drawing
     */
    public void setIsOverlay(boolean _saving) {
        savingFlag = _saving;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //создание битмапа, на основе котором будет проводиться отрисовка
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //создание канваса для отрисовки на указаном битмапе
        Canvas tempCanvas = new Canvas(bitmap);

        if(isSaved){
            tempCanvas.drawBitmap(mBitmap, 0, 0, null);
        } else {
            tempCanvas.drawColor(Color.BLACK);
        }
        switch (mFigure) {
            case Constants.FIGURE_CIRCLE:
                Coords center = Coords.getCoordsOfCenter(startCoords, endCoords);
                tempCanvas.drawCircle(center.x, center.y, Coords.getLength(startCoords, endCoords), mPaint);
                break;
            case Constants.FIGURE_LINE:
                tempCanvas.drawLine(startCoords.x, startCoords.y, endCoords.x, endCoords.y, mPaint);
                break;
            case Constants.FIGURE_PATH:
                for (Path p : paths) {
                    tempCanvas.drawPath(p, mPaint);
                }
                break;
            case Constants.FIGURE_POINT:
                tempCanvas.drawPoint(startCoords.x, startCoords.y, mPaint);
                break;
            case Constants.FIGURE_RECTANGLE:
                tempCanvas.drawRect(startCoords.x, startCoords.y, endCoords.x, endCoords.y, mPaint);
                break;
            case Constants.FIGURE_TEXT:
                tempCanvas.drawText("Text", startCoords.x, startCoords.y, mPaint);
                break;
        }
        if (endDrawing) {
            //сохранение рисунка в конечном битмапе
            mBitmap = bitmap;
            isSaved = true;
        }
        //отрисовка на экране
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    private void touchDown(int _x, int _y) {
        if (mFigure == Constants.FIGURE_PATH) {
            paths = new ArrayList<>();
            path = new Path();
            path.moveTo(_x, _y);
            paths.add(path);
        } else {
            endDrawing = false;
            startCoords.x = _x;
            startCoords.y = _y;
            endCoords.x = _x;
            endCoords.y = _y;
        }
    }

    private void touchMove(int _x, int _y) {
        if (mFigure == Constants.FIGURE_PATH) {
            path.lineTo(_x, _y);
            paths.add(path);
        } else {
            endCoords.x = _x;
            endCoords.y = _y;
        }
    }

    private void touchUp() {
        if (savingFlag)
            //если установлен режим "сохранения нарисованного", то, при отрывании пальца от экрана, сохранить результат
            endDrawing = true;
    }

    //класс для хранения координат
    private static class Coords {
        int x;
        int y;

        public Coords() {
        }

        public Coords(int _x, int _y) {
            x = _x;
            y = _y;
        }

        /**
         * @param _start - coordinates of start point
         * @param _end   - coordinates of end point
         * @return coordinates of midpoint
         */
        public static Coords getCoordsOfCenter(Coords _start, Coords _end) {
            return new Coords(Math.abs(_start.x + _end.x) / 2, Math.abs(_start.y + _end.y) / 2);
        }

        /**
         * @param _start - coordinates of start point
         * @param _end   - coordinates of end point
         * @return - length of segment
         */
        public static float getLength(Coords _start, Coords _end) {
            return (float) Math.sqrt((_start.x - _end.x) * (_start.x - _end.x) +
                    (_start.y - _end.y) * (_start.y - _end.y)) / 2;
        }

        @Override
        public String toString() {
            return "x = " + x + " |y = " + y;
        }
    }
}
