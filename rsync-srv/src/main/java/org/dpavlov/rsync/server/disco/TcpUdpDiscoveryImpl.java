package org.dpavlov.rsync.server.disco;

import org.dpavlov.rsync.core.ConcurrencyUtil;
import org.dpavlov.rsync.server.DiscoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TcpUdpDiscoveryImpl implements IDiscovery {
    private ExecutorService multicastListenerThread = Executors.newSingleThreadExecutor();

    @Autowired
    private DiscoServer srv;

    @PostConstruct
    public void startDisco() {
        multicastListenerThread.submit(srv);
    }

    @PreDestroy
    public void stop() {
        ConcurrencyUtil.stopExecutor(multicastListenerThread);
    }
}
