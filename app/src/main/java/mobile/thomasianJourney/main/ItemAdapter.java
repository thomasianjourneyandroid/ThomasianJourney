package mobile.thomasianJourney.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobile.thomasianJourney.main.vieweventsfragments.R;

public class ItemAdapter extends ArrayAdapter<ItemData> {
    private Context context;
    private int resource;

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<ItemData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override

    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(i).getEventName();
        String venue = getItem(i).getEventVenue();
        String time = getItem(i).getEventTime();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView eventname = convertView.findViewById(R.id.event_title);
        TextView eventvenue = convertView.findViewById(R.id.event_venue);
        TextView eventtime = convertView.findViewById(R.id.event_time);

        eventname.setText(name);
        eventvenue.setText(venue);
        eventtime.setText(time);

        return convertView;


    }
}