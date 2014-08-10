package cn.glassx.wear.common;

public final class Constants {

    private Constants() {
    }

    public static final int WATCH_ONLY_ID     = 2;
    public static final int PHONE_ONLY_ID     = 3;
    public static final int BOTH_ID           = 4;
    public static final int MSG_WATCH_ONLY_ID = 5;

    private static final String PREFIX              = "/glassx-wear";
    public static final  String BOTH_PATH           = PREFIX + "/both";
    public static final  String WATCH_ONLY_PATH     = PREFIX + "/watch-only";
    public static final  String MSG_WATCH_ONLY_PATH = PREFIX + "/msg-watch-only";
    public static final  String KEY_NOTIFICATION_ID = "notification-id";
    public static final  String KEY_TITLE           = "title";
    public static final  String KEY_CONTENT         = "content";

    public static final String ACTION_DISMISS = "cn.glassx.wear.demos.DISMISS";
}