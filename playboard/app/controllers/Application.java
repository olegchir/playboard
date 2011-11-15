package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.SortBar;

@With(Secure.class)
public class Application extends Controller {


    @Before
    static void addDefaults() {
        renderArgs.put("boardTitle", Play.configuration.getProperty("board.title"));
        renderArgs.put("boardBaseline", Play.configuration.getProperty("board.baseline"));
    }

    public static void index(String sortBy, String lastSortBy, String sortingReversed) {
        ModelPaginator advertsFound = new ModelPaginator(Advert.class);
        SortBar sortBar = new SortBar(advertsFound,sortingReversed,sortBy,lastSortBy);
        advertsFound = sortBar.getAdvertsFound();
        advertsFound.setPageSize(5);
        render(advertsFound,sortBar);
    }

    public static void show(Long id) {
        Advert advert = Advert.findById(id);
        String randomID = Codec.UUID();
        render(advert, randomID);
    }

    public static void postComment(
        Long postId,
        @Required(message="Author is required") String author,
        @Required(message="A message is required") String content,
        @Required(message="Please type the code") String code,
        String randomID
    ) {
        Advert advert = Advert.findById(postId);

        validation.equals(
                code, Cache.get(randomID)
        ).message("Invalid code. Please type it again");

        if (validation.hasErrors()) {
            Cache.delete(randomID);
            randomID = Codec.UUID();
            render("Application/show.html", advert, randomID);
        }

        advert.addComment(author, content);

        flash.success("Thanks for posting %s", author);

        show(postId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

}