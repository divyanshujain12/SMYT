package com.example.divyanshu.smyt.musicupload;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.divyanshu.smyt.Constants.API;
import com.neopixl.pixlui.components.textview.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class UploadFileToServer extends AsyncTask<Void, Integer, String> {
    long totalSize = 0;
    String filePath,json;
    TextView percentageTV;


    public UploadFileToServer(String filePath,String json, TextView percentageTV) {
        this.filePath = filePath;
        this.percentageTV = percentageTV;
        this.json = json;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        percentageTV.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(API.POST_MP3);
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
            File sourceFile = new File(filePath);
            entity.addPart("image", new FileBody(sourceFile));
            entity.addPart("data", new StringBody(json));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: " + statusCode;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG, "Response from server: " + result);
        super.onPostExecute(result);
    }
}
 