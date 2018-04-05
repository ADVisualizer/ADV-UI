package ch.adv.ui.core.service;

import java.net.Socket;

/**
 * Guice interface for assisted inject
 */
public interface ADVConnectionFactory {

    /**
     * Creates a {@link ADVConnection} instance
     * @param client custom argument
     * @return ADVConnection
     */
    ADVConnection create(Socket client);
}