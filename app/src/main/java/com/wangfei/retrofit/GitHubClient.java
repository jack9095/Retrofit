package com.wangfei.retrofit;

import com.wangfei.retrofit.model.Contributor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by fei.wang on 2016/3/7.
 */
public interface GitHubClient {

   @GET("/repos/{owner}/{repo}/contributors")   // {}中的表示待定参数,路径中的参数使用@Path(“XXX”)
   Call<List<Contributor>> contributors(
           @Path("owner") String owner,
           @Path("repo") String repo
   );

}
