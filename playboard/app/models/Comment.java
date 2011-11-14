package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
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
 
}