package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.opengl.bigjelly.android_opengl_example.R;
import com.opengl.bigjelly.android_opengl_example.program.ColorShaderProgram;
import com.opengl.bigjelly.android_opengl_example.program.TextureShaderProgram;
import com.opengl.bigjelly.android_opengl_example.utils.BitmapHelper;
import com.opengl.bigjelly.android_opengl_example.utils.TextureHelper;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Lesson5Render implements GLSurfaceView.Renderer {

    private static final float MIN_SCALE = 0.15f; // 最小缩放比例
    private static final float ZOOM_SCROLL_ACCURACY = 10f;

    private Context context;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

    private final float[] mRctMVPMatrix = new float[16];

    private final float[] mDelMVPMatrix = new float[16];

    private final float[] mScaMVPMatrix = new float[16];

    private float mDistance;

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
    int toolWidth;
    int toolHeight;

    private int mHeight;
    private int mWidth;

    float posX;
    float posY;
    float offsetWidth;
    float offsetHeight;
    float scale;
    float angle;
    float newDelX;
    float newDelY;

    float newScaX;
    float newScaY;

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
        toolWidth = delBitmap.getWidth();
        toolHeight = delBitmap.getHeight();
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

        // 1. 矩阵数组
        // 2. 结果矩阵起始的偏移量
        // 3. left：x的最小值
        // 4. right：x的最大值
        // 5. bottom：y的最小值
        // 6. top：y的最大值
        // 7. near：z的最小值
        // 8. far：z的最大值

        Matrix.orthoM(mProjectMatrix, 0, 0, width, 0, height, -1, 1);

        posX = mWidth / 2f;
        posY = mHeight / 2f;
        scale = 2.0f;
        angle = 0;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //Third:清空屏幕，擦除屏幕上所有的颜色，并用之前glClearColor定义的颜色填充整个屏幕
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        float newWidth = mBitmapWidth / 2f;
        float newHeight = mBitmapHeight / 2f;

        setMatrixData(angle, posX, posY, newWidth * scale, newHeight * scale);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, texture);
        image.bindData(textureProgram);
        image.draw();

        offsetWidth = newWidth * (scale + 0.2f);
        offsetHeight = newHeight * (scale + 0.2f);
        setMatrixData(angle, posX, posY, offsetWidth, offsetHeight);
        Matrix.multiplyMM(mRctMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        colorProgram.useProgram();
        colorProgram.setUniforms(mRctMVPMatrix);
        rct.bindData(colorProgram);
        rct.draw();


        float sinA = (float) Math.sin(Math.toRadians(angle));
        float cosA = (float) Math.cos(Math.toRadians(angle));

        newDelX = (-offsetWidth) * cosA - (offsetHeight) * sinA + posX;
        newDelY = (-offsetWidth) * sinA + (offsetHeight) * cosA + posY;

        setMatrixData(angle, newDelX, newDelY, newWidth * 0.5f, newHeight * 0.5f);
        Matrix.multiplyMM(mDelMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mDelMVPMatrix, delTexture);
        deleteButton.bindData(textureProgram);
        deleteButton.draw();


        newScaX = (offsetWidth) * cosA - (-offsetHeight) * sinA + posX;
        newScaY = (offsetWidth) * sinA + (-offsetHeight) * cosA + posY;

        setMatrixData(angle, newScaX, newScaY, newWidth * 0.5f, newHeight * 0.5f);
        Matrix.multiplyMM(mScaMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        textureProgram.useProgram();
        textureProgram.setUniforms(mScaMVPMatrix, scaleTexture);
        scaleButton.bindData(textureProgram);
        scaleButton.draw();
    }

    private void setMatrixData(float angle, float posX, float posY, float scaleX, float scaleY) {
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.translateM(mViewMatrix, 0, posX, posY, 1.0f);
        Matrix.rotateM(mViewMatrix, 0, angle % 360, 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(mViewMatrix, 0, scaleX, scaleY, 0f);
    }


    private int mSelectStatus; // 当前状态
    private final static int STATUS_IDLE = 100;
    private final static int STATUS_DELETE = STATUS_IDLE + 1; // 删除
    private final static int STATUS_MOVE = STATUS_DELETE + 1; // 移动
    private final static int STATUS_ROTATE = STATUS_MOVE + 1; // 旋转
    private final static int STATUS_CLICK = STATUS_ROTATE + 1; // 点击
    private final static int STATUS_POINTER_ROTATE = STATUS_CLICK + 1; // 双指操作

    private float oldx, oldy, oldx1, oldy1;


    public void onTouchEvent(MotionEvent event) {
        if (event != null) {

            int action = event.getAction();
            final float x = event.getX(0);
            final float y = event.getY(0);

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    handleTouchPress(x, y);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e("mby", ">>>two pointer touch<<<");
                    handleMoreTouchPress(event, x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 2) {
                        handleMoreTouchDrag(event, x, y);
                    } else {
                        handleTouchDrag(x, y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    mSelectStatus = STATUS_IDLE;
                    break;

            }
        }

    }

    private void handleMoreTouchPress(MotionEvent event, float x, float y) {
        float x1 = event.getX(1);
        float y1 = event.getY(1);

        oldx = x;
        oldy = y;

        oldx1 = x1;
        oldy1 = y1;
        if (isInTools(x1, y1)) {
            mSelectStatus = STATUS_POINTER_ROTATE;
        }
    }

    private void handleMoreTouchDrag(MotionEvent event, float x, float y) {
        if (STATUS_POINTER_ROTATE == mSelectStatus) {

            Log.e("mby", ">>>two pointer touch drag<<<");
            float x2 = event.getX(1);
            float y2 = event.getY(1);
            updateRotateAndScale(x, y, x2, y2);
            oldx = x;
            oldy = y;

            oldx1 = x2;
            oldy1 = y2;
        }
    }

    /**
     * 按下
     *
     * @param x
     * @param y
     */
    public void handleTouchPress(float x, float y) {
        mSelectStatus = STATUS_IDLE;
        oldx = x;
        oldy = y;
        if (isInScale(x, y)) {
            Log.e("mby", ">>>isInScale<<<");
            mSelectStatus = STATUS_ROTATE;
        } else if (isInTools(x, y)) {
            Log.e("mby", ">>>isInTools<<<");
            mSelectStatus = STATUS_MOVE;
        }
    }

    /**
     * 移动
     *
     * @param x
     * @param y
     */
    public void handleTouchDrag(float x, float y) {
        if (STATUS_ROTATE == mSelectStatus) {
            Log.e("mby", ">>>isInRotate move<<<");
            updateRotateAndScale(x, y);
        } else if (STATUS_MOVE == mSelectStatus) {
            Log.e("mby", ">>>isInScale move<<<");
            float dx = x - oldx;
            float dy = y - oldy;
            posX = posX + dx;
            posY = posY - dy;
        }
        oldx = x;
        oldy = y;
    }

    /**
     * 获取p1到p2的线段的长度
     *
     * @param p1
     * @param p2
     * @return
     */
    float getLineLength(PointF p1, PointF p2) {
        return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * 获取原点到p的线段的长度
     *
     * @param p
     * @return
     */
    float getLineLength(PointF p) {
        return (float) Math.sqrt(p.x * p.x + p.y * p.y);
    }

    public void updateRotateAndScale(float dx, float dy, float dx1, float dy1) {
        PointF spt1 = new PointF(oldx, mHeight - oldy);
        PointF ept1 = new PointF(oldx1, mHeight - oldy1);
        PointF spt2 = new PointF(dx, mHeight - dy);
        PointF ept2 = new PointF(dx1, mHeight - dy1);
        Log.e("mby", "\n spt1-->" + spt1.toString() + ", ept1 -->" + ept1.toString()
                + ", spt2 -->" + spt2.toString() + ", ept2 -->" + ept2.toString());
        //两个线段之间的运动幅度过小，删除。
        //此处要注意，为什么幅度过小要删除呢，因为在某些手机屏幕，在按压时灵敏度过高，
        //或者硬件计算手指位置算法不够好，造成用户感觉手指没动而显示出来的坐标一直在抖动

        if (getLineLength(spt1, spt2) + getLineLength(ept1, ept2) < 5) {
            return;
        }

        //根据触点 构建两个向量，计算两个向量角度.
        PointF v1 = new PointF(ept1.x - spt1.x, ept1.y - spt1.y);
        PointF v2 = new PointF(ept2.x - spt2.x, ept2.y - spt2.y);

        //计算两个向量的夹角.
        changeAngleAndScale(v1, v2);
    }

    private void updateRotateAndScale(float dx, float dy) {

        float x = dx - oldx;
        float y = dy - oldy;

        float n_x = newScaX + x;
        float n_y = newScaY - y;

        PointF spt1 = new PointF(newScaX, newScaY);
        PointF ept1 = new PointF(posX, posY);
        PointF spt2 = new PointF(n_x, n_y);
        //两个线段之间的运动幅度过小，删除。
        //此处要注意，为什么幅度过小要删除呢，因为在某些手机屏幕，在按压时灵敏度过高，
        //或者硬件计算手指位置算法不够好，造成用户感觉手指没动而显示出来的坐标一直在抖动

        if (getLineLength(spt1, ept1) + getLineLength(ept1, spt2) < 5) {
            return;
        }

        //根据触点 构建两个向量，计算两个向量角度.
        PointF v1 = new PointF(ept1.x - spt1.x, ept1.y - spt1.y);
        PointF v2 = new PointF(ept1.x - spt2.x, ept1.y - spt2.y);
        changeAngleAndScale(v1, v2);

    }

    private void changeAngleAndScale(PointF v1, PointF v2) {
        //计算两个向量的夹角.
        float v1Len = getLineLength(v1);
        float v2Len = getLineLength(v2);


        float cosAlpha = (v1.x * v2.x + v1.y * v2.y) / (v1Len * v2Len);

        if (cosAlpha > 1 || cosAlpha < -1) {
            return;
        }
        float angle1 = (float) Math.toDegrees(Math.acos(cosAlpha));

        // 定理
        float calMatrix = v1.x * v2.y - v2.x * v1.y;// 行列式计算 确定转动方向

        int flag = calMatrix > 0 ? 1 : -1;
        float angle2 = flag * angle1;
        angle += angle2;
        Log.e("mby", "\n angle -->" + angle);
        float scaleFactor = v2Len / v1Len;// 计算缩放比

        float scale1 = scale;
        scale1 *= scaleFactor;
        if (scale1 < MIN_SCALE) {
            return;
        }

        scale *= scaleFactor;
        Log.e("mby", "\n scale -->" + scale);
    }

    // 两点的距离
    private float spacingBetweenFingers(MotionEvent event) {
        if (event == null) {
            return 0f;
        }
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public boolean isInTools(float x, float y) {
        RectF rectF = new RectF(posX - offsetWidth, mHeight - posY, posX + offsetWidth, mHeight - posY + offsetHeight * 2);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preRotate(angle, posX, mHeight - posY);
        matrix.mapRect(rectF);
        if (rectF.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInScale(float x, float y) {
        RectF rectF = new RectF(newScaX - toolWidth / 4, mHeight - newScaY + 200, newScaX + toolWidth / 4, mHeight - newScaY + toolHeight / 2 + 200);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.mapRect(rectF);
        if (rectF.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }
}
