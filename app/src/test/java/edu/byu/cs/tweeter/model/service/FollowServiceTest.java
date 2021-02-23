package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowServiceTest {

    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;

    private FollowService followServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(currentUser.getAlias(), resultUser1.getAlias());
        invalidRequest = new FollowRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse("Followed user");
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.follow(validRequest)).thenReturn(successResponse);

        // Create a FollowersService instance and wrap it with a spy that will use the mock service
        followServiceSpy = Mockito.spy(new FollowService());
        Mockito.when(followServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException {
        FollowResponse response = followServiceSpy.follow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    // Doesn't actually return on failure (will instead throw and exception) so just checking to see
    // that the response is not equal to a successful response
    @Test
    public void testFollow_invalidRequest_returnsNoFollowees() throws IOException {
        FollowResponse response = followServiceSpy.follow(invalidRequest);
        Assertions.assertNotEquals(successResponse, response);
    }
}
