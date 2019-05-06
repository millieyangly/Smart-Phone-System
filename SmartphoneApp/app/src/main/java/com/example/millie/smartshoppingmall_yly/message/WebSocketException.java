package com.example.millie.smartshoppingmall_yly.message;

/**
 * Created by Millie on 2016/6/16.
 */
public class WebSocketException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable t) {
        super(message, t);
    }
}
