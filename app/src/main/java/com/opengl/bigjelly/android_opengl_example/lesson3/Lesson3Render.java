package com.opengl.bigjelly.android_opengl_example.lesson3;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.opengl.bigjelly.android_opengl_example.lesson2.Triangle;
import com.opengl.bigjelly.android_opengl_example.utils.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson3Render implements GLSurfaceView.Renderer {
    private Context context;

    Cube cube;

    public Lesson3Render(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //First:设置清空屏幕用的颜色，前三个参数对应红绿蓝，最后一个对应alpha
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
//		//打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
//        //打开背面剪裁
        GLES30.glEnable(GLES30.GL_CULL_FACE);
//		circle = new Circle(context);
        cube = new Cube(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Second:设置视口尺寸，即告诉opengl可以用来渲染的surface大小
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        //设置投影矩阵
//		circle.projectionMatrix(width, height);
        // 调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
        // 调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(-16f, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Third:清空屏幕，擦除屏幕上所有的颜色，并用之前glClearColor定义的颜色填充整个屏幕
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        //绘制三角形
        cube.draw();
    }
}
