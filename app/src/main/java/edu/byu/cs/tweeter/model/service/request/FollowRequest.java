package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a request to have the server reflect the active user
 * following the user whose page they are viewing
 */
public class FollowRequest {
    private final String userToFollowAlias;
    private final String activeUserAlias;

    /**
     * Creates an instance.
     *
     * @param activeUserAlias the alias of the user who is wanting to follow someone
     * @param userToFollowAlias the alias of the user who is being followed
     */
    public FollowRequest(String activeUserAlias, String userToFollowAlias) {
        this.userToFollowAlias = userToFollowAlias;
        this.activeUserAlias = activeUserAlias;
    }

    public String getUserToFollowAlias() {
        return userToFollowAlias;
    }

    public String getActiveUserAlias() {
        return activeUserAlias;
    }
}
