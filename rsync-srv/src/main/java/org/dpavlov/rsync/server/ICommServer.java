package org.dpavlov.rsync.server;

public interface ICommServer {
    public int getLocalPort() throws InterruptedException;
}
