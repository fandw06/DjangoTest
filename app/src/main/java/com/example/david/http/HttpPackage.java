package com.example.david.http;

/**
 * This is a class holding http request data, including url, JSON data, and command.
 *
 * Created by David on 2015/3/28.
 */
import org.json.JSONObject;

public class HttpPackage {
    public String url;
    public JSONObject data;
    public byte command;

    public static final byte HTTP_POST = 0x01;
    public static final byte HTTP_GET = 0x02;

    public HttpPackage(String s, JSONObject d, byte c){
        url = s;
        data = d;
        command = c;
    }
}
