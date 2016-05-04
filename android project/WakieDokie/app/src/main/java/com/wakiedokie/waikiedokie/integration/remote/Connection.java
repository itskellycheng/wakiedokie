package com.wakiedokie.waikiedokie.integration.remote;

/**
 * Constants for configuring connection.
 * Created by chaovictorshin-deh on 5/2/16.
 */
public class Connection {

    public final static String DOMAIN = "http://128.237.218.172:8080";

    public final static String USER_SERVLET = DOMAIN + "/AndroidAppServlet/UserServlet";
    public final static String SET_ALARM_SERVLET = DOMAIN + "/AndroidAppServlet/SetAlarmRequestServlet";
    public final static String GET_USER_TABLE_SERVLET = DOMAIN + "/AndroidAppServlet/UserTableIOServlet";
    public final static String TRACK_WAKE_UP_SERVLET =DOMAIN + "/AndroidAppServlet/TrackWakeUpStatusServlet";
    public final static String WAKE_UP_STATUS_SERVLET = DOMAIN + "/AndroidAppServlet/WakeUpStatusServlet";
    public final static String EDIT_ALARM_TYPE_SERVLET = DOMAIN + "/AndroidAppServlet/EditAlarmTypeServlet";
    public final static String VIDEO_UPLOAD_SERVLET = DOMAIN + "/AndroidAppServlet/VideoUploadServlet";
    public final static String VIDEO_DOWNLOAD_SERVLET = DOMAIN + "/AndroidAppServlet/video/";
    public final static String GET_ALARM_TYPE_SERVLET = DOMAIN + "/AndroidAppServlet/GetAlarmTypeServlet";

}
