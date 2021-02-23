package edu.byu.cs.tweeter.view.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.main.story.StoryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages
 * of the Main Activity.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static int[] tabTitles = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;
    private final User user;
    private final AuthToken authToken;
    private int pages = 0;
    private int followersFragmentPosition = -1;
    private int followingFragmentPosition = -2;
    private int storyFragmentPosition = -3;
    private int feedFragmentPosition = -4;

    public SectionsPagerAdapter(Context context, FragmentManager fm, User user, AuthToken authToken, boolean activeUser) {
        super(fm);
        tabTitles = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
        mContext = context;
        this.user = user;
        this.authToken = authToken;

        if (activeUser) {
            pages = 4;
            followersFragmentPosition = pages - 1;
            followingFragmentPosition = pages - 2;
            storyFragmentPosition = pages - 3;
            feedFragmentPosition = pages - 4;
        }
        else {
            tabTitles = new int[]{R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
            pages = 3;
            followersFragmentPosition = pages - 1;
            followingFragmentPosition = pages - 2;
            storyFragmentPosition = pages - 3;
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (position == followingFragmentPosition) {
            return FollowingFragment.newInstance(user, authToken);
        }
        else if (position == followersFragmentPosition) {
            return FollowersFragment.newInstance(user, authToken);
        }
        else if (position == storyFragmentPosition) {
            return StoryFragment.newInstance(user, authToken);
        }
        else if (position == feedFragmentPosition) {
            return FeedFragment.newInstance(user, authToken);
        }
        else {
            return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(tabTitles[position]);
    }

    @Override
    public int getCount() {
        // Show total pages.
        return pages;
    }
}