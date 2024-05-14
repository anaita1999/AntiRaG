package its.antiragg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class registeration_form extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName, editTextEmail, editTextPassword, editTextRePassword, editTextPhone;
    private Button mbutton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_form);

        editTextName = findViewById(R.id.etname);
        editTextEmail = findViewById(R.id.etmail);
        editTextPassword = findViewById(R.id.etpass);
        editTextRePassword = findViewById(R.id.etrepass);
        editTextPhone = findViewById(R.id.etphone);

        mAuth = FirebaseAuth.getInstance();

        mbutton = findViewById(R.id.btnsignup);
        mbutton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
            finish();
            startActivity(new Intent(registeration_form.this, MainActivity.class));

        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repassword = editTextRePassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(registeration_form.this);
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        if (name.isEmpty()) {
            progressDialog.cancel();
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            progressDialog.cancel();
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressDialog.cancel();
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            progressDialog.cancel();
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            progressDialog.cancel();
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (repassword.isEmpty()) {
            progressDialog.cancel();
            editTextRePassword.setError(getString(R.string.input_error_password));
            editTextRePassword.requestFocus();
            return;
        }

        if (repassword.length() < 6) {
            progressDialog.cancel();
            editTextRePassword.setError(getString(R.string.input_error_password_length));
            editTextRePassword.requestFocus();
            return;
        }

        if (!repassword.equals(password)) {
            progressDialog.cancel();
            editTextRePassword.setError("Password Doesn't Matches");
            editTextRePassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            progressDialog.cancel();
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            progressDialog.cancel();
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(
                                    name,
                                    phone,
                                    email
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(
                                            FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.cancel();
                                        Toast.makeText(registeration_form.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {

                                    }
                                }
                            });

                        } else {
                            progressDialog.cancel();
                            Toast.makeText(registeration_form.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == mbutton)
        {
            registerUser();
        }
    }
}

