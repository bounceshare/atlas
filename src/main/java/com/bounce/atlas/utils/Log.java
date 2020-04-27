package com.bounce.atlas.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {

    public static Logger getLogger(String name) {
        return getLogger(name, Level.INFO);
    }

    public static Logger getLogger(String name, Level level) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(level);
        return logger;
    }

}
