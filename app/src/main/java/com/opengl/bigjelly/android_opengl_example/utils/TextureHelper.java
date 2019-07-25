package com.opengl.bigjelly.android_opengl_example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        GLES30.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.i(TAG, "没有获取到texture object");
            }
            return 0;
        }

        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.i(TAG, "没有获取到 bitmap");
            }
            GLES30.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
}
