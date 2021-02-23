package edu.byu.cs.tweeter.presenter.status;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

/**
 * The presenter for the "status" functionality of the application.
 */
public abstract class StatusPresenter {

    /**
     * Returns the users that the user specified in the request is feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses.
     */
    public StatusResponse getStatuses(StatusRequest request) throws IOException {
        return getStatusesSpecific(request);
    }

    abstract StatusResponse getStatusesSpecific(StatusRequest request) throws IOException;

}
