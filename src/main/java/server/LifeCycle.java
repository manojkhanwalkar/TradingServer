package server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by mahesh on 11/18/14.
 */
public class LifeCycle
{

    public static void main(String[] args)
    {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");

    }


}
