package its.antiragg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button Register;
    private Button Login;
    private Button Forgot;
    private EditText email;
    private EditText password;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        if(mauth.getCurrentUser()!= null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), home.class));
        }

        Register = (Button)findViewById(R.id.register);
        Login = (Button)findViewById(R.id.login);
        Forgot = (Button)findViewById(R.id.forgot);
        email = (EditText)findViewById(R.id.email_field);
        password = (EditText)findViewById(R.id.password_field);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registeration_form.class);
                startActivity(intent);
            }
        });

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, recovery.class);
                startActivity(intent2);
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().equals("admin@gmail.com") )
                {if(password.getText().toString().equals("admin"))
                {
                    Intent intent4 = new Intent(MainActivity.this, admin_home.class);
                    startActivity(intent4);
                }}
                else {
                    userLogin();
//                    Intent intent3 = new Intent(MainActivity.this, home.class);
//                    startActivity(intent3);
                }
            }
        });

    }

    private void userLogin(){

        //getting email and password from edit texts
        String email1 = email.getText().toString().trim();
        String password1  = password.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        if (email1.isEmpty()) {
            progressDialog.cancel();
            email.setError(getString(R.string.input_error_email));
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            progressDialog.cancel();
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }

        if (password1.isEmpty()) {
            progressDialog.cancel();
            password.setError(getString(R.string.input_error_password));
            password.requestFocus();
            return;
        }

        if (password1.length() < 6) {
            progressDialog.cancel();
            password.setError(getString(R.string.input_error_password_length));
            password.requestFocus();
            return;
        }

        mauth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.cancel();
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(MainActivity.this, home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
