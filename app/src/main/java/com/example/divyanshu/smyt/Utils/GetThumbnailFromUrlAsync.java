package com.example.divyanshu.smyt.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by as on 1/6/2017.
 */

public class GetThumbnailFromUrlAsync extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        try {
            Bitmap bitmap = CommonFunctions.getInstance().retrieveVideoFrameFromVideo(url);
            String baseImage = PictureHelper.getInstance().convertBitmapToBase64(bitmap);
            return baseImage;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String image) {
        super.onPostExecute(image);


    }
}
