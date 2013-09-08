package ru.stereohorse.rotundr.server.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stereohorse.rotundr.server.core.Server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerBootstrapListener implements ServletContextListener, Runnable {
    private static final Logger log = LoggerFactory.getLogger( ServerBootstrapListener.class );

    private static final int DEFAULT_PORT = 6699;
    private Server server;
    private long startTime;

    @Override
    public void contextInitialized( ServletContextEvent servletContextEvent ) {
        int port = DEFAULT_PORT;
        Object rotundrServerPort = servletContextEvent.getServletContext().getInitParameter( "rotundrServerPort" );
        try {
            port = Integer.parseInt( rotundrServerPort.toString() );
        } catch ( Exception e ) {
            log.warn( "Unable to set rotundr server port to value [{}]", rotundrServerPort, port );
        }

        server = new Server( port );
        new Thread( this ).start();
    }

    @Override
    public void contextDestroyed( ServletContextEvent servletContextEvent ) {
        server.stop();

        if ( startTime != 0 ) {
            log.info( "Stopped. Uptime: {} ms", System.currentTimeMillis() - startTime );
        }
    }

    @Override
    public void run() {
        server.start().syncUninterruptibly();

        startTime = System.currentTimeMillis();
        log.info( "Started" );
    }
}
