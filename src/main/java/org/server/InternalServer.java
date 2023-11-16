package org.server;

public class InternalServer {
    private static InternalServer instance = null;


    public static InternalServer getInstance(){
        if (instance == null ) {
            instance = new InternalServer();
        }

        return instance;
    }

    public static boolean isRunning(){
        return (instance != null);
    }

}
