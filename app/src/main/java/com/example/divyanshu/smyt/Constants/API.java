package com.example.divyanshu.smyt.Constants;

/**
 * Created by divyanshu.jain on 9/15/2016.
 */
public interface API {

    //  String BASE = "http://www.whatsupguys.in/demo/smyt/api/";
    String BASE = "http://whatsupguys.in/demo/smyt/api/";
    String REGISTRATION = BASE + "registration";
    String LOGIN = BASE + "login";
    String FORGOT_PASSWORD = BASE + "forgetpassword";
    String GET_CATEGORIES = BASE + "getcategory";
    String UPDATE_USER = BASE + "changepassword";
    String GET_CATEGORY_USER = BASE + "getcategoryUsers";
    String JOIN_CATEGORY = BASE + "join_category";
    String GET_CUSTOMER_VIDEO = BASE + "getCustomerVideos";
    String DELETE_CUSTOMER_VIDEO = BASE + "deleteCustomerVideos";
    String GET_CUSTOMER_VIDEO_DETAIL = BASE + "getCustomerVideoDetail";
    String ADD_COMMENT = BASE + "addComment";
    String DELETE_COMMENT = BASE + "deleteCustomerComment";
    String LIKE_UNLIKE_VIDEO = BASE + "actionLikesDislike"; 	/* 1 for Like & 0 for Remove Like */
    String ADD_REMOVE_FOLLOWING = BASE + "addFollowing";
    String GET_FOLLOWING = BASE + "getUserFollowing";
    String GET_FOLLOWERS = BASE + "getUserFollowers";
    String USER_SEARCH = BASE + "autosearchUser";
    String POST_CHALLENGE = BASE + "postChallenge";
    String GET_ONGOING_CHALLENGES = BASE + "getCustomerChallengeVideosOngoing";
    String GET_COMPLETED_CHALLENGES = BASE + "getCustomerChallengeVideosComplete";
    String POST_VIDEO = BASE + "postVideo";
    String ACCEPT_REJECT_CHALLENGE = BASE + "challengeAction";
    String GET_CUSTOMER_DETAIL = BASE + "getCustomerDetails";
    String CHALLENGE_DESCRIPTION = BASE + "getChallengeDetails";
    String VOTE_UP = BASE + "actionVote";
    String USER_ACTIVE_STATUS = BASE + "updateCustomerLastActive";
    String ALL_VIDEOS = BASE + "getCategoriesAllVideos";
    String GET_SINGLE_ROUND_VIDEO_DETAIL = BASE + "getSingleRoundVideoDetails";
    String POST_VIDEO_PREVIOUS = BASE + "postVideoPrevious";
    String GET_OTHER_CUSTOMER_DETAIL = BASE + "getCustomerDetailsAndFollow";
    String HOME_LIVE_VIDEOS = BASE + "getCategoriesLiveVideos";
    String PURCHASE_BANNER = BASE + "purchase_banner";
    String CHECK_BANNER_SUBSCRIPTION = BASE + "checkBannerSubscription";
    String ADD_BANNER = BASE + "customerAddBannerVideo";
    String GET_CATEGORY_BANNER = BASE + "getCategoriesBanners";
    String CHECK_CATEGORY_SUBSCRIPTION_FOR_NEW_VIDEO = BASE + "checkCategorySubscriptionForNewVideo";
    String ADD_VIDEO_OTHER_TO_PREMIUM_CATEGORY = BASE + "customerAddPremiumCategoryRecorderVideo";
    String PURCHASE_CATEGORY = BASE + "purchase_category";
}
