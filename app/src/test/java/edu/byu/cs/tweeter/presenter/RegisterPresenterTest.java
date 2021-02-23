package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;

public class RegisterPresenterTest {

    private RegisterRequest request;

    private RegisterResponse response;

    private RegisterPresenter presenter;

    private RegisterService mockRegisterService;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "newAlias",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        // Passwords aren't implemented yet
        request = new RegisterRequest(currentUser.getAlias(), "1234",
                currentUser.getFirstName(), currentUser.getLastName());

        // Setup a mock ServerFacade that will return known responses
        response = new RegisterResponse(currentUser, new AuthToken());

        mockRegisterService = Mockito.mock(RegisterService.class);

        try {
            Mockito.when(mockRegisterService.register(request)).thenReturn(response);
        } catch (IOException e) {

        }

        // Create a RegisterersPresenter instance and wrap it with a spy that will use the mock presenter
        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException {
        Mockito.when(presenter.register(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.register(request));
    }

}
