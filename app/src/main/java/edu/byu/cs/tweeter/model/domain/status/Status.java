package edu.byu.cs.tweeter.model.domain.status;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Status implements Serializable {

    private final String userAlias;
    private final String content;
    private Vector<Tag> tags;
    private Vector<URL> links;
    private final Date timeStamp;

    public Status(String userAlias, String content, Vector<Tag> tags, Vector<URL> links, Date timeStamp) {
        this.userAlias = userAlias;
        this.content = content;
        this.tags = tags;
        this.links = links;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public Vector<Tag> getTags() {
        return tags;
    }

    public Vector<URL> getLinks() {
        return links;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getUserAlias() {
        return userAlias;
    }
}
