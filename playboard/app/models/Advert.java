package models;

import java.util.*;
import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;
import play.mvc.Scope;

import play.libs.*;
import play.cache.Cache;
import utils.SessionUtil;

@Entity
public class Advert extends Model {
    @Required
    public String title;
    @Required
    public Date postedAt;

    @Lob
    @Required
    @MaxSize(10000)
    public String content;

    @Required
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
        SessionUtil.saveCommentAccessInSession(newComment.getId());
        return this;
    }

    public Advert previous() {
        return Advert.find("postedAt < ? order by postedAt desc", postedAt).first();
    }

    public Advert next() {
        return Advert.find("postedAt > ? order by postedAt asc", postedAt).first();
    }
}