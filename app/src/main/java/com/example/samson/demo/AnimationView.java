package com.example.samson.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class AnimationView extends View implements SurfaceHolder.Callback {

    private static int RADIUS = 100;
    private static int SIDE = 200;
    private static int START_X = 150;
    private int AXIS_Y, END_X, LENGTH;
    private float degrees = -1.58f;
    private boolean isAnimate = false;
    private boolean isPreview = true;

    private Paint paint;
    private Bitmap square;
    private Matrix rotator;

    private AnimatorThread animator;

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

//        getHolder().addCallback(this);
    }

    public boolean isAnimate() {
        return isAnimate;
    }

    public void showAnim(int typeAnim) {
        if (!isAnimate) {
            switch (typeAnim) {
                case Constants.ANIM_SURFACE:
                    animSurface();
                    break;
                case Constants.ANIM_THREAD:
                    animThread();
                    break;
            }
        }
    }

    private int x, y;

    private void animThread() {
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

    private void animSurface() {
        isPreview = false;
        animator.setRunning(true);
        animator.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (isPreview) {
            AXIS_Y = getHeight() / 2;
            END_X = getWidth() - START_X;
            LENGTH = END_X - START_X;
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            temp.drawColor(Color.BLACK);
            temp.drawRect(START_X - RADIUS, AXIS_Y - RADIUS, START_X + RADIUS, AXIS_Y + RADIUS, paint);
            canvas.drawBitmap(bitmap, 0, 0, null);
            square = Bitmap.createBitmap(bitmap,START_X - RADIUS, AXIS_Y - RADIUS, SIDE, SIDE);
            rotator = new Matrix();
        } else {
            rotator.preRotate(degrees, square.getWidth() / 2, square.getHeight() / 2);
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(Bitmap.createBitmap(square, 0, 0, SIDE, SIDE, rotator, false), x - RADIUS, y - RADIUS, null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        animator = new AnimatorThread(getHolder());
        animator.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                animator.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class AnimatorThread extends Thread {

        private SurfaceHolder surfaceHolder;
        private boolean running = false;

        public AnimatorThread(SurfaceHolder _surfaceHolder) {
            this.surfaceHolder = _surfaceHolder;

            AXIS_Y = getHeight() / 2;
            END_X = getWidth() - START_X;
            LENGTH = END_X - START_X;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            if (running) {
                for (x = START_X; x <= END_X; ++x) {
                    y = (int) (500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawRect(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                for (x = END_X; x >= START_X; ++x) {
                    y = (int) -(500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawRect(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                isAnimate = false;
            } else {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawRect(START_X - RADIUS, AXIS_Y - RADIUS, START_X + RADIUS, AXIS_Y + RADIUS, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
