package its.antiragg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter {
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Contact> contacts;

    public ContactAdapter(@NonNull Context context, int resource, List<Contact> contacts) {
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact currentContact = contacts.get(position);

        viewHolder.name.setText(currentContact.getName());
        viewHolder.phno.setText(currentContact.getPhno());

        return convertView;
    }
    private class ViewHolder{
        final TextView name;
        final TextView phno;

        ViewHolder(View v){
            this.name = v.findViewById(R.id.member_name);
            this.phno = v.findViewById(R.id.ph_no);
        }

    }

}
