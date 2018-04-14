package com.nixmash.fileupload.dto;

import java.io.Serializable;

public class EmailMessage implements Serializable {
    private static final long serialVersionUID = 8298162513547104751L;

    private String to;
    private String from;
    private String host;
    private String subject;
    private String body;

    public EmailMessage() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
