package com.wangfei.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wangfei.retrofit.model.Contributor;
import com.wangfei.retrofit.model.User;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserById();
            }
        });   //  GET

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpLoadActivity.class);
                startActivity(intent);
            }
        });   // 上传

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });   // POST JSON

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login();
            }
        });    // POST PARAM

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UpLoadActivity.class));
            }
        });    // 跳转上传图片（文件）页面

    }

    /**同步的请求会在同一个线程中执行，在Android中必须另开线程，否则在主线程中执行会抛出异常。*/
    private void getUser() {
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<List<User>> call = userClient.getUser();
        try {
            List<User> contributors = call.execute().body();
            for (User contributor : contributors) {
                Log.i("MainActivity", "contributor  " + contributor.getName() + " " + contributor.getSex());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步请求调用
     * 异步请求实现了一个CallBack，包含了两个回调方法：onResponse和 onFailure，
     * 在onResponse中我们可以拿到我们需要的实体数据，
     * 在onFailure中，可以拿到错误的Log
     */
    private void getUserById() {
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<List<User>> call = userClient.getUserById("1");
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i("MainActivity", response.body().toString());
                for (User contributor : response.body()) {
                    Log.i("MainActivity", "contributor  " + contributor.getName() + " " + contributor.getSex());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("MainActivity", t.getMessage());
            }
        });
    }

    private void getAsyncUser() {
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<List<User>> call = userClient.getUser();
        //Call<List<User>> call=userClient.getUser2();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                for (User contributor : response.body()) {
                    Log.i("MainActivity", "contributor  " + contributor.getName() + " " + contributor.getSex());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("MainActivity", t.getMessage());
            }
        });
    }


    private void register() {
        User user = new User("哈哈", "123456", "男", "110", "", "0", "", "9", "100");
        UserClient userClient = ServiceGenerator.createService2(UserClient.class);
        Call<String> call = userClient.register(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("MainActivity", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("MainActivity", t.getMessage());
            }
        });
    }

    private void login() {
        UserClient userClient = ServiceGenerator.createService(UserClient.class);
        Call<List<User>> call = userClient.login("18072850706","123456");
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i("MainActivity", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("MainActivity", t.getMessage());
            }
        });
    }

    private void getContributor() {
        GitHubClient gitHubClient = ServiceGenerator.createService(GitHubClient.class);
        Call<List<Contributor>> call = gitHubClient.contributors("square", "retrofit");
        try {
            List<Contributor> contributors = call.execute().body();
            for (Contributor contributor : contributors) {
                Log.i("MainActivity", "contributor  " + contributor.login + " " + contributor.contributions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
