package com.wangfei.retrofit;


import com.wangfei.retrofit.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by fei.wang on 2016/2/22.
 * 在我们定义请求的接口时，会传入具体的接口的地址
 */
public interface UserClient {

    @GET("/index.php?c=User&m=getUser")
    Call<List<User>> getUser();   //  返回的数据为List<User>

    @GET("/index.php?c=User&m=getUserById")
    Call<List<User>> getUserById(@Query("id") String id);  // 把服务端需要的id传上去，返回通过id查到的值

    @POST("/index.php?c=User&m=register")
    Call<String> register(@Body User user);   // 通过register方法提交对象User到服务端，返回安卓端的是String字符串


    // 利用表单的方式把数据提交到服务端，返回安卓端的是List<User>
    @FormUrlEncoded   // @FormUrlEncoded注解来发送表单数据
    @POST("/index.php?c=User&m=login")
    Call<List<User>> login(@Field("phone") String phone, @Field("password") String password);  //通过@Field注释添加键值对
    // 使用 @Field 注解和参数来指定每个表单项的Key，value为参数的值
}
