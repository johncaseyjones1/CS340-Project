package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

/**
 * Contains the business logic to support the login operation.
 */
public class LoginService {

    public LoginResponse login(LoginRequest request) throws IOException {
        ServerFacade serverFacade = getServerFacade();
        LoginResponse loginResponse;
        loginResponse = serverFacade.login(request);
        System.out.println("about to do the image before i send my login response");
        if(loginResponse.isSuccess()) {
            loadImage(loginResponse.getUser());
        }

        System.out.println("about to send my login response");
        return loginResponse;
    }

    /**
     * Loads the profile image data for the user.
     *
     * @param user the user whose profile image data is to be loaded.
     */
    private void loadImage(User user) throws IOException {
        System.out.println("about to load image as a byte array");
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        System.out.println("about to set the user iamge as the byte array");
        user.setImageBytes(bytes);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        //return ServerFacade.getInstance();
        return new ServerFacade();
    }
}
