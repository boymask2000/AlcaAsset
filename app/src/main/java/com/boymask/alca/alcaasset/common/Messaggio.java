package com.boymask.alca.alcaasset.common;

import java.util.ArrayList;
import java.util.List;

public class Messaggio {
    private String username;
    private String text;
    private MsgType msgType;

    private String msgCode;
    private List<String> parameters = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public String getTextParam() {
        String out = msgCode;
        for (String s : parameters)
            out += "|" + s;
        return out;
    }

    public void addParameter(String p) {
        parameters.add(p);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }



}
