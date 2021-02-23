package edu.byu.cs.tweeter.view.main.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.StatusCreatorPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;
import edu.byu.cs.tweeter.view.main.MainActivity;
import edu.byu.cs.tweeter.R;

public class CreateStatusFragment extends Fragment implements PostStatusTask.Observer, StatusCreatorPresenter.View {
    private View view;
    MainActivity mainActivity;
    StatusCreatorPresenter presenter;
    private String userAlias;
    private static final String USER_KEY = "UserKey";

    public CreateStatusFragment(MainActivity activity){
        this.mainActivity = activity;
        mainActivity.fab.setClickable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.status_creation, container, false);
        mainActivity.fab.setAlpha(0.0f);

        userAlias = getArguments().getString("userAlias");
        EditText statusField = view.findViewById(R.id.status_field);
        final Button postButton = view.findViewById(R.id.post);

        presenter = new StatusCreatorPresenter(this);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Posting your status!",Toast.LENGTH_SHORT).show();
                // Actually make the post
                postStatus(statusField.getText().toString());
            }
        });
        return view;
    }

    void postStatus(String statusContent) {
        PostStatusTask postStatusTask = new PostStatusTask(presenter, this);
        Date localDateTime = Calendar.getInstance().getTime();
        System.out.println("Status made at: " + localDateTime.toString());
        PostStatusRequest request = new PostStatusRequest(userAlias, statusContent, localDateTime);
        postStatusTask.execute(request);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainActivity.fab.setAlpha(1.0f);
        mainActivity.fab.setClickable(true);
    }

    @Override
    public void postStatusCompleted(PostStatusResponse response) {
        Toast.makeText(getContext(), "Status posted successfully!", Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(getContext(), "Sorry, at least one of the users you have tagged does not exist! Please try again.", Toast.LENGTH_LONG).show();
    }
}
