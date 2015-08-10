package com.backend.fitpet.api;

import com.backend.fitpet.containers.PetOutputContainer;
import com.backend.fitpet.containers.PetsOutputContainer;
import com.backend.fitpet.containers.UserOutputContainer;
import com.backend.fitpet.containers.UsersOutputContainer;
import com.backend.fitpet.model.Person;
import com.backend.fitpet.model.Pet;
import com.backend.fitpet.utils.BCrypt;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.*;
import com.google.gson.JsonObject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

@Api(
        name = "fitpetapi",
        version = "v1"/*,
        namespace = @ApiNamespace(
                ownerDomain = "fitpet.backend.com",
                ownerName = "fitpet.backend.com",
                packagePath = ""
        )*/
)
@Path("fitpet")
public class API {

//    @ApiMethod(
//            name = "getUser",
//            httpMethod = ApiMethod.HttpMethod.GET
//    )
//    public UserOutputContainer getUser(@Named("email") String email){
//        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
//
//        Query.Filter emailFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
//        Query q = new Query("Person").setFilter(emailFilter);
//        PreparedQuery pq = dataStore.prepare(q);
//
//        Entity ePerson = pq.asSingleEntity();
//
//        Person person = null;
//        String message = null;
//        int status = -1;
//
//        if(ePerson != null){
//            person = new Person();
//
//            person.setName((String) ePerson.getProperty("name"));
//            person.setEmail((String) ePerson.getProperty("email"));
//            person.setBirthDate((Date) ePerson.getProperty("birthDate"));
//            person.setGender((Long) ePerson.getProperty("gender"));
//            person.setCreateDate((Date) ePerson.getProperty("createDate"));
//
//            message = "Successfully retrieved user";
//            status = 200;
//        }else{
//            message = "Cannot find user";
//            status = 500;
//        }
//
//        UserOutputContainer container = new UserOutputContainer();
//        container.setMessage(message);
//        container.setData(person);
//        container.setStatus(status);
//
//        return container;
//    }

    @GET
    @Path("/getUser")
    public UserOutputContainer getUser(@QueryParam("user_id") String userId){
//        userId = userId.replace("+", "/");
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter emailFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);
        Query q = new Query("Person").setFilter(emailFilter);
        PreparedQuery pq = dataStore.prepare(q);
        Entity ePerson = pq.asSingleEntity();

        Person person = null;
        String message = null;
        int status = -1;

        if(ePerson != null){
            person = new Person();

            person.setName((String) ePerson.getProperty("name"));
            person.setEmail((String) ePerson.getProperty("email"));
            person.setBirthDate((Date) ePerson.getProperty("birthDate"));
            person.setGender((Long) ePerson.getProperty("gender"));
            person.setCreateDate((Date) ePerson.getProperty("createDate"));
            person.setId((String) ePerson.getProperty("id"));
            person.setCoins(Double.parseDouble(String.valueOf(ePerson.getProperty("coins") != null ? String.valueOf(ePerson.getProperty("coins")) : "0")));

            message = "Successfully retrieved user";
            status = 200;
        }else{
            message = "Cannot find user";
            status = 500;
        }

        UserOutputContainer container = new UserOutputContainer();
        container.setMessage(message);
        container.setData(person);
        container.setStatus(status);

        return container;
    }

//    @ApiMethod(
//            name = "getUsers",
//            httpMethod = ApiMethod.HttpMethod.GET
//    )
//    public UsersOutputContainer getUsers(){
//        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
//        String message = null;
//        int status = -1;
//
//        Query q = new Query("Person");
//        PreparedQuery pq = dataStore.prepare(q);
//
//        ArrayList<Person> listPerson = new ArrayList<>();
//
//        for (Entity ePerson : pq.asIterable()){
//            if(ePerson != null){
//                Person person = new Person();
//
//                person.setName((String) ePerson.getProperty("name"));
//                person.setEmail((String) ePerson.getProperty("email"));
//                person.setBirthDate((Date) ePerson.getProperty("birthDate"));
//                person.setGender((Long) ePerson.getProperty("gender"));
//                person.setCreateDate((Date) ePerson.getProperty("createDate"));
//                person.setId(String.valueOf(ePerson.getKey().getId()));
//
//                listPerson.add(person);
//            }
//        }
//
//        if(listPerson.size() > 0){
//            message = "Successfully retrieved users";
//            status = 200;
//        }else{
//            message = "No users found";
//            status = 500;
//        }
//
//        UsersOutputContainer container = new UsersOutputContainer();
//        container.setMessage(message);
//        container.setStatus(status);
//        container.setData(listPerson);
//
//        return container;
//    }

    @GET
    @Path("/getUsers")
    public UsersOutputContainer getUsers(){
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        String message = null;
        int status = -1;

        Query q = new Query("Person");
        PreparedQuery pq = dataStore.prepare(q);

        ArrayList<Person> listPerson = new ArrayList<>();

        for (Entity ePerson : pq.asIterable()){
            if(ePerson != null){
                Person person = new Person();

                person.setName((String) ePerson.getProperty("name"));
                person.setEmail((String) ePerson.getProperty("email"));
                person.setBirthDate((Date) ePerson.getProperty("birthDate"));
                person.setGender((ePerson.getProperty("gender") != null ? (Long) ePerson.getProperty("gender") : -1));
                person.setCreateDate((Date) ePerson.getProperty("createDate"));
                person.setId((String) ePerson.getProperty("id"));

                listPerson.add(person);
            }
        }

        if(listPerson.size() > 0){
            message = "Successfully retrieved users";
            status = 200;
        }else{
            message = "No users found";
            status = 500;
        }

        UsersOutputContainer container = new UsersOutputContainer();
        container.setMessage(message);
        container.setStatus(status);
        container.setData(listPerson);

        return container;
    }

//    @ApiMethod(
//            name = "addUser",
//            httpMethod = ApiMethod.HttpMethod.POST
//    )
//    public UserOutputContainer addUser(@Named("name") String name, @Named("email") String email, @Named("birth_date") String birthDate, @Named("gender") int gender){
//        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
//        String message = null;
//        int status = -1;
//
//        Entity ePerson = new Entity("Person");
//        Person person = null;
//
//        try {
//            person = new Person();
//            person.setName(name);
//            person.setEmail(email);
//            person.setGender(gender);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//
//            person.setBirthDate(dateFormat.parse(birthDate));
//            person.setCreateDate(new Date());
//
//            ePerson.setProperty("name", person.getName());
//            ePerson.setProperty("email", person.getEmail());
//            ePerson.setProperty("birthDate", person.getBirthDate());
//            ePerson.setProperty("gender", person.getGender());
//
//            Key keyPerson = dataStore.put(ePerson);
//
//            if(keyPerson != null){
//                message = "Successfully added user";
//                status = 200;
//            }else{
//                message = "Failed to create user";
//                status = 500;
//                person = null;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//
//            message = "Birthdate cannot be parsed";
//            status = 500;
//            person = null;
//
//        } catch (NullPointerException e){
//            e.printStackTrace();
//
//            message = "A value is null";
//            status = 500;
//            person = null;
//        }
//
//        UserOutputContainer container = new UserOutputContainer();
//        container.setMessage(message);
//        container.setStatus(status);
//        container.setData(person);
//
//        return container;
//    }

    @POST
    @Path("/addUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserOutputContainer addUser(HashMap<String, String> postData){
        String name = postData.get("name");
        String email = postData.get("email");
        String birthDate = postData.get("birthdate");
        long gender = Long.parseLong(postData.get("gender"));

        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter emailFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query query = new Query("Person").setFilter(emailFilter);
        PreparedQuery pq = dataStore.prepare(query);

        String message = null;
        int status = -1;

        Entity ePerson = new Entity("Person");
        Person person = null;

        if(email != null){

            if(pq.asSingleEntity() == null){

                try {
                    person = new Person();
                    person.setName(name);
                    person.setEmail(email);
                    person.setGender(gender);
                    person.setId(BCrypt.hashpw(person.getName() + person.getEmail(), BCrypt.gensalt()));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                    person.setBirthDate(dateFormat.parse(birthDate));
                    person.setCreateDate(new Date());

                    ePerson.setProperty("name", person.getName());
                    ePerson.setProperty("email", person.getEmail());
                    ePerson.setProperty("birthDate", person.getBirthDate());
                    ePerson.setProperty("gender", person.getGender());
                    ePerson.setProperty("id", person.getId());

                    Key keyPerson = dataStore.put(ePerson);

                    if(keyPerson != null){
                        message = "Successfully added user";
                        status = 200;
                    }else{
                        message = "Failed to create user";
                        status = 500;
                        person = null;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();

                    message = "Birthdate cannot be parsed";
                    status = 500;
                    person = null;

                } catch (NullPointerException e){
                    e.printStackTrace();

                    message = "A value is null";
                    status = 500;
                    person = null;
                }
            }else{
                message = "Email is already taken";
                status = 500;
            }
        }else{
            message = "Please provide Email";
            status = 500;
        }

        UserOutputContainer container = new UserOutputContainer();
        container.setMessage(message);
        container.setStatus(status);
        container.setData(person);

        return container;
    }

//    @ApiMethod(
//            name = "updateUser",
//            httpMethod = ApiMethod.HttpMethod.POST
//    )
//    public UserOutputContainer updateUser(@Named("email") String email, @Named("name") String name, @Named("birth_date") String birthDate, @Named("gender") int gender){
//        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
//        String message = null;
//        int status = -1;
//
//        Query.Filter emailFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
//
//        Query q = new Query("Person").setFilter(emailFilter);
//        PreparedQuery pq = dataStore.prepare(q);
//
//        Entity ePerson = pq.asSingleEntity();
//        Person person = null;
//
//        if(ePerson != null){
//            try {
//                person = new Person();
//
//                person.setName(name);
//                person.setGender(gender);
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                person.setBirthDate(dateFormat.parse(birthDate));
//
//                //update values
//                ePerson.setProperty("name", person.getName());
//                ePerson.setProperty("birthDate", person.getBirthDate());
//                ePerson.setProperty("gender", person.getGender());
//
//                Key keyPerson = dataStore.put(ePerson);
//
//                if(keyPerson != null){
//                    message = "Successfully updated user";
//                    status = 200;
//                }else{
//                    message = "Failed to update user";
//                    status = 500;
//                    person = null;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//
//                message = "Birthdate cannot be parsed";
//                status = 500;
//                person = null;
//            }
//        }else{
//            message = "Failed to update user. Email not found";
//            status = 500;
//        }
//
//        UserOutputContainer container = new UserOutputContainer();
//        container.setData(person);
//        container.setMessage(message);
//        container.setStatus(status);
//
//        return container;
//    }

    @POST
    @Path("/updateUser/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserOutputContainer updateUser(@QueryParam("user_id") String userId, HashMap<String, String> postData){
        String email = postData.get("email");
        String name = postData.get("name");
        String birthDate = postData.get("birthdate");
        int gender = Integer.parseInt(postData.get("gender"));

        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        String message = null;
        int status = -1;

        Query.Filter emailFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);

        Query q = new Query("Person").setFilter(emailFilter);
        PreparedQuery pq = dataStore.prepare(q);

        Entity ePerson = pq.asSingleEntity();
        Person person = null;

        if(ePerson != null){
            try {
                person = new Person();

                person.setName(name);
                person.setGender(gender);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                person.setBirthDate(dateFormat.parse(birthDate));

                //update values
                ePerson.setProperty("name", person.getName());
                ePerson.setProperty("birthDate", person.getBirthDate());
                ePerson.setProperty("gender", person.getGender());

                Key keyPerson = dataStore.put(ePerson);

                if(keyPerson != null){
                    message = "Successfully updated user";
                    status = 200;
                }else{
                    message = "Failed to update user";
                    status = 500;
                    person = null;
                }
            } catch (ParseException e) {
                e.printStackTrace();

                message = "Birthdate cannot be parsed";
                status = 500;
                person = null;
            }
        }else{
            message = "Failed to update user. Email not found";
            status = 500;
        }

        UserOutputContainer container = new UserOutputContainer();
        container.setData(person);
        container.setMessage(message);
        container.setStatus(status);

        return container;
    }

//    @ApiMethod(
//            name = "deleteUser",
//            httpMethod = ApiMethod.HttpMethod.DELETE
//    )
//    public UserOutputContainer deleteUser(@Named("email") String email){
//        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
//        String message = null;
//        int status = -1;
//
//        Query.Filter emailFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
//        Query q = new Query("Person").setFilter(emailFilter);
//        PreparedQuery pq = dataStore.prepare(q);
//
//        Entity ePerson = pq.asSingleEntity();
//
//        if(ePerson != null){
//            dataStore.delete(ePerson.getKey());
//
//            Entity temp = pq.asSingleEntity();
//
//            if (temp == null) {
//                message = "Successfully deleted user";
//                status = 200;
//            }else{
//                message = "Failed to delete user";
//                status = 500;
//            }
//        }else{
//            message = "Failed to retrieve user. Email not found";
//            status = 500;
//        }
//
//        UserOutputContainer container = new UserOutputContainer();
//        container.setData(null);
//        container.setMessage(message);
//        container.setStatus(status);
//
//        return container;
//    }

    @DELETE
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    public UserOutputContainer deleteUser(@QueryParam("user_id") String userId){
//        userId = userId.replace("+", "/");
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        String message = null;
        int status = -1;

        Query.Filter emailFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);
        Query q = new Query("Person").setFilter(emailFilter);
        PreparedQuery pq = dataStore.prepare(q);

        Entity ePerson = pq.asSingleEntity();

        if(ePerson != null){
            dataStore.delete(ePerson.getKey());

            Entity temp = pq.asSingleEntity();

            if (temp == null) {
                message = "Successfully deleted user";
                status = 200;
            }else{
                message = "Failed to delete user";
                status = 500;
            }
        }else{
            message = "Failed to retrieve user. Email not found";
            status = 500;
        }

        UserOutputContainer container = new UserOutputContainer();
        container.setData(null);
        container.setMessage(message);
        container.setStatus(status);

        return container;
    }

    @POST
    @Path("/uploadPetResource/{path_name}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public PetOutputContainer uploadPetResource(@FormDataParam("file")InputStream uploadedInputStream, @FormDataParam("file")FormDataContentDisposition fileDetail, @PathParam("path_name") String path_name/*LinkedHashMap<String, String> data, String fileName, String content*/){
        String message = null;
        int status = -1;

        //test multipart form
//        if(uploadedInputStream != null){
//            String filename = path_name.replace(" ", "_");
//            message = "filename: " + filename + " type: " + fileDetail.getType();
//            status = 200;
//        }else{
//            message = "unsuccessful upload";
//            status = 500;
//        }

        //save file to Google cloud storage
        AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
        String filename = path_name.replace(" ", "_");

        try {
            GcsService service = GcsServiceFactory.createGcsService();
            GcsFilename gcsFilename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), filename);
            GcsFileOptions options = new GcsFileOptions.Builder().mimeType("image/*")/*.acl("OWNER")*/.build();
            GcsOutputChannel outputChannel = service.createOrReplace(gcsFilename, options);


            ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
//            outputStream.writeObject(uploadedInputStream);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = uploadedInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
            outputStream.close();

            message = "Successfully uploaded file " + filename;
            status = 200;
        } catch (IOException e) {
            e.printStackTrace();

            message = "Upload failed. Something went wrong while uploading the file " + filename;
            status = 500;
        }

        PetOutputContainer container = new PetOutputContainer();
        container.setData(null);
        container.setStatus(status);
        container.setMessage(message);

        return container;
    }

    @GET
    @Path("/getImageResources/{pet_name}")
    public List getImageResources(@PathParam("pet_name") String petName){
        AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
        GcsService gcsService = GcsServiceFactory.createGcsService();

        ListOptions listOptions = new ListOptions.Builder().setPrefix(petName).setRecursive(true).build();
        List<String> list = new ArrayList<>();

        try {
            ListResult listImages = gcsService.list(appIdentityService.getDefaultGcsBucketName(), listOptions);

//            return listImages;
            do{
                ListItem item = listImages.next();
                list.add(item.getName());

//                GcsFilename gcsFilename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), item.getName());
//                ImagesService imgService = ImagesServiceFactory.getImagesService();
//                Image img = ImagesServiceFactory.makeImageFromFilename(item.getName());
//                ServingUrlOptions options = ServingUrlOptions.Builder.withGoogleStorageFileName(String.format("/gs/%s/%s", gcsFilename.getBucketName(), gcsFilename.getObjectName()));
//
//                list.add(imgService.getServingUrl(options));
//                GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
            }while (listImages.hasNext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @DELETE
    @Path("/deleteImageResources/{pet_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public PetOutputContainer deleteImageResources(@PathParam("pet_name") String petName){
        AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
        GcsService gcsService = GcsServiceFactory.createGcsService();

        ListOptions listOptions = new ListOptions.Builder().setPrefix(petName).setRecursive(true).build();
        List<Boolean> list = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        try {
            ListResult listImages = gcsService.list(appIdentityService.getDefaultGcsBucketName(), listOptions);

//            return listImages;
            do{
                ListItem item = listImages.next();
                tempList.add(item.getName());

                GcsFilename gcsFilename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), item.getName());
                boolean flag = gcsService.delete(gcsFilename);

                if(flag){
                    list.add(flag);
                }
//                ImagesService imgService = ImagesServiceFactory.getImagesService();
//                Image img = ImagesServiceFactory.makeImageFromFilename(item.getName());
//                ServingUrlOptions options = ServingUrlOptions.Builder.withGoogleStorageFileName(String.format("/gs/%s/%s", gcsFilename.getBucketName(), gcsFilename.getObjectName()));
//
//                list.add(imgService.getServingUrl(options));
//                GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
            }while (listImages.hasNext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PetOutputContainer container = new PetOutputContainer();
        String message = null;
        int status = -1;
        if(tempList.size() == list.size()){
            status = 200;
            message = "Successfully deleted all pet images";
        }else if(tempList.size() > list.size()){
            status = 500;
            message = "Only some pet images are deleted";
        }else{
            status = 500;
            message = "Unable to delete pet images";
        }

        container.setStatus(status);
        container.setMessage(message);

        return container;
    }

//    @GET
//    @Path("/getImageResource/{pet_name}")
//    public List getImageResources(@PathParam("pet_name") String petName){
//        AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
//        GcsService gcsService = GcsServiceFactory.createGcsService();
//
//        ListOptions listOptions = new ListOptions.Builder().setPrefix(petName).setRecursive(true).build();
//        List<String> list = new ArrayList<>();
//
//        try {
//            ListResult listImages = gcsService.list(appIdentityService.getDefaultGcsBucketName(), listOptions);
//
////            return listImages;
//            do{
//                ListItem item = listImages.next();
////                list.add(item.getName());
//
//                GcsFilename gcsFilename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), item.getName());
//                ImagesService imgService = ImagesServiceFactory.getImagesService();
////                Image img = ImagesServiceFactory.makeImageFromFilename(item.getName());
//                ServingUrlOptions options = ServingUrlOptions.Builder.withGoogleStorageFileName(String.format("/gs/%s/%s", gcsFilename.getBucketName(), gcsFilename.getObjectName()));
//
//                list.add(imgService.getServingUrl(options));
////                GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
//            }while (listImages.hasNext());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }

    @GET
    @Path("/getSpecificFile/{path}")
    @Produces("image/*")
    public ObjectInputStream getSpecificImage(@PathParam("path") String filename){
        filename = filename.replace("+","/");
        AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
        GcsService gcsService = GcsServiceFactory.createGcsService();
        GcsFilename gcsFilename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), filename);
        GcsInputChannel inputChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(Channels.newInputStream(inputChannel));
            return objectInputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @POST
    @Path("/addPet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PetOutputContainer addPet(HashMap<String, String> postData){
//        @Named("pet_name") String petName, /*@Named("filename") String filename, @Named("content") Object content,*/
//        @Named("price") double price, @Named("expiry_date") String expiryDate, @Named("description") String description, @Named("enabled") boolean enabled
        String message = null;
        int status = -1;
        Pet pet = null;

        if(postData != null && postData.size() > 0){
            DatastoreService dataService = DatastoreServiceFactory.getDatastoreService();
            Query.Filter petNameFilter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, postData.get("pet_name"));
            Query q = new Query("Pet").setFilter(petNameFilter);
            PreparedQuery pq = dataService.prepare(q);

            try{
                Entity tempPet = pq.asSingleEntity();

                if(tempPet == null){
                    Entity ePet = new Entity("Pet");

                    //init pet model
                    pet = new Pet();
                    pet.setName(postData.get("pet_name"));
                    pet.setDescription(postData.get("description"));
                    pet.setPrice(Double.parseDouble(postData.get("price")));
                    pet.setEnabled(true);
                    pet.setAvatar(postData.get("avatar"));
                    pet.setCreateDate(new Date());
                    pet.setUpdateDate(null);

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        pet.setExpiryDate(format.parse(postData.get("expiry_date")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //end initialization

                    //populate ePet
                    ePet.setProperty("name", pet.getName());
                    ePet.setProperty("description", pet.getDescription());
                    ePet.setProperty("price", pet.getPrice());
                    ePet.setProperty("enabled", pet.isEnabled());
                    ePet.setProperty("expiry_date", pet.getExpiryDate());
                    ePet.setProperty("avatar", pet.getAvatar());
                    ePet.setProperty("create_date", pet.getCreateDate());
                    ePet.setProperty("update_date", pet.getUpdateDate());
                    //end populating ePet

                    Key keyPet = dataService.put(ePet);

                    if(keyPet != null){
                        message = "Successfully created pet";
                        status = 200;
                    }else{
                        message = "failed created pet";
                        status = 500;
                    }
                }else{
                    message = "Pet already taken";
                    status = 500;
                }
            }catch (PreparedQuery.TooManyResultsException e){
                e.printStackTrace();

                message = "Pet already taken";
                status = 500;
            }


        }else{
            message = "data null";
            status = 500;
        }

        PetOutputContainer container = new PetOutputContainer();
        container.setStatus(status);
        container.setMessage(message);
        container.setData(pet);

        return container;
    }

    @POST
    @Path("/updatePet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PetOutputContainer updatePet(HashMap<String, String> postData){
        String message = null;
        int status = -1;

        DatastoreService dataService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter petNameFilter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, postData.get("pet_name"));
        Query q = new Query("Pet").setFilter(petNameFilter);
        PreparedQuery pq = dataService.prepare(q);

        Entity ePet = pq.asSingleEntity();
        Pet pet = null;

        if(ePet != null){
            pet = new Pet();
            pet.setName(postData.get("pet_name"));
            pet.setDescription(postData.get("description"));
            pet.setPrice(Double.parseDouble(postData.get("price")));
            pet.setEnabled(Boolean.parseBoolean(postData.get("enabled")));
            pet.setUpdateDate(new Date());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                pet.setExpiryDate(format.parse(postData.get("expiry_date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //end initialization

            //populate ePet
            ePet.setProperty("name", pet.getName());
            ePet.setProperty("description", pet.getDescription());
            ePet.setProperty("price", pet.getPrice());
            ePet.setProperty("enabled", pet.isEnabled());
            ePet.setProperty("expiry_date", pet.getExpiryDate());
            ePet.setProperty("update_date", pet.getUpdateDate());
            //end populating ePet

            Key keyPet = dataService.put(ePet);

            if(keyPet != null){
                message = "Successfully updated pet";
                status = 200;
            }else{
                message = "failed update pet";
                status = 500;
            }
        }else{
            message = "Failed to update pet. Pet not found";
            status = 500;
        }

        PetOutputContainer container = new PetOutputContainer();
        container.setData(pet);
        container.setMessage(message);
        container.setStatus(status);

        return container;
    }

    @DELETE
    @Path("/deletePet/{pet_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public PetOutputContainer deletePet(@PathParam("pet_name") String petName){
        String message = null;
        int status = -1;

        DatastoreService dataService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter petNameFilter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, petName);
        Query q = new Query("Pet").setFilter(petNameFilter);
        PreparedQuery pq = dataService.prepare(q);

        Entity ePet = pq.asSingleEntity();

        if(ePet != null){
            dataService.delete(ePet.getKey());

            Entity temp = pq.asSingleEntity();

            if(temp == null){
                message = "Successfully deleted pet";
                status = 200;
            }else{
                message = "Failed to delete pet";
                status = 500;
            }
        }else{
            message = "Failed to delete pet. Pet name not found";
            status = 500;
        }

        PetOutputContainer container = new PetOutputContainer();
        container.setMessage(message);
        container.setStatus(status);

        return container;
    }

    @GET
    @Path("/getPet/{pet_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public PetOutputContainer getPet(@PathParam("pet_name")String petName){
        String message = null;
        int status = -1;

        DatastoreService dataService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter petNameFilter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, petName);
        Query q = new Query("Pet").setFilter(petNameFilter);
        PreparedQuery pq = dataService.prepare(q);

        Entity ePet = pq.asSingleEntity();
        Pet pet = null;

        if(ePet != null){
            pet = new Pet();

            pet.setName((String) ePet.getProperty("name"));
            pet.setDescription((String) ePet.getProperty("description"));
            pet.setPrice((Double) ePet.getProperty("price"));
            pet.setExpiryDate((Date) ePet.getProperty("expiry_date"));
            pet.setCreateDate((Date) ePet.getProperty("create_date"));
            pet.setUpdateDate((Date) ePet.getProperty("update_date"));
            pet.setEnabled((Boolean) ePet.getProperty("enabled"));
            pet.setAvatar((String) ePet.getProperty("avatar"));

            message = "Successfully retrieved pet!";
            status = 200;
        }else{
            message = "Failed to retrieve pet. Pet name not found";
            status = 500;
        }

        PetOutputContainer container = new PetOutputContainer();
        container.setMessage(message);
        container.setStatus(status);
        container.setData(pet);

        return container;
    }

    @GET
    @Path("/getPets")
    @Produces(MediaType.APPLICATION_JSON)
    public PetsOutputContainer getPets(){
        String message = null;
        int status = -1;

        DatastoreService dataService = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("Pet");
        PreparedQuery pq = dataService.prepare(q);
        ArrayList<Pet> listPet = new ArrayList<>();

        for(Entity ePet : pq.asIterable()){
            if(ePet != null){
                Pet pet = new Pet();

                pet.setName((String) ePet.getProperty("name"));
                pet.setDescription((String) ePet.getProperty("description"));
                pet.setPrice((Double) ePet.getProperty("price"));
                pet.setExpiryDate((Date) ePet.getProperty("expiry_date"));
                pet.setCreateDate((Date) ePet.getProperty("create_date"));
                pet.setUpdateDate((Date) ePet.getProperty("update_date"));
                pet.setEnabled((Boolean) ePet.getProperty("enabled"));
                pet.setAvatar((String) ePet.getProperty("avatar"));

                listPet.add(pet);
            }
        }

        if(listPet.size() > 0){
            message = "Successfully retrieved pets!";
            status = 200;
        }else{
            message = "No pets found";
            status = 500;
        }

        PetsOutputContainer container = new PetsOutputContainer();
        container.setMessage(message);
        container.setStatus(status);
        container.setData(listPet);

        return container;
    }

    @POST
    @Path("/buyCoins/")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject buyCoins(@QueryParam("user_id") String userId, @QueryParam("amt") String amt){
        String message = null;
        int status = -1;
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query.Filter idFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);
        Query userQuery = new Query("Person").setFilter(idFilter);
        PreparedQuery pq = datastoreService.prepare(userQuery);
        Entity ePerson = pq.asSingleEntity();

        if(ePerson != null){
            Entity userCoinTransaction = new Entity("User_Coins");
            userCoinTransaction.setProperty("user_id", ePerson.getProperty("id"));
            userCoinTransaction.setProperty("coins_purchased", amt);

            Key userCoinKey = datastoreService.put(userCoinTransaction);

            if(userCoinKey != null){
                double coins = Double.parseDouble(String.valueOf(ePerson.getProperty("coins")));
                double totalCoins = 0;

                if(coins > 0){
                    double purchasedCoins = Double.parseDouble(amt);

                    totalCoins = coins + purchasedCoins;
                }else{
                    totalCoins = Double.parseDouble(amt);
                }

                ePerson.setProperty("coins", totalCoins);
                System.out.println("total coins: " + totalCoins);

                Key personKey = datastoreService.put(ePerson);

                if(personKey != null){
                    message = amt + " was successfully added to your account";
                    status = 200;
                }else{
                    message = "Unable to update your account";
                    status = 500;
                }
            }else{
                message = "failed to add " + amt + " of coins to your account";
                status = 500;
            }
        }else{
            message = "User not found";
            status = 500;
        }

        JsonObject data = new JsonObject();
        data.addProperty("status", status);
        data.addProperty("message", message);

        return data;
    }

    @POST
    @Path("/buyPet")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject buyPet(@QueryParam("user_id") String userId, @QueryParam("pet_purchase") String petPurchase){
        String message = null;
        int status = -1;

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        //serach user by id
        Query.Filter idFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);
        Query userQuery = new Query("Person").setFilter(idFilter);
        PreparedQuery pq = datastoreService.prepare(userQuery);
        Entity ePerson = pq.asSingleEntity();

        if(ePerson != null){
            //search pet by pet name
            Query.Filter petFilter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, petPurchase);
            Query petQuery = new Query("Pet").setFilter(petFilter);
            pq = datastoreService.prepare(petQuery);
            Entity ePet = pq.asSingleEntity();

            if(ePet != null){
                //check if user has enough coins
                double coins = Double.parseDouble(ePerson.getProperty("coins") != null ? String.valueOf(ePerson.getProperty("coins")) : "0");
                double price = Double.parseDouble(String.valueOf(ePet.getProperty("price")));

                if(coins >= price){
                    //save pet purchased by user
                    Entity userPet = new Entity("User_Pet");
                    userPet.setProperty("user_id", ePerson.getProperty("id"));
                    userPet.setProperty("pet_purchased", petPurchase);

                    Key purchaseKey = datastoreService.put(userPet);

                    if(purchaseKey != null){
                        ePerson.setProperty("coins", coins - price);

                        Key personKey = datastoreService.put(ePerson);

                        if(personKey != null){
                            message = "You have successfully purchased " + petPurchase + " Pet!";
                            status = 200;
                        }else{
                            message = "Unable to update your coins.";
                            status = 500;
                        }
                    }else{
                        message = "Unable to purchase pet";
                        status = 500;
                    }
                }else{
                    message = "You have insufficient coins. Please buy Additional coins to purchase this pet.";
                    status = 500;
                }
            }else{
                message = "Pet not found";
                status = 500;
            }
        }else{
            message = "User not found";
            status = 500;
        }

        JsonObject data = new JsonObject();
        data.addProperty("status", status);
        data.addProperty("message", message);

        return data;
    }

}
