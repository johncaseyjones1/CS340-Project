package edu.byu.cs.tweeter.model.service;

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
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class StoryServiceTest {

    private StatusRequest validRequest;
    private StatusRequest invalidRequest;

    private StatusResponse successResponse;
    private StatusResponse failureResponse;

    private StoryService storyServiceSpy;

    @BeforeEach
    public void setup() {
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
        validRequest = new StatusRequest(currentUser.getAlias(), 3, null, null);
        invalidRequest = new StatusRequest(null, 0, null,null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new StatusResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStory(validRequest)).thenReturn(successResponse);

        failureResponse = new StatusResponse("An exception occurred");
        Mockito.when(mockServerFacade.getStory(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowersService instance and wrap it with a spy that will use the mock service
        storyServiceSpy = Mockito.spy(new StoryService());
        Mockito.when(storyServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException {
        StatusResponse response = storyServiceSpy.getStatuses(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that for failed requests the {@link FeedService#getStatuses (StatusRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_invalidRequest_returnsNoFollowerss() throws IOException {
        StatusResponse response = storyServiceSpy.getStatuses(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
