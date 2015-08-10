package com.backend.fitpet.containers;

import com.backend.fitpet.model.Person;

import java.io.Serializable;

/**
 * Created by David on 7/10/2015.
 */
public class UserOutputContainer implements Serializable{
    private int status;
    private Person data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
