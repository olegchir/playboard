package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    @Before
    static void addDefaults() {
        renderArgs.put("boardTitle", Play.configuration.getProperty("board.title"));
        renderArgs.put("boardBaseline", Play.configuration.getProperty("board.baseline"));
    }

    public static void index() {
        Advert frontAdvert = Advert.find("order by postedAt desc").first();
        List<Advert> olderAdverts = Advert.find(
                "order by postedAt desc"
        ).from(1).fetch(10);
        render(frontAdvert, olderAdverts);
    }

    public static void show(Long id) {
        Advert advert = Advert.findById(id);
        render(advert);
    }

    public static void postComment(Long postId, @Required String author, @Required String content) {
        Advert advert = Advert.findById(postId);

        if (validation.hasErrors()) {
            render("Application/show.html", advert);
        }

        advert.addComment(author, content);

        flash.success("Thanks for posting %s", author);

        show(postId);
    }

}