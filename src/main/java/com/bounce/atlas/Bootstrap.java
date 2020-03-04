package com.bounce.atlas;

import com.bounce.utils.*;
import io.sentry.Sentry;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;

public class Bootstrap extends HttpServlet {

    private static Logger logger = Log.getLogger(Bootstrap.class.getCanonicalName());

    public static final String SERVICE_NAME = "atlas";

    static {
        main(null);
    }

    public static void main(String[] args) {
        logger.info("------------------------");
        logger.info("Service booting : " + SERVICE_NAME);
        logger.info("------------------------");

//        PlatformBootstrap.setup("atlas");
//
//        Sentry.init(ConfigData.getConfig().get("sentry.dsn"));

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    static class ShutdownHook extends Thread {

        @Override
        public void run() {
            try {
                logger.info("------------------------");
                logger.info("Service shutting down : " + SERVICE_NAME);
                logger.info("------------------------");
            } catch (Exception e) {
                e.printStackTrace();
                BounceUtils.logError(e);
            }
        }
    }
}
