package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerNumRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingNumRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerNumResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingNumResponse;
import edu.byu.cs.tweeter.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.presenter.FollowingPresenter;
import edu.byu.cs.tweeter.presenter.FollowPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowerNumTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingNumTask;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The user activity. Shows a users name, image, followers, followees and story.
 */
public class UserActivity extends AppCompatActivity implements FollowTask.Observer, FollowPresenter.View, GetFollowerNumTask.Observer, FollowersPresenter.View, GetFollowingNumTask.Observer, FollowingPresenter.View {

    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowPresenter followPresenter;
    private FollowersPresenter followersPresenter;
    private FollowingPresenter followingPresenter;
    TextView followerCount;
    TextView followeeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        User user = (User) getIntent().getSerializableExtra(FollowingFragment.USER);
        User activeUser = (User) getIntent().getSerializableExtra(FollowingFragment.ACTIVE_USER);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }
        if(activeUser == null) {
            throw new RuntimeException("ActiveUser not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), user, authToken, false);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        followPresenter = new FollowPresenter(this);
        followersPresenter = new FollowersPresenter(this);
        followingPresenter = new FollowingPresenter(this);

        FollowTask.Observer observer = (FollowTask.Observer) this;
        GetFollowerNumTask.Observer observerFollower = (GetFollowerNumTask.Observer) this;
        GetFollowingNumTask.Observer observerFollowing = (GetFollowingNumTask.Observer) this;

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        followeeCount = findViewById(R.id.followeeCount);
        GetFollowingNumTask getFollowingNumTask = new GetFollowingNumTask(followingPresenter, observerFollowing);
        FollowingNumRequest followingRequest = new FollowingNumRequest(user.getAlias());
        getFollowingNumTask.execute(followingRequest);

        followerCount = findViewById(R.id.followerCount);
        GetFollowerNumTask getFollowerNumTask = new GetFollowerNumTask(followersPresenter, observerFollower);
        FollowerNumRequest followersRequest = new FollowerNumRequest(user.getAlias());
        getFollowerNumTask.execute(followersRequest);



        Button follow = findViewById(R.id.followButton);


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Trying to follow...", Toast.LENGTH_SHORT).show();
                if (follow.getText().equals("Follow"))
                    follow.setText(R.string.unfollow);
                else
                    follow.setText(R.string.follow);
                FollowTask followTask = new FollowTask(followPresenter, observer);
                FollowRequest request = new FollowRequest(activeUser.getAlias(), user.getAlias());
                followTask.execute(request);
                //FollowRequest followRequest
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void followCompleted(FollowResponse followResponse) {
        Toast.makeText(this.getApplicationContext(), followResponse.getReponse(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void followerNumRetrieved(FollowerNumResponse followerNumResponse) {
        followerCount.setText(getString(R.string.followerCount, followerNumResponse.getNumFollowers()));
    }

    @Override
    public void followingNumRetrieved(FollowingNumResponse followingNumResponse) {
        followeeCount.setText(getString(R.string.followeeCount, followingNumResponse.getNumFollowees()));
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(this.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }


}