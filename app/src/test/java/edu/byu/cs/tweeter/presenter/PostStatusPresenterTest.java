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
import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.StatusCreatorPresenter;

public class PostStatusPresenterTest {

    private PostStatusRequest request;

    private PostStatusResponse response;

    private PostStatusService mockPostStatusService;
    private StatusCreatorPresenter presenter;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        Status resultStatus1 = new Status("@testUser", "boring content",
                new Vector<>(), new Vector<>(), Calendar.getInstance().getTime());

        // Setup request objects to use in the tests
        request = new PostStatusRequest(currentUser.getAlias(), resultStatus1.getContent(), Calendar.getInstance().getTime());

        // Setup a mock ServerFacade that will return known responses
        response = new PostStatusResponse("Success!");

        mockPostStatusService = Mockito.mock(PostStatusService.class);
        try {
            Mockito.when(mockPostStatusService.postStatus(request)).thenReturn(response);
        } catch (Exception E) {

        }

        // Create a FollowersPresenter instance and wrap it with a spy that will use the mock presenter
        presenter = Mockito.spy(new StatusCreatorPresenter(new StatusCreatorPresenter.View() {}));
        Mockito.when(presenter.getPostStatusService()).thenReturn(mockPostStatusService);
    }

    @Test
    public void testGetFollowers_returnsServiceResult() throws IOException {
        try {
            Mockito.when(mockPostStatusService.postStatus(request)).thenReturn(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        try {
            Assertions.assertEquals(response, presenter.postStatus(request));
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetFollowers_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        try {
            Mockito.when(mockPostStatusService.postStatus(request)).thenThrow(new IOException());
        } catch (Exception e) {
        }

        Assertions.assertThrows(IOException.class, () -> {
            presenter.postStatus(request);
        });
    }

}
