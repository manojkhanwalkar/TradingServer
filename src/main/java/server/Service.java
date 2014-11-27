package server;

/**
 * Created by mahesh on 11/27/14.
 */

    public interface Service {
        public void init()  ;
        public void start()  ;
        public void stop()    ;
        public void pause()    ;
        public void resume()    ;
        public void destroy()    ;
        public void setName(String s);
        public String getName();

    }
