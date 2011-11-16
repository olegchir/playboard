package controllers;

import controllers.deadbolt.Deadbolt;
import play.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import models.*;
import utils.SortBar;

public class Application extends Controller {
    @Before
    static void addDefaults() {
        renderArgs.put("boardTitle", Play.configuration.getProperty("board.title"));
        renderArgs.put("boardBaseline", Play.configuration.getProperty("board.baseline"));
    }

    public static void indexScreenSetMinimumDate(String minimumDate) {
        //The only way to parse ISO date in a standard way — can't use it there
        //Date dtMinimumDate = javax.xml.bind.DatatypeConverter.parseDateTime(minimumDate).getTime();

        //Get date in ISO format from session as a String and parse it into Data object
        Date dtMinimumDate = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            dtMinimumDate = format.parse(minimumDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Put this date into session by serializing it into long time
        Scope.Session.current().put("indexScreenMinimumDate",Long.toString(dtMinimumDate.getTime()));

        //Send response coz all operations are finished correctly
        renderJSON("{\"ok\":\"ok\"}");
    }

    public static void index(String sortBy, String lastSortBy, String sortingReversed) {
        //Get minimum date from session as a string that holds long time, and convert it into Date object
        String strMinTime = Scope.Session.current().get("indexScreenMinimumDate");
        Date minimumDate = null;
        if (null!=strMinTime) {
            Long longMinTime = Long.parseLong(strMinTime);
            minimumDate = new Date(longMinTime);
        }

        ModelPaginator advertsFound = null;
        //If mimimumDate successfully extracted from the session, inject into into initial query
        if (null==minimumDate) {
            advertsFound = new ModelPaginator(Advert.class);
        } else {
            advertsFound = new ModelPaginator(Advert.class,"postedAt >= ?", minimumDate);
        }

        //Apply additional params to this query
        SortBar sortBar = new SortBar(advertsFound,sortingReversed,sortBy,lastSortBy);
        advertsFound = sortBar.getAdvertsFound();
        advertsFound.setPageSize(5);

        //And render it back
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