package edu.byu.cs.tweeter.model.service.request;

public class FollowingNumRequest {
    private String userAlias;

    public FollowingNumRequest(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }
}
