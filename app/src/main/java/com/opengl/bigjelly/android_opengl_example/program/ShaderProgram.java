package com.opengl.bigjelly.android_opengl_example.program;

import android.content.Context;
import android.opengl.GLES30;

import com.opengl.bigjelly.android_opengl_example.utils.ShaderHelper;
import com.opengl.bigjelly.android_opengl_example.utils.TextResourceReader;

public class ShaderProgram {

    //Uniform常量
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    //Attribute 常量
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Animation
    protected static final String TIME = "time";
    protected static final String RESOLUTION = "resolution";

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderId, int fragmentShaderId) {
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderId));
    }



    public void useProgram() {
        GLES30.glUseProgram(program);
    }
}
