package ch.adv.ui.service;

import com.google.gson.annotations.SerializedName;

public enum ProtocolCommand {

    @SerializedName("TRANSMIT")
    TRANSMIT("TRANSMIT"),

    @SerializedName("ACK")
    ACKNOWLEDGE("ACK"),

    @SerializedName("EXCEPTION")
    EXCEPTION("EXCEPTION"),

    @SerializedName("END")
    END("END");

    private final String command;

    ProtocolCommand(String command) {
        this.command = command;
    }
}