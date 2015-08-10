package com.backend.fitpet.config;

import com.backend.fitpet.Constants;
import com.backend.fitpet.api.API;
import com.backend.fitpet.api.GsonMessageBodyHandler;
import com.backend.fitpet.containers.PetOutputContainer;
import com.backend.fitpet.containers.PetsOutputContainer;
import com.backend.fitpet.containers.UserOutputContainer;
import com.backend.fitpet.containers.UsersOutputContainer;
import com.backend.fitpet.model.Person;
import com.backend.fitpet.model.Pet;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by David on 7/20/2015.
 */
public class Resources extends Application {
    @Override
    public Set<Class<?>> getClasses() {
//        return super.getClasses();
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(API.class);
        s.add(GsonMessageBodyHandler.class);
//        s.add(Pet.class);
//        s.add(Person.class);
//        s.add(PetOutputContainer.class);
//        s.add(PetsOutputContainer.class);
//        s.add(UserOutputContainer.class);
//        s.add(UsersOutputContainer.class);
//        s.add(Constants.class);

        return s;
    }
}
