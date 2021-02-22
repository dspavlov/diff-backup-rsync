package org.dpavlov.rsync;

import org.dpavlov.rsync.server.ICommServer;
import org.dpavlov.rsync.server.NetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import sun.nio.ch.Net;

import static org.dpavlov.rsync.server.NetConfig.BASE_WEB_PORT;

@Component
public class ServerPortCustomizer
        implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Autowired
    ICommServer commServer;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        int correction = 0;
        try {
            correction = commServer.getLocalPort() - NetConfig.BASE_PORT;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            correction = 0;
        }
        factory.setPort(BASE_WEB_PORT + correction);
    }

}
