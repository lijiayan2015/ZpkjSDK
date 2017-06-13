package com.zpkj.tools.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Email:lijiayan_mail@163.com
 * @created_time 2017/06/09 17:32
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG             #
 * #                                                   #
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class OkHttpUtils {
    private static final String TAG = "com.zpkj.OkHttpUtils";
    /**
     * 连接超时
     */
    public static long DEFALUT_TIMEOUT = 30;

    /**
     * 读超时
     */
    public static long DEFAULT_READTIMEOUT = 10;

    /**
     * 写超时
     */
    public static long DEFAULT_WRITEOUT = 60;

    public static OkHttpUtils instance;

    private OkHttpClient ljyOkHttpClient;


    private Handler ljyHander;

    private OkHttpClient.Builder ljyBuilder;

    private OkHttpUtils() {
        ljyBuilder = new OkHttpClient.Builder();
        ljyBuilder.connectTimeout(DEFALUT_TIMEOUT, TimeUnit.SECONDS);
        ljyBuilder.readTimeout(DEFAULT_READTIMEOUT, TimeUnit.SECONDS);
        ljyBuilder.writeTimeout(DEFAULT_WRITEOUT, TimeUnit.SECONDS);
        ljyHander = new Handler(Looper.getMainLooper());
        ljyOkHttpClient = ljyBuilder.build();
    }

    public OkHttpClient.Builder getBuilder() {
        return ljyBuilder;
    }

    public static OkHttpClient.Builder setTimeOut(long timeOut, TimeUnit timeUnit) {
        getInstance().getBuilder().connectTimeout(timeOut, timeUnit);
        return getInstance().ljyBuilder;
    }

    public static OkHttpClient.Builder setReadTimeOut(long timeOut, TimeUnit timeUnit) {
        getInstance().getBuilder().readTimeout(timeOut, timeUnit);
        return getInstance().ljyBuilder;
    }

    public static OkHttpClient.Builder setWriteTimeOut(long timeOut, TimeUnit timeUnit) {
        getInstance().getBuilder().writeTimeout(timeOut, timeUnit);
        return getInstance().ljyBuilder;
    }

    public synchronized static OkHttpUtils getInstance() {
        if (instance == null) {
            instance = new OkHttpUtils();
        }
        return instance;
    }

    /**
     * 参数内部类
     */
    public static class Param {
        public String key;
        public String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.value = value;
            this.key = key;
        }
    }

    public static abstract class ResultCallBack<T> {
        Type type;

        public ResultCallBack() {
            this.type = getSupperClassTypeParameter(getClass());
        }

        static Type getSupperClassTypeParameter(Class<?> subClass) {
            Type superclass = subClass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功
         *
         * @param respose
         */
        public abstract void onSuccess(T respose);

        /**
         * @param e
         */
        public abstract void onFailure(Exception e);

        /**
         * 请求的call
         *
         * @param call
         */
        public void requestCall(Call call) {

        }

        /**
         * 下载文件
         *
         * @param progress 当前下载进度
         */
        public void downLoading(int progress) {
        }

        /**
         * 下载文件的结果
         *
         * @param result true:成功  false:失败
         */
        public void downFileDone(boolean result) {
        }
    }


    /**
     * get request
     *
     * @param url      request url not null
     * @param callBack request callback not null
     */
    public static void doGet(@NonNull String url, @NonNull ResultCallBack callBack) {
        getInstance().getRequest(url, callBack);
    }

    private void getRequest(String url, final ResultCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        deliverResultCallBack(callBack, request);
    }

    private void deliverResultCallBack(final ResultCallBack callBack, Request request) {
        final Call call = ljyOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null && !call.isCanceled()) {
                    sendFailCallBack(callBack, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callBack.type == String.class) {
                        sendSuccessCallBack(callBack, str);

                    } else {
                        Object obj = JsonUtils.deSerilize(str, callBack.type);
                        sendSuccessCallBack(callBack, obj);
                    }
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "convert json failure");
                    e.printStackTrace();
                }

                if (callBack != null) {
                    callBack.requestCall(call);
                }
            }

        });
    }

    private void sendFailCallBack(final ResultCallBack callBack, final IOException e) {
        ljyHander.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallBack(final ResultCallBack callBack, final Object obj) {
        ljyHander.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(obj);
                }
            }
        });
    }

    /**
     * get request with header
     *
     * @param url      request url not null
     * @param headers  callBack request callback not null
     * @param callBack request headers not null
     */
    public static void doGet(@NonNull String url, @NonNull List<Param> headers, @NonNull ResultCallBack callBack) {
        getInstance().getRequest(url, headers, callBack);
    }

    private void getRequest(String url, List<Param> headers, ResultCallBack callBack) {
        if (headers == null || headers.size() == 0) {
            doGet(url, callBack);
        } else {
            Headers.Builder mheaders = new Headers.Builder();
            for (Param p : headers) {
                if (paramsIsNull(p)) continue;
                mheaders.add(p.key, p.value);
            }
            Request request = new Request.Builder()
                    .url(url)
                    .headers(mheaders.build())
                    .build();
            deliverResultCallBack(callBack, request);
        }
    }

    private boolean StringIsEmpty(String url) {
        return url == null || url.length() == 0 || url.trim().length() == 0;
    }

    private boolean paramsIsNull(Param p) {
        return p == null || StringIsEmpty(p.key) || StringIsEmpty(p.value);
    }

    /**
     * post request
     *
     * @param url      request url not null
     * @param params   request params not null
     * @param callBack request callback not null
     */
    public static void doPost(@NonNull String url, @NonNull List<Param> params, @NonNull ResultCallBack callBack) {
        getInstance().postRequest(url, params, callBack);
    }

    private void postRequest(String url, List<Param> params, ResultCallBack callBack) {
        deliverResultCallBack(callBack, buildPostRequest(url, params));
    }

    private Request buildPostRequest(String url, List<Param> params) {
        Request.Builder builder = new Request.Builder();
        if (params == null || params.size() == 0) {
            return builder.url(url).build();
        } else {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (Param p : params) {
                if (paramsIsNull(p)) continue;
                bodyBuilder.add(p.key, p.value);
            }
            RequestBody requestBody = bodyBuilder.build();
            return new Request.Builder().url(url).post(requestBody).build();
        }
    }

    /**
     * post request
     *
     * @param url      request url not null
     * @param params   request params not null
     * @param headers  request headers not null
     * @param callBack request callback not null
     */
    public static void doPost(@NonNull String url, @NonNull List<Param> params, @NonNull List<Param> headers, @NonNull ResultCallBack callBack) {
        getInstance().postRequest(url, params, headers, callBack);
    }

    private void postRequest(String url, List<Param> params, List<Param> headers, ResultCallBack callBack) {
        deliverResultCallBack(callBack, buildPostRequest(url, params, headers));
    }

    /**
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Request buildPostRequest(String url, @NonNull List<Param> params, @NonNull List<Param> headers) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Param p : params) {
            if (paramsIsNull(p)) continue;
            builder.add(p.key, p.value);
        }
        RequestBody requestBody = builder.build();
        Headers.Builder buildHeader = new Headers.Builder();
        for (Param p : headers) {
            if (paramsIsNull(p)) continue;
            buildHeader.add(p.key, p.value);
        }
        return new Request.Builder().url(url).post(requestBody).headers(buildHeader.build()).build();
    }

    /**
     * upload file
     *
     * @param url      upload url
     * @param file     upload file
     * @param params   upload params
     * @param headers  upload headers
     * @param callBack upload callback
     */
    public static void doUpLoadFile(@NonNull String url, @NonNull File file, List<Param> params, List<Param> headers, @NonNull ResultCallBack callBack) {
        getInstance().postImageFile(url, file, callBack, params, headers);
    }

    private void postImageFile(String url, File file, ResultCallBack callBack, List<Param> params, List<Param> headers) {
        deliverResultCallBack(callBack, buildPostReqeust(url, file, params, headers));
    }

    private Request buildPostReqeust(String url, File file, List<Param> params, List<Param> headers) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Param param : params) {
            if (paramsIsNull(param))
                continue;
            builder.addFormDataPart(param.key, param.value);
        }
        builder.addPart(Headers.of("Content-Disposition", "form-data;" +
                        " name=\"file\";filename=\"" + file.getName() + "\""),
                RequestBody.create(MediaType.parse("*/*"), file));
        RequestBody requestBody = builder.build();

        Headers.Builder buildHeader = new Headers.Builder();
        for (Param p : headers) {
            if (paramsIsNull(p))
                continue;
            buildHeader.add(p.key, p.value);
        }
        return new Request.Builder().url(url).post(requestBody).headers(buildHeader.build()).build();
    }

    /**
     * download file
     *
     * @param url
     * @param filePath save dir path not null
     * @param fileName save file name's filename not null
     * @param params   download params
     * @param headers  download headers
     * @param callBack download callBack not null
     */
    public static void doDownLoadFile(@NonNull String url,
                                      @NonNull String filePath,
                                      @NonNull String fileName,
                                      List<Param> params,
                                      List<Param> headers,
                                      @NonNull ResultCallBack callBack) {
        getInstance().postDownFile(url, filePath, fileName, callBack, params, headers);
    }

    private void postDownFile(String url, String filePath, String fileName, ResultCallBack callBack, List<Param> params, List<Param> headers) {
        deliverDownResultCallBack(filePath, fileName, callBack, buildPostRequest(url, params, headers));
    }

    private void deliverDownResultCallBack(final String filePath, final String fileName, final ResultCallBack callBack, Request request) {
        OkHttpClient.Builder builder = ljyOkHttpClient.newBuilder();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                return response.newBuilder().body(response.body()).build();
            }
        };
        builder.networkInterceptors().add(interceptor);
        final Call call = builder.build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.downFileDone(false);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    File parent = new File(filePath);
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    File file = new File(parent, fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    //读取文件
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    is = response.body().byteStream();
                    try {
                        long total = response.body().contentLength();
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) ((sum * 1.0 / total) * 100);
                            Log.d("DownLoadProgress:", "progress:" + progress);
                            callBack.downLoading(progress);
                        }
                        fos.flush();
                        //下载成功
                        Log.d("DownLoadFile", "downLoad Done");
                        callBack.downFileDone(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("DownLoadFile", "downLoad Fail");
                        callBack.downFileDone(false);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else callBack.downFileDone(false);
            }
        });
    }


}
