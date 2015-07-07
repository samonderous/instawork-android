package co.instawork.instawork;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

public class ProfileActivity extends AppCompatActivity {

    static final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText phone = (EditText) findViewById(R.id.phone);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button choose = (Button) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = {"Attach file from disk", "Image from Camera", "Image from Library"};
                new MaterialDialog.Builder(ProfileActivity.this)
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 4);
                                } else if(which == 1){
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 4);
                                } else if(which == 2) {

                                }
                            }
                        })
                        .show();
            }
        });

        Button references = (Button) findViewById(R.id.references);
        references.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
//                if (resultCode == Activity.RESULT_OK) {
//
//                    Uri contactData = data.getData();
//                    Cursor c = managedQuery(contactData, null, null, null, null);
//                    if (c.moveToFirst()) {
//
//
//                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//
//                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//                        if (hasPhone.equalsIgnoreCase("1")) {
//                            Cursor phones = getContentResolver().query(
//                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
//                                    null, null);
//                            phones.moveToFirst();
//                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
//                            System.out.println("number is:" + cNumber);
//                        }
//                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                        System.out.println(name);
//                    }
//                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
