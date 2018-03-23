package ch.adv.ui.access;

import java.io.File;

public interface Datastore {

    String read(File file);
    boolean write(File file, String json);}
