package mobile.thomasianJourney.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mobile.thomasianJourney.main.vieweventsfragments.R;

public class RecyclerViewAdapterCalendar extends RecyclerView.Adapter<RecyclerViewAdapterCalendar.MyViewHolder> {

    Context mContext;
    List<ItemData> mData;
    String id;

    public RecyclerViewAdapterCalendar(Context context, List<ItemData> data) {
        mContext = context;
        mData = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;

        v = LayoutInflater.from(mContext).inflate(R.layout.row_calendar, viewGroup, false);
        final RecyclerViewAdapterCalendar.MyViewHolder vHolder = new RecyclerViewAdapterCalendar.MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterCalendar.MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mData.get(i).getEventName());
        myViewHolder.venue.setText(mData.get(i).getEventTime());
        myViewHolder.time.setText(mData.get(i).getEventVenue());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_contact;
        private TextView title;
        private TextView venue;
        private TextView time;

        MyViewHolder(View itemView){
            super(itemView);
            item_contact = itemView.findViewById(R.id.item_contact);
            title = itemView.findViewById(R.id.event_title);
            venue = itemView.findViewById(R.id.event_venue);
            time = itemView.findViewById(R.id.event_time);

        }
    }

}
