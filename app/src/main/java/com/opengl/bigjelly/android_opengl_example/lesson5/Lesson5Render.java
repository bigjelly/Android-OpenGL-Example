package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.opengl.bigjelly.android_opengl_example.R;
import com.opengl.bigjelly.android_opengl_example.data.Mallet;
import com.opengl.bigjelly.android_opengl_example.program.ColorShaderProgram;
import com.opengl.bigjelly.android_opengl_example.program.TextureShaderProgram;
import com.opengl.bigjelly.android_opengl_example.utils.BitmapHelper;
import com.opengl.bigjelly.android_opengl_example.utils.Geometry;
import com.opengl.bigjelly.android_opengl_example.utils.MatrixHelper;
import com.opengl.bigjelly.android_opengl_example.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson5Render implements GLSurfaceView.Renderer {
    private Context context;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

    private final float[] mRctMVPMatrix = new float[16];

    private final float[] mDelMVPMatrix = new float[16];

    private final float[] mScaMVPMatrix = new float[16];

    //定义三角形对象
    Rct rct;
    Image image;
    DeleteButton deleteButton;
    ScaleButton scaleButton;

    TextureShaderProgram textureProgram;
    ColorShaderProgram colorProgram;

    int texture;
    int delTexture;
    int scaleTexture;

    private int mBitmapHeight;
    private int mBitmapWidth;

    private int mHeight;
    private int mWidth;

    float posX;
    float posY;
    float offsetWidth;
    float offsetHeight;

    public Lesson5Render(Context context) {
        this.context = context;

        rct = new Rct();
        image = new Image();
        deleteButton = new DeleteButton();
        scaleButton = new ScaleButton();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //First:设置清空屏幕用的颜色，前三个参数对应红绿蓝，最后一个对应alpha
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glEnable(GLES30.GL_TEXTURE_2D);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        Bitmap bitmap = BitmapHelper.loadBitmap(context, R.mipmap.ic_launcher);
        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();
        texture = TextureHelper.loadTexture(bitmap);

        Bitmap delBitmap = BitmapHelper.loadBitmap(context, R.mipmap.ic_delect);
        delTexture = TextureHelper.loadTexture(delBitmap);

        Bitmap scaleBitmap = BitmapHelper.loadBitmap(context, R.mipmap.ic_scale);
        scaleTexture = TextureHelper.loadTexture(scaleBitmap);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;

        //Second:设置视口尺寸，即告诉opengl可以用来渲染的surface大小
        GLES30.glViewport(0, 0, mWidth, mHeight);
//        float sWH=mBitmapWidth/(float)mBitmapHeight;
//        float sWidthHeight=width/(float)height;
//        if(width>height){
//            if(sWH>sWidthHeight){
//                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight*sWH,sWidthHeight*sWH, -1,1, 3, 5);
//            }else{
//                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight/sWH,sWidthHeight/sWH, -1,1, 3, 5);
//            }
//        }else{
//            if(sWH>sWidthHeight){
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 5);
//            }else{
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 5);
//            }
//        }

        // 1. 矩阵数组
        // 2. 结果矩阵起始的偏移量
        // 3. left：x的最小值
        // 4. right：x的最大值
        // 5. bottom：y的最小值
        // 6. top：y的最大值
        // 7. near：z的最小值
        // 8. far：z的最大值
//        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1, -1, 1);

        Matrix.orthoM(mProjectMatrix, 0, 0, width, 0, height, -1, 1);

        posX = mWidth / 2f;
        posY = mHeight / 2f;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //Third:清空屏幕，擦除屏幕上所有的颜色，并用之前glClearColor定义的颜色填充整个屏幕
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        float newWidth = mBitmapWidth / 2f;
        float newHeight = mBitmapHeight / 2f;
        float angle = 20;
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.translateM(mViewMatrix, 0, posX, posY, 1.0f);
        Matrix.rotateM(mViewMatrix, 0, angle % 360, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(mViewMatrix, 0, newWidth * 1.4f, newHeight * 1.4f, 0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, texture);
        image.bindData(textureProgram);
        image.draw();


        offsetWidth = newWidth * 1.6f;
        offsetHeight = newHeight * 1.6f;
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.translateM(mViewMatrix, 0, posX, posY, 1.0f);
        Matrix.rotateM(mViewMatrix, 0, angle % 360, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(mViewMatrix, 0, offsetWidth, offsetHeight, 0f);
        Matrix.multiplyMM(mRctMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        colorProgram.useProgram();
        colorProgram.setUniforms(mRctMVPMatrix);
        rct.bindData(colorProgram);
        rct.draw();


        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.translateM(mViewMatrix, 0, posX - offsetWidth, posY + offsetHeight, 1.0f);
        Matrix.rotateM(mViewMatrix, 0, angle % 360, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(mViewMatrix, 0, newWidth * 0.5f, newHeight * 0.5f, 0f);
        Matrix.multiplyMM(mDelMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mDelMVPMatrix, delTexture);
        deleteButton.bindData(textureProgram);
        deleteButton.draw();


        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.translateM(mViewMatrix, 0, posX + offsetWidth, posY - offsetHeight, 1.0f);
        Matrix.rotateM(mViewMatrix, 0, angle % 360, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(mViewMatrix, 0, newWidth * 0.5f, newHeight * 0.5f, 0f);
        Matrix.multiplyMM(mScaMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mScaMVPMatrix, scaleTexture);
        scaleButton.bindData(textureProgram);
        scaleButton.draw();
    }

    /**
     * 按下
     *
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchPress(float normalizedX, float normalizedY) {

    }

    /**
     * 移动
     *
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if (isInTools(normalizedX, normalizedY)) {
            posX = normalizedX;
            posY = mHeight - normalizedY;
        }
    }

    public boolean isInTools(float x, float y) {
        RectF rectF = new RectF(posX - mBitmapWidth / 2, mHeight - posY - mBitmapHeight / 2, posX - mBitmapWidth / 2 + mBitmapWidth, mHeight - posY - mBitmapHeight / 2 + mBitmapHeight);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.mapRect(rectF);
        if (rectF.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInScale(float x, float y) {
        RectF rectF = new RectF(posX - mBitmapWidth / 2, mHeight - posY - mBitmapHeight / 2, posX - mBitmapWidth / 2 + mBitmapWidth, mHeight - posY - mBitmapHeight / 2 + mBitmapHeight);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.mapRect(rectF);
        if (rectF.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }
}
