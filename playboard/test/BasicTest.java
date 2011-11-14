import org.junit.*;

import java.util.*;

import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        // Retrieve the user with e-mail address bob@gmail.com
        User bob = User.find("byEmail", "bob@gmail.com").first();

        // Test
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }

    @Test
    public void tryConnectAsUser() {
        // Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        // Test
        assertNotNull(User.connect("bob@gmail.com", "secret"));
        assertNull(User.connect("bob@gmail.com", "badpassword"));
        assertNull(User.connect("tom@gmail.com", "secret"));
    }

    @Test
    public void createAdvert() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        // Create a new Advert
        new Advert(bob, "My first Advert", "Hello world").save();

        // Test that the Advert has been created
        assertEquals(1, Advert.count());

        // Retrieve all Adverts created by Bob
        List<Advert> bobAdverts = Advert.find("byAuthor", bob).fetch();

        // Tests
        assertEquals(1, bobAdverts.size());
        Advert firstAdvert = bobAdverts.get(0);
        assertNotNull(firstAdvert);
        assertEquals(bob, firstAdvert.author);
        assertEquals("My first Advert", firstAdvert.title);
        assertEquals("Hello world", firstAdvert.content);
        assertNotNull(firstAdvert.postedAt);
    }

    @Test
    public void AdvertComments() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        // Create a new Advert
        Advert bobAdvert = new Advert(bob, "My first Advert", "Hello world").save();

        // Advert a first comment
        new Comment(bobAdvert, "Jeff", "Nice Advert").save();
        new Comment(bobAdvert, "Tom", "I knew that !").save();

        // Retrieve all comments
        List<Comment> bobAdvertComments = Comment.find("byAdvert", bobAdvert).fetch();

        // Tests
        assertEquals(2, bobAdvertComments.size());

        Comment firstComment = bobAdvertComments.get(0);
        assertNotNull(firstComment);
        assertEquals("Jeff", firstComment.author);
        assertEquals("Nice Advert", firstComment.content);
        assertNotNull(firstComment.postedAt);

        Comment secondComment = bobAdvertComments.get(1);
        assertNotNull(secondComment);
        assertEquals("Tom", secondComment.author);
        assertEquals("I knew that !", secondComment.content);
        assertNotNull(secondComment.postedAt);
    }

    @Test
    public void useTheCommentsRelation() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        // Create a new Advert
        Advert bobAdvert = new Advert(bob, "My first Advert", "Hello world").save();

        // Advert a first comment
        bobAdvert.addComment("Jeff", "Nice Advert");
        bobAdvert.addComment("Tom", "I knew that !");

        // Count things
        assertEquals(1, User.count());
        assertEquals(1, Advert.count());
        assertEquals(2, Comment.count());

        // Retrieve Bob's Advert
        bobAdvert = Advert.find("byAuthor", bob).first();
        assertNotNull(bobAdvert);

        // Navigate to comments
        assertEquals(2, bobAdvert.comments.size());
        assertEquals("Jeff", bobAdvert.comments.get(0).author);

        // Delete the Advert
        bobAdvert.delete();

        // Check that all comments have been deleted
        assertEquals(1, User.count());
        assertEquals(0, Advert.count());
        assertEquals(0, Comment.count());
    }
    
    @Test
    public void fullTest() {
        Fixtures.loadModels("data.yml");
     
        // Count things
        assertEquals(4, User.count());
        assertEquals(2, Advert.count());
        assertEquals(3, Comment.count());
     
        // Try to connect as users
        assertNotNull(User.connect("lenin@gmail.com", "secret"));
        assertNotNull(User.connect("stalin@gmail.com", "secret"));
        assertNull(User.connect("jeff@gmail.com", "badpassword"));
        assertNull(User.connect("tom@gmail.com", "secret"));
     
        // Find all of Putin's adverts
        List<Advert> putinadverts = Advert.find("author.email", "putin@gmail.com").fetch();
        assertEquals(1, putinadverts.size());

        // Find all comments related to Putin's posts
        List<Comment> bobComments = Comment.find("advert.author.email", "putin@gmail.com").fetch();
        assertEquals(2, bobComments.size());
     
        // Find the most recent advert
        Advert frontadvert = Advert.find("order by postedAt desc").first();
        assertNotNull(frontadvert);
        assertEquals("my first advert", frontadvert.title);
     
        // Check that this advert has two comments
        assertEquals(1, frontadvert.comments.size());
     
        // advert a new comment
        frontadvert.addComment("Jim", "Hello guys");
        assertEquals(2, frontadvert.comments.size());
        assertEquals(4, Comment.count());
    }

}
