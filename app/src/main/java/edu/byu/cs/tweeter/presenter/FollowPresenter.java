package edu.byu.cs.tweeter.presenter;

import android.text.Layout;
import android.view.View;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowPresenter(View view) { this.view = view;}

    /**
     * Returns whether or not the user was successfully followed
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowResponse follow(FollowRequest request) throws IOException {
        FollowService followService = getFollowService();
        return followService.follow(request);
    }

    /**
     * Returns an instance of {@link FollowService}. Allows mocking of the FollowService class
     * for testing purposes. All usages of FollowService should get their FollowService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowService getFollowService() {
        return new FollowService();
    }
}
