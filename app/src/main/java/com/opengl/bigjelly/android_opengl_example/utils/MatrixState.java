package com.opengl.bigjelly.android_opengl_example.utils;

import android.opengl.Matrix;

/**
 * 存储系统矩阵状态的类
 *
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class MatrixState {
    private static float[] mProjMatrix = new float[16];// 4x4矩阵 存储投影矩阵
    private static float[] mVMatrix = new float[16];// 摄像机位置朝向9参数矩阵

    /*
     * 第一步 ：新建平移变换矩阵
     */
    private static float[] mtMatrix = new float[16];// 平移变换矩阵

    /*
     * 第二步: 初始化为单位矩阵
     */
    static {
        //初始化为单位矩阵
        Matrix.setIdentityM(mtMatrix, 0);
    }

    /*
     * 第三步 : 平移变换方法共外部使用
     */
    public static void translate(float x, float y, float z) {//设置沿xyz轴移动
        Matrix.translateM(mtMatrix, 0, x, y, z);
    }

    //旋转变换
    public static void rotate(float angle, float x, float y, float z) {// 设置绕xyz轴移动
        Matrix.rotateM(mtMatrix, 0, angle, x, y, z);
    }


    // 设置摄像机
    public static void setCamera(float cx, // 摄像机位置x
                                 float cy, // 摄像机位置y
                                 float cz, // 摄像机位置z
                                 float tx, // 摄像机目标点x
                                 float ty, // 摄像机目标点y
                                 float tz, // 摄像机目标点z
                                 float upx, // 摄像机UP向量X分量
                                 float upy, // 摄像机UP向量Y分量
                                 float upz // 摄像机UP向量Z分量
    ) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    // 设置透视投影参数
    public static void setProjectFrustum(float left, // near面的left
                                         float right, // near面的right
                                         float bottom, // near面的bottom
                                         float top, // near面的top
                                         float near, // near面距离
                                         float far // far面距离
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    // 获取具体物体的总变换矩阵
    static float[] mMVPMatrix = new float[16];

    public static float[] getFinalMatrix() {
        /*
         * 第四步  : 乘以平移变换矩阵
         */
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mtMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
