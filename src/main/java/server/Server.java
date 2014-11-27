package server;

/**
 * Created by mahesh on 11/27/14.
 */

import logger.Logger;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.management.*;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Server {
    String config;
    ApplicationContext context;
    ServiceList list;
    static Server server = new Server();
    Log log = Logger.getLog();

    public static Server getInstance() {
        return server;
    }

    public static Service getService(String name) {
        return server.list.getService(name);
    }

    public ServiceList getList() {
        return list;
    }

    public void setList(ServiceList list) {
        this.list = list;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private Server() {
    }

    public void init() {
        try {
            context = new FileSystemXmlApplicationContext(config);
            list = (ServiceList) context.getBean("ServiceList");


            for (Service s : list.getServices()) {
                s.init();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void start() {
        log.info("Starting Services");
        for (Service s : list.getServices()) {
            s.start();
        }
    }

    public void stop() {
        log.info("Stopping Services");

        for (Service s : list.getServices()) {
            s.stop();
        }

    }

    public void pause() {
        for (Service s : list.getServices()) {
            s.pause();
        }
    }

    public void resume() {
        for (Service s : list.getServices()) {
            s.resume();
        }
    }

    public void destroy() {
        List<Service> services = new ArrayList<Service>(list.getServices());

        for (int i = services.size() - 1; i > 0; i--) {
            Service service = services.get(i);
            service.destroy();
        }

        synchronized (this) {
            notify();
        }

   /*     log.info("Exiting JVM...");
        new Thread("App-exit") {
            @Override
            public void run() {
                System.exit(0);
            }
        }.start();*/
    }

    public ApplicationContext getContext() {
        return context;
    }


    public static void main(String[] args) {
        Server s = Server.getInstance();
        s.setConfig(args[0]);
        Runtime.getRuntime().addShutdownHook(new Thread(new ServerShutDownHook(s)));
        try
        {
            s.init();
            s.start();
                s.waitForExit();
        }
        catch (Exception ex)
        {
            Logger.getLog().fatal("Fatal error encountered. Shutting down.", ex);
            System.exit(-1);
        }

        System.exit(0);
    }

    private synchronized void waitForExit() {

        try {
            wait();
        } catch (InterruptedException e) {
            Logger.getLog().error(e.getMessage(), e);
        }
    }
}
