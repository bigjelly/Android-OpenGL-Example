package com.opengl.bigjelly.android_opengl_example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper {

    public static Bitmap loadBitmap(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

        return bitmap;
    }
}
