package edu.byu.cs.tweeter.model.net;

import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.status.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.domain.status.Tag;
import edu.byu.cs.tweeter.model.domain.status.URL;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerNumRequest;
import edu.byu.cs.tweeter.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingNumRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerNumResponse;
import edu.byu.cs.tweeter.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingNumResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.util.ExtractTagsAndLinksUtil;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {


    // This is the hard coded followee data returned by the 'getFollowees()' method

    FakeDatabase db;

    public ServerFacade() {
        db = FakeDatabase.getInstance();
        //just making temp users and filing them up for the front end so I have things to add and
        //remove from


    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation doesn't check for passwords as those will be implemented when the
     * backend/server is created.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request) throws IOException {
        if (request.getUsername() == null) {
            throw new IOException("Username was null");
        }
        System.out.println("calling login");
        Vector<User> usersInDb = db.getUsers();

        LoginResponse lr = new LoginResponse("Could not find " + request.getUsername() + " in the database.");

        User activeUser = null;
        for(User user : usersInDb) {
            if (user.getAlias().equals(request.getUsername()) || user.getAlias().substring(1).equals(request.getUsername())) {
                System.out.println("Found the user");
                activeUser = user;
                lr = new LoginResponse(user, new AuthToken());
            }
        }

        if (activeUser == null) {
            throw new IOException("Username was null");
        }

        System.out.println("About to return the user and he isn't null");
        return lr;
    }

    public RegisterResponse register(RegisterRequest request) {
        User newUser = new User(request.getFirstName(), request.getLastName(), request.getUsername(),
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        for (User user : db.getUsers()) {
            if (user.getAlias().equals(newUser.getAlias())) {
                return new RegisterResponse("Failed to add user (user with username " + newUser.getAlias() + " already exists)");
            }
        }

        if (newUser.getAlias().charAt(0) != '@') {
            newUser.setAlias("@".concat(newUser.getAlias()));
        }
        db.addUser(newUser);
        return new RegisterResponse(newUser, new AuthToken());
    }

    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse(true, "Logout successful");
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the following response.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollowerAlias() == null) {
                throw new AssertionError();
            }
        }


        //Here, when the server actually exists, we will be accessing the followees of a specific user, not just a generic list
        List<User> allFollowees = getDummyFollowees(request.getFollowerAlias());
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

            for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                responseFollowees.add(allFollowees.get(followeesIndex));
            }

            hasMorePages = followeesIndex < allFollowees.size();
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees(String userAlias) {
        System.out.println("There were " + db.getUsers().size() + " users in the database");
        //If we're getting the followees of anyone but the active user
        for (User dummyUser : db.getUsers()) {
            if (dummyUser.getAlias().equals(userAlias)) {
                System.out.println("Found user "  + dummyUser.getAlias());
                Vector<User> followees = new Vector<>();
                for (Integer index : dummyUser.getFollowing()) {
                    followees.add(db.getUsers().elementAt(index));
                }
                return followees;
            }
        }
        //else
        return db.getUsers();
    }

    /**
     * Returns the users that the user specified in the request is followers. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers response.
     */
    public FollowersResponse getFollowers(FollowersRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFolloweeAlias() == null) {
                throw new AssertionError();
            }
        }

        List<User> allFollowers = getDummyFollowers(request.getFolloweeAlias());
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

            for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                responseFollowers.add(allFollowers.get(followersIndex));
            }

            hasMorePages = followersIndex < allFollowers.size();
        }

        return new FollowersResponse(responseFollowers, hasMorePages);
    }


    /**
     * Determines the index for the first followee in the specified 'allFollowers' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowers the generated list of followers from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFollowersStartingIndex(String lastFolloweeAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }

        return followersIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followers.
     *
     * @return the followers.
     */
    List<User> getDummyFollowers(String userAlias) {
        //If we're getting the followers of anyone but the active user
        for (User dummyUser : db.getUsers()) {
            if (dummyUser.getAlias().equals(userAlias)) {
                Vector<User> followers = new Vector<>();
                for (Integer index : dummyUser.getFollowers()) {
                    followers.add(db.getUsers().elementAt(index));
                }
                return followers;
            }
        }
        //else
        return db.getUsers();
    }

    /**
     * Follows the user and returns whether or not the follow request was successful.
     * @return success/failure message
     */
    public FollowResponse follow(FollowRequest request) {
        for (int i = 0; i < db.getUsers().size(); i++) {
            if (db.getUsers().elementAt(i).getAlias().equals(request.getUserToFollowAlias())) {
                System.out.println("Found alias of user to follow");
                System.out.println("There are " + db.getUsers().size() + " users in the database");
                for (int j = 0; j < db.getUsers().size(); j++) {
                    System.out.println(db.getUsers().elementAt(j).getAlias() + " comparing with " + request.getActiveUserAlias());
                    if (db.getUsers().elementAt(j).getAlias().equals(request.getActiveUserAlias())) {
                        // If already following
                        if (db.getUsers().elementAt(i).getFollowers().contains(j)) {
                            System.out.println(db.getUsers().elementAt(j).getAlias() + " is unfollowing " + db.getUsers().elementAt(i).getAlias());
                            db.getUsers().elementAt(i).getFollowers().removeElement(j);
                            db.getUsers().elementAt(j).getFollowing().removeElement(i);
                            return new FollowResponse("Successfully unfollowed " + db.getUsers().elementAt(i).getAlias());
                        }
                        db.getUsers().elementAt(i).getFollowers().add(j);
                        db.getUsers().elementAt(j).getFollowing().add(i);
                        return new FollowResponse("Successfully followed " + db.getUsers().elementAt(i).getAlias());
                    }
                }
            }
        }
        return new FollowResponse("Failed to follow user (you are not in the list of users)");
    }

    /**
     * Posts a status to the fake database for the active user.
     * @return success/failure message
     */
    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException {
        for (User user : db.getUsers()) {
            System.out.println("comparing user " + request.getUserAlias() + " with user " + user.getAlias());
            if (user.getAlias().equals(request.getUserAlias())) {
                Pair<Vector<Tag>, Vector<URL>> tagsAndLinks = ExtractTagsAndLinksUtil.parseContent(request.getContent());
                System.out.println("TAGS:");
                if (tagsAndLinks != null && tagsAndLinks.first != null && tagsAndLinks.second != null) {
                    for (Tag tag : tagsAndLinks.first) {
                        if (tag.getUser() == null) {
                            System.out.println("Can't find user!");
                            throw new IOException("Sorry, the user you have tagged does not exist!");
                        }
                        System.out.println(tag.getUser().getAlias() + " starts at " + tag.getPositionInString() + " and is " + tag.getLength() + " long.");
                    }

                    System.out.println("LINKS:");
                    for (URL link : tagsAndLinks.second) {
                        System.out.println(link.getUrlText() + " starts at " + link.getPositionInString() + " and is " + link.getLength() + " long.");
                    }
                }
                user.getStatuses().add(new Status(request.getUserAlias(), request.getContent(), tagsAndLinks.first, tagsAndLinks.second, request.getTimeStamp()));
                return new PostStatusResponse("Success!");
            }
        }

        throw new IOException("Failed! Can't find user");
    }

    public StatusResponse getStory(StatusRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUserAlias() == null) {
                throw new AssertionError();
            }
        }


        List<Status> allStatuses = new ArrayList<>();
        for (User user : db.getUsers()) {
            if (user.getAlias().equals(request.getUserAlias())) {
                allStatuses = user.getStatuses();
            }
        }

        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            int statusesIndex = getStatusesStartingIndex(request.getLastStatusContent(), request.getUserAlias(), allStatuses);

            for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }


    /**
     * Determines the index for the first followee in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastContent the string of the last content
     * @param lastContent the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getStatusesStartingIndex(String lastContent, String lastAlias, List<Status> allStatuses) {

        int statusesIndex = 0;

        if(lastContent != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastContent.equals(allStatuses.get(i).getContent()) && lastAlias.equals(allStatuses.get(i).getUserAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    public StatusResponse getFeed(StatusRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUserAlias() == null) {
                throw new AssertionError();
            }
        }


        List<Status> allStatuses = new ArrayList<>();
        for (User user : db.getUsers()) {
            if (user.getAlias().equals(request.getUserAlias())) {
                System.out.println("user alias is " + user.getAlias());
                for (int followeeIndex : user.getFollowing()) {
                    allStatuses.addAll(db.getUsers().elementAt(followeeIndex).getStatuses());
                }
            }
        }

        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            int statusesIndex = getStatusesStartingIndex(request.getLastStatusContent(), request.getLastUserAlias(), allStatuses);

            for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                responseStatuses.add(allStatuses.get(statusesIndex));
            }

            hasMorePages = statusesIndex < allStatuses.size();
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    public FollowerNumResponse getFollowerNum(FollowerNumRequest request) throws IOException {
        for (User user : db.getUsers()) {
            if (user.getAlias().equals(request.getUserAlias())) {
                return new FollowerNumResponse(user.getFollowers().size());
            }
        }
        throw new IOException("User not found");
    }

    public FollowingNumResponse getFollowingNum(FollowingNumRequest request) throws IOException {
        for (User user : db.getUsers()) {
            if (user.getAlias().equals(request.getUserAlias())) {
                return new FollowingNumResponse(user.getFollowing().size());
            }
        }
        throw new IOException("User not found");
    }

}
