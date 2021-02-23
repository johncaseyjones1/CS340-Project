package edu.byu.cs.tweeter.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

/**
 * Contains the minimum UI required to allow the user to register with a hard-coded user. Most or all
 * of this should be replaced when the back-end is implemented.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.View, RegisterTask.Observer {

    private static final String REG_TAG = "RegisterActivity";

    private RegisterPresenter presenter;
    private Toast registerInToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter(this);

        EditText userNameInput = findViewById(R.id.userNameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);

        Button registerButton = findViewById(R.id.registerButtonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Makes a register request. The user is hard-coded, so it doesn't matter what data we put
             * in the RegisterRequest object.
             *
             * @param view the view object that was clicked.
             */
            @Override
            public void onClick(View view) {
                registerInToast = Toast.makeText(RegisterActivity.this, "Logging In", Toast.LENGTH_LONG);
                registerInToast.show();

                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                String userName = userNameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String firstName = firstNameInput.getText().toString();
                String lastName = lastNameInput.getText().toString();
                RegisterRequest registerRequest = new RegisterRequest(userName, password, firstName, lastName);
                RegisterTask registerTask = new RegisterTask(presenter, RegisterActivity.this);
                registerTask.execute(registerRequest);
            }
        });
    }

    /**
     * The callback method that gets invoked for a successful register. Displays the MainActivity.
     *
     * @param registerResponse the response from the register request.
     */
    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());

        registerInToast.cancel();
        startActivity(intent);
    }

    /**
     * The callback method that gets invoked for an unsuccessful register. Displays a toast with a
     * message indicating why the register failed.
     *
     * @param registerResponse the response from the register request.
     */
    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(this, "Failed to register. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * A callback indicating that an exception was thrown in an asynchronous method called on the
     * presenter.
     *
     * @param exception the exception.
     */
    @Override
    public void handleException(Exception exception) {
        Log.e(REG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to register because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
