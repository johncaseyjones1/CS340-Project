package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

/**
 * Contains the business logic for following a user.
 */
public class FollowService {

    /**
     * Returns response containing success/failure message of the follow request. Uses the
     * {@link ServerFacade} to perform the follow.
     *
     * @param request contains the data required to fulfill the request.
     * @return the success/failure message.
     */
    public FollowResponse follow(FollowRequest request) throws IOException {
        return getServerFacade().follow(request);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
