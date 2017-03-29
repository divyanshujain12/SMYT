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
    String OTHER_USER = "other_user";
    String MESSAGE = "message";
    String CUSTOMER_ID = "customer_id";
    String CUSTOMER_ID_ONE = "customer_id1";
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
    String USER_FRAGMENT_TAB_UI = "user_fragment_tab_ui";
    String USER_FAV_FRAGMENT_TAB_UI = "user_fav_fragment_tab_ui";
    String UPDATE_USER_INFO = "update_user_info";
    String USER_ONGOING_CHALLENGE_FRAGMENT = "USER_ONGOING_CHALLENGE_FRAGMENT";
    String STATUS = "status";
    String LIVE_STATUS = "live_status";
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
    String E_DATE_1 = "edate1";
    String VIDEO_NAME = "video_name";
    String TYPE = "type";
    String VOTING_CUSTOMER_ID = "voting_customer_id";
    String IN_APP_TYPE = "in_app_type";
    String PACKAGE_BANNER_TYPE = "packages_banner_type";
    String CAT_NORMAL = "Normal";
    String IS_PRCHASED = "is_purchased";
    String CAT_PREMIUM = "Premium";
    String TRANSACTION_ID = "transaction_id";
    String CUSTOM_ID = "custom_id";
    String SUCCESS = "Sucess";
    String VALID_TILL = "valid_till";
    String PRODUCT_ID = "product_id";
    String BANNERS = "banners";
    String NEW_CHALLENGE = "new_challenge";
    String FCM_ID = "fcm_id";
    String ALL_VIDEO_TAB_UI = "all_video_tab_ui";
    String LIVE_CHALLENGES_TAB_UI = "challenge_videos_tab_ui";
    String FILTER = "filter";
    String PAGE_NO = "page_no";
    String FROM_NOTIFICATION = "from_notification";
    String VOTE_COUNT = "vote_count";
    String USER_NUMBER = "user_number";
    String FROM_BANNER = "from_banner";
    String PUSH_DATA = "push_data";
    int COMMENT_COUNT = 0;
    int VOTE_COUNT_INT = 1;
    int FAVORITE_STATUS = 102;
    int LIKE_COUNT = 103;
    String POST_VIDEO_DATA = "post_video_data";
    String FAVORITE = "favourite";
    String IS_FAVORITE = "is_favourite";
    String MOBILE_TYPE = "mobile_type";
    String FILE_NAME = "file_name";
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.valdioveliu.valdio.audioplayer.PlayNewAudio";


    int REQUEST_TIMEOUT_TIME = 30000;


    //WOWZA server ----------------------------------------------------------
    String WOWZA_APPLICATION_NAME = "smytex";
    String RTSP = "rtsp://";
    String HTTP = "http://";
    String IP_ADDRESS = "162.214.21.189";
    String PORT_NUMBER = "1935";
    //String WOWZA_STREAM_URL = RTSP + IP_ADDRESS + ":" + PORT_NUMBER + "/" + WOWZA_APPLICATION_NAME + "/";
    String WOWZA_STREAM_URL = HTTP + IP_ADDRESS + ":" + PORT_NUMBER + "/" + WOWZA_APPLICATION_NAME + "/";
    String WOWZA_USERNAME = "smytex";
    String WOWZA_PASSWORD = "chris!@#$%^";
    String WOWZA_MYSTREAM_PREFIX = "myStream_";

    //IN-APP Purchase Ids
    String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr6GFNrWL8AxANIRSRfTuQpgJwlgZZwQeqPgOldEH8bk1IeKR54cDcPaozZCAl9JSLLp8PnBiTw5jB3XCk5pbeZWzebyAfwVdGgXDvSqJ5emlY3tJ++SuSidb/FF2lmn3ZuQOmK0B48W2lCtwoslhwtC4uHw+e/LXrvA82cHHDsmgejpzj+42H0FYzO/hiMzoBbJwrIJ8xyz74XBAceHuOJxDKN9D84DRrTc6xiCFoIgTK2NyPNbWGjeq5oiRmQNrVev9I63UFcLS3yVDx/ecjqmLJeBcUu1jLGnmhqRbSQ3MK7zb0qtnw/uCvJ19YgKVcqE1+eOeTl83Q2FpmAQBbQIDAQAB";
    String MERCHANT_ID = "0061-7515-1038";
    String DOT = ".";
    String IN_APP_BASE = "com.smytex.livestream";
    String OTHER_CATEGORIES = IN_APP_BASE + DOT + "othercategories";
    String PREMIUM_CATEGORIES = IN_APP_BASE + DOT + "premiumcategory";

    //*** show video to other category banner
    String OTHER_CATEGORY_BANNER_SINGLE_VIDEOS_PACK = OTHER_CATEGORIES + DOT + "bannersinglevideo";
    String OTHER_CATEGORY_BANNER_THREE_VIDEOS_PACK = OTHER_CATEGORIES + DOT + "bannerthreevideos";
    String OTHER_CATEGORY_BANNER_FIVE_VIDEOS_PACK = OTHER_CATEGORIES + DOT + "bannerfivevideos";
    //***from other categories to premium category
    String PREMIUM_NEW_SEVEN_DAYS_VIDEO_PACK = PREMIUM_CATEGORIES + DOT + "premiumsevendaysvideos";
    String PREMIUM_NEW_THIRTY_DAYS_VIDEO_PACK = PREMIUM_CATEGORIES + DOT + "premiumthirtydaysvideos";
    String PREMIUM_NEW_NINTY_DAYS_VIDEO_PACK = PREMIUM_CATEGORIES + DOT + "premiumnintydaysvideos";
    //*** show video to premium category banner
    String PREMIUM_CATEGORY_BANNER_SINGLE_VIDEO_PACK = PREMIUM_CATEGORIES + DOT + "premiumbannersinglevideo";
    String PREMIUM_CATEGORY_BANNER_THREE_VIDEOS_PACK = PREMIUM_CATEGORIES + DOT + "premiumbannerthreevideos";
    String PREMIUM_CATEGORY_BANNER_FIVE_VIDEOS_PACK = PREMIUM_CATEGORIES + DOT + "premiumbannerfivevideos";
}
