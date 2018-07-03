package ch.hsr.adv.ui.core.logic.exceptions;

/**
 * To be thrown for any exception to do with parsing of a session, snapshot etc.
 */
public class ADVParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public ADVParseException(String message) {
        super(message);
    }
}
