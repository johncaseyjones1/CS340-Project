package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuss for a specified user.
 */
public class StatusRequest {

    private final String userAlias;
    private final int limit;
    private final String lastStatusContent;
    private final String lastUserAlias;

    public StatusRequest(String userAlias, int limit, String lastStatusContent, String lastUserAlias) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatusContent = lastStatusContent;
        this.lastUserAlias = lastUserAlias;
    }



    public String getUserAlias() {
        return userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public String getLastStatusContent() {
        return lastStatusContent;
    }

    public String getLastUserAlias() {
        return lastUserAlias;
    }
}
