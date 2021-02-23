package edu.byu.cs.tweeter.model.service.request;

import java.util.Date;
import java.util.Vector;

public class PostStatusRequest {

    private final String userAlias;
    private final String content;
    private final Date timeStamp;

    public PostStatusRequest(String userAlias, String content, Date timeStamp) {
        this.userAlias = userAlias;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
