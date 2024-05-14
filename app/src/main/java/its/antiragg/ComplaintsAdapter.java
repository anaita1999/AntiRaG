package its.antiragg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ComplaintViewHolder> {
    private final Context mCtx;
    private final List<Complaint> complaintList;
    private FirebaseFirestore db;

    public ComplaintsAdapter(Context mCtx, List<Complaint> complaintList) {
        this.mCtx = mCtx;
        this.complaintList = complaintList;
    }
    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ComplaintViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_complaint, parent, false)
        );
    }

    class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView textViewcname, textViewphone, textViewmail, textViewvname, textViewdetails,options;
        public ComplaintViewHolder(View itemView) {
            super(itemView);
            textViewcname = itemView.findViewById(R.id.textview_cname);
            textViewphone = itemView.findViewById(R.id.textview_phone);
            textViewmail = itemView.findViewById(R.id.textview_mail);
            textViewvname = itemView.findViewById(R.id.textview_vname);
            textViewdetails = itemView.findViewById(R.id.textview_details);
            options = itemView.findViewById(R.id.textview_menu);
            db = FirebaseFirestore.getInstance();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ComplaintViewHolder holder, int position) {
        final Complaint complaint = complaintList.get(position);
        holder.textViewcname.setText(complaint.getComplainant_Name());
        holder.textViewphone.setText(complaint.getPhone_No());
        holder.textViewmail.setText(complaint.getE_Mail());
        holder.textViewvname.setText(complaint.getVictim_Name());
        holder.textViewdetails.setText(complaint.getComplaint_Details());
        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(mCtx, holder.options);
            popupMenu.inflate(R.menu.complaints_processing_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final CollectionReference complaintsRef = db.collection("complaints");

                    int itemId = item.getItemId();

                    if (itemId == R.id.processing) {
                        complaintsRef.whereEqualTo("id", complaint.getId()).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<Object, String> map = new HashMap<>();
                                    map.put("status", "Processing...");
                                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                                }
                            }
                        });

                    } else if (itemId == R.id.solved) {
                        complaintsRef.whereEqualTo("id", complaint.getId()).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<Object, String> map = new HashMap<>();
                                    map.put("status", "Solved !!!");
                                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                                }
                            }
                        });

                    }

                    return false;
                }
            });
            popupMenu.show();
        });
    }
    @Override
    public int getItemCount() {
        return complaintList.size();
    }

}