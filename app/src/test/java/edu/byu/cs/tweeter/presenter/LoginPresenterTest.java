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
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginPresenterTest {

    private LoginRequest request;

    private LoginResponse response;

    private LoginPresenter presenter;

    private LoginService mockLoginService;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "testUser",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        // Passwords aren't implemented yet
        request = new LoginRequest(currentUser.getAlias(), "1234");

        // Setup a mock ServerFacade that will return known responses
        response = new LoginResponse(currentUser, new AuthToken());
        mockLoginService = Mockito.mock(LoginService.class);
        try {
            Mockito.when(mockLoginService.login(request)).thenReturn(response);
        } catch (IOException e) {

        }

        // Create a LoginersPresenter instance and wrap it with a spy that will use the mock presenter
        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginService);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException {
        Mockito.when(presenter.login(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.login(request));
    }
}
