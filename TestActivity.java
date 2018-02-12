package com.ghat.socket;

import android.os.Bundle;

import android.util.Log;


public class TestActivity extends BaseActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        SocketEmitter.emitTesting(this);
    }


    /**
     * You will receive all your socket response along with the data here
     * @param requestCode
     * @param args
     */
    @Override
    protected void onSocketApiResult(int requestCode, Object... args) {
        Log.e("response", args[0].toString());
        switch (requestCode) {
            case NetworkSocketConstant.TEST_RESPONSE:
                TestMessageResponseBean deleteMessageResponse = SocketResponseParser.parseTestMessage(args[0].toString(), this);
                break;

        }
    }
}
