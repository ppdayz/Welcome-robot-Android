package com.csjbot.rosclient.constant;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 */
public class NetModule {
    public static final int RESERVE = 0;
    public static final int HEARTBEAT_CMD = 2;
    public static final int MOVE_FORWARD = 3;
    public static final int MOVE_BACK = 4;
    public static final int MOVE_LEFT = 5;
    public static final int MOVE_RIGHT = 6;
    public static final int MOVE_STOP = 7;
    public static final int JERK_STOP = 8;
    public static final int GET_POSE = 9;
    
    @Deprecated
    public static final int SET_POSE = 10;

    public static final int GO_POSE = 11;
    public static final int NAVI_RESULT = 12;
    public static final int CANCEL_NAVI = 13;
    public static final int GET_BATT = 14;  //battery by added in 20161124
    //    public static final int SET_ACTION = 14;
//    public static final int SET_HOME = 15;
    public static final int UNDEFINE = 0;

}
