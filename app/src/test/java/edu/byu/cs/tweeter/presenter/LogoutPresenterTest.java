package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenterTest {

    private LogoutRequest request;

    private LogoutResponse response;

    private LogoutPresenter presenter;

    private LogoutService mockLogoutService;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "testUser",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        // Passwords aren't implemented yet
        request = new LogoutRequest(currentUser.getAlias());

        // Setup a mock ServerFacade that will return known responses
        response = new LogoutResponse(true, "Success!");
        mockLogoutService = Mockito.mock(LogoutService.class);
        try {
            Mockito.when(mockLogoutService.logout(request)).thenReturn(response);
        } catch (IOException e) {

        }

        // Create a LogoutersPresenter instance and wrap it with a spy that will use the mock presenter
        presenter = Mockito.spy(new LogoutPresenter(new LogoutPresenter.View() {
        }));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException {
        Mockito.when(presenter.logout(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.logout(request));
    }
}
