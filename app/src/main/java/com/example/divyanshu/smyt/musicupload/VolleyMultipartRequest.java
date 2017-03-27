package com.example.divyanshu.smyt.musicupload;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.divyanshu.smyt.Constants.API;
import com.neopixl.pixlui.components.textview.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Custom request to make multipart header and upload file.
 * <p>
 * Sketch Project Studio
 * Created by Angga on 27/04/2016 12.05.
 */
public class VolleyMultipartRequest extends AsyncTask<String, String, String> {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private String charset;

    long totalSize = 0;
    String filePath;
    TextView percentageTV;
    String fileHeader1;
    StringBuilder stringBuilder = new StringBuilder();
    File sourceFile;

    public VolleyMultipartRequest(String filePath, TextView percentageTV) {
        this.filePath = filePath;
        this.percentageTV = percentageTV;
        boundary = "===" + System.currentTimeMillis() + "===";
    }

    @Override
    protected void onPreExecute() {

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
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        String fileName = sourceFile.getName();

        try {
            connection = (HttpURLConnection) new URL(API.POST_MP3).openConnection();
            connection.setRequestMethod("POST");
            String boundary = "---------------------------boundary";
            String tail = "\r\n--" + boundary + "--\r\n";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);

            String metadataPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                    + "" + "\r\n";

            fileHeader1 = stringBuilder.toString();

            long fileLength = sourceFile.length() + tail.length();
            String fileHeader2 = "Content-length: " + fileLength + "\r\n";
            String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
            String stringData = metadataPart + fileHeader;

            long requestLength = stringData.length() + fileLength;
            connection.setRequestProperty("Content-length", "" + requestLength);
            connection.setFixedLengthStreamingMode((int) requestLength);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(stringData);
            out.flush();

            int progress = 0;
            int bytesRead = 0;
            byte buf[] = new byte[1024];
            BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
            while ((bytesRead = bufInput.read(buf)) != -1) {
                // write output
                out.write(buf, 0, bytesRead);
                out.flush();
                progress += bytesRead; // Here progress is total uploaded bytes

                publishProgress("" + (int) ((progress * 100) / totalSize)); // sending progress percent to publishProgress
            }

            // Write closing boundary and close stream
            out.writeBytes(tail);
            out.flush();
            out.close();

            // Get server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
            // Exception
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("Response", "Response from server: " + result);
        super.onPostExecute(result);
    }

    public void addFormField(String name, String value) {
        stringBuilder.append("--" + boundary).append(LINE_FEED);
        stringBuilder.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        stringBuilder.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        stringBuilder.append(LINE_FEED);
        stringBuilder.append(value).append(LINE_FEED);

    }
}