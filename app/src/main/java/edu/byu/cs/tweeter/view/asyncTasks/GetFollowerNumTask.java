package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowerNumRequest;
import edu.byu.cs.tweeter.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerNumResponse;
import edu.byu.cs.tweeter.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.presenter.FollowersPresenter;

/**
 * An {@link AsyncTask} for retrieving followers for a user.
 */
public class GetFollowerNumTask extends AsyncTask<FollowerNumRequest, Void, FollowerNumResponse> {

    private final FollowersPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void followerNumRetrieved(FollowerNumResponse followerNumResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowerNumTask(FollowersPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(FollowerNumRequest...)}.
     *
     * @param followerNumRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowerNumResponse doInBackground(FollowerNumRequest... followerNumRequests) {

        FollowerNumResponse response = null;

        try {
            response = presenter.getFollowerNum(followerNumRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followerNumResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowerNumResponse followerNumResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            System.out.println(followerNumResponse.getNumFollowers());
            observer.followerNumRetrieved(followerNumResponse);
        }
    }
}
