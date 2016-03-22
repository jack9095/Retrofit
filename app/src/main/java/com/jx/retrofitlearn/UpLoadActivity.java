package com.jx.retrofitlearn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jx.retrofitlearn.model.Msg;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpLoadActivity extends AppCompatActivity {
    private ImageView mImageView;
    public static final int PICKED_PICTURE = 100;
    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        findView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKED_PICTURE) {
            String filePath = getSelectImageFromGallery(data);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            mImageView.setImageBitmap(bitmap);
            mFile = new File(filePath);
        }
    }

    public void findView() {
        mImageView = (ImageView) findViewById(R.id.mImageView);
        findViewById(R.id.mSelectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        findViewById(R.id.mUploadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(mFile);
            }
        });
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICKED_PICTURE);
    }

    private void uploadFile( File file) {
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        Call<Msg> call = service.upload(requestBody, description);
        call.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Log.e("Upload", t.getMessage());
            }
        });
    }

    


    public String getSelectImageFromGallery(Intent data) {
        if (data == null)
            return null;
        Uri thisUri = data.getData();
        String thePath = UriUtils.getPath(this, thisUri);
        return thePath;
    }
}
