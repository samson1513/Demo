package com.example.samson.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimationSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private boolean isAnimate = false;

    private AnimatorThread animator;

    public AnimationSurfaceView(Context context) {
        super(context);
        init();
    }

    public AnimationSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimationSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        getHolder().addCallback(this);
    }

    public boolean isAnimate() {
        return isAnimate;
    }

    public void showAnim() {
        animator.setRunning(true);
        animator.start();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        animator = new AnimatorThread(getHolder());
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

        private int RADIUS = 100;
        private int SIDE = 200;
        private int START_X = 150;
        private int AXIS_Y, END_X, LENGTH;
        private int x, y;
        private boolean running = false;

        private SurfaceHolder surfaceHolder;

        private Paint paint;
        private Canvas canvas;

        public AnimatorThread(SurfaceHolder _surfaceHolder) {
            this.surfaceHolder = _surfaceHolder;

            initPaint();
            initCoords();
            drawStartImage();
        }

        private void initPaint(){
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0xff563274);
            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);
        }

        private void initCoords(){
            AXIS_Y = getHeight() / 2;
            END_X = getWidth() - START_X;
            LENGTH = END_X - START_X;
        }

        private void drawStartImage(){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawRect(START_X - RADIUS, AXIS_Y - RADIUS, START_X + RADIUS, AXIS_Y + RADIUS, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while(running) {
                isAnimate = true;
                for (x = START_X; x <= END_X; ++x) {
                    y = (int) (500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawRect(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                for (x = END_X; x >= START_X; --x) {
                    y = (int) -(500 * Math.sin(2 * Math.PI * (x - START_X) / LENGTH)) + AXIS_Y;
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawRect(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                isAnimate = false;
                running = false;
            }
        }
    }
}
