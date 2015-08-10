package com.backend.fitpet.containers;

import com.backend.fitpet.model.Pet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by David on 7/14/2015.
 */
public class PetsOutputContainer implements Serializable{
    private int status;
    private ArrayList<Pet> data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Pet> getData() {
        return data;
    }

    public void setData(ArrayList<Pet> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
