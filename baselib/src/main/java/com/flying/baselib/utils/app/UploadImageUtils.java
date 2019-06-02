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
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
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
        if (response != null && response.isSuccessful()) {
            try {
                String str = response.body().string();
                JSONObject jsonObject = new JSONObject(str);
                mstr = jsonObject.getString("file_name");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mstr;
    }

}


