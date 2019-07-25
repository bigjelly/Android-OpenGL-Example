package com.opengl.bigjelly.android_opengl_example.program;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.R;

public class ColorShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int uColorLocation;


    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        uMatrixLocation = GLES30.glGetUniformLocation(program, U_MATRIX);

        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);

        uColorLocation = GLES30.glGetUniformLocation(program, U_COLOR);
    }

    public void setUniforms(float[] matrix) {
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttrobuteLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return uColorLocation;
    }
}
