package com.example.divyanshu.smyt.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TakePictureHelper {

    public final static int REQUEST_CAMERA = 1;
    public final static int REQUEST_OTHER = 2;
    private static final String TAG = TakePictureHelper.class.getName();

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
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        intent.putExtra("return-data", true);
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
            Uri result = null;
            Bitmap bitmap;
            if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                Log.d(TAG,String.valueOf(bitmapSizeInKB(bitmap)));
                result = getImageUri(activity, bitmap);
            } else
                result = data.getData();

            String filePath = getRealPathFromURI(activity, result);
            if (filePath != null && filePath.length() > 0) {
                bitmap = getBitmap(activity, result);
                Log.d(TAG,String.valueOf(bitmapSizeInKB(bitmap)));
                HashMap<String, Bitmap> hashMap = new HashMap<>();
                hashMap.put(filePath, bitmap);
                return hashMap;
            }
        }

        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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


    /*private Bitmap rotatePicInPortraitMode(String photoPath, Bitmap bitmap) throws IOException {
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
    }*/

  /*  private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }*/

    private int bitmapSizeInKB(Bitmap bitmap) {
        return bitmap.getByteCount() / 1000;
    }

    private Bitmap getBitmap(Context context, Uri uri) {
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 40000;
            in = context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d(TAG, "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}