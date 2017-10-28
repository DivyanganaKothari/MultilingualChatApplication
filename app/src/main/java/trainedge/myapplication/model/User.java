package trainedge.myapplication.model;

/**
 * Created by DIVYANGANA KOTHARI on 08-09-2017.
 */

public class User {
   public String name,email,id,photo,language;
    public User(){

    }

    public User(String name, String email, String id, String photo,String language) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.photo = photo;
        this.language=language;
    }
}
