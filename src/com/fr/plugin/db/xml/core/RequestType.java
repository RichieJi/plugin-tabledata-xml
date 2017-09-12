package com.fr.plugin.db.xml.core;

/**
 * Created by richie on 2017/5/16.
 */
public enum RequestType {

    GET(0),POST(1);

    private int mark;

    RequestType(int mark) {
        this.mark = mark;
    }

    public int toInt() {
        return mark;
    }

    public static RequestType parse(int mark) {
        for (RequestType type : values()) {
            if (mark == type.mark) {
                return type;
            }
        }
        return GET;
    }
}
