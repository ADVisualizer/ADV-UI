package ch.adv.ui.array;

import ch.adv.ui.ADVModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayModule implements ADVModule {
    private static final Logger logger = LoggerFactory.getLogger(ArrayModule.class);

    @Override
    public void print() {
        logger.info("printing...");
        System.out.println("Holla Array");
    }
}
