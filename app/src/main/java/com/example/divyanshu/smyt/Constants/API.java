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
    String RESET_PASSWORD = BASE + "changepassword";
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
}
