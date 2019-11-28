package at.fhv.td.rss;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lukas Bals
 */
public class FeedMessage implements Serializable {
    private String _title;
    private String _description;
    private String _link;
    private Date _publishDate;

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getLink() {
        return _link;
    }

    public void setLink(String link) {
        this._link = link;
    }

    public Date getPublishDate() {
        return _publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this._publishDate = publishDate;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
