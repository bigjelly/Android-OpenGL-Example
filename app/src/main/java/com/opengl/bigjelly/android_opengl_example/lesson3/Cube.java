package com.opengl.bigjelly.android_opengl_example.lesson3;

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
public class Cube {
    //顶点坐标
    private FloatBuffer vertexBuffer;
    //颜色坐标
    private FloatBuffer colorBuffer;
    private Context context;
    //float类型的字节数
    private static final int BYTES_PER_FLOAT = 4;
    //共有72个顶点坐标，每个面包含12个顶点坐标
    private static final int POSITION_COMPONENT_COUNT = 12*6;
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
            //前面
            0,0,1.0f,
            1.0f,1.0f,1.0f,
            -1.0f,1.0f,1.0f,
            0,0,1.0f,
            -1.0f,1.0f,1.0f,
            -1.0f,-1.0f,1.0f,
            0,0,1.0f,
            -1.0f,-1.0f,1.0f,
            1.0f,-1.0f,1.0f,
            0,0,1.0f,
            1.0f,-1.0f,1.0f,
            1.0f,1.0f,1.0f,
            //后面
            0,0,-1.0f,
            1.0f,1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            0,0,-1.0f,
            1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            0,0,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f,1.0f,-1.0f,
            0,0,-1.0f,
            -1.0f,1.0f,-1.0f,
            1.0f,1.0f,-1.0f,
            //左面
            -1.0f,0,0,
            -1.0f,1.0f,1.0f,
            -1.0f,1.0f,-1.0f,
            -1.0f,0,0,
            -1.0f,1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f,0,0,
            -1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,1.0f,
            -1.0f,0,0,
            -1.0f,-1.0f,1.0f,
            -1.0f,1.0f,1.0f,
            //右面
            1.0f,0,0,
            1.0f,1.0f,1.0f,
            1.0f,-1.0f,1.0f,
            1.0f,0,0,
            1.0f,-1.0f,1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f,0,0,
            1.0f,-1.0f,-1.0f,
            1.0f,1.0f,-1.0f,
            1.0f,0,0,
            1.0f,1.0f,-1.0f,
            1.0f,1.0f,1.0f,
            //上面
            0,1.0f,0,
            1.0f,1.0f,1.0f,
            1.0f,1.0f,-1.0f,
            0,1.0f,0,
            1.0f,1.0f,-1.0f,
            -1.0f,1.0f,-1.0f,
            0,1.0f,0,
            -1.0f,1.0f,-1.0f,
            -1.0f,1.0f,1.0f,
            0,1.0f,0,
            -1.0f,1.0f,1.0f,
            1.0f,1.0f,1.0f,
            //下面
            0,-1.0f,0,
            1.0f,-1.0f,1.0f,
            -1.0f,-1.0f,1.0f,
            0,-1.0f,0,
            -1.0f,-1.0f,1.0f,
            -1.0f,-1.0f,-1.0f,
            0,-1.0f,0,
            -1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            0,-1.0f,0,
            1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,1.0f
    };

    //顶点颜色值数组，每个顶点4个色彩值RGBA
    static float colors[]=new float[]{
            //前面
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            //后面
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            //左面
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            //右面
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            //上面
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            //下面
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
    };

    public Cube(Context context){
        this.context = context;

        vertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(vertices);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);

        //颜色buffer
        colorBuffer = ByteBuffer
                .allocateDirect(colors.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        getProgram();

        aColorLocation = GLES30.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX);

        //---------传入顶点数据数据
        GLES30.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(aPositionLocation);
        //---------传入颜色数据
        GLES30.glVertexAttribPointer(aColorLocation, COORDS_PER_COLOR,
                GLES30.GL_FLOAT, false, 0, colorBuffer);
        GLES30.glEnableVertexAttribArray(aColorLocation);
    }

    //获取program
    private void getProgram(){
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

    public void draw(){
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, MatrixState.getFinalMatrix(),0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
    }
}
