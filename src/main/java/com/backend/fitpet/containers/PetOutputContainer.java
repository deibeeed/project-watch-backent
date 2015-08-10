package com.backend.fitpet.containers;

import com.backend.fitpet.model.Pet;

import java.io.Serializable;

/**
 * Created by David on 7/14/2015.
 */
public class PetOutputContainer implements Serializable{
    private int status;
    private Pet data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Pet getData() {
        return data;
    }

    public void setData(Pet data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
