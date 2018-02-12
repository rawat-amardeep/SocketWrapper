package com.ghat.socket;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Am on 4/21/2015.
 */
public class NetworkManager {
    private static NetworkManager mInstance;
    private static NetworkInterface mNetworkInterface;
    private Socket mSocket;
    private int RECONNECTION_ATTEMPT = 10;
    private long CONNECTION_TIMEOUT = 30000;

    /*************Default callback provided by Socket.IO for all apps***********************************/
    /**
     *
     * The purpose of this method is to get the call back for any type of connection error
     */
    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onConnectionError");
            mNetworkInterface.networkCallReceive(NetworkSocketConstant.SERVER_CONNECTION_ERROR, args);
        }
    };
    /**
     * The purpose of this method to get the call back for connection getting timed out
     */
    private Emitter.Listener onConnectionTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onConnectionTimeOut");
            mNetworkInterface.networkCallReceive(NetworkSocketConstant.SERVER_CONNECTION_TIMEOUT, args);
        }
    };
    /**
     * The purpose of this method is to receive the call back when the server get connected
     */
    private Emitter.Listener onServerConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onServerConnected");
            mNetworkInterface.networkCallReceive(NetworkSocketConstant.SERVER_CONNECTED, args);
        }
    };

    /**
     * The purpose of this method is to receive the call back when the server get disconnected
     */
    private Emitter.Listener onServerDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onServerDisconnection");
            mNetworkInterface.networkCallReceive(NetworkSocketConstant.SERVER_DISCONNECTED, args);
        }
    };



    /**
     * A sample implementation of your callback method (Create such callback method for each of your socket api)
     */
    private Emitter.Listener testing = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", args[0].toString());
        }
    };

    public static NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public void registerInterface(NetworkInterface interfaces) {
        mNetworkInterface = interfaces;
    }

    /**
     * The purpose of this method to create the socket object
     */
    public void connectToSocket() {
        try {
            IO.Options opts = new IO.Options();
            opts.timeout = CONNECTION_TIMEOUT;
            opts.reconnection = true;
            opts.reconnectionAttempts = RECONNECTION_ATTEMPT;
            opts.reconnectionDelay = 1000;
            opts.forceNew = true;
            mSocket = IO.socket(NetworkSocketConstant.SOCKET_CONNECTION_URL, opts);
            makeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The purpose of the method is to return the instance of socket
     *
     * @return
     */
    public Socket getSocket() {
        return mSocket;
    }

    /**
     * The purpose of this method is to connect with the socket
     */

    public void makeConnection() {
        if (mSocket != null) {
            registerConnectionAttributes();
            mSocket.connect();
        }

    }

    /**
     * The purpose of this method is to disconnect from the socket interface
     */
    public void disconnectFromSocket() {
        unregisterConnectionAttributes();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
            mInstance = null;
            BaseApplicationActivty.networkManager = null;
        }
    }

    /**
     * The purpose of this method is to register default connection attributes
     */
    public void registerConnectionAttributes() {
        try {
            if (mSocket != null) {
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
                mSocket.on(Socket.EVENT_DISCONNECT, onServerDisconnect);
                mSocket.on(Socket.EVENT_CONNECT, onServerConnect);
                registerHandlers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The purpose of this method is to unregister default connection attributes
     */
    public void unregisterConnectionAttributes() {
        try {
            if (mSocket != null) {
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
                mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
                mSocket.off(Socket.EVENT_DISCONNECT, onServerDisconnect);
                mSocket.off(Socket.EVENT_CONNECT, onServerConnect);
                unRegisterHandlers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The purpose of this method is to unregister the connection from the socket
     * Unregister all the method in the same way as shown
     */
    private void unRegisterHandlers() {
        try {
            mSocket.off(NetworkSocketConstant.TEST_API, testing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method is to register the connection from the socket
     * register all your method in the same way as shown
     */
    private void registerHandlers() {
        try {

            mSocket.on(NetworkSocketConstant.TEST_API, testing);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * The purpose of this method is register a single method on server
     * If you want to register a specific method required in a specific class use this
     *
     * @param methodOnServer
     * @param handlerName
     */
    public void registerHandler(String methodOnServer, Emitter.Listener handlerName) {
        try {
            if (mSocket != null) {
                mSocket.on(methodOnServer, handlerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method is to unregister a single method from server
     * If you want to unregister a specific method required in a specific class use this
     * @param methodOnServer
     * @param handlerName
     */
    public void unRegisterHandler(String methodOnServer, Emitter.Listener handlerName) {
        try {
            if (mSocket != null) {
                mSocket.off(methodOnServer, handlerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method is to send the data to the server
     *
     * @param methodOnServer
     * @param request
     */
    public void sendDataToServer(String methodOnServer, JsonObject request) {

        try {
            if (mSocket != null && mSocket.connected()) {
                Log.e("JSON ", request.toString());
                mSocket.emit(methodOnServer, request);
            } else {
                mNetworkInterface.networkCallReceive(NetworkSocketConstant.SERVER_CONNECTION_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface NetworkInterface {
        void networkCallReceive(int responseType, Object... args);
    }
}

