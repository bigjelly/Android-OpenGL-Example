package com.opengl.bigjelly.android_opengl_example.lesson5;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.R;
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
public class Image {
    //顶点坐标
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer; // buffer holding the texture coordinates
    private Context context;
    //float类型的字节数
    private static final int BYTES_PER_FLOAT = 4;
    //共有72个顶点坐标，每个面包含12个顶点坐标
    private static final int POSITION_COMPONENT_COUNT = 12 * 6;
    // 数组中每个顶点的坐标数
    private static final int COORDS_PER_VERTEX = 3;
    // 颜色数组中每个颜色的值数
    private static final int COORDS_PER_COLOR = 4;

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private int aColorLocation;
    private int aPositionLocation;
    private int program;

    static float vertices[] = {
            -1.0f, -1.0f, 0.0f, // V1 - bottom left
            -1.0f, 1.0f, 0.0f, // V2 - top left
            1.0f, -1.0f, 0.0f, // V3 - bottom right
            1.0f, 1.0f, 0.0f // V4 - top right
    };

    static float texture[] = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f, // top left (V2)
            0.0f, 0.0f, // bottom left (V1)
            1.0f, 1.0f, // top right (V4)
            1.0f, 0.0f // bottom right (V3)
    };

    public Image(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(vertices);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);


        vertexBuffer = ByteBuffer.allocateDirect(texture.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        getProgram();

        aColorLocation = GLES30.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX);

        //---------传入顶点数据数据
        GLES30.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(aPositionLocation);
        //---------传入颜色数据
//        GLES30.glVertexAttribPointer(aColorLocation, COORDS_PER_COLOR,
//                GLES30.GL_FLOAT, false, 0, colorBuffer);
        GLES30.glEnableVertexAttribArray(aColorLocation);
    }

    //获取program
    private void getProgram() {
        //获取顶点着色器文本
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.vertex_shader_cube);
        //获取片段着色器文本
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.fragment_shader_cube);
        //获取program的id
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES30.glUseProgram(program);
    }

    public void draw() {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
    }
}
