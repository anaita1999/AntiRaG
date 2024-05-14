package its.antiragg;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class compor extends AppCompatActivity implements View.OnClickListener {

    private EditText Cnaam, Phone, Mail, Vnaam, Complaints;
    private Button Submit;
    private FirebaseFirestore db;
    private static final String ID_KEY = "id";
    private static final String TAG = "comporshi";
    private static final int[] id= new int[1];
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private String flag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compor);

        db = FirebaseFirestore.getInstance();

        Cnaam = (EditText)findViewById(R.id.cnaam);
        Phone = (EditText)findViewById(R.id.phn);
        Mail = (EditText)findViewById(R.id.mail);
        Vnaam = (EditText)findViewById(R.id.vnaam);
        Complaints = (EditText)findViewById(R.id.comtext);
        Submit = (Button) findViewById(R.id.smt);

        Submit.setOnClickListener(this);
        sentPI = PendingIntent.getBroadcast(compor.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(compor.this, 0, new Intent(DELIVERED), 0);


    }

    ProgressDialog progressDialog;

    private boolean hasValidationErrors(String cnaam, String phn, String mail, String vnaam, String comtext) {



        if (cnaam.isEmpty()) {
            Cnaam.setError("Complainant Name required");
            Cnaam.requestFocus();
            return true;
        }
        if (phn.isEmpty()) {
            progressDialog.cancel();
            Phone.setError("Phone required");
            Phone.requestFocus();
            return true;
        }
        if (mail.isEmpty()) {
            progressDialog.cancel();
            Mail.setError("E-mail required");
            Mail.requestFocus();
            return true;
        }
        if (vnaam.isEmpty()) {
            progressDialog.cancel();
            Vnaam.setError("Victim Name required");
            Vnaam.requestFocus();
            return true;
        }
        if (comtext.isEmpty()) {
            progressDialog.cancel();
            Complaints.setError("Fill Complaint Details");
            Complaints.requestFocus();
            return true;
        }
        return false;
    }

    private void saveComplaint(){

        progressDialog = new ProgressDialog(compor.this);
        progressDialog.setMessage("Submitting..");
        progressDialog.show();
        String Complainant_Name = Cnaam.getText().toString().trim();
        String Phone_No = Phone.getText().toString().trim();
        String E_Mail = Mail.getText().toString().trim();
        String Victim_Name = Vnaam.getText().toString().trim();
        String Complaint_Details = Complaints.getText().toString().trim();
       final int[] id = new int[1];

//Log.d("saveComplaint: retireve " + id[0], TAG);



        if (!hasValidationErrors(Complainant_Name, Phone_No, E_Mail, Victim_Name, Complaint_Details)) {
            final Complaint complaint = new Complaint(
                    Complainant_Name,
                    Phone_No,
                    E_Mail,
                    Victim_Name,
                    Complaint_Details,
                    id[0]

            );
            flag = Phone_No;
            db.collection("ids").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<Id> types = queryDocumentSnapshots.toObjects(Id.class);
                        id[0] = types.get(0).getId();
                        complaint.setId(id[0]+1);
                        complaint.setStatus("Received");

                        CollectionReference dbProducts = db.collection("complaints");
                        dbProducts.add(complaint)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.cancel();
                                        Toast.makeText(compor.this, "Complaint Submit", Toast.LENGTH_LONG).show();
                                        DocumentReference complaintId = db.collection("ids").document("x4ReZZwHAWAT2hvCyOgj");
//
                                        complaintId.update(ID_KEY, id[0]+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
//
                                                String message = "We recieved your complaint. Your complaint id is ";
                                                message += Integer.toString(id[0]+1);
                                                message += " .";
                                                String telNr = flag;

                                                //============================================================================================
                                                if (ContextCompat.checkSelfPermission(compor.this, Manifest.permission.SEND_SMS)
                                                        != PackageManager.PERMISSION_GRANTED)
                                                {
                                                    ActivityCompat.requestPermissions(compor.this, new String [] {Manifest.permission.SEND_SMS},
                                                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                                                }
                                                else
                                                {
                                                    SmsManager sms = SmsManager.getDefault();
                                                    sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);
                                                }

                                                finish();
                                            }
                                        });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.cancel();
                                        Toast.makeText(compor.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.smt) {
            saveComplaint();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //The deliveredPI PendingIntent does not fire in the Android emulator.
        //You have to test the application on a real device to view it.
        //However, the sentPI PendingIntent works on both, the emulator as well as on a real device.

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        //Toast.makeText(context, "Location sent successfully!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        //Toast.makeText(context, "Location delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        //Toast.makeText(context, "Location not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //register the BroadCastReceivers to listen for a specific broadcast
        //if they "hear" that broadcast, it will activate their onReceive() method
        registerReceiver(smsSentReceiver, new IntentFilter(SENT), Context.RECEIVER_NOT_EXPORTED);
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
}
