package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowersService;
import edu.byu.cs.tweeter.model.service.request.FollowerNumRequest;
import edu.byu.cs.tweeter.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerNumResponse;
import edu.byu.cs.tweeter.model.service.response.FollowersResponse;

/**
 * The presenter for the "followers" functionality of the application.
 */
public class FollowersPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public FollowersPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is followers. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
        FollowersService followersService = getFollowersService();
        return followersService.getFollowers(request);
    }

    public FollowerNumResponse getFollowerNum(FollowerNumRequest request) throws IOException {
        FollowersService followersService = getFollowersService();
        return followersService.getFollowerNum(request);
    }

    /**
     * Returns an instance of {@link FollowersService}. Allows mocking of the FollowersService class
     * for testing purposes. All usages of FollowersService should get their FollowersService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowersService getFollowersService() {
        return new FollowersService();
    }
}
