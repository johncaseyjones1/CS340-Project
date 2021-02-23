package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.presenter.status.FeedPresenter;

/**
 * An {@link AsyncTask} for retrieving feed for a user.
 */
public class GetFeedTask extends AsyncTask<StatusRequest, Void, StatusResponse> {

    private final FeedPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void feedRetrieved(StatusResponse statusResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve feed.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFeedTask(FeedPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve feed. This method is
     * invoked indirectly by calling {@link #execute(StatusRequest...)}.
     *
     * @param statusRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected StatusResponse doInBackground(StatusRequest... statusRequests) {

        StatusResponse response = null;

        try {
            response = presenter.getStatuses(statusRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param statusResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(StatusResponse statusResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.feedRetrieved(statusResponse);
        }
    }
}
