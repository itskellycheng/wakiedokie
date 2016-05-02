package com.wakiedokie.waikiedokie.util;

import android.util.Log;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//
//import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by kellycheng on 5/1/16.
 */
public class UploadVideo {

    private static final String SERVER_URL = "http://10.0.0.25:8080/AndroidAppServlet/VideoUploadServlet";

    private final OkHttpClient client = new OkHttpClient();

    public void run(String videoPath, String ownerID, String user2ID) throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ownerID", ownerID)
                .addFormDataPart("user2ID", user2ID)
                .addFormDataPart("video", videoPath, RequestBody.create(
                        MediaType.parse("video/mp4"), new File(videoPath)))
                .build();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }

//    private void uploadVideo(String videoPath) {
//        try
//        {
//            HttpClient client = new DefaultHttpClient();
//            File file = new File(videoPath);
//            HttpPost post = new HttpPost(server +"/api/docfile/");
//
//            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entityBuilder.addBinaryBody("uploadfile", file);
//            // add more key/value pairs here as needed
//
//            HttpEntity entity = entityBuilder.build();
//            post.setEntity(entity);
//
//            HttpResponse response = client.execute(post);
//            HttpEntity httpEntity = response.getEntity();
//
//            Log.v("result", EntityUtils.toString(httpEntity));
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
