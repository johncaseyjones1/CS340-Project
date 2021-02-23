package edu.byu.cs.tweeter.model.service.response;

public class PostStatusResponse {

    private final String message;

    public PostStatusResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
