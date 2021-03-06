package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.domain.status.Status;
import edu.byu.cs.tweeter.model.domain.status.Tag;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.presenter.status.FeedPresenter;

public class FeedPresenterTest {

    private StatusRequest request;

    private StatusResponse response;

    private FeedPresenter presenter;
    private FeedService mockFeedService;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);
        User resultUser1 = new User("FirstName1", "LastName1",
                "alias1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "alias2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "alias3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Vector<Tag> tags = new Vector<>();
        tags.add(new Tag(resultUser2, 0, 7));
        Status resultStatus1 = new Status("alias1", "@alias2",
                tags, new Vector<>(), Calendar.getInstance().getTime());
        tags.clear();
        tags.add(new Tag(resultUser3, 0, 7));
        Status resultStatus2 = new Status("alias2", "@alias3",
                tags, new Vector<>(), Calendar.getInstance().getTime());
        tags.clear();
        tags.add(new Tag(resultUser1, 0, 7));
        Status resultStatus3 = new Status("alias3", "@alias1",
                tags, new Vector<>(), Calendar.getInstance().getTime());
        // Setup request objects to use in the tests
        request = new StatusRequest(currentUser.getAlias(), 3, null, null);

        // Setup a mock ServerFacade that will return known responses
        response = new StatusResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        mockFeedService = Mockito.mock(FeedService.class);

        Mockito.when(mockFeedService.getStatuses(request)).thenReturn(response);


        // Create a FollowersPresenter instance and wrap it with a spy that will use the mock presenter
        presenter = Mockito.spy(new FeedPresenter(new FeedPresenter.View() {}));
        Mockito.when(presenter.getFeedService()).thenReturn(mockFeedService);
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException {
        Mockito.when(mockFeedService.getStatuses(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.getStatuses(request));
    }

    @Test
    public void testGetFollowers_invalidRequest() throws IOException {
        Mockito.when(mockFeedService.getStatuses(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStatuses(request);
        });
    }
}
