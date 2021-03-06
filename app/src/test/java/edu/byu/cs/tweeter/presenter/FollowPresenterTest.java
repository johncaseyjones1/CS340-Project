package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowPresenterTest {

    private FollowRequest request;

    private FollowResponse response;

    private FollowPresenter presenter;

    private FollowService mockFollowService;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");


        request = new FollowRequest(currentUser.getAlias(), resultUser1.getAlias());
        response = new FollowResponse("Followed user");

        mockFollowService = Mockito.mock(FollowService.class);


        Mockito.when(mockFollowService.follow(request)).thenReturn(response);



        presenter = Mockito.spy(new FollowPresenter(new FollowPresenter.View() {}));
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowService);
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException {
        Mockito.when(presenter.follow(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.follow(request));
    }


}
