package its.antiragg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ComplaintActivity extends AppCompatActivity {

    private ComplaintsAdapter adapter;
    private List<Complaint> complaintsList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        progressBar = findViewById(R.id.progressbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_products);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        complaintsList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("complaints").orderBy("id").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<Complaint> types = queryDocumentSnapshots.toObjects(Complaint.class);
                            complaintsList.addAll(types);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        adapter = new ComplaintsAdapter(this, complaintsList);
        recyclerView.setAdapter(adapter);
    }
}
