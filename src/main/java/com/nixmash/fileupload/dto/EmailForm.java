package com.nixmash.fileupload.dto;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailForm implements Serializable {

    private static final long serialVersionUID = -957066692737314097L;

    @FormParam("fullName")
    private String fullName;

    @FormParam("email")
    private String email;

    @FormParam("message")
    private String message;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EmailForm() {
    }

    public EmailForm(String fullName, String email, String message) {
        this.fullName = fullName;
        this.email = email;
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailForm{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
