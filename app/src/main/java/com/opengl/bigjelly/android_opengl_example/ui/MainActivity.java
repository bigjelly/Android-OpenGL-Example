package com.opengl.bigjelly.android_opengl_example.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.opengl.bigjelly.android_opengl_example.R;
import com.opengl.bigjelly.android_opengl_example.lesson1.Lesson1Activity;
import com.opengl.bigjelly.android_opengl_example.lesson4.Lesson4Activity;
import com.opengl.bigjelly.android_opengl_example.utils.Constants;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLesson1Click(View view) {
        Intent intent = new Intent(this, Lesson1Activity.class);
        intent.putExtra(Constants.TYPE, 1);
        startActivity(intent);
    }

    public void onLesson2Click(View view) {
        Intent intent = new Intent(this, Lesson1Activity.class);
        intent.putExtra(Constants.TYPE, 2);
        startActivity(intent);
    }

    public void onLesson3Click(View view) {
        Intent intent = new Intent(this, Lesson1Activity.class);
        intent.putExtra(Constants.TYPE, 3);
        startActivity(intent);
    }

    public void onLesson4Click(View view) {
        Intent intent = new Intent(this, Lesson4Activity.class);
        startActivity(intent);
    }
}
