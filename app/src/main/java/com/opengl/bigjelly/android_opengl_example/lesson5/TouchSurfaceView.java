package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchSurfaceView extends GLSurfaceView {

    private Lesson5Render mRenderer;

    public TouchSurfaceView(Context context) {
        super(context);
    }

    public TouchSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        mRenderer = (Lesson5Render) renderer;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event != null) {
//            final float normalizedX = (event.getX() / (float) getWidth()) * 2 - 1;
//            final float normalizedY = -((event.getY() / (float) getHeight()) * 2 - 1);
            final float x = event.getX();
            final float y = event.getY();
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.onTouchEvent(event);
                }
            });
            return true;
        }
        return super.onTouchEvent(event);
    }
}
