package edu.byu.cs.tweeter.model.service.response;

import android.content.Intent;

public class FollowerNumResponse {

    private Integer numFollowers;

    public FollowerNumResponse(Integer numFollowers) {
        this.numFollowers = numFollowers;
    }

    public Integer getNumFollowers() {
        return numFollowers;
    }
}
