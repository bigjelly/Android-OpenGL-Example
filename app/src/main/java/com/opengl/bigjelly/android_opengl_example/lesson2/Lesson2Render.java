package com.opengl.bigjelly.android_opengl_example.lesson2;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson2Render implements GLSurfaceView.Renderer {
    private Context context;

    //定义三角形对象
    Triangle triangle;

    public Lesson2Render(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //First:设置清空屏幕用的颜色，前三个参数对应红绿蓝，最后一个对应alpha
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        triangle = new Triangle(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Second:设置视口尺寸，即告诉opengl可以用来渲染的surface大小
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Third:清空屏幕，擦除屏幕上所有的颜色，并用之前glClearColor定义的颜色填充整个屏幕
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        //绘制三角形
        triangle.draw();
    }
}
