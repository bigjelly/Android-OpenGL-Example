package com.opengl.bigjelly.android_opengl_example.data;

import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.utils.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexArray {

    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        floatBuffer.position(0);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {


        //在vertexData的缓冲区中 对应 a_Position的位置
        //aPositionLoacation是被传入的属性位置，这里是位置属性位置
        //POSITION_COMPONENT_COUNT指的是，多少个数组分量与一个顶点相关联，因为x y 坐标，所以传入两个
        //GL FLOAT指的是数据类型
        //STRIDE 是跨距的意思，告诉两组参数之间差多少位置
        GLES30.glEnableVertexAttribArray(attributeLocation);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, floatBuffer);

    }
}
