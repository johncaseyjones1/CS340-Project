package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observer;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingTask;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The user activity. Shows a users name, image, followers, followees and story.
 */
public class UserActivity extends AppCompatActivity implements FollowTask.Observer {

    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        User user = (User) getIntent().getSerializableExtra(FollowingFragment.USER);
        User activeUser = (User) getIntent().getSerializableExtra(FollowingFragment.ACTIVE_USER);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken, 3);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //TODO FIRST THING IDK FIX THIS
        LayoutInflater factory = LayoutInflater.from(this);
        View userView = factory.inflate(R.layout.activity_user, null);
        presenter = new UserPresenter((UserPresenter.View) userView);

        FollowTask.Observer observer = (FollowTask.Observer) this;

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        //TODO Get actual following count from presenter
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 42));

        //TODO Get actual followers count from presenter
        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 27));


        Button follow = findViewById(R.id.followButton);


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO Implement follow/unfollow button
                FollowTask followTask = new FollowTask(presenter, observer);
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
        //TODO Update followers of user being viewed
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(this.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}