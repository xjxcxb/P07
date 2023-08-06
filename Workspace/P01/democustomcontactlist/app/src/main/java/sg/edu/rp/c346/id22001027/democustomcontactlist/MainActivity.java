package sg.edu.rp.c346.id22001027.democustomcontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvContact;
    ArrayList<Contact> alContactList;
    CustomAdapter caContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = findViewById(R.id.listViewContacts);
        alContactList = new ArrayList<Contact>();
        sg.edu.rp.c346.id22001027.democustomcontactlist.Contact item1 = new sg.edu.rp.c346.id22001027.democustomcontactlist.Contact("Mary", 65, 1234567, 'F');
        sg.edu.rp.c346.id22001027.democustomcontactlist.Contact item2 = new sg.edu.rp.c346.id22001027.democustomcontactlist.Contact("Ken", 65, 7654321, 'M');
        alContactList.add(item1);
        alContactList.add(item2);

        caContact = new CustomAdapter(this, R.layout.row, alContactList);
        lvContact.setAdapter(caContact);

    }
}