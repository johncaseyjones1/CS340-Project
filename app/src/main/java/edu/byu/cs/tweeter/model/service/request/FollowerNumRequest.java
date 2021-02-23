package edu.byu.cs.tweeter.model.service.request;

public class FollowerNumRequest {
    private String userAlias;

    public FollowerNumRequest(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }
}
