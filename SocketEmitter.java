package com.ghat.socket;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

/**
 * Created by Amardeep on 11/23/17.
 */

public class SocketEmitter {


    /**
     * The purpose of this method is hit the service to change the catchment area of the user
     * You can define your emitter for all service in the specific way as shown below.
     * You can pass bean or hashmap or any collection to the method to set the data in json to emit.
     */
    public static void emitTesting(Context context) {
        try {
            JSONObject userData = new JSONObject();
            userData.put("_id", "test data");
            userData.put("usertype", "test data");
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(userData.toString());
            BaseApplicationActivty.networkManager.sendDataToServer(NetworkSocketConstant.TEST_API, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
