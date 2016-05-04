package com.wakiedokie.waikiedokie.integration.remote;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kellycheng on 5/4/16.
 */
public class DownloadVideo {



//    String name;

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//
//    }
//
//
//    @Override
//    protected void onCancelled() {
//        super.onCancelled();
//
//    }

//    /**
//     * Downloading file in background thread
//     * */
//    @Override
//    protected Integer doInBackground(Object... params) {
//
//        try {
//
//            URL url = new URL((String) params[1]);
//
//            name = ((String) params[1]).substring(((String) params[1])
//                    .lastIndexOf("/") + 1);
//
//            URLConnection conection = url.openConnection();
//            conection.connect();
//            // getting file length
//            int lengthOfFile = conection.getContentLength();
//
//            // input stream to read file - with 8k buffer
//            InputStream input = new BufferedInputStream(url.openStream(),
//                    8192);
//            File download = new File(Environment.getExternalStorageDirectory()
//                    + "/downloadWakie/");
//            if (!download.exists()) {
//                download.mkdir();
//            }
//            String strDownloadURL = download + "/" + name;
//            Log.v("log_tag", " down url   " + strDownloadURL);
//            FileOutputStream output = new FileOutputStream(strDownloadURL);
//
//            byte data[] = new byte[1024];
//
//            long total = 0;
//
//            // flushing output
//            output.flush();
//
//            // closing streams
//            output.close();
//            input.close();
//
//        }catch (Exception e) {
//        Log.e("Error: ", e.getMessage());
//    }
//    return 0;
//
//}
//
//
//
//
//    protected void onPostExecute(String file_url) {
//
//        // Do after downloaded file
//
//    }
}
