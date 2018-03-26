package ch.adv.ui.service;

import ch.adv.ui.access.GsonProvider;

public class ADVResponse {

    private final ProtocolCommand command;
    private final String exceptionMessage;

    private transient final GsonProvider gsonProvider = new GsonProvider();

    public ADVResponse(ProtocolCommand command) {
        this(command, null);
    }

    public ADVResponse(ProtocolCommand command, String exceptionMessage) {
        this.command = command;
        this.exceptionMessage = exceptionMessage;
    }

    public String toJson() {
        return gsonProvider.getMinifier().toJson
                (this);
    }
}
