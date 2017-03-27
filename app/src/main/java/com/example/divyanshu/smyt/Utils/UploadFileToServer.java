package com.example.divyanshu.smyt.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.musicupload.MultipartUtility;
import com.neopixl.pixlui.components.textview.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadFileToServer extends AsyncTask<String, String, String> {
    long totalSize = 0;
    String filePath, json, title;
    TextView percentageTV;
    File sourceFile;
    String charset = "UTF-8";
    Context context;
    ProgressDialog progressDialog;

    public UploadFileToServer(Context context, String filePath, String json, String title, TextView percentageTV) {
        this.filePath = filePath;
        this.percentageTV = percentageTV;
        this.json = json;
        this.context = context;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("File Uploading!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        sourceFile = new File(filePath);
        totalSize = (int) sourceFile.length();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("PROG", progress[0]);
        percentageTV.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(String... args) {
        String responseString = "";
        try {
            MultipartUtility multipart = new MultipartUtility(API.POST_MP3, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            setParams(context, filePath, multipart);
            multipart.addFilePart("fileUpload", new File(filePath));

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                responseString = line;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("Response", "Response from server: " + result);
        super.onPostExecute(result);
        if (progressDialog != null)
            progressDialog.cancel();
    }

    private void setParams(Context context, String filename, MultipartUtility multipartUtility) {
        multipartUtility.addFormField(Constants.CUSTOMER_ID, MySharedPereference.getInstance().getString(context, Constants.CUSTOMER_ID));
        multipartUtility.addFormField(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(context, Constants.CATEGORY_ID));
        multipartUtility.addFormField(Constants.FILE_NAME, filename);
        multipartUtility.addFormField(Constants.TITLE, title);
        multipartUtility.addFormField(Constants.SHARE_STATUS, "Public");
        multipartUtility.addFormField(Constants.E_DATE, String.valueOf(Utils.getCurrentTimeInMillisecond()));


    }

}