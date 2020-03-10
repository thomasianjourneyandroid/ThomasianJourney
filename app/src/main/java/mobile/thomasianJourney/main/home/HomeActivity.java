package mobile.thomasianJourney.main.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

import mobile.thomasianJourney.main.EventDetails;
import mobile.thomasianJourney.main.MainActivity;
import mobile.thomasianJourney.main.MenuPortfolio;
import mobile.thomasianJourney.main.OkHttpHandler;
import mobile.thomasianJourney.main.interfaces.AsyncResponse;
import mobile.thomasianJourney.main.register.async.StudentDetails;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {

    private TextView home_studentNumber;
    private TextView home_totalPoints;
    private TextView home_Welcome;

    private Dialog dialog_help;
    private ProgressDialog progressDialog;

    private TextView txtContent1, txtContent2, txtContent3, txtContent4, txtContent5, txtContent6;
    private Animation animationUp1, animationUp2, animationUp3, animationUp4,animationUp5,
            animationUp6;
    private Animation animationDown1, animationDown2, animationDown3, animationDown4,
            animationDown5,
            animationDown6;
    //private String buttons[] ={"streambtn1","streambtn2","streambtn3","button1"};
    private int dates[] ={R.id.streambtn1,R.id.streambtn2,R.id.streambtn3,R.id.button1};
    private int eventnames[] = {R.id.EventName1,R.id.EventName2,R.id.EventName3,R.id.EventName4};
    public String url = "https://thomasianjourney.website/Register/checkEvents";
    public String[] eventtab = {"false","false","false"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        home_studentNumber = findViewById(R.id.home_studentNumber);
        home_totalPoints = findViewById(R.id.home_totalPoints);
        TextView home_currentDate = findViewById(R.id.home_currentDate);
        home_Welcome = findViewById(R.id.studname);
        ImageView img_help = findViewById(R.id.img_help);

        dialog_help = new Dialog(HomeActivity.this);
        dialog_help.setContentView(R.layout.dialog_help);
        initializeHelpDialog();
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("mobile.thomasianJourney.main_FCM-MESSAGE"));
        home_studentNumber.setText("");
        home_totalPoints.setText("");
        home_currentDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime()));

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading, please wait");
        progressDialog.setMessage("Please wait while we load your information");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("mobile.thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);

        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogHelp();
            }
        });

        if (sharedPreferences != null) {
            String studentId =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID
                    , "");
            String collegeId =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_COLLEGE_ID, "");
            String yearLevel =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, "");

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            //DITO PAPASOK YUNG ID NG EVENT SA VIEW EVENTS


            okHttpHandler.execute(url, collegeId, yearLevel, studentId);

            AsyncResponse asyncResponse = new AsyncResponse() {
                @Override
                public void doWhenFinished(String output) {
                    verifyCredentials(output);
                }
            };

            StudentDetails studentDetails = new StudentDetails(asyncResponse);
            studentDetails.execute(studentId+"", getString(R.string.studentDetails));

            AsyncResponse asyncResponse2 = new AsyncResponse() {
                @Override
                public void doWhenFinished(String output) {
                    get4events(output);
                }
            };

            EventStreamAsync eventStreamAsync = new EventStreamAsync(asyncResponse2);
            eventStreamAsync.execute(getString(R.string.get4vents));


        }
    }

    private void initializeHelpDialog() {
        ImageView closeDialogHelp = dialog_help.findViewById(R.id.closeDialogHelp);
        txtContent1 = dialog_help.findViewById(R.id.title_text1);
        TextView txtTitle1 = dialog_help.findViewById(R.id.content_text1);
        txtContent1.setVisibility(View.GONE);

        animationUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent1.isShown()){
                    txtContent1.setVisibility(View.GONE);
                    txtContent1.startAnimation(animationUp1);
                } else{
                    txtContent1.setVisibility(View.VISIBLE);
                    txtContent1.startAnimation(animationDown1);
                }
            }
        });

        // help 2
        txtContent2 = dialog_help.findViewById(R.id.title_text2);
        TextView txtTitle2 = dialog_help.findViewById(R.id.content_text2);
        txtContent2.setVisibility(View.GONE);

        animationUp2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent2.isShown()){
                    txtContent2.setVisibility(View.GONE);
                    txtContent2.startAnimation(animationUp2);
                } else {
                    txtContent2.setVisibility(View.VISIBLE);
                    txtContent2.startAnimation(animationDown2);
                }
            }
        });

        // help 3
        txtContent3 = dialog_help.findViewById(R.id.title_text3);
        TextView txtTitle3 = dialog_help.findViewById(R.id.content_text3);
        txtContent3.setVisibility(View.GONE);

        animationUp3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent3.isShown()){
                    txtContent3.setVisibility(View.GONE);
                    txtContent3.startAnimation(animationUp3);
                } else {
                    txtContent3.setVisibility(View.VISIBLE);
                    txtContent3.startAnimation(animationDown3);
                }
            }
        });

        // help 4
        txtContent4 = dialog_help.findViewById(R.id.title_text4);
        TextView txtTitle4 = dialog_help.findViewById(R.id.content_text4);
        txtContent4.setVisibility(View.GONE);

        animationUp4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent4.isShown()){
                    txtContent4.setVisibility(View.GONE);
                    txtContent4.startAnimation(animationUp4);
                } else {
                    txtContent4.setVisibility(View.VISIBLE);
                    txtContent4.startAnimation(animationDown4);
                }
            }
        });

        // help 5
        txtContent5 = dialog_help.findViewById(R.id.title_text5);
        TextView txtTitle5 = dialog_help.findViewById(R.id.content_text5);
        txtContent5.setVisibility(View.GONE);

        animationUp5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent5.isShown()){
                    txtContent5.setVisibility(View.GONE);
                    txtContent5.startAnimation(animationUp5);
                } else {
                    txtContent5.setVisibility(View.VISIBLE);
                    txtContent5.startAnimation(animationDown5);
                }
            }
        });

        // help 6
        txtContent6 = dialog_help.findViewById(R.id.title_text6);
        TextView txtTitle6 = dialog_help.findViewById(R.id.content_text6);
        txtContent6.setVisibility(View.GONE);

        animationUp6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent6.isShown()){
                    txtContent6.setVisibility(View.GONE);
                    txtContent6.startAnimation(animationUp6);
                } else {
                    txtContent6.setVisibility(View.VISIBLE);
                    txtContent6.startAnimation(animationDown6);
                }
            }
        });

        closeDialogHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_help.dismiss();
            }
        });
    }

    public void verifyCredentials(String s) {

        if (!TextUtils.isEmpty(s)) {
            Gson gson = new Gson();

            JsonObject jsonObject;

            try {
                jsonObject = gson.fromJson(s, JsonObject.class);

                if (jsonObject.has("data")) {

                    JsonObject dataObject = jsonObject.get("data").getAsJsonObject();

                    if (dataObject != null) {
                        if (dataObject.has("studregEmail") && dataObject.has("studregmobileNum") && dataObject.has("studentsId")) {
                            String emailAddress = dataObject.get("studregEmail").getAsString();
                            String mobileNumber = dataObject.get("studregmobileNum").getAsString();
                            //int studentsId = dataObject.get("studNumber").getAsInt();
                            String studentsId = dataObject.get("studNumber").getAsString();

                            int studPoints = dataObject.get("studPoints").getAsInt();
                            String studname = dataObject.get("studregName").getAsString();

                            home_studentNumber.setText("Student Number: "+studentsId + "");
                            home_totalPoints.setText("Accumulated Points: "+studPoints+"");
                            home_Welcome.setText("Welcome, "+studname);

/*
                            Intent intent = new Intent(RegisterFirstLoading.this, RegisterSecond.class);
                            intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS, emailAddress);
                            intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER, mobileNumber);
                            intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, studentsId);
*/
                        } else {
                            Toast.makeText(this, "null json",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No data",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No object",
                    Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                //Toast.makeText(this, "Json Syntax",
                        //Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
           Toast.makeText(this, "Cannot find Student Details",
                    Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();
    }


    private int getStudentId() {

        SharedPreferences sharedPreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);

        String email = sharedPreferences.getString("email", "");
        String mobile = sharedPreferences.getString("mobile", "");
        int studentId = sharedPreferences.getInt("studentsId", -1);

        if (email != null && mobile != null && !email.isEmpty() && !mobile.isEmpty())
            return studentId;
        else
            return -1;
    }

    public void showDialogHelp() {
        if (dialog_help.getWindow() != null) {
            dialog_help.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog_help.show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences settings = getSharedPreferences("mobile.thomasianJourney.main.register" +
                ".USER_CREDENTIALS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.commit();
    }



//    public void showDialogHelp() {
//        dialog_help.setContentView(R.layout.dialog_help);
//        closeDialogHelp = (ImageView) dialog_help.findViewById(R.id.closeDialogHelp);
////        title_text1 = (TextView) dialog_help.findViewById(R.id.title_text1);
////        title_text2 = (TextView) dialog_help.findViewById(R.id.title_text2);
////        title_text3 = (TextView) dialog_help.findViewById(R.id.title_text3);
////        title_text4 = (TextView) dialog_help.findViewById(R.id.title_text4);
////        title_text5 = (TextView) dialog_help.findViewById(R.id.title_text5);
////        title_text6 = (TextView) dialog_help.findViewById(R.id.title_text6);
////        content_text1 = (TextView) dialog_help.findViewById(R.id.content_text1);
////        content_text2 = (TextView) dialog_help.findViewById(R.id.content_text2);
////        content_text3 = (TextView) dialog_help.findViewById(R.id.content_text3);
////        content_text4 = (TextView) dialog_help.findViewById(R.id.content_text4);
////        content_text5 = (TextView) dialog_help.findViewById(R.id.content_text5);
////        content_text6 = (TextView) dialog_help.findViewById(R.id.content_text6);
////        txthelp = (TextView) dialog_help.findViewById(R.id.txthelp);
////        scrollhelp = (NestedScrollView) dialog_help.findViewById(R.id.scrollhelp);
////        layouthelp = (LinearLayout) dialog_help.findViewById(R.id.layouthelp);
//        txtContent1 = (TextView) dialog_help.findViewById(R.id.title_text1);
//        TextView txtTitle1 = (TextView) dialog_help.findViewById(R.id.content_text1);
//        txtContent1.setVisibility(View.GONE);
//
//        animationUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent1.isShown()){
//                    txtContent1.setVisibility(View.GONE);
//                    txtContent1.startAnimation(animationUp1);
//                }
//                else{
//                    txtContent1.setVisibility(View.VISIBLE);
//                    txtContent1.startAnimation(animationDown1);
//                }
//            }
//        });
//
//        // help 2
//        txtContent2 = (TextView) dialog_help.findViewById(R.id.title_text2);
//        TextView txtTitle2 = (TextView) dialog_help.findViewById(R.id.content_text2);
//        txtContent2.setVisibility(View.GONE);
//
//        animationUp2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent2.isShown()){
//                    txtContent2.setVisibility(View.GONE);
//                    txtContent2.startAnimation(animationUp2);
//                }
//                else{
//                    txtContent2.setVisibility(View.VISIBLE);
//                    txtContent2.startAnimation(animationDown2);
//                }
//            }
//        });
//
//        // help 3
//        txtContent3 = (TextView) dialog_help.findViewById(R.id.title_text3);
//        TextView txtTitle3 = (TextView) dialog_help.findViewById(R.id.content_text3);
//        txtContent3.setVisibility(View.GONE);
//
//        animationUp3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent3.isShown()){
//                    txtContent3.setVisibility(View.GONE);
//                    txtContent3.startAnimation(animationUp3);
//                }
//                else{
//                    txtContent3.setVisibility(View.VISIBLE);
//                    txtContent3.startAnimation(animationDown3);
//                }
//            }
//        });
//
//        // help 4
//        txtContent4 = (TextView) dialog_help.findViewById(R.id.title_text4);
//        TextView txtTitle4 = (TextView) dialog_help.findViewById(R.id.content_text4);
//        txtContent4.setVisibility(View.GONE);
//
//        animationUp4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent4.isShown()){
//                    txtContent4.setVisibility(View.GONE);
//                    txtContent4.startAnimation(animationUp4);
//                }
//                else{
//                    txtContent4.setVisibility(View.VISIBLE);
//                    txtContent4.startAnimation(animationDown4);
//                }
//            }
//        });
//
//        // help 5
//        txtContent5 = (TextView) dialog_help.findViewById(R.id.title_text5);
//        TextView txtTitle5 = (TextView) dialog_help.findViewById(R.id.content_text5);
//        txtContent5.setVisibility(View.GONE);
//
//        animationUp5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent5.isShown()){
//                    txtContent5.setVisibility(View.GONE);
//                    txtContent5.startAnimation(animationUp5);
//                }
//                else{
//                    txtContent5.setVisibility(View.VISIBLE);
//                    txtContent5.startAnimation(animationDown5);
//                }
//            }
//        });
//
//        // help 6
//        txtContent6 = (TextView) dialog_help.findViewById(R.id.title_text6);
//        TextView txtTitle6 = (TextView) dialog_help.findViewById(R.id.content_text6);
//        txtContent6.setVisibility(View.GONE);
//
//        animationUp6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
//        animationDown6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
//
//        txtTitle6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(txtContent6.isShown()){
//                    txtContent6.setVisibility(View.GONE);
//                    txtContent6.startAnimation(animationUp6);
//                }
//                else{
//                    txtContent6.setVisibility(View.VISIBLE);
//                    txtContent6.startAnimation(animationDown6);
//                }
//            }
//        });
//
//        closeDialogHelp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_help.dismiss();
//            }
//
//
//        });
//
//        dialog_help.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog_help.show();
//
//
//    }

    public void EventsAnim(View view) {
        if (view == findViewById(R.id.eventId)) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("eventtab", eventtab);
            startActivity(i);
            //add animation

            Animatoo.animateCard(this);
        }
    }
    public void PortAnim(View view) {
        if (view == findViewById(R.id.portId)) {
            //open viewevents
            startActivity(new Intent(this, MenuPortfolio.class));
            //add animation
            Animatoo.animateCard(this);
        }
    }

    public void StreamAnim1(View view) {
        if (view == findViewById(R.id.stream1)) {
            //open viewevents
            startActivity(new Intent(this, EventDetails.class));
            //add animation
            Animatoo.animateCard(this);
        }

    }
    public void get4events(String s){
        Log.i("","thhe String = "+s);
        if (!TextUtils.isEmpty(s)) {
            Gson gson = new Gson();

            JsonObject jsonObject;

            try {
                jsonObject = gson.fromJson(s, JsonObject.class);

                if (jsonObject.has("data")) {




                        JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                    if (dataArray != null) {
                        for (int i = 0 ; i < dataArray.size() ; i++){

                            JsonObject dataObject2 = dataArray.get(i).getAsJsonObject();
                            String eventVenue = dataObject2.get("eventVenue").getAsString();
                            String activityName = dataObject2.get("activityName").getAsString();
                            String eventDate = dataObject2.get("eventDate").getAsString();
                            String activityId = dataObject2.get("activityId").getAsString();


                            Button btn = (Button) findViewById(dates[i]);
                            btn.setText(eventDate);
                            TextView tv = (TextView) findViewById(eventnames[i]);
                            tv.setText(activityName);


                        }
                    } else {
                        Toast.makeText(this, "No data",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No object",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                //Toast.makeText(this, "Json Syntax",
                //Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Events Found",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AsyncResponse asyncResponse2 = new AsyncResponse() {
                @Override
                public void doWhenFinished(String output) {
                    get4events(output);
                }
            };

            EventStreamAsync eventStreamAsync = new EventStreamAsync(asyncResponse2);
            eventStreamAsync.execute(getString(R.string.get4vents));

        }
    };

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

//            }
//            textView.setText(s);
            insertList(s);
//            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();

        }
    }
    public void insertList(String s){


        if(!TextUtils.isEmpty(s)){
            try{
                Gson gson = new Gson();

                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

                if  (jsonObject.has("data")) {
                    JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                    for (int i = 0 ; i < dataArray.size() ; i++){
                        eventtab[i] = dataArray.get(i).toString();
                    }
                }else{

                }

            }catch(Exception err){
//                mRecyclerView.setVisibility(View.GONE);
//                empty = getActivity().findViewById(R.id.empty);
//                empty.setVisibility(View.VISIBLE);
//                Toast.makeText(this, year1.length+"HELLO", Toast.LENGTH_SHORT).show();
            }
        }else{
        }
    }
}