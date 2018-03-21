package ch.adv.ui.array;

import ch.adv.ui.Parser;
import ch.adv.ui.Session;
import ch.adv.ui.Snapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Singleton
public class ArrayParser implements Parser {

    private final Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(ArrayParser.class);

    public ArrayParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Snapshot.class, new SnapshotInstanceCreator());
        gson = gsonBuilder.create();
    }

    @Override
    public Session parse(String json) {
        logger.info("Parsing json...");
        logger.debug("json: \n {}", json);
        Session session = gson.fromJson(json, Session.class);
        return session;
    }

    private static class SnapshotInstanceCreator implements InstanceCreator<Snapshot> {
        @Override
        public Snapshot createInstance(Type type) {
            return new ArraySnapshot();
        }

    }

}
