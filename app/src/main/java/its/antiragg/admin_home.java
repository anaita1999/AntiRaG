package its.antiragg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class admin_home extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button button = findViewById(R.id.comp);
        Button logout = findViewById(R.id.lout);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(admin_home.this, ComplaintActivity.class);
            startActivity(intent);
        });
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(admin_home.this, MainActivity.class));
        });
    }
}
