package com.finalproject.giveandtake.util;


public class MyConstants
{
    public static final String IS_TAKE_REQUEST= "isTakeRequest";
    public static final String IS_FROM_NOTIFICATION= "isfromNotification";
    public static final String SELECTED_TAG= "tag";
    public static final String FIRST_RUN= "firstRun";
    public static final long FETCH_USER_DATA_TIMEOUT = 12000;
    public static final int TAGS_BULK_COUNT = 5;

    public static final String REQUEST_TYPE= "type";
    public static final String START_SESSION= "startSession";

    public static final String UID = "uid";
    public static final String SESSION_RESTORED ="sessionRestored" ;
    public static final String PHONE_OTHER_USER ="phoneRequestOtherUserId" ;

    public  enum PhonePermissionStatus  {
        PENDING,
        ACCEPT,
        REJECT
    }
}
