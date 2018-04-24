package ch.adv.ui.core.service;


/**
 * Encapsulates a request from the ADV Lib.
 */
class ADVRequest {

    private final ProtocolCommand command;
    private final String json;

    ADVRequest(ProtocolCommand command, String json) {
        this.command = command;
        this.json = json;
    }

    String getJson() {
        return this.json;
    }

    ProtocolCommand getCommand() {
        return command;
    }
}
