package com.opengl.bigjelly.android_opengl_example.program;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.R;

public class TextureShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;
    private final int aTextureCoordnatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        //从GLSL着色器文件中读取各种属性，利用父函数所定义的String值(U_MATRIX = "u_Matrix")
        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES30.glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        aTextureCoordnatesLocation = GLES30.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }


    public void setUniforms(float[] matrix, int textureId) {
        //传递正交投影矩阵
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //纹理单元保存纹理，GPU只能绘制有限的纹理，需要提高渲染速度的话，需要多个纹理单元保存纹理切换
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //将纹理绑定到单元
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        //将纹理单元传递给u_TextureUnit
        GLES30.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getaTextureCoordnatesLocation() {
        return aTextureCoordnatesLocation;
    }
}
