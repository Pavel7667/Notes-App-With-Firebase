package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // create links
    EditText emailEditText;
    EditText passwordEditText;
    Button loginBTN;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set links
        emailEditText = findViewById(R.id.set_email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBTN = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        loginBTN.setOnClickListener(v -> loginUser());
        createAccountBtnTextView.setOnClickListener
                (v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }
        loginAccountInFirebase(email, password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    //login success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Utility.showToast(LoginActivity.this, "Email not verified, Please go to @EMAIl to verify");
                    }
                } else {
                    //login fail
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }


    /**
     * When user click on createAccountBtn connect to Firebase show spinner
     * If no show BTN
     */
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBTN.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBTN.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checking is all field a right form
     */
    boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email not valid");
            return false;
        }
        if (password.length() < 4) {
            passwordEditText.setError("Password to short");
            return false;
        }
        return true;
    }
}