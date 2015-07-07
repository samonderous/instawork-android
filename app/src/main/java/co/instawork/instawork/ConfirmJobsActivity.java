package co.instawork.instawork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.util.ArrayList;

public class ConfirmJobsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_jobs);

        populateSelectedJobs();

        Button applyButton = (Button) findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmJobsActivity.this, ProfileActivity.class);
                startActivity(i);
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
