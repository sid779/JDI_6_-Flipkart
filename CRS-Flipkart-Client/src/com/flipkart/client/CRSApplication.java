package com.flipkart.client;


import com.flipkart.exception.UserCRSException;
import org.apache.log4j.Logger;

public class CRSApplication {

    private static Logger logger = Logger.getLogger(CRSApplication.class);

    /**
     * Main driver class to run the CRS application
     * @param args
     * @throws UserCRSException
     */
    public static void main(String[] args) throws UserCRSException {
        logger.info("--------Welcome to Course Registration System--------\n ");
        UserCRSClient user = new UserCRSClient();
        user.getUserMenu();
        logger.info("--------Thankyou--------");
    }
}
