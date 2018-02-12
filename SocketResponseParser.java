package com.ghat.socket;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;

/**
 * Created by Amardeep on 11/22/17.
 */

public class SocketResponseParser {

    /**
     * You can define your parsers for every api  in the same way as mentioned
     * The DeleteMessageResponse is a bean class here you can define your bean class as per the responses
     * @param response
     * @param context
     * @return
     */
    public static TestMessageResponseBean parseTestMessage(String response, Context context) {
        TestMessageResponseBean responseBean = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            responseBean = objectMapper.readValue(response, TestMessageResponseBean.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBean;
    }


}
