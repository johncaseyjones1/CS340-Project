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
import edu.byu.cs.tweeter.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class PostStatusServiceTest {

    private PostStatusRequest validRequest;
    private PostStatusRequest invalidRequest;

    private PostStatusResponse successResponse;
    private PostStatusResponse failureResponse;

    private PostStatusService postStatusServiceSpy;

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
        validRequest = new PostStatusRequest(currentUser.getAlias(), resultStatus1.getContent(), Calendar.getInstance().getTime());
        invalidRequest = new PostStatusRequest(null, "", null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostStatusResponse("Success!");
        failureResponse = new PostStatusResponse("An exception occurred");
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);

        Mockito.when(mockServerFacade.postStatus(validRequest)).thenReturn(successResponse);
        Mockito.when(mockServerFacade.postStatus(invalidRequest)).thenReturn(failureResponse);


        // Create a FollowersService instance and wrap it with a spy that will use the mock service
        postStatusServiceSpy = Mockito.spy(new PostStatusService());
        Mockito.when(postStatusServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testPostStatus_validRequest_correctResponse() throws IOException {
        PostStatusResponse response = null;
        try {
            response = postStatusServiceSpy.postStatus(validRequest);
        } catch (Exception E) {
            Assertions.fail();
        }
        Assertions.assertEquals(successResponse, response);
    }

}
