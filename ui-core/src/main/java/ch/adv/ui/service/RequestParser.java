package ch.adv.ui.service;

import ch.adv.ui.access.GsonProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RequestParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestParser
            .class);
    private final GsonProvider gsonProvider;

    @Inject
    public RequestParser(GsonProvider gsonProvider) {
        this.gsonProvider = gsonProvider;
    }


    public ADVRequest parse(String json) {
        return gsonProvider.getMinifier().fromJson(json, ADVRequest.class);
    }
}
