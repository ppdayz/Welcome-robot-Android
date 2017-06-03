package com.csjbot.rosclient.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-21:08.
 * Email: puyz@csjbot.com
 * <p>
 * 简单封装的日志库，用户简单Debug
 */
public class CSJLogger {
    private static final Logger logger = Logger.getLogger("RosClient");
    private static boolean ISDEBUG = true;

    public static void info(String info) {
        if (ISDEBUG) {
            logger.log(Level.INFO, info);
        }
    }

    public static void warn(String warn) {
        if (ISDEBUG) {
            logger.log(Level.WARNING, warn);
        }
    }

    public static void error(String error) {
        if (ISDEBUG) {
            logger.log(Level.SEVERE, error);
        }
    }
}
