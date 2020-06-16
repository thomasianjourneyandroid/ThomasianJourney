package mobile.thomasianJourney.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.CalendarView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
    ArrayList<String[]> list = new ArrayList<>();

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendardata.clear();

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

                date = year + "-" + monthstring + "-" + daystring;

                list.clear();

                for (int i = 0; i < dataArray.size(); i++) {
                    String[] rowArray = new String[6];
                    JsonArray stringArray = dataArray.get(i).getAsJsonArray();

                    Arrays.fill(rowArray, null);
                    for (int j = 0; j < stringArray.size(); j++) {

                        String eventid = stringArray.get(0).toString();
                        String eventname = stringArray.get(1).toString();
                        String eventvenue = stringArray.get(2).toString();
                        String eventstart = stringArray.get(3).toString();
                        String eventend = stringArray.get(4).toString();
                        String eventstatus = stringArray.get(5).toString();

                        if (eventstart.contains(date)) {
                            rowArray[0] = eventid;
                            rowArray[1] = eventname;
                            rowArray[2] = eventvenue;
                            rowArray[3] = eventstart;
                            rowArray[4] = eventend;
                            rowArray[5] = eventstatus;

                            list.add(rowArray);
                            break;
                        }

                    }

                }

                String[][] eventsarray = list.toArray(new String[list.size()][6]);

                try {
                    insertEvents(eventsarray);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Arrays.fill(eventsarray, null);

            }
        });
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

                    // STARTUP DATA
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

                    list.clear();

                    for (int i = 0; i < dataArray.size(); i++) {
                        String[] rowArray = new String[6];
                        JsonArray stringArray = dataArray.get(i).getAsJsonArray();

                        Arrays.fill(rowArray, null);
                        for (int j = 0; j < stringArray.size(); j++) {

                            String eventid = stringArray.get(0).toString();
                            String eventname = stringArray.get(1).toString();
                            String eventvenue = stringArray.get(2).toString();
                            String eventstart = stringArray.get(3).toString();
                            String eventend = stringArray.get(4).toString();
                            String eventstatus = stringArray.get(5).toString();


                            if (eventstart.contains(formatter.format(date))) {
                                rowArray[0] = eventid;
                                rowArray[1] = eventname;
                                rowArray[2] = eventvenue;
                                rowArray[3] = eventstart;
                                rowArray[4] = eventend;
                                rowArray[5] = eventstatus;

                                list.add(rowArray);
                                break;
                            }

                        }

                    }

                    String[][] eventsarray = list.toArray(new String[list.size()][6]);

                    try {
                        insertEvents(eventsarray);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Arrays.fill(eventsarray, null);
                }

            } catch (Exception err) {

            }
        } else {
        }

    }

    private void insertEvents (String[][] eventsarray) throws ParseException {
        for (int row = 0; row < eventsarray.length; row++) {
            for (int column = 0; column < eventsarray[row].length; column++) {
                String eventid = eventsarray[row][0].replaceAll("^\"|\"$", "");;
                String eventname = eventsarray[row][1].replaceAll("^\"|\"$", "");;
                String eventvenue = eventsarray[row][2].replaceAll("^\"|\"$", "");;
                String eventstart = eventsarray[row][3].replaceAll("^\"|\"$", "");;
                String eventend = eventsarray[row][4].replaceAll("^\"|\"$", "");;
                String eventstatus = eventsarray[row][5].replaceAll("^\"|\"$", "");;

                String startTime = eventstart.split(" ")[1];
                String endTime = eventend.split(" ")[1];
                String splittedTime = startTime.split(":")[0] + ":" + startTime.split(":")[1];
                String splittedEndTime = endTime.split(":")[0] + ":" + endTime.split(":")[1];
                DateFormat dfTime = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat target = new SimpleDateFormat("h:mm a");
                SimpleDateFormat source = new SimpleDateFormat("HH:mm");
                String formattedTime = target.format(source.parse(splittedTime));
                String formattedEndTime = target.format(source.parse(splittedEndTime));

                calendardata.add(new ItemData(eventname + "", eventvenue + "", formattedTime + " - " + formattedEndTime));
                break;
            }
        }

        RecyclerView mRecyclerView = findViewById(R.id.listview);
        RecyclerViewAdapterCalendar adapter = new RecyclerViewAdapterCalendar(this, calendardata);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }
}
