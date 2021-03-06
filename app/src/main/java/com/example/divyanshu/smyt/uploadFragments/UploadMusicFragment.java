package com.example.divyanshu.smyt.uploadFragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.request.SimpleMultiPartRequest;
import com.example.divyanshu.smyt.Adapters.MusicAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.CustomViews.CustomToasts;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.MusicModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PictureHelper;
import com.example.divyanshu.smyt.Utils.Utils;
import com.example.divyanshu.smyt.activities.BottomTabActivity;
import com.example.divyanshu.smyt.broadcastreceivers.BroadcastSenderClass;
import com.example.divyanshu.smyt.musicupload.MultipartUtility;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by divyanshuPC on 3/9/2017.
 */

public class UploadMusicFragment extends BaseFragment {

    private static final int PICK_IMAGE_MULTIPLE = 101;
    @InjectView(R.id.videoTitleET)
    EditText videoTitleET;
    @InjectView(R.id.titleLL)
    LinearLayout titleLL;
    @InjectView(R.id.postVideoBT)
    Button postVideoBT;
    @InjectView(R.id.selectedMusicRV)
    RecyclerView selectedMusicRV;
    @InjectView(R.id.selectImageFB)
    FloatingActionButton selectImageFB;
    @InjectView(R.id.musicThumbIV)
    ImageView musicThumbIV;
    @InjectView(R.id.addImageIV)
    ImageView addImageIV;
    @InjectView(R.id.addThumbLL)
    LinearLayout addThumbLL;
    @InjectView(R.id.musicThumbRL)
    RelativeLayout musicThumbRL;

    private ArrayList<MusicModel> musicModels = new ArrayList<>();
    private String path;
    private String filename;
    private MusicAdapter musicAdapter;
    SimpleMultiPartRequest simpleMultiPartRequest;
    HashMap<String, Bitmap> bitmapHashMap;
    private String thumbPath;
    ProgressDialog progressDialog;

    public static UploadMusicFragment getInstance() {
        UploadMusicFragment uploadMusicFragment = new UploadMusicFragment();
        return uploadMusicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_music, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        selectedMusicRV.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.selectImageFB, R.id.postVideoBT, R.id.musicThumbRL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectImageFB:
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                // intent_upload.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent_upload, 1);
                break;
            case R.id.postVideoBT:


                hitMultiPartRequest();
                break;
            case R.id.musicThumbRL:
                openGallery();
                break;
        }
    }

    private void hitMultiPartRequest() {
        String title = videoTitleET.getText().toString();

        if (title.length() > 0) {
            TextView textView = (TextView) selectedMusicRV.getLayoutManager().getChildAt(0).findViewById(R.id.uploadPercentTV);
            UploadFileToServer uploadFileToServer = new UploadFileToServer(getActivity(), musicModels.get(0).getFilePath(), thumbPath, createJsonForUploadMusic(), videoTitleET.getText().toString(), textView);
            uploadFileToServer.execute();
        } else {
            CustomToasts.getInstance(getContext()).showErrorToast("Title Cant left blank!");
        }

    }

    private String createJsonForUploadMusic() {
        JSONObject jsonObject = CommonFunctions.customerIdJsonObject(getContext());
        try {
            jsonObject.put(Constants.CUSTOMER_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CUSTOMER_ID));
            jsonObject.put(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
            jsonObject.put(Constants.FILE_NAME, filename);
            jsonObject.put(Constants.SHARE_STATUS, "Public");
            jsonObject.put(Constants.E_DATE, String.valueOf(Utils.getCurrentTimeInMillisecond()));
            jsonObject.put(filename, path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    getAudioTracksData(data);
                    break;
                case PICK_IMAGE_MULTIPLE:
                    try {
                        bitmapHashMap = PictureHelper.getInstance().retrievePicturePath(getActivity(), requestCode, resultCode, data);
                        Set<String> keys = bitmapHashMap.keySet();
                        for (String filePath : keys) {
                            musicThumbIV.setImageBitmap(bitmapHashMap.get(filePath));
                            thumbPath = filePath;
                        }
                        addThumbLL.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    private void getThumbData() {

    }

    private void getAudioTracksData(Intent data) {
        musicModels.clear();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri uri = clipData.getItemAt(i).getUri();
                addItemToArrayList(uri);
            }
        } else {
            Uri uri = data.getData();
            addItemToArrayList(uri);
        }
        musicAdapter = new MusicAdapter(getContext(), musicModels, this);
        selectedMusicRV.setAdapter(musicAdapter);
    }

    private void addItemToArrayList(Uri uri) {
        path = PictureHelper.getPath(getContext(), uri);
        filename = path.substring(path.lastIndexOf("/") + 1);
        MusicModel musicModel = new MusicModel();
        musicModel.setFileName(filename);
        musicModel.setFilePath(path);
        musicModels.add(musicModel);
    }

    public class UploadFileToServer extends AsyncTask<String, String, String> {
        long totalSize = 0;
        String filePath, json, title;
        TextView percentageTV;
        File sourceFile;
        String charset = "UTF-8";
        Context context;
        String thumbPath;

        public UploadFileToServer(Context context, String filePath, String thumbPath, String json, String title, TextView percentageTV) {
            this.filePath = filePath;
            this.percentageTV = percentageTV;
            this.json = json;
            this.context = context;
            this.title = title;
            this.thumbPath = thumbPath;
            ;
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
                if (thumbPath != null && !thumbPath.equals(""))
                multipart.addFilePart("thumbnail", new File(thumbPath));

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
            if (result != null && !result.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.getBoolean(Constants.STATUS_CODE)) {
                        CustomToasts.getInstance(getContext()).showErrorToast(jsonObject.getString(Constants.MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (progressDialog != null)
                progressDialog.cancel();

            reset();

        }

        private void reset() {
            musicAdapter.removeMusicModel(0);
            videoTitleET.setText("");
            addThumbLL.setVisibility(View.VISIBLE);
            musicThumbIV.setImageResource(R.drawable.black_shade);
            BroadcastSenderClass.getInstance().reloadAllVideoData(getContext());
            ((BottomTabActivity)getActivity()).onResetPager();
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }
}
