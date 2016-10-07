package com.example.divyanshu.smyt.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TakePictureHelper {

    public final static int REQUEST_CAMERA = 1;
    public final static int REQUEST_OTHER = 2;

    private Uri cameraImageUri;
    private static TakePictureHelper takePictureHelper = null;

    private TakePictureHelper() {
    }

    public static TakePictureHelper getInstance() {
        if (takePictureHelper == null)
            takePictureHelper = new TakePictureHelper();
        return takePictureHelper;
    }

    /**
     * Request picture from camera using the given title
     */
    public void takeFromCamera(Activity activity, String title) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File cameraImageOutputFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                createCameraImageFileName());
        cameraImageUri = Uri.fromFile(cameraImageOutputFile);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * Request picture from any app (gallery or whatever) using the given title
     */
    public void takeFromGallery(Activity activity, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, title), REQUEST_OTHER);
    }

    /**
     * Retrieve the picture, taken from camera or gallery.
     *
     * @return the picture Uri, or null if no picture was taken.
     */
    public HashMap<String, Bitmap> retrievePicturePath(Activity activity, int requestCode, int resultCode, Intent data) throws Exception {

        if (resultCode == Activity.RESULT_OK) {
            Uri result = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), result);
            String filePath = getRealPathFromURI(activity, result);
            bitmap = rotatePicInPortraitMode(filePath, bitmap);
            bitmap = resizeImageForImageView(300, bitmap);
            HashMap<String, Bitmap> hashMap = new HashMap<>();
            hashMap.put(filePath, bitmap);
            return hashMap;
        }

        return null;
    }


    private String getRealPathFromURI(Context context, Uri contentUri) throws Exception {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String createCameraImageFileName() {
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return "SmytProfile" + ".jpg";
    }


    private Bitmap rotatePicInPortraitMode(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface
                ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                break;
        }
        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public Bitmap resizeImageForImageView(int scaleSize, Bitmap bitmap) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if (originalHeight > originalWidth) {
            newHeight = scaleSize;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (originalWidth > originalHeight) {
            newWidth = scaleSize;
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        } else if (originalHeight == originalWidth) {
            newHeight = scaleSize;
            newWidth = scaleSize;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }
}