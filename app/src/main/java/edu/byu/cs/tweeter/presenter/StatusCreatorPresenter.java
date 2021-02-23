package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class StatusCreatorPresenter {
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
    public StatusCreatorPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a status post request.
     *
     * @param postStatusRequest the request.
     */
    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest) throws Exception {
        PostStatusService postStatusService = new PostStatusService();
        return postStatusService.postStatus(postStatusRequest);
    }

    public PostStatusService getPostStatusService() { return new PostStatusService();}
}
