package at.fhv.td.rss;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class FeedReaderTest {
    SyndFeed _mockSyndFeed = new SyndFeedImpl();

    @Before
    public void before() throws IOException, FeedException {
        SyndEntry mockSyndEntry = new SyndEntryImpl();
        SyndContent mockSyndContent = new SyndContentImpl();
        mockSyndContent.setValue("Description");
        mockSyndEntry.setTitle("Title");
        mockSyndEntry.setDescription(mockSyndContent);
        mockSyndEntry.setPublishedDate(new Date());
        mockSyndEntry.setLink("https://www.apple.com");
        _mockSyndFeed.setEntries(Collections.singletonList(mockSyndEntry));
        mockStatic(Utils.class);
        when(Utils.getFeed(any(URL.class))).thenReturn(_mockSyndFeed);
    }

    @Test
    public void readFeed() throws IOException, FeedException {
        FeedMessage feedMessage = FeedReader.readFeed(
                "https://www.festivalticker.de/rss-festivalfeed/festivalkalender.xml"
        ).get(0);
        assertEquals("Title", feedMessage.getTitle());
        assertEquals("Description", feedMessage.getDescription());
        assertEquals("https://www.apple.com", feedMessage.getLink());
    }

    @Test
    public void readFeedException() throws IOException, FeedException {
        when(Utils.getFeed(any(URL.class))).thenThrow(IOException.class);
        List<FeedMessage> feedMessages = FeedReader.readFeed(
                "https://www.festivalticker.de/rss-festivalfeed/festivalkalender.xml"
        );
        assertEquals(0, feedMessages.size());
    }
}