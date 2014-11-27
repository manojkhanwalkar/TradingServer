package server;

import logger.Logger;

/**
 * Created by mahesh on 11/27/14.
 */

public class ServerShutDownHook implements Runnable {

    Server server;
    public ServerShutDownHook(Server s)
    {
        server=s;
    }

    public void run()
    {
        Logger.getLog().info("Shutdown signal received");
        server.stop();
        server.destroy();
        Logger.getLog().info("Server stopped");
    }
}
