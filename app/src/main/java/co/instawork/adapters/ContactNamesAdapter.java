package co.instawork.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import co.instawork.classes.SelectedContact;
import co.instawork.instawork.R;
import co.instawork.views.LetterTileProvider;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ankitgoyal on 7/8/15.
 */
public class ContactNamesAdapter extends
        RecyclerView.Adapter<ContactNamesAdapter.ViewHolder> {

    public static HashMap<Integer, SelectedContact> selectedNames = new HashMap<>();

    private static String[] contacts;
    private static Activity activity;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView name;
        public CircleImageView image;
        public LinearLayout layout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.image = (CircleImageView) itemView.findViewById(R.id.letter_tile);
            this.layout = (LinearLayout) itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if (((ColorDrawable) view.getBackground()).getColor() == activity.getResources().getColor(R.color.gray)) {
                selectedNames.remove(getLayoutPosition());
                view.setBackgroundColor(activity.getResources().getColor(R.color.background_window));
            } else {
                CharSequence[] items = {"Friend/family", "Coworker", "Manager", "Owner", "Other"};
                new MaterialDialog.Builder(activity)
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View v, int which, CharSequence text) {
                                selectedNames.put(getLayoutPosition(),
                                        new SelectedContact(name.getText().toString(), which, getLayoutPosition()));
                                view.setBackgroundColor(activity.getResources().getColor(R.color.gray));
                            }
                        })
                        .show();
            }
        }
    }

    // Pass in the users array into the constructor
    public ContactNamesAdapter(Activity activity, String[] contacts) {
        this.contacts = contacts;
        this.activity = activity;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ContactNamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.contact_list_item, parent, false);
        // Return a new holder instance
        return new ContactNamesAdapter.ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactNamesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        String name = contacts[position];
        // Set item views based on the data model
        holder.name.setText(name);

        final int tileSize = activity.getResources().getDimensionPixelSize(R.dimen.letter_tile_size);

        final LetterTileProvider tileProvider = new LetterTileProvider(activity);
        final Bitmap letterTile = tileProvider.getLetterTile(name, name, tileSize, tileSize);
        holder.image.setImageBitmap(letterTile);

        if (selectedNames.containsKey(position)) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.gray));
        } else {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.background_window));
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return contacts.length;
    }

    public HashMap<Integer, SelectedContact> getSelectedNames() {
        return selectedNames;
    }

}