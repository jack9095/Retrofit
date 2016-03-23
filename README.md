Retrofit

Retrofit的基本用法

第一： 首先需要依赖以下JAR：
compile 'com.squareup.retrofit2:retrofit:2.0.0'
compile 'com.squareup.retrofit2:converter-gson:2.0.0'
compile 'com.squareup.okhttp3:okhttp:3.2.0'


Eclipse需要自己去下载相应的JAR

第二 ： Retrofit 在初始化的时候，需要指定一个baseUrl: private static Retrofit.Builder mBuilder = new Retrofit.Builder() .baseUrl("https://github.com/") .addConverterFactory(GsonConverterFactory.create());

在我们定义请求的接口时，会传入具体的接口的地址： @GET("wangfeigit/Retrofit.git") // 这里也可以为完整的URL Call> getUser();

Retrofit会帮我们完成拼接，最后的URL是：https://github.com/wangfeigit/Retrofit.git 需要注意的是baseUrl最好以”/”结尾

Get查询参数

查询参数是一种很常见的客户端往服务端传递数据的方式，比如我们需要传一个id给服务端，那么URL可能是这样的：

https://github.com/wangfeigit/tasks?id=1231

Retrofit 定义实现查询参数：

public interface TaskService {
 @GET("/tasks") Call getTask(@Query("id") long taskId); }

方法getTask需要传入taskId，这个参数会被映射到@Query(“id”)中的id中，最后的URL会变成这样：

/tasks?id=1

有时我们需要对于一个id参数传入多个值，比如这样：

https://github.com/wangfeigit/tasks?id=123&id=124&id=125 1

对于这样的需求，Retrofit 通过传入一个List来实现：

public interface TaskService {
 @GET("/tasks") Call> getTask(@Query("id") List taskIds); }

这样，拼接后的URL就是我们需要的那样。

有时，我们在给服务端传递数据时，有些参数是可选的，比如下面的URL：

https://github.com/wangfeigit/tasks?sort=value-of-order-parameter1

sort参数是可选的，有些情况下可能不需要传入。

接口的定义：

public interface TaskService {
 @GET("/tasks") Call> getTasks(@Query("sort") String order); }

那么，在我们不想添加排序控制的时候，我们可以传入null,Retrofit 会忽略值为null的参数。

service.getTasks(null); 1

需要注意的是，可忽略参数的参数类型不能是int, float, long这些基本类型，应该用Integer, Float, Long来代替。

Post 提交参数

Retrofit Post方式提交表单形式的参数需要添加标记@FormUrlEncoded，通过@Field注释添加键值对。 接口定义：

public interface UserClient { @FormUrlEncoded @POST("/index.php?c=User&m=login") Call> login(@Field("phone") String phone, @Field("password") String password); }

客户端调用：

private void login() { UserClient userClient = ServiceGenerator.createService(UserClient.class); Call> call = userClient.login("18072850706","123456"); call.enqueue(new Callback>() {
    });
}


Post 提交JSON数据

有时提交的数据量比较大时，用键值对的方式提交参数不太方便，Retrofit可以通过@Body注释，直接传递一个对象给请求主体，Retrofit通过JSON转化器，把对象映射成JSON数据。

接口定义：

public interface TaskService {
 @POST("/tasks") Call createTask(@Body Task task); }

传递实体需要有Model：

public class Task {
 private long id; private String text;
public Task() {}
public Task(long id, String text) {
    this.id = id;
    this.text = text;
}


}

客户端调用：

Task task = new Task(1, "my task title");
 Call call = taskService.createTask(task);
 call.enqueue(new Callback() {}); 

这样，服务端得到的是JOSN数据：

{ "id": 1, "text": "my task title" }

文件上传

Retrofit 支持 Multipart 请求，所以我们可以用它来实现文件上传，对于要实现文件上传的接口，需要添加@Multipart注释。 接口定义：

public interface FileUploadService { @Multipart @POST("index.php?c=Upload&m=doUpload") Call upload( @Part("file\"; filename=\"image.png\" ") RequestBody file, @Part("description") RequestBody description); }

对于要上传的文件，我们需要把它包装成RequestBody 对象：

private void uploadFile( File file) { FileUploadService service = ServiceGenerator.createService(FileUploadService.class);
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


同步请求和异步请求

同步的请求会在同一个线程中执行，在Android中必须另开线程，否则在主线程中执行会抛出异常。 同步和异步请求的接口都是一样的，只是调用的时候会不一样：

public interface TaskService {
 @GET("/tasks") Call> getTasks(); }

同步请求调用：

TaskService taskService = ServiceGenerator.createService(TaskService.class);
 Call> call = taskService.getTasks();
 List> tasks = call.execute().body(); 

异步请求调用：

TaskService taskService = ServiceGenerator.createService(TaskService.class);
 Call> call = taskService.getTasks();
 call.enqueue(new Callback>() {
 @Override public void onResponse(Call> call, Response> response) { if (response.isSuccess()) { // tasks available } else { // error response, no access to resource? } }
@Override
public void onFailure(Call<List<Task>> call, Throwable t) {
    // something went completely south (like no internet connection)
    Log.d("Error", t.getMessage());
}


}

异步请求实现了一个CallBack，包含了两个回调方法：onResponse和 onFailure，在onResponse中我们可以拿到我们需要的实体数据，在onFailure中，可以拿到错误的Log。

以上简单描述，有什么问题可以相互讨论 
