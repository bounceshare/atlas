package com.bounce.atlas;

import com.bounce.atlas.utils.Log;
import com.bounce.atlas.utils.Utils;
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
                Utils.logError(e);
            }
        }
    }
}
