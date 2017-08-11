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

    /* 换表情 */
    public static final String CHANGE_HAPPY = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5000\",\"once\":1\",\"time\":0}";
    public static final String CHANGE_SADNESS = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5001\",\"once\":1\",\"time\":0}";
    public static final String CHANGE_SURPRISED = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5002\",\"once\":1\",\"time\":0}";
    public static final String CHANGE_SMILE = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5003\",\"once\":1\",\"time\":0}";
    public static final String CHANGE_NORMAL = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5004\",\"once\":1\",\"time\":0}";
    public static final String CHANGE_ANGER = "{\"msg_id\":\"SET_ROBOT_EXPRESSION_REQ\",\"expression\":5005\",\"once\":1\",\"time\":0}";

    /* 说话 */
    public static final String SPEAK_MODE = "{\"msg_id\":\"SPEECH_TTS_REQ\",\"content\":\"%s\"}";

    public static final String BODY_MOVE_MODE = "{\"msg_id\":\"ROBOT_BODY_CTRL_CMD\",\"body_part\":%d,\"action\":%d}";

    /* 请求pose */
    public static final String NAVI_GET_POS_REQ = "{\"msg_id\":\"NAVI_GET_POS_REQ\"}";

    /* 移动到某个点 */
    public static final String NAVI_ROBOT_MOVE_TO_REQ = "{\"msg_id\":\"NAVI_ROBOT_MOVE_TO_REQ\",\"pos\":{\"x\":%d,\"y\":%d,\"z\":%d,\"rotation\":%d}}";

    //打印和裁剪
    public static final String PRINT_HARD_OPEN= "{\"msg_id\":\"PRINTER_OPEN_CMD\"}";
    public static final String PRINT_TEXT_CMD= "{\"msg_id\":\"PRINTER_PRINT_TEXT_CMD\",\"text\":\"%s\"}";
    public static final String CUT_CMD= "{\"msg_id\":\"PRINTER_PAPER_CUT_CMD\"}";

    //拍照
    public static final String PHOTO_REQ = "{\"msg_id\":\"FACE_SNAPSHOT_REQ\"}";
    public static final String FACE_REG_START_REQ = "{\"msg_id\":\"FACE_REG_START_REQ\"}";
    public static final String TEST = "{\"msg_id\":\"SPEECH_SERVICE_START_REQ\"}";


    //语音采集
    public static final String OPEN_ONCE_AUDIO_START_REQ = "{\"msg_id\":\"SPEECH_ISR_ONCE_START_REQ\"}";
    public static final String OPEN_ONCE_AUDIO_STOP_REQ = "{\"msg_id\":\"SPEECH_ISR_ONCE_STOP_REQ\"}";


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
