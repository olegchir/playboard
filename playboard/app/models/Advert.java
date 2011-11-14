package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Advert extends Model {

    public String title;
    public Date postedAt;

    @Lob
    public String content;

    @ManyToOne
    public User author;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    public List<Comment> comments;

    public Advert(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }

    public Advert addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content).save();
        this.comments.add(newComment);
        this.save();
        return this;
    }

    public Advert previous() {
        return Advert.find("postedAt < ? order by postedAt desc", postedAt).first();
    }

    public Advert next() {
        return Advert.find("postedAt > ? order by postedAt asc", postedAt).first();
    }
}