package com.csjbot.welcomebot_zkhl.utils;

/**
 * Created by Administrator on 2016/10/24 0024.
 */

public class Constants {
    /* 移动动作 */
    public static final String MOVE_ACTION_UP = "{\"msg_id\":\"NAVI_ROBOT_MOVE_REQ\",\"direction\":0}";
    public static final String MOVE_ACTION_DOWM = "{\"msg_id\":\"NAVI_ROBOT_MOVE_REQ\",\"direction\":1}";
    public static final String MOVE_ACTION_LEFT = "{\"msg_id\":\"NAVI_ROBOT_MOVE_REQ\",\"direction\":2}";
    public static final String MOVE_ACTION_RIGHT = "{\"msg_id\":\"NAVI_ROBOT_MOVE_REQ\",\"direction\":3}";
    public static final String MOVE_ACTION_STOP = "{\"msg_id\":\"NAVI_ROBOT_STOP_REQ\",\"force_stop\":false}";

    /* 头部动作 */
    public static final String ACTION_HEAD_UP = "{\"msg_id\":\"NAVI_ROBOT_STOP_REQ\",\"force_stop\":false}";
    public static final String ACTION_HEAD_DOWN = "{\"msg_id\":\"NAVI_ROBOT_STOP_REQ\",\"force_stop\":false}";
    public static final String ACTION_HEAD_LEFT = "{\"msg_id\":\"NAVI_ROBOT_STOP_REQ\",\"force_stop\":false}";
    public static final String ACTION_HEAD_RIGHT = "{\"msg_id\":\"NAVI_ROBOT_STOP_REQ\",\"force_stop\":false}";

    /* 说话 */
    public static final String SPEAK_MODE = "{\"msg_id\":\"SPEECH_TTS_REQ\",\"content\":\"%s\"}";

    public static final String BODY_MOVE_MODE = "{\"msg_id\":\"ROBOT_BODY_CTRL_CMD\",\"body_part\":%d,\"action\":%d}";

    /* 请求pose */
    public static final String NAVI_GET_POS_REQ = "{\"msg_id\":\"NAVI_GET_POS_REQ\"}";

    /* 移动到某个点 */
    public static final String NAVI_ROBOT_MOVE_TO_REQ = "{\"msg_id\":\"NAVI_ROBOT_MOVE_TO_REQ\",\"pos\":{\"x\":%d,\"y\":%d,\"z\":%d,\"rotation\":%d}}";

    public static class BodyPart {
        public static final int RESET = 1;
        public static final int HEAD = 2;
        public static final int LEFT_ARM = 3;
        public static final int RIGHT_ARM = 4;
        public static final int DOUBLE_ARM = 5;
        public static final int LEFT_FOREARM = 6;
        public static final int RIGHT_FOREARM = 7;
        public static final int DOUBLE_FOREARM = 8;
        public static final int WAIST = 9;
    }


    public static class BodyAction {
        public static final int STOP = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int LEFT_THEN_RIGHT = 4;
        public static final int UP = 5;
        public static final int DOWN = 6;
        public static final int UP_AND_DOWN = 7;
        public static final int HEAD_UP_AND_DOWN_STOP = 8;
    }

    public static class WaistAction {
        public static final int UP = 2;
        public static final int DOWN = 3;
    }

    public static class ResponseType {
        public static final String NAVI_GET_POS_RSP = "NAVI_GET_POS_RSP";
    }
}
