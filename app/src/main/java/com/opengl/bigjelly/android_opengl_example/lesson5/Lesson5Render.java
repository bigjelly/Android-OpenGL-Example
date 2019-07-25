package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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

    //定义三角形对象
    Rct rct;
    Image image;
    DeleteButton deleteButton;
    ScaleButton scaleButton;

    TextureShaderProgram textureProgram;
    ColorShaderProgram colorProgram;

    private Mallet mallet;

    int texture;
    int delTexture;
    int scaleTexture;

    private int mBitmapHeight;
    private int mBitmapWidth;

    public Lesson5Render(Context context) {
        this.context = context;

        rct = new Rct();
        image = new Image();
        deleteButton = new DeleteButton();
        scaleButton = new ScaleButton();

        mallet = new Mallet(0.08f,0.15f,32);
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


        blueMalletPosition = new Geometry.Point(0f,mallet.height/2f,0.4f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Second:设置视口尺寸，即告诉opengl可以用来渲染的surface大小
        GLES30.glViewport(0, 0, width, height);

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
        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1, 3, 5);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Third:清空屏幕，擦除屏幕上所有的颜色，并用之前glClearColor定义的颜色填充整个屏幕
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        colorProgram.useProgram();
        colorProgram.setUniforms(mMVPMatrix);
        rct.bindData(colorProgram);
        rct.draw();

        textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, texture);
        image.bindData(textureProgram);
        image.draw();

        textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, delTexture);
        deleteButton.bindData(textureProgram);
        deleteButton.draw();

        textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, scaleTexture);
        scaleButton.bindData(textureProgram);
        scaleButton.draw();

    }

    private boolean malletPressed = false;

    private Geometry.Point blueMalletPosition;

    private final float[] invertedViewProjectionmatrix = new float[16];

    /**
     * 按下
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);

        //创造了一个球，包在木槌周围
        Geometry.Sphere malletBoundingSphere =new Geometry.Sphere(new Geometry.Point(
                blueMalletPosition.x,
                blueMalletPosition.y,
                blueMalletPosition.z),
                mallet.height/2);

        malletPressed = Geometry.intersects(malletBoundingSphere,ray);
    }

    /**
     * 移动
     * @param normalizedX
     * @param normalizedY
     */
    public void handleTouchDrag(float normalizedX, float normalizedY){

    }

    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY){
        //近远点矩阵
        final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
        final float[] farPointNdc = {normalizedX,normalizedY,1,1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        //获取转换为2维空间的矩阵
        Matrix.multiplyMV(nearPointWorld,0,invertedViewProjectionmatrix,0,nearPointNdc,0);
        Matrix.multiplyMV(farPointWorld,0,invertedViewProjectionmatrix,0,farPointNdc,0);

//        divideByW(nearPointWorld);
//        divideByW(farPointWorld);

        //获取远近点
        Geometry.Point nearPointRay =
                new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay =
                new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));

    }
}
