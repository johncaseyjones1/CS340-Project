package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a logout request.
 */
public class LogoutRequest {

    private final String username;

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     */
    public LogoutRequest(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

}
