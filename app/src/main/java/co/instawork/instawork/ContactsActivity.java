package co.instawork.instawork;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import co.instawork.adapters.ContactNamesAdapter;

public class ContactsActivity extends AppCompatActivity {

    Handler handler = new Handler();
    private ContactNamesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading contacts...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Set<String> contactNames = new HashSet<>();
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                contactNames.add(name);
                            }
                            pCur.close();
                        }
                    }
                }

                final String[] array = contactNames.toArray(new String[contactNames.size()]);
                Arrays.sort(array);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();

                        RecyclerView contactsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                        adapter = new ContactNamesAdapter(ContactsActivity.this, array);
                        contactsRecyclerView.setAdapter(adapter);
                        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));
                    }
                });
            }
        }).start();

        Button finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("selected_names", adapter.getSelectedNames());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
