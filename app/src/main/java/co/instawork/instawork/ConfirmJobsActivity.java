package co.instawork.instawork;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.util.ArrayList;

public class ConfirmJobsActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_jobs);

        populateSelectedJobs();

        final TextView email = (TextView) findViewById(R.id.email);

        Button applyButton = (Button) findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmJobsActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

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
}
