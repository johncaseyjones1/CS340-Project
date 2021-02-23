package edu.byu.cs.tweeter.model.service.response;

import android.content.Intent;

public class FollowingNumResponse {

    private Integer numFollowees;

    public FollowingNumResponse(Integer numFollowees) {
        this.numFollowees = numFollowees;
    }

    public Integer getNumFollowees() {
        return numFollowees;
    }
}
