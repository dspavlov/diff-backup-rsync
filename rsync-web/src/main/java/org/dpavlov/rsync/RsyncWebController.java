package org.dpavlov.rsync;

import org.dpavlov.rsync.server.ICommServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RsyncWebController {
    @Autowired
    ICommServer commServer;

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/localport")
    public String localPort() throws InterruptedException {
        return String.valueOf(commServer.getLocalPort());
    }
}