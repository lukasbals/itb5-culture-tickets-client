package at.fhv.td.rss;

import com.rometools.rome.feed.synd.SyndEntry;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FeedReader {
    private FeedReader() {
    }

    // Example url: https://www.festivalticker.de/rss-festivalfeed/festivalkalender.xml
    public static List<FeedMessage> readFeed(String url) {
        List<FeedMessage> feedMessages = new LinkedList<>();
        try {
            URL feedUrl = new URL(url);

            for (SyndEntry entry : Utils.getFeed(feedUrl).getEntries()) {
                FeedMessage feedMessage = new FeedMessage();
                feedMessage.setTitle(entry.getTitle());
                feedMessage.setDescription(entry.getDescription().getValue());
                feedMessage.setLink(entry.getLink());
                feedMessage.setPublishDate(entry.getPublishedDate());
                feedMessages.add(feedMessage);
            }
        } catch (Exception ex) {
            feedMessages.clear();
            return feedMessages;
        }
        return feedMessages;
    }
}