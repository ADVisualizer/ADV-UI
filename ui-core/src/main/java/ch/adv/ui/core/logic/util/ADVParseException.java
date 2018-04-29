package ch.adv.ui.core.logic.util;

/**
 * To be thrown for any exception to do with parsing of a session, snapshot etc.
 */
public class ADVParseException extends ADVException {

    public ADVParseException(String message) {
        super(message);
    }
}
