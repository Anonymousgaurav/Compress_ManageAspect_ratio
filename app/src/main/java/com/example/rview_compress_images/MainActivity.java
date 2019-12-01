package com.example.rview_compress_images;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rview_compress_images.Adapter.BookingsStatus;
import com.fxn.pix.Pix;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> totalGalleryImagesList;
    ArrayList<String> selectedImagesList;
    private RecyclerView rview;
    ImageButton imgbtn;
    int RequestCode = 2;
    long length;
    File file;
    Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgbtn = findViewById(R.id.imgbtn);
        rview = findViewById(R.id.rview);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rview.setLayoutManager(llm);

        totalGalleryImagesList = new ArrayList<String>();
        selectedImagesList = new ArrayList<String>();

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfPics = totalGalleryImagesList.size();
                if (noOfPics < 5) {
                    Pix.start(MainActivity.this,
                            RequestCode,
                            5 - noOfPics);
                } else {
                    Toast.makeText(getApplicationContext(), "Limited to 5 ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {

            selectedImagesList = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            for (int i = 0; i < selectedImagesList.size(); i++) {

                file = new File(selectedImagesList.get(i));
                length = file.length() / 1024;
                Log.d("abc", String.valueOf(length));
                Log.d("def", String.valueOf(file));

                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                myBitmap= scaleBitmap(bm);

                if (length < 1024)
                {
                    Toast.makeText(this, "You Choose Right Image", Toast.LENGTH_SHORT).show();

                    Uri temp1Uri = getImageUri(getApplicationContext(), myBitmap);
                    File finalFile1 = new File(getRealPathFromURI(temp1Uri));
                    String ff1 = finalFile1.getPath();
                    totalGalleryImagesList.add(ff1);

                    System.out.println("Images ===" + totalGalleryImagesList.size());
                } else {
                    Uri tempUri = getImageUri(getApplicationContext(), myBitmap);
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    String ff = finalFile.getPath();
                    totalGalleryImagesList.add(ff);
                    Log.d("finl", ff);
                    Toast.makeText(this, "Compressed", Toast.LENGTH_SHORT).show();
                }

            }
            BookingsStatus bs = new BookingsStatus(MainActivity.this,totalGalleryImagesList);
            rview.setAdapter(bs);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Bitmap scaleBitmap(Bitmap bm)
    {
        int min = 250;

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width > height)
        {
            // landscape
            float ratio = (float) width / height;
            height = min;
            width = (int) (height*ratio);
        } else
        {
            float ratio = (float) height / width;
            width = min;
            height = (int)(width*ratio);
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

}
