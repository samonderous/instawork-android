package co.instawork.instawork;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "FileChooserExampleActivity";

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
                CharSequence[] items = {"Attach file from disk", "Image from Camera", "Image from Library", "Paste from clipboard"};
                new MaterialDialog.Builder(ProfileActivity.this)
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {
                                    showChooser();
                                } else if (which == 1) {
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 0);
                                } else if (which == 2) {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);
                                } else if (which == 3) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                    if(clipboard.getPrimaryClip() != null) {
                                        String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                                        Toast.makeText(ProfileActivity.this, copiedText, Toast.LENGTH_LONG).show();
                                    }
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

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, "Choose file");
        try {
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 0 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Toast.makeText(ProfileActivity.this, photo.getHeight() + " x " + photo.getWidth(), Toast.LENGTH_LONG).show();
        } else if (reqCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Toast.makeText(ProfileActivity.this,
                    "Image Selected: " + FileUtils.getPath(this, selectedImage), Toast.LENGTH_LONG).show();
        } else if (reqCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected file
                final Uri uri = data.getData();
                try {
                    File resume = new File(uri.getPath());
                    // Get the file path from the URI
                    final String path = FileUtils.getPath(this, uri);
                    Toast.makeText(ProfileActivity.this,
                            "File Selected: " + path, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
