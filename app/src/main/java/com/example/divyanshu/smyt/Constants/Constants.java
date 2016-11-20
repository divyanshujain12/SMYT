package com.example.divyanshu.smyt.Constants;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public interface Constants {

    String STATUS_CODE = "statuscode";
    String EMAIl = "email";
    String PASSWORD = "password";
    String USER_NAME = "username";
    String USER_DATA = "user_data";
    String FIRST_NAME = "first_name";
    String LAST_NAME = "last_name";
    String PHONE_NUMBER = "phonenumber";
    String DATE_OF_BIRTH = "date_of_birth";
    String DATA = "data";
    String FROM_FOLLOWER = "from_follower";
    String MESSAGE = "message";
    String CUSTOMER_ID = "customer_id";
    String CATEGORY = "category";
    String CUSTOMERS = "customers";
    String CATEGORY_ID = "category_id";
    String NAME = "name";
    String DESC = "description";
    String COUNT = "count";
    String CUSTOMER_VIDEO_COMMENT_ID = "customers_videos_comment_id";
    String COMMENT = "comment";
    String IS_LOGGED_IN = "is_logged_in";
    String GENDER = "gender";
    String ROUND_TIME = "round_time";
    String ROUND_DATE = "round_date";
    String CUSTOMERS_VIDEO_ID = "customers_videos_id";
    String UPDATE_COMMENT_COUNT = "update_comment_count";
    String UPDATE_USER_INFO = "update_user_info";
    String UPDATE_ACCEPT_REJECT_BROADCAST = "UPDATE_ACCEPT_REJECT_BROADCAST";
    String STATUS = "status";
    String LIKES = "likes";
    String FOLLOWING_ID = "following_id";
    String FOLLOW_STATUS = "follow_status";
    String SEARCH_TEXT = "search_text";
    String USER_VIDEO = "user_video";
    String TITLE = "title";
    String GENRE = "genre";
    String SHARE_STATUS = "share_status";
    String FRIEND_ID = "friend_id";
    String TOTAL_ROUND = "total_round";
    String ROUND_ARRAY = "round_array";
    String PROFILE_IMAGE = "profileimage";
    String CHALLENGE_ID = "challenge_id";
    String THUMBNAIL = "thumbnail";
    String VIDEO_URL = "video_url";
    String ACCEPT_STATUS = "accept_status";
    String TIMELINE_MSG = "timeline_msg";
    String LAST_LOGIN = "last_login";
    String AVAILABLE = "available";
    String CATEGORY_NAME = "category_name";
    String E_DATE = "edate";
    String VIDEO_NAME = "video_name";
    int REQUEST_TIMEOUT_TIME = 30000;

    //WOWZA server ----------------------------------------------------------
    String WOWZA_APPLICATION_NAME = "smytex";
    String WOWZA_STREAM_URL = "rtsp://192.254.214.197:1935" + "/" + WOWZA_APPLICATION_NAME + "/";
    String WOWZA_USERNAME = "root";
    String WOWZA_PASSWORD = "SHAD!@#$%^";
    String WOWZA_MYSTREAM_PREFIX = "myStream_";

    //IN-APP Purchase Ids
    String DOT = ".";
    String IN_APP_BASE = "com.smytex.livestream";
    String OTHER_CATEGORIES = IN_APP_BASE + DOT + "othercategories";
    String PREMIUM_CATEGORIES = IN_APP_BASE + DOT + "premiumcategory";
    String SINGLE_BANNER_VIDEO = DOT + "singlebannervideo";
    String MONTHLY_VIDEOS = DOT + "monthlyvideos";
    //*** show video to other category banner
    String OTHER_CATEGORY_BANNER_SINGLE_VIDEOS_PACK = "com.smytex.livestream.othercategories.bannersinglevideo";
    String OTHER_CATEGORY_BANNER_THREE_VIDEOS_PACK = "com.smytex.livestream.othercategories.bannerthreevideos";
    String OTHER_CATEGORY_BANNER_FIVE_VIDEOS_PACK = "com.smytex.livestream.othercategories.bannerfivevideos";
    //***from other categories to premium category
    String PREMIUM_NEW_SINGLE_VIDEO_PACK = "com.smytex.livestream.premiumcategory.premiumnewsinglevideo";
    String PREMIUM_NEW_MONTHLY_VIDEO_PACK = "com.smytex.livestream.premiumcategory.premiumnewmonthlyvideos";
    //*** show video to premium category banner
    String PREMIUM_CATEGORY_BANNER_SINGLE_VIDEO_PACK = "com.smytex.livestream.premiumcategory.premiumbannersinglevideo";
    String PREMIUM_CATEGORY_BANNER_THREE_VIDEOS_PACK = "com.smytex.livestream.premiumcategory.premiumbannerthreevideos";
    String PREMIUM_CATEGORY_BANNER_FIVE_VIDEOS_PACK = "com.smytex.livestream.premiumcategory.premiumbannerfivevideos";
}
