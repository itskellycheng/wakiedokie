package com.wakiedokie.waikiedokie.integration.remote;

/**
 * Constants for configuring connection.
 * Created by chaovictorshin-deh on 5/2/16.
 */
public class Connection {
    public final static String DOMAIN = "http://10.0.0.25:8080";
    public final static String USER_SERVLET = DOMAIN + "/AndroidAppServlet/UserServlet";
    public final static String SET_ALARM_SERVLET = DOMAIN + "/AndroidAppServlet/SetAlarmRequestServlet";
    public final static String GET_USER_TABLE_SERVLET = DOMAIN + "/AndroidAppServlet/UserTableIOServlet";
    public final static String TRACK_WAKE_UP_SERVLET =DOMAIN + "/AndroidAppServlet/TrackWakeUpStatusServlet";
    public final static String WAKE_UP_STATUS_SERVLET = DOMAIN + "/AndroidAppServlet/WakeUpStatusServlet";
}
