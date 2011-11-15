package models;
 
import java.util.*;
import javax.persistence.*;

import controllers.Check;
import play.db.jpa.*;
import utils.AuthUtil;
import utils.SessionUtil;

@Check({"admin", "advertiser"})
@Entity
public class Comment extends Model {

    public String author;
    public Date postedAt;
     
    @Lob
    public String content;
    
    @ManyToOne
    public Advert advert;
    
    public Comment(Advert advert, String author, String content) {
        this.advert = advert;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

    public boolean isHidden() {
        if (!AuthUtil.isUser()) {
            if (SessionUtil.checkCommentAccessInSession(this.getId())) {
                return false;
            }
        }
        return true;
    }

}