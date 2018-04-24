package ch.adv.ui.core.service;

import ch.adv.ui.core.logic.GsonProvider;

/**
 * Encapsulates a response to the ADV Lib.
 */
class ADVResponse {

    private final ProtocolCommand command;
    private final String exceptionMessage;

    private final transient GsonProvider gsonProvider = new GsonProvider();

    ADVResponse(ProtocolCommand command) {
        this(command, null);
    }

    ADVResponse(ProtocolCommand command, String exceptionMessage) {
        this.command = command;
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * @return the serialized string representation of this class
     */
    String toJson() {
        return gsonProvider.getMinifier().toJson(this);
    }
}
