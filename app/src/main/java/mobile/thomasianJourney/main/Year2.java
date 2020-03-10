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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobile.thomasianJourney.main.Contact;
import mobile.thomasianJourney.main.RecyclerViewAdapterPort;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Year2 extends Fragment {
    private RecyclerView mRecyclerView;
    //    private List<> mList;
    RecyclerView list;
    private RecyclerViewAdapterPort mRecyclerViewAdapter;
    String dates [] = {"Jan 01", "Jan 02", "Jan 14", "Feb 03", "Feb 14", "Feb 30", "Mar 03", "Mar 05"};
    String titles[] = {"Title One", "Title Two", "Title Three", "Title Four","Title Five","Title Six","Title Seven","Title Eight"};
    String descriptions[] = {"Description One...", "Description Two...", "Description Three...", "Description Four...","Description Five...","Description Six...","Description Seven...","Description Eight..."};

    public String url = "https://thomasianjourney.website/Register/portfolioInfo";
    LinearLayout empty;
    public List<Contact> listContact = new ArrayList<>();
    public ProgressDialog dialog;
    int x = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent i = getActivity().getIntent();

        if (i != null) {
            String yearLevel = i.getStringExtra("yearLevel");
            String[] tabs = i.getStringArrayExtra("emptytab"+yearLevel);

            View rootView;
            if(tabs != null && tabs[1].equals("false")){
                rootView = inflater.inflate(R.layout.activity_year1, container, false);
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(rootView.getResources().getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

                String eventClass = "2";

                String accountId =
                        sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");

                Year2.OkHttpHandler okHttpHandler = new Year2.OkHttpHandler();

                okHttpHandler.execute(url, accountId, eventClass, yearLevel);

                list = rootView.findViewById(R.id.list2);
                mRecyclerView = rootView.findViewById(R.id.list2);

                mRecyclerViewAdapter = new RecyclerViewAdapterPort(getContext(),listContact);
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
        dialog = new ProgressDialog(getContext());


//        for (int i = 0 ; i < dates.length ; i++){
//            listContact.add(new Contact(titles[i], descriptions[i], dates[i]));
//        }
        Intent i = getActivity().getIntent();

        View view = getView();

        if (view != null) {
            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences(view.getResources().getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);

            if (sharedPreferences != null) {
                if (i != null) {
                    String eventClass = "2";
                    String yearLevel =
                            sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, "");
                    String accountId =
                            sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");

                    Year2.OkHttpHandler okHttpHandler = new Year2.OkHttpHandler();

                    okHttpHandler.execute(url, accountId, eventClass, yearLevel);
                }
            }
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
                        .addFormDataPart("accountId", params[1])
                        .addFormDataPart("eventClass", params[2])
                        .addFormDataPart("yearLevel", params[3])
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
                        listContact.add(new Contact(activityName, eventVenue, eventDate));
                    }

                    mRecyclerViewAdapter = new RecyclerViewAdapterPort(getContext(),listContact);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);



                }else{
                    x = 1;
                }

            }catch(Exception err){
//                mRecyclerView.setVisibility(View.GONE);
//                empty = getActivity().findViewById(R.id.empty);
//                empty.setVisibility(View.VISIBLE);
                x = 1;
            }
        }else{
            x = 1;
        }



    }


}
