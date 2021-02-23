package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.StatusCreatorPresenter;

public class PostStatusTask extends AsyncTask<PostStatusRequest, Void, PostStatusResponse> {
    private final StatusCreatorPresenter presenter;
    private final PostStatusTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void postStatusCompleted(PostStatusResponse response);
        void handleException(Exception exception);
    }

    public PostStatusTask(StatusCreatorPresenter presenter, PostStatusTask.Observer observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected PostStatusResponse doInBackground(PostStatusRequest... postStatusRequests) {

        PostStatusResponse response = null;

        try {
            response = presenter.postStatus(postStatusRequests[0]);
        } catch (Exception e) {
            exception = e;
        }

        return response;
    }

    @Override
    protected void onPostExecute(PostStatusResponse response) {
        if (exception != null) {
            observer.handleException(exception);
        } else {
            observer.postStatusCompleted(response);
        }
    }
}
