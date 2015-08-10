package com.backend.fitpet.containers;

import com.backend.fitpet.model.Person;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by David on 7/10/2015.
 */
public class UsersOutputContainer implements Serializable {
    private int status;
    private ArrayList<Person> data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
