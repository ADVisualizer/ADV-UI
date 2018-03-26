package ch.adv.ui.service;

import java.net.Socket;


public interface ADVConnectionFactory {

    ADVConnection create(Socket client);
}