package com.jx.retrofitlearn;

import com.jx.retrofitlearn.model.Msg;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by jiang.xu on 2016/2/29.
 */
public interface FileUploadService {
    @Multipart  //@Multipart 注解来发送 multipart (文件)数据
    @POST("index.php?c=Upload&m=doUpload")
  //  @POST("index.php/Upload/doUpload")
    Call<Msg> upload(
            @Part("file\"; filename=\"image.png\" ") RequestBody file,
            @Part("description") RequestBody description);
            // 使用 @Part 注解定义要发送的每个文件。
}
