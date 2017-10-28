package trainedge.myapplication.model;

/**
 * Created by DIVYANGANA KOTHARI on 09-10-2017.
 */

public class ContactModel {

    public String tv_name;

    public ContactModel() {
    }
    public ContactModel(String tv_name) {
        this.tv_name = tv_name;

    }
    public String getName() {
        return tv_name;
    }

    public void setName(String tv_name) {
        this.tv_name = tv_name;
    }
}

