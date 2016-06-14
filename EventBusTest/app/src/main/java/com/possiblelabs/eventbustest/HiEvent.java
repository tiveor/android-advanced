package com.possiblelabs.eventbustest;

/**
 * Created by Alvaro Orellana on 6/14/16.
 */

public class HiEvent {

    private final String message;

    public HiEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HiEvent{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getMessage() {
        return message;
    }
}
