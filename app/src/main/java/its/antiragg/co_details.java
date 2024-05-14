package its.antiragg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class co_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_details);

        ListView listView = (ListView) findViewById(R.id.co_list);
        ArrayList<Contact>contacts = new ArrayList<>();

        contacts.add(new Contact("Prof. (Dr.) Suranjan Das (Chairman)","9830202966"));
        contacts.add(new Contact("Prof. Shauvik Roy Chowdhury (Member)","9073364738"));
        contacts.add(new Contact("Prof. (Dr.) Anand Prakash (Member)","9801637657"));
        contacts.add(new Contact("Prof. (Dr.) Sujoy Bhattacharya (Member)","9618062749"));
        contacts.add(new Contact("Mr. Sanjoy Biswas (SubInspector,Duttapukur)","9836355605"));
        contacts.add(new Contact("Ms. Soma Sinha (Member)","7443997964"));
        contacts.add(new Contact("Prof. (Dr.) Souvik Roy (Member)","8473805758"));

        ContactAdapter contactAdapter = new ContactAdapter(co_details.this, R.layout.contact_item, contacts);
        listView.setAdapter(contactAdapter);


    }
}
