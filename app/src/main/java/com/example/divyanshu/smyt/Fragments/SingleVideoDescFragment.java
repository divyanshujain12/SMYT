package com.example.divyanshu.smyt.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.divyanshu.smyt.activities.UserProfileActivity;
import com.example.divyanshu.smyt.Adapters.CommentsAdapter;
import com.example.divyanshu.smyt.Constants.API;
import com.example.divyanshu.smyt.Constants.ApiCodes;
import com.example.divyanshu.smyt.Constants.Constants;
import com.example.divyanshu.smyt.GlobalClasses.BaseDialogFragment;
import com.example.divyanshu.smyt.Models.VideoDetailModel;
import com.example.divyanshu.smyt.Parser.UniversalParser;
import com.example.divyanshu.smyt.R;
import com.example.divyanshu.smyt.Utils.CallWebService;
import com.example.divyanshu.smyt.Utils.ImageLoading;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by divyanshu.jain on 8/30/2016.
 */
public class SingleVideoDescFragment extends BaseDialogFragment implements View.OnClickListener, DialogInterface.OnDismissListener {

    @InjectView(R.id.titleTV)
    TextView titleTV;
    @InjectView(R.id.moreIV)
    ImageView moreIV;
    @InjectView(R.id.firstVideoPlayer)
    JCVideoPlayerStandard firstVideoPlayer;
    @InjectView(R.id.videoThumbIV)
    ImageView videoThumbIV;
    @InjectView(R.id.playVideoIV)
    ImageView playVideoIV;
    @InjectView(R.id.firstUserNameTV)
    TextView firstUserNameTV;
    @InjectView(R.id.videoFL)
    FrameLayout videoFL;
    @InjectView(R.id.commentsTV)
    TextView commentsTV;
    @InjectView(R.id.uploadedTimeTV)
    TextView uploadedTimeTV;
    @InjectView(R.id.userOneVoteCountTV)
    TextView userOneVoteCountTV;
    @InjectView(R.id.leftSideVotingView)
    LinearLayout leftSideVotingView;
    @InjectView(R.id.commentsRV)
    RecyclerView commentsRV;
    @InjectView(R.id.commentsET)
    EditText commentsET;
    @InjectView(R.id.commentBar)
    FrameLayout commentBar;

    private VideoDetailModel videoDetailModel;
    private CommentsAdapter commentsAdapter;

    public static SingleVideoDescFragment getInstance(String videoId) {
        SingleVideoDescFragment playSingleVideoFragment = new SingleVideoDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CUSTOMERS_VIDEO_ID, videoId);
        playSingleVideoFragment.setArguments(bundle);
        return playSingleVideoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_video_desc, null);
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

        String videoID = getArguments().getString(Constants.CUSTOMERS_VIDEO_ID);
        commentsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        CallWebService.getInstance(getActivity(), true, ApiCodes.SINGLE_VIDEO_DATA).hitJsonObjectRequestAPI(CallWebService.POST, API.GET_CUSTOMER_VIDEO_DETAIL, createJsonForGettingVideoInfo(videoID), this);
    }

    private JSONObject createJsonForGettingVideoInfo(String videoId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CUSTOMERS_VIDEO_ID, videoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {
        super.onJsonObjectSuccess(response, apiType);
        videoDetailModel = UniversalParser.getInstance().parseJsonObject(response.getJSONObject(Constants.DATA).getJSONArray(Constants.CUSTOMERS).getJSONObject(0), VideoDetailModel.class);
        if (getUserVisibleHint())
            updateUI();
    }

    private void updateUI() {

        titleTV.setText(videoDetailModel.getTitle());
        String commentsFound = getResources().getQuantityString(R.plurals.numberOfComments, videoDetailModel.getVideo_comment_count(), videoDetailModel.getVideo_comment_count());
        commentsTV.setText(commentsFound);
        userOneVoteCountTV.setText(String.valueOf(videoDetailModel.getLikes()));
        firstUserNameTV.setText(videoDetailModel.getFirst_name());

        boolean popUp = firstVideoPlayer.setUp(videoDetailModel.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");

        if (popUp)
            new ImageLoading(getContext()).LoadImage(videoDetailModel.getThumbnail(), firstVideoPlayer.thumbImageView, null);

        firstVideoPlayer.fullscreenButton.setOnClickListener(this);
        firstVideoPlayer.backButton.setOnClickListener(this);
        commentsAdapter = new CommentsAdapter(getContext(), videoDetailModel.getCommentArray(), this);
        commentsRV.setAdapter(commentsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        if (v == firstVideoPlayer.fullscreenButton) {
            firstVideoPlayer.startWindowFullscreen();
            getDialog().hide();
        }
    }
}
