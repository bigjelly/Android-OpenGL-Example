package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.data.VertexArray;
import com.opengl.bigjelly.android_opengl_example.program.TextureShaderProgram;

public class ScaleButton {

    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray posVertexArray;
    private final VertexArray coordVertexArray;

    // 顶点坐标 0.5f, -0.5f
    private final float[] sPos = {
//            0.4f, -0.4f,
//            0.4f, -0.6f,
//            0.6f, -0.4f,
//            0.6f, -0.6f

            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };
    // 纹理坐标
    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    public ScaleButton() {
        posVertexArray = new VertexArray(sPos);
        coordVertexArray = new VertexArray(sCoord);
    }

    public void bindData(TextureShaderProgram animationProgram) {

        //从着色器构建的程序中获取每一个属性的位置，
        //通过getPositionAttributeLocation把程序位置属性绑定到着色器位置属性上
        //通过getTextureCoordinatesAttributeLocation把程序纹理位置属性绑定到着色器属纹理置属性上
        posVertexArray.setVertexAttribPointer(
                0,
                animationProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);

        coordVertexArray.setVertexAttribPointer(
                0,
                animationProgram.getaTextureCoordnatesLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                0);

    }

    //根据位置和纹理绘制形状
    public void draw() {
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
