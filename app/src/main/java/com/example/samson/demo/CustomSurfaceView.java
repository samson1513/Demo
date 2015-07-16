package com.example.samson.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private Bitmap mBitmap;

    public CustomSurfaceView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //добавление Callback
        getHolder().addCallback(this);
    }

    public Bitmap getBitmap() {
        return drawThread.getBitmap();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //инициализация и запуск потока
        drawThread = new DrawThread(getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //блокирование потока в случае срабатывания onPause()
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            default:
                break;
        }
        return true;
    }

    private void touchDown(MotionEvent event) {
        drawThread.setRunning(true);
        drawThread.setCoords((int) event.getX(), (int) event.getY());
        drawThread.run();
    }

    private class DrawThread extends Thread {

        private boolean running = false;
        private int x, y;
        private SurfaceHolder surfaceHolder;
        private Paint mPaint;
        private Bitmap bitmap;

        public DrawThread(SurfaceHolder _surfaceHolder) {
            this.surfaceHolder = _surfaceHolder;

            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(0xFF235576);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(15);
        }

        public void setCoords(int _x, int _y) {
            x = _x;
            y = _y;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        @Override
        public void run() {

            Canvas canvas;
            if (running) {
                //резервирование канваса для рисования в данной вью
                canvas = surfaceHolder.lockCanvas();
                //рисование
                Canvas temp = new Canvas(bitmap);
                temp.drawCircle(x, y, 150, mPaint);
                canvas.drawBitmap(bitmap,0,0,null);
                //освобождение канваса и вывод на экран результата рисования
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
