package edu.byu.cs.tweeter.model.domain.status;

import java.io.Serializable;

import edu.byu.cs.tweeter.model.domain.User;

public class Tag implements Serializable {
    private final User user;
    private final Integer positionInString;
    private final Integer length;

    public Tag(User user, Integer positionInString, Integer length) {
        this.user = user;
        this.positionInString = positionInString;
        this.length = length;
    }

    public User getUser() {
        return user;
    }

    public Integer getPositionInString() {
        return positionInString;
    }

    public Integer getLength() {
        return length;
    }
}
