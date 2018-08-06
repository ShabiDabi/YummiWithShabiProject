import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class FeedItem {
    @PrimaryKey
    @NonNull
    public String id;
    public Date dateCreated;
    public String picture;
    public String userID;
    public String text;


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userID;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getPicture() {
        return picture;
    }

    public String getText() {
        return text;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }

    public void setbDateCreated(Date date) {
        this.dateCreated = date;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setText(String text) {
        this.text = text;
    }

}
