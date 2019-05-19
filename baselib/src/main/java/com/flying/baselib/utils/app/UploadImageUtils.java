package com.flying.baselib.utils.app;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class UploadImageUtils {


    public static String postFile(final String url, final Map<String, Object> map, File file, Context context) {
          String mstr = null;
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();


        Call requestCall = client.newCall(request);
        Response response = null;
        try {
            response = requestCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //响应成功
        if (response.isSuccessful()) {
            try {
                String str = response.body().string();
                JSONObject jsonObject = new JSONObject(str);
                mstr = jsonObject.getString("file_name");
            } catch (IOException e) {


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // readTimeout("请求超时时间" , 时间单位);
//        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("lfq", "onFailure");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    try {
//                        JSONObject jsonObject = new JSONObject(str);
//                        mstr = jsonObject.getString("file_name");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("lfq", response.message() + " , body " + str);
//
//                } else {
//                    Log.i("lfq", response.message() + " error : body " + response.body().string());
//                }
//            }
//        });
        return mstr;

    }

}


