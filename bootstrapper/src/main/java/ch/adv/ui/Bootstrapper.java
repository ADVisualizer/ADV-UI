package ch.adv.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrapper {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

    public static void main(String[] args) {
        logger.info("Bootstrapping ADV UI");

        ADVApplication.launch(args);
    }
}
