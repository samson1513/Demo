package com.example.samson.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnimationView extends View {

    private static int RADIUS = 100;
    private static int SIDE = 200;
    private static int START_X = 150;
    private int AXIS_Y, END_X, LENGTH;
    private int x, y;
    private float degrees = -1.58f;
    private boolean isAnimate = false;
    private boolean isPreview = true;

    private Paint paint;
    private Bitmap square, bitmap;
    private Matrix rotator;

    public AnimationView(Context context) {
        super(context);
        init();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff563274);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);

        rotator = new Matrix();
    }

    public boolean isAnimate() {
        return isAnimate;
    }

    public void showAnim() {
        isPreview = false;
        isAnimate = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                degrees = -degrees;
                for (x = START_X; x <= END_X; ++x) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    y = (int) (500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    postInvalidate();
                }
                degrees = -degrees;
                for (x = END_X; x >= START_X; --x) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    y = (int) -(500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    postInvalidate();
                }
                isAnimate = false;
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPreview) {
            AXIS_Y = getHeight() / 2;
            END_X = getWidth() - START_X;
            LENGTH = END_X - START_X;
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            temp.drawColor(Color.BLACK);
            temp.drawRect(START_X - RADIUS, AXIS_Y - RADIUS, START_X + RADIUS, AXIS_Y + RADIUS, paint);
            canvas.drawBitmap(bitmap, 0, 0, null);
            square = Bitmap.createBitmap(bitmap,START_X - RADIUS, AXIS_Y - RADIUS, SIDE, SIDE);
        } else {
            rotator.preRotate(degrees, square.getWidth() / 2, square.getHeight() / 2);
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, SIDE, SIDE, rotator, false), x - RADIUS, y - RADIUS, null);
        }
    }
}
