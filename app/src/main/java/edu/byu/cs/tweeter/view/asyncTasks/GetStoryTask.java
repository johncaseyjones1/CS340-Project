package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.presenter.status.StoryPresenter;

/**
 * An {@link AsyncTask} for retrieving story for a user.
 */
public class GetStoryTask extends AsyncTask<StatusRequest, Void, StatusResponse> {

    private final StoryPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void storyRetrieved(StatusResponse statusResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve story.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetStoryTask(StoryPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve story. This method is
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
            observer.storyRetrieved(statusResponse);
        }
    }
}
