package edu.byu.cs.tweeter.presenter.status;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

/**
 * The presenter for the "feed" functionality of the application.
 */
public class FeedPresenter extends StatusPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FeedPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is feed. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses.
     */
    @Override
    public StatusResponse getStatusesSpecific(StatusRequest request) throws IOException {
        FeedService feedService = getFeedService();
        return feedService.getStatuses(request);
    }



    /**
     * Returns an instance of {@link FeedService}. Allows mocking of the FeedService class
     * for testing purposes. All usages of FeedService should get their FeedService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public FeedService getFeedService() {
        return new FeedService();
    }
}
