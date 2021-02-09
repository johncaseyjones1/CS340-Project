package edu.byu.cs.tweeter.model.service.response;

/**
 * A string response for a {@link edu.byu.cs.tweeter.model.service.request.FollowRequest}.
 */
public class FollowResponse {

    private final String reponse;

    public FollowResponse(String message) {
        this.reponse = message;
    }

    public String getReponse() {
        return reponse;
    }
}
