package com.opengl.bigjelly.android_opengl_example.program;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.R;

public class AnimationShaderProgram extends ShaderProgram{

    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aTextureCoordnatesLocation;

    private final int uTimeLocation;
    private final int uResolutionLocation;

    public AnimationShaderProgram(Context context){
        super(context, R.raw.animation_vertex_shader,R.raw.animation_fragment_shader);

        //从GLSL着色器文件中读取各种属性，利用父函数所定义的String值(U_MATRIX = "u_Matrix")
        uMatrixLocation = GLES30.glGetUniformLocation(program,U_MATRIX);
        aPositionLocation = GLES30.glGetAttribLocation(program,A_POSITION);
        aTextureCoordnatesLocation = GLES30.glGetAttribLocation(program,A_TEXTURE_COORDINATES);
        uTimeLocation = GLES30.glGetUniformLocation(program,TIME);
        uResolutionLocation = GLES30.glGetUniformLocation(program,RESOLUTION);
    }


    public void setUniforms(float[] matrix,float time,int width,int height){
        //传递正交投影矩阵
        GLES30.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES30.glUniform1f(uTimeLocation,time);
        GLES30.glUniform2i(uResolutionLocation,width,height);

    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getaTextureCoordnatesLocation(){
        return aTextureCoordnatesLocation;
    }

}
