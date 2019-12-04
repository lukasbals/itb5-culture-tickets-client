package at.fhv.td.rss;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class FeedMessageTest {

    @Test
    public void setAndGetTitle() {
        FeedMessage feedMessage = new FeedMessage();
        feedMessage.setTitle("Title");
        assertEquals("Title", feedMessage.getTitle());
    }

    @Test
    public void setAndGetDescription() {
        FeedMessage feedMessage = new FeedMessage();
        feedMessage.setDescription("Description");
        assertEquals("Description", feedMessage.getDescription());
    }

    @Test
    public void getLink() {
        FeedMessage feedMessage = new FeedMessage();
        feedMessage.setLink("link");
        assertEquals("link", feedMessage.getLink());
    }

    @Test
    public void getPublishDate() {
        FeedMessage feedMessage = new FeedMessage();
        Date expected = new Date();
        feedMessage.setPublishDate(expected);
        assertEquals(expected, feedMessage.getPublishDate());
    }

    @Test
    public void testToString() {
        FeedMessage feedMessage = new FeedMessage();
        feedMessage.setTitle("Title");
        assertEquals("Title", feedMessage.toString());
    }
}