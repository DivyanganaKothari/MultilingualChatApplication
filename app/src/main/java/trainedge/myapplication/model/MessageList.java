package trainedge.myapplication.model;

/**
 * Created by DIVYANGANA KOTHARI on 26-10-2017.
 */

public class MessageList {
    public String Receiver_Id;
    public String Sender_Id;
    public Long Time;
    public String content;
    public String receiver_lang;
    public String sender_lang;
    public String translated ="";
    public MessageList(){

    }
    public MessageList(String Receiver_Id,String Sender_Id,Long Time,String content,String receiver_lang,String sender_lang ){
        this.Receiver_Id=Receiver_Id;
        this.Sender_Id=Sender_Id;
        this.Time=Time;
        this.content=content;
        this.receiver_lang=receiver_lang;
        this.sender_lang=sender_lang;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
