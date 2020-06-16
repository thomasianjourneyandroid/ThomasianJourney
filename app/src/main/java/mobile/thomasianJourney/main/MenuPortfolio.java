package mobile.thomasianJourney.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;

import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MenuPortfolio extends AppCompatActivity {
    Button first, second, third, fourth;
    Dialog dialog_help;
    ImageView closeDialogHelp;
    TextView txtContent1, txtContent2, txtContent3, txtContent4, txtContent5, txtContent6;
    Animation animationUp, animationUp1, animationUp2, animationUp3, animationUp4,animationUp5, animationUp6;
    Animation animationDown, animationDown1, animationDown2, animationDown3, animationDown4, animationDown5, animationDown6 ;

    public String[] year1 = {"true","true","true","true"};
    public String[] year2 = {"true","true","true","true"};
    public String[] year3 = {"true","true","true","true"};
    public String[] year4 = {"true","true","true","true"};
    public ProgressDialog dialog;
    public String url = "https://thomasianjourney.website/Register/checkPortfolio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_portfolio);
        first = findViewById(R.id.btnyr1);
        second = findViewById(R.id.btnyr2);
        third = findViewById(R.id.btnyr3);
        fourth = findViewById(R.id.btnyr4);
        dialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = getSharedPreferences("mobile" +
                ".thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            final String yearLevel =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, "");
            OkHttpHandler okHttpHandler = new OkHttpHandler();

            String accountId =
                    sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");
            okHttpHandler.execute(url, accountId);

            first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Integer.parseInt(yearLevel) >= 1){
                        Intent i = new Intent(MenuPortfolio.this, Portfolio.class);
                        i.putExtra("yearLevel", "1");
                        i.putExtra("emptytab1", year1);
                        startActivity(i);
                    }else{
                        Toast.makeText(MenuPortfolio.this, "Not Yet Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(yearLevel) >= 2){
                        Intent i = new Intent(MenuPortfolio.this, Portfolio.class);
                        i.putExtra("yearLevel", "2");
                        i.putExtra("emptytab2", year2);
                        startActivity(i);
                    }else{
                        Toast.makeText(MenuPortfolio.this, "Not Yet Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(yearLevel) >= 3){
                        Intent i = new Intent(MenuPortfolio.this, Portfolio.class);
                        i.putExtra("yearLevel", "3");
                        i.putExtra("emptytab3", year3);
                        startActivity(i);
                    }else{
                        Toast.makeText(MenuPortfolio.this, "Not Yet Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            fourth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(yearLevel) >= 4){
                        Intent i = new Intent(MenuPortfolio.this, Portfolio.class);
                        i.putExtra("yearLevel", "4");
                        i.putExtra("emptytab4", year4);
                        startActivity(i);
                    }else{
                        Toast.makeText(MenuPortfolio.this, "Not Yet Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        dialog_help = new Dialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ShowDialogHelp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowDialogHelp() {
        dialog_help.setContentView(R.layout.dialog_help);
        closeDialogHelp = (ImageView) dialog_help.findViewById(R.id.closeDialogHelp);
        txtContent1 = (TextView) dialog_help.findViewById(R.id.title_text1);
        TextView txtTitle1 = (TextView) dialog_help.findViewById(R.id.content_text1);
        txtContent1.setVisibility(View.GONE);

        animationUp1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent1.isShown()){
                    txtContent1.setVisibility(View.GONE);
                    txtContent1.startAnimation(animationUp1);
                }
                else{
                    txtContent1.setVisibility(View.VISIBLE);
                    txtContent1.startAnimation(animationDown1);
                }
            }
        });

        // help 2
        txtContent2 = (TextView) dialog_help.findViewById(R.id.title_text2);
        TextView txtTitle2 = (TextView) dialog_help.findViewById(R.id.content_text2);
        txtContent2.setVisibility(View.GONE);

        animationUp2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent2.isShown()){
                    txtContent2.setVisibility(View.GONE);
                    txtContent2.startAnimation(animationUp2);
                }
                else{
                    txtContent2.setVisibility(View.VISIBLE);
                    txtContent2.startAnimation(animationDown2);
                }
            }
        });

        // help 3
        txtContent3 = (TextView) dialog_help.findViewById(R.id.title_text3);
        TextView txtTitle3 = (TextView) dialog_help.findViewById(R.id.content_text3);
        txtContent3.setVisibility(View.GONE);

        animationUp3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent3.isShown()){
                    txtContent3.setVisibility(View.GONE);
                    txtContent3.startAnimation(animationUp3);
                }
                else{
                    txtContent3.setVisibility(View.VISIBLE);
                    txtContent3.startAnimation(animationDown3);
                }
            }
        });

        // help 4
        txtContent4 = (TextView) dialog_help.findViewById(R.id.title_text4);
        TextView txtTitle4 = (TextView) dialog_help.findViewById(R.id.content_text4);
        txtContent4.setVisibility(View.GONE);

        animationUp4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent4.isShown()){
                    txtContent4.setVisibility(View.GONE);
                    txtContent4.startAnimation(animationUp4);
                }
                else{
                    txtContent4.setVisibility(View.VISIBLE);
                    txtContent4.startAnimation(animationDown4);
                }
            }
        });

        // help 5
        txtContent5 = (TextView) dialog_help.findViewById(R.id.title_text5);
        TextView txtTitle5 = (TextView) dialog_help.findViewById(R.id.content_text5);
        txtContent5.setVisibility(View.GONE);

        animationUp5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent5.isShown()){
                    txtContent5.setVisibility(View.GONE);
                    txtContent5.startAnimation(animationUp5);
                }
                else{
                    txtContent5.setVisibility(View.VISIBLE);
                    txtContent5.startAnimation(animationDown5);
                }
            }
        });

        // help 6
        txtContent6 = (TextView) dialog_help.findViewById(R.id.title_text6);
        TextView txtTitle6 = (TextView) dialog_help.findViewById(R.id.content_text6);
        txtContent6.setVisibility(View.GONE);

        animationUp6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        txtTitle6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContent6.isShown()){
                    txtContent6.setVisibility(View.GONE);
                    txtContent6.startAnimation(animationUp6);
                }
                else{
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

        dialog_help.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_help.show();


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
            dialog.dismiss();
            insertList(s);
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
                        String[] temp = new String[4];
                        JsonArray stringArray = dataArray.get(i).getAsJsonArray();
                        Log.d("MenuPortfolioData", "String Array: " + stringArray);
                        for(int j = 0 ; j < stringArray.size() ; j++){

                            String data = stringArray.get(j).toString();
                            temp[j] = data;
                        }
                        if(i == 0){
                            year1 = temp;
                        }else if(i == 1){
                            year2 = temp;
                        }else if(i == 2){
                            year3 = temp;
                        }else if(i == 3){
                            year4 = temp;
                        }

                    }

                }else{

                }

            }catch(Exception err){

            }
        }else{
        }
    }
}
