package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowingNumRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingNumResponse;
import edu.byu.cs.tweeter.presenter.FollowingPresenter;

/**
 * An {@link AsyncTask} for retrieving followees for a user.
 */
public class GetFollowingNumTask extends AsyncTask<FollowingNumRequest, Void, FollowingNumResponse> {

    private final FollowingPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void followingNumRetrieved(FollowingNumResponse followingNumResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followees.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowingNumTask(FollowingPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followees. This method is
     * invoked indirectly by calling {@link #execute(FollowingNumRequest...)}.
     *
     * @param followingNumRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowingNumResponse doInBackground(FollowingNumRequest... followingNumRequests) {

        FollowingNumResponse response = null;

        try {
            response = presenter.getFollowingNum(followingNumRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followingNumResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowingNumResponse followingNumResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            System.out.println(followingNumResponse.getNumFollowees());
            observer.followingNumRetrieved(followingNumResponse);
        }
    }
}
