package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.R;
import com.opengl.bigjelly.android_opengl_example.data.VertexArray;
import com.opengl.bigjelly.android_opengl_example.program.ColorShaderProgram;
import com.opengl.bigjelly.android_opengl_example.utils.Constants;
import com.opengl.bigjelly.android_opengl_example.utils.MatrixState;
import com.opengl.bigjelly.android_opengl_example.utils.ShaderHelper;
import com.opengl.bigjelly.android_opengl_example.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author maboyu
 * @version V1.0
 * @since 2019/03/08
 */
public class Rct {

    // 数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 2;
    private static final int POSTION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSTION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    // 每个顶点的坐标数  						X ,  Y
    static float triangleCoords[] = {
//            -0.5f, 0.0f,   // top left
//            0.5f, 0.0f,    // top right
//            0.5f, -0.5f,    // bottom right
//            -0.5f, -0.5f   // bottom left
//            -0.5f, 0.5f,
//            0.5f, 0.5f,
//            0.5f, -0.5f,
//            -0.5f, -0.5f
            -1.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
            -1.0f, -1.0f

    };
    private final VertexArray vertexArray;

    public Rct() {
        vertexArray = new VertexArray(triangleCoords);
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttrobuteLocation(),
                COORDS_PER_VERTEX,
                0
        );
        GLES30.glUniform4f(colorShaderProgram.getColorAttributeLocation(), 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void draw() {
        GLES30.glLineWidth(10.0f);
        GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, POSTION_COMPONENT_COUNT);
    }
}
