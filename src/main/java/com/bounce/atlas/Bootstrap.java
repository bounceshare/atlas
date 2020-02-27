package com.bounce.atlas;

import com.bounce.utils.Log;
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
                logger.info("Server shutting down");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
