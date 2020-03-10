package mobile.thomasianJourney.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobile.thomasianJourney.main.Contact;
import mobile.thomasianJourney.main.RecyclerViewAdapter;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tab1 extends Fragment {
    private RecyclerView mRecyclerView;
//    private List<> mList;
    RecyclerView list;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    String dates [] = {"Feb 26", "Date 00", "Date 00", "Date 00", "Date 00", "Date 00", "Date 00", "Date 00"};
    String titles[] = {"Science Week Opening Ceremony", "Title Two", "Title Three", "Title Four","Title Five","Title Six","Title Seven","Title Eight"};
    String descriptions[] = {"Medicine Auditorium", "Description Two...", "Description Three...", "Description Four...","Description Five...","Description Six...","Description Seven...","Description Eight..."};
    public String url = "https://thomasianjourney.website/Register/insertEvents";
    public List<Contact> listContact = new ArrayList<>();
    public ProgressDialog dialog;
    LinearLayout empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Intent i = getActivity().getIntent();

        View rootView;

        if (i != null) {

            String[] tabs = i.getStringArrayExtra("eventtab");

            if(tabs != null && tabs[0].equals("true")){
                rootView = inflater.inflate(R.layout.activity_year1, container, false);
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(rootView.getResources().getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

                String accountId =
                        sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");
                String collegeId =
                        sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_COLLEGE_ID, "");

                String yearLevel =
                        sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, "");

                Tab1.OkHttpHandler okHttpHandler = new Tab1.OkHttpHandler();
                okHttpHandler.execute(url, collegeId, yearLevel, accountId);


                rootView = inflater.inflate(R.layout.tab1, container, false);
                list = rootView.findViewById(R.id.list1);
                mRecyclerView = rootView.findViewById(R.id.list1);

                mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(),listContact);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mRecyclerViewAdapter);

                return rootView;
            }else{
                rootView = inflater.inflate(R.layout.activity_emptytab, container, false);
                return rootView;
            }
        }

        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        listContact = new ArrayList<>();
//        for (int i = 0 ; i < dates.length ; i++){
//
//            listContact.add(new Contact(titles[i], descriptions[i], dates[i]));
//        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mobile" +
                ".thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);

        dialog = new ProgressDialog(getContext());

        if (sharedPreferences != null) {
            String collegeId =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_COLLEGE_ID, "");
            String yearLevel =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, "");
            String accountId =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");

            if (!collegeId.isEmpty() && !yearLevel.isEmpty() && !accountId.isEmpty()) {
                Tab1.OkHttpHandler okHttpHandler = new Tab1.OkHttpHandler();

                okHttpHandler.execute(url, collegeId, yearLevel, accountId);
            } else {
                Toast.makeText(getActivity(), "Student info not found", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Student info not found", Toast.LENGTH_LONG).show();
        }

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
                        .addFormDataPart("yearLevel", params[2])
                        .addFormDataPart("accountId", params[3])
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String s) {

//            if(dialog.isShowing()){
            dialog.dismiss();
//            }
//            textView.setText(s);
            insertList(s);
//            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();

        }
    }
    public void insertList(String s){
        dialog.dismiss();
        if(!TextUtils.isEmpty(s)){
            try{
                Gson gson = new Gson();

                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

                if  (jsonObject.has("data")) {

                    JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                    for (int i = 0 ; i < dataArray.size() ; i++){

                        JsonObject dataObject = dataArray.get(i).getAsJsonObject();
                        String eventVenue = dataObject.get("eventVenue").getAsString();
                        String activityName = dataObject.get("activityName").getAsString();
                        String eventDate = dataObject.get("eventDate").getAsString();
                        String activityId = dataObject.get("activityId").getAsString();
                        String status = dataObject.get("status").getAsString();
                        listContact.add(new Contact(activityName, eventVenue, eventDate, activityId, status));
                    }

                    mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(),listContact);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);

                }

            }catch(Exception err){
//                mRecyclerView.setVisibility(View.GONE);
//                empty = getActivity().findViewById(R.id.empty);
//                empty.setVisibility(View.VISIBLE);
            }
        }



    }


}
