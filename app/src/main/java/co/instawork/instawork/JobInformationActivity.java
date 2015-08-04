package co.instawork.instawork;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.model.Marker;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import co.instawork.classes.SelectedContact;

public class JobInformationActivity extends AppCompatActivity {

    private static final String TAG = "FileChooserExampleActivity";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_information);

        Button choose = (Button) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = {"Attach file from disk", "Image from Camera", "Image from Library", "Paste from clipboard"};
                new MaterialDialog.Builder(JobInformationActivity.this)
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
                                    if (clipboard.getPrimaryClip() != null) {
                                        String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                                        Toast.makeText(JobInformationActivity.this, copiedText, Toast.LENGTH_LONG).show();
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
                displayContacts();
            }
        });

        Button continueButton = (Button) findViewById(R.id.cont);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(JobInformationActivity.this, ConfirmationActivity.class);
                startActivity(i);
            }
        });

        TextView otherRef = (TextView) findViewById(R.id.other_ref);
        otherRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogWrapper.Builder dialog = new AlertDialogWrapper.Builder(context);
                dialog.setTitle("Add other reference");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (12 * scale + 0.5f);

                final EditText nameBox = new EditText(context);
                nameBox.setHint("Name");
                nameBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(dpAsPixels, 0, dpAsPixels, 0);
                nameBox.setLayoutParams(params);
                layout.addView(nameBox);

                final EditText phoneBox = new EditText(context);
                phoneBox.setHint("Phone Number");
                phoneBox.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                phoneBox.setInputType(InputType.TYPE_CLASS_PHONE);
                phoneBox.setLayoutParams(params);
                layout.addView(phoneBox);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Reference added: " + nameBox.getText().toString() + ", " + phoneBox.getText().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                dialog.setView(layout);
                dialog.show();
            }
        });


        populateSelectedJobs();

        final TextView email = (TextView) findViewById(R.id.email);

        ImageButton editEmail = (ImageButton) findViewById(R.id.edit_email);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogWrapper.Builder dialog = new AlertDialogWrapper.Builder(context);
                dialog.setTitle("Edit email");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (12 * scale + 0.5f);

                final EditText emailBox = new EditText(context);
                emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(dpAsPixels, 0, dpAsPixels, 0);
                emailBox.setLayoutParams(params);
                layout.addView(emailBox);
                emailBox.setText(email.getText().toString());
                emailBox.setSingleLine(false);
                emailBox.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                emailBox.setSelection(emailBox.getText().length());

                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        email.setText(emailBox.getText().toString());
                        dialogInterface.cancel();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                dialog.setView(layout);
                dialog.show();
            }
        });
    }

    private void displayContacts() {
        Intent i = new Intent(JobInformationActivity.this, ContactsActivity.class);
        startActivityForResult(i, 3);
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
            Toast.makeText(JobInformationActivity.this, photo.getHeight() + " x " + photo.getWidth(), Toast.LENGTH_LONG).show();
        } else if (reqCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Toast.makeText(JobInformationActivity.this,
                    "Image Selected: " + FileUtils.getPath(this, selectedImage), Toast.LENGTH_LONG).show();
        } else if (reqCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected file
                final Uri uri = data.getData();
                try {
                    File resume = new File(uri.getPath());
                    // Get the file path from the URI
                    final String path = FileUtils.getPath(this, uri);
                    Toast.makeText(JobInformationActivity.this,
                            "File Selected: " + path, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (reqCode == 3 && resultCode == RESULT_OK) {
            System.out.println((HashMap<Integer, SelectedContact>) data.getSerializableExtra("selected_names"));
        }
    }

    private void populateSelectedJobs() {
        final ListView list = (ListView) findViewById(R.id.list_view);

        ArrayList<String> jobTitles = new ArrayList<>();
        for(Marker m : MainActivity.selectedMarkers) {
            try {
                jobTitles.add(MainActivity.data.get(m).getString("title") + " - " + MainActivity.data.get(m).getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, jobTitles);
        list.setAdapter(adapter);
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
