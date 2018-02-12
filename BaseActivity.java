package com.ghat.socket;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by Amardeep Rawat on 21/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * The purpose of this method is to receive the callback from the server
     */
    private NetworkManager.NetworkInterface networkInterface = new NetworkManager.NetworkInterface() {
        @Override
        public void networkCallReceive(int responseType, Object... args) {
            switch (responseType) {
                case NetworkSocketConstant.SERVER_CONNECTION_TIMEOUT:
                    //these are general responses hence need not to be forwarded
                    //you can simply show a toast with proper message here
                    //if you still want to forward it to the activity onSocketApiResult method as shown below
                    break;
                case NetworkSocketConstant.SERVER_CONNECTION_ERROR:

                    break;
                case NetworkSocketConstant.SERVER_CONNECTED:
                    break;
                case NetworkSocketConstant.SERVER_DISCONNECTED:
                                       break;
                //Handle your All other api response in the same way as
                case NetworkSocketConstant.TEST_RESPONSE:
                    //forward your resonse to the activity in this way
                    onSocketApiResult(NetworkSocketConstant.TEST_RESPONSE, args);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socketInit();

    }

    // initialization socket
    //Hold your network manager instance in Base application activity class only if you want your socket instanse to be available through out the app
    private void socketInit() {
        if (BaseApplicationActivty.networkManager == null) {
            BaseApplicationActivty.networkManager = NetworkManager.getInstance(getApplicationContext());
            BaseApplicationActivty.networkManager.registerInterface(networkInterface);
            BaseApplicationActivty.networkManager.connectToSocket();
        } else {
            BaseApplicationActivty.networkManager.registerInterface(networkInterface);
        }
    }



    /**
     * This Method must call all ui fragment/activity using Rx subs.
     */
    protected abstract void onSocketApiResult(int requestCode, Object... args);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //use this only if you want your socket instance limited to only specific activity(Other wise remove this code)
        if (BaseApplicationActivty.networkManager != null) {
            BaseApplicationActivty.networkManager.disconnectFromSocket();
            BaseApplicationActivty.networkManager=null;
        }
    }
}
