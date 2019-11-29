package at.fhv.td.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;

public class Utils {
    private Utils() {
    }

    static SyndFeed getFeed(URL feedUrl) throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedUrl));
    }
}