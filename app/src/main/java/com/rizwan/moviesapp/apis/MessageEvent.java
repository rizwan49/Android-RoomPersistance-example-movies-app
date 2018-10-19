package com.rizwan.moviesapp.apis;

/***
 * this class help for EventBus;
 */
public class MessageEvent {
    int type;

    public MessageEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
