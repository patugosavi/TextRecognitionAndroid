package com.example.textrecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    Button buttonGallery;
    TextView recognizeText;
    ImageView captureImage;
    private TextExtractCallback mCallback;

    public static final int REQUEST_FOR_IMAGE_FROM_GALLERY = 101;

    Uri uri;
    int isFirstCallback=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGallery = findViewById(R.id.button_gallery);
        recognizeText = findViewById(R.id.text);
        captureImage = findViewById(R.id.imageView);

        isFirstCallback=1;


        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });



    }




    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            captureImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (isFirstCallback){
            case 1:
                textScan(uri);
                textScan(uri);
                isFirstCallback=0;
                break;

            case 0:
                textScan(uri);
                break;

            default:
                textScan(uri);
                break;
        }




    }

    private void textScan(Uri uri) {
        TextScanner.getInstance(this)
                .init()
                .load(uri)
                .getCallback(new TextExtractCallback() {
                    @Override
                    public void onGetExtractText(List<String> textList) {
                        // Here you will get list of text

                        final StringBuilder text = new StringBuilder();
                        for (String s : textList) {
                            text.append(s).append("\n");
                        }
                        recognizeText.post(new Runnable() {
                            @Override
                            public void run() {
                                recognizeText.setText(text.toString());
                            }
                        });

                    }
                });
    }


    /**
     * Method for Open default device gallery
     */
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_FOR_IMAGE_FROM_GALLERY);
    }

}
