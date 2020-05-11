package mobile.thomasianJourney.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewCalendar extends AppCompatActivity {
    private OkHttpClient client;

    public String url = "https://thomasianjourney.website/Register/insertAllEvents";
    ProgressDialog dialog;
    CalendarView calendarView;
    String date = "";
    JsonArray dataArray;
    ArrayList<ItemData> calendardata = new ArrayList<>();
    ListView listevents;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        setContentView(R.layout.activity_calendar);

        SharedPreferences sharedPreferences = getSharedPreferences("mobile.thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);
        String collegeid = sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_COLLEGE_ID, "");

        ViewCalendar.OkHttpHandler okHttpHandler = new ViewCalendar.OkHttpHandler();
        okHttpHandler.execute(url, collegeid);

        calendarView = findViewById(R.id.calendarView);
        String date = "4/1/2020";
        String parts[] = date.split("/");

        int day = Integer.parseInt(parts[1]);
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();

        calendarView.setDate (milliTime, true, true);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthstring = "";
                String daystring = "";

                if (month >= 1 && month <= 9) {
                    month++;
                    monthstring = "0" + month;
                }

                else {
                    month++;
                    monthstring = "" + month;
                }

                if (dayOfMonth >= 1 && dayOfMonth <= 9) {
                    daystring = "0" + dayOfMonth;
                }

                else {
                    daystring = "" + dayOfMonth;
                }

//                date = year + "-" + monthstring + "-" + daystring;
//                Log.d("ViewCalendar", "Check: " + date);
////                Log.d("ViewCalendar", "Check Json Array" + dataArray);
//
//                if (date.contains("2020-05-01")) {
//                    Log.d("ViewCalendar", "Check May 1");
////                    calendardata.add(new ItemData("New Test Event 2", "Medicine Auditorium", "01 - 11:00"));
//                }

                /*ArrayList<String[]> list = new ArrayList<>();

                list.clear();

                for (int i = 0; i < dataArray.size(); i++) {
                    String[] rowArray = new String[6];
                    JsonArray stringArray = dataArray.get(i).getAsJsonArray();
//                    Log.d("ViewCalendar", "Check String Array" + stringArray);

                    Arrays.fill(rowArray, null);
                    for (int j = 0; j < stringArray.size(); j++) {
//                        String data = stringArray.get(j).toString();

//                        Log.d("ViewCalendar", "Data Check: " + data);

                        String eventid = stringArray.get(0).toString();
                        String eventname = stringArray.get(1).toString();
                        String eventvenue = stringArray.get(2).toString();
                        String eventstart = stringArray.get(3).toString();
                        String eventend = stringArray.get(4).toString();
                        String eventstatus = stringArray.get(5).toString();

//                        Log.d("ViewCalendar", "Checking Event Start." + eventstart);

                        if (eventstart.contains(date)) {
//                            Log.d("ViewCalendar", "Place success stuff here." + eventstart);
                            rowArray[0] = eventid;
                            rowArray[1] = eventname;
                            rowArray[2] = eventvenue;
                            rowArray[3] = eventstart;
                            rowArray[4] = eventend;
                            rowArray[5] = eventstatus;

//                            Log.d("ViewCalendar", "rowArray Check " + rowArray);

//                            for (int k = 0; k < rowArray.length; k++) {
//                                Log.d("ViewCalendar", "rowArray Check " + rowArray[k]);
//                            }

                            list.add(rowArray);
                            break;
//                            list.add(rowArray);

                        }

                    }

                }

//                Iterator i = list.iterator();
//                Log.d("ViewCalendar", "The ArrayList elements are:");
//                while (i.hasNext()) {
//                    Log.d("ViewCalendar", ""+ i.next());
//                }

                // Convert ArrayList to Array
//                String[] eventslist = new String[list.size()];
//                eventslist = list.toArray(eventslist);
//                String[] eventslist = list.toArray();
//                String[] eventslist = list.toArray(new String[0]);

                // Print the array
//                for (String s : eventslist) {
//                    Log.d("ViewCalendar", "List Item Check: " + s);
//                }
//                for (int row = 0; row<eventslist.length; row++) {
//
//                    for (int column = 0; column < eventslist[row].length(); column++) {
//                        Log.d("ViewCalendar", "List Item Check" + eventslist.toString());
//                    }
//                }

//                for (String[] string : list) {
//                    Log.d("ViewCalendar", "List Item Check" + string);
//                }

//                Log.d("ViewCalendar", "List Item Check" + Arrays.toString(list.toArray()));

                // for loop
//                for (Iterator<String[]> iterator = list.iterator(); iterator.hasNext();) {
//                    Log.d("ViewCalendar", "List Item Check value= " + iterator.next());
//                }*/
            }
        });

        calendardata.add(new ItemData("New Test Event 2", "Medicine Auditorium", "01:00 - 11:00"));
        calendardata.add(new ItemData("IICS Test Event", "Medicine Auditorium", "04:02 - 04:02"));
        calendardata.add(new ItemData("IICS Future Event 2", "Medicine Auditorium", "11:20 - 12:00"));
        calendardata.add(new ItemData("Test Event for IICS TJ Version 3.0", "Medicine Auditorium", "10:26 - 11:26"));
        calendardata.add(new ItemData("Test Event for all TJ Version 3.0", "Medicine Auditorium", "10:28 - 10:28"));
        calendardata.add(new ItemData("2nd Test Event for IICS TJ Version 3.0", "Medicine Auditorium", "08:59 - 08:59"));

        ItemAdapter adapter = new ItemAdapter(this, R.layout.row_calendar, calendardata);
        listevents = findViewById(R.id.listview);
        listevents.setAdapter(adapter);

    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("collegeId", params[1])
                        .build();

                Request.Builder builder = new Request.Builder();
                builder.url(params[0])
                        .post(requestBody);
                Request request = builder.build();

                Response response = client.newCall(request).execute();

                System.out.print("Response: " + response.code());

                if (response.isSuccessful()) {

                    return response.body().string();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
//            Log.d("ViewCalendar", "Check" + s);
            viewEvents(s);
        }
    }

    private void viewEvents(String s) {
        if (!TextUtils.isEmpty(s)) {

            try {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

                if (jsonObject.has("data")) {
                    dataArray = jsonObject.get("data").getAsJsonArray();
                }

            } catch (Exception err) {
//                mRecyclerView.setVisibility(View.GONE);
//                empty = getActivity().findViewById(R.id.empty);
//                empty.setVisibility(View.VISIBLE);
//                Toast.makeText(this, year1.length+"HELLO", Toast.LENGTH_SHORT).show();
            }
        } else {
        }

    }
}
