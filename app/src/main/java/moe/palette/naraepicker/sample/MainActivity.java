package moe.palette.naraepicker.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import moe.palette.naraepicker.NaraePickerActivity;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView preview1;
    ImageView preview2;
    ImageView preview3;
    ImageView preview4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        preview1 = (ImageView) findViewById(R.id.imageView);
        preview2 = (ImageView) findViewById(R.id.imageView2);
        preview3 = (ImageView) findViewById(R.id.imageView3);
        preview4 = (ImageView) findViewById(R.id.imageView4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NaraePickerActivity.class);
                startActivityForResult(i, NaraePickerActivity.FINISH_ALL_WORK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NaraePickerActivity.FINISH_ALL_WORK) {
            if (data != null) {
                ArrayList<String> imageList = data.getStringArrayListExtra(NaraePickerActivity.GETTING_IMAGES);
                for (String image : imageList) {
                    Log.d("NaraePicker", " path: " + image);
                }
                if (!imageList.isEmpty()) {
                    if (imageList.size() >= 2) {
                        Glide.with(MainActivity.this).load(new File(imageList.get(0))).into(preview1);
                        Glide.with(MainActivity.this).load(new File(imageList.get(1))).into(preview2);
                        if (imageList.size() >= 3) {
                            Glide.with(MainActivity.this).load(new File(imageList.get(2))).into(preview3);
                            if (imageList.size() >= 4) {
                                Glide.with(MainActivity.this).load(new File(imageList.get(3))).into(preview4);
                            }
                        }
                    } else {
                        Glide.with(MainActivity.this).load(new File(imageList.get(0))).into(preview1);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
