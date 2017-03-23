package com.example.divyanshu.smyt.uploadFragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.request.SimpleMultiPartRequest;
import com.example.divyanshu.smyt.Adapters.MusicAdapter;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseFragment;
import com.example.divyanshu.smyt.Models.MusicModel;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CommonFunctions;
import com.example.divyanshu.smyt.Utils.MySharedPereference;
import com.example.divyanshu.smyt.Utils.PictureHelper;
import com.example.divyanshu.smyt.Utils.UploadFileToServer;
import com.example.divyanshu.smyt.Utils.Utils;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by divyanshuPC on 3/9/2017.
 */

public class UploadMusicFragment extends BaseFragment {

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

    private ArrayList<MusicModel> musicModels = new ArrayList<>();
    private String path;
    private String filename;
    private MusicAdapter musicAdapter;
    SimpleMultiPartRequest simpleMultiPartRequest;

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

    @OnClick({R.id.selectImageFB, R.id.postVideoBT})
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
        }
    }

    private void hitMultiPartRequest() {

        TextView textView = (TextView) selectedMusicRV.getLayoutManager().getChildAt(0).findViewById(R.id.uploadPercentTV);
        textView.setVisibility(View.VISIBLE);

        UploadFileToServer uploadFileToServer = new UploadFileToServer(getActivity(), musicModels.get(0).getFilePath(), createJsonForUploadMusic(), videoTitleET.getText().toString(), textView);
        uploadFileToServer.execute();
    /*  simpleMultiPartRequest  = new SimpleMultiPartRequest( API.POST_MP3, new Response.Listener<String>() {
           @Override
           public void onResponse(String s) {
               String response = s;
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError volleyError) {
            VolleyError error = CallWebService.configureErrorMessage(volleyError);
               String message = error.getMessage();
           }
       });
        setParams();


        MyApp.getInstance().addToRequestQueue(simpleMultiPartRequest);*/
    }

    protected void setParams() {
        simpleMultiPartRequest.addStringParam(Constants.CUSTOMER_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CUSTOMER_ID));
        simpleMultiPartRequest.addStringParam(Constants.CATEGORY_ID, MySharedPereference.getInstance().getString(getContext(), Constants.CATEGORY_ID));
        simpleMultiPartRequest.addStringParam(Constants.FILE_NAME, filename);
        simpleMultiPartRequest.addStringParam(Constants.SHARE_STATUS, "Public");
        simpleMultiPartRequest.addStringParam(Constants.E_DATE, String.valueOf(Utils.getCurrentTimeInMillisecond()));
        simpleMultiPartRequest.addFile(filename, path);

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

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                getData(data);

            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    private void getData(Intent data) {
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
}
