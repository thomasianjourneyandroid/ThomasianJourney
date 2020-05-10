package mobile.thomasianJourney.main;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttendedEvent extends AppCompatActivity {
    private OkHttpClient client;
    private Button btnAttend;
    Dialog dialog_help;
    ImageView closeDialogHelp, img_help;
    TextView txtContent1, txtContent2, txtContent3, txtContent4, txtContent5, txtContent6;
    Animation animationUp, animationUp1, animationUp2, animationUp3, animationUp4,animationUp5, animationUp6;
    Animation animationDown, animationDown1, animationDown2, animationDown3, animationDown4, animationDown5, animationDown6 ;
    TextView tv_date, tv_title, tv_description, tv_venue, tv_time, tv_point;

    RelativeLayout eventdetails;
    public String url = "https://thomasianjourney.website/Register/eventTime";
    public String eventUrl = "https://thomasianjourney.website/Register/eventDetails";
    public String stickerURL = "https://thomasianjourney.website/Register/printSticker";
    ProgressDialog dialog;

    public String activityName, eventVenue, eventDate, description, eventendDate, points, attend, startTime, endTime, splittedTime, splittedEndTime, formattedTime, formattedEndTime, date, year, month, day, refereceNo, printStickerData, printSticker;

    public static final int STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        setContentView(R.layout.activity_attendedevent);
        tv_date = findViewById(R.id.home_currentDate);
        tv_title = findViewById(R.id.title);
        tv_description = findViewById(R.id.description);
        tv_venue = findViewById(R.id.venue);
        tv_time = findViewById(R.id.time);
        tv_point = findViewById(R.id.point);

        AttendedEvent.OkHttpHandler2 okHttpHandler2 = new AttendedEvent.OkHttpHandler2();

        SharedPreferences sharedPreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);

        int studentId = sharedPreferences.getInt("studentsId", -1);

        Intent i = getIntent();
        String id = i.getExtras().getString("activityId");
        //String accountId = "1";
        okHttpHandler2.execute(eventUrl, id, studentId + "");

        dialog_help = new Dialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String permissions = (Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissions(new String[]{permissions}, 1);
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                }

                else {
                    Toast.makeText(this, "Please make sure you have enabled Storage permissions on your device.", Toast.LENGTH_LONG).show();
                }
            }
        }
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
        protected String doInBackground(String... params) {

            try {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("activityId", params[1])
                        .addFormDataPart("accountId", params[2])
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
            super.onPostExecute(s);
//            textView.setText(s);
//            openVerifyLoginCred(s);
            // INSERT IF STICKER ALREADY CLICKED ACTION
//            Log.d("ScanSuccess", "On Post Execute: " + s);
            printSticker(s);
//            Toast.makeText(EventDetails.this, ""+s, Toast.LENGTH_SHORT).show();

        }
    }

    public class OkHttpHandler2 extends AsyncTask<String, Void, String> {

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
                        .addFormDataPart("activityId", params[1])
                        .addFormDataPart("accountId", params[2])
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
            super.onPostExecute(s);
            dialog.dismiss();
            getEventDetails(s);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEventDetails(String s){
        dialog.dismiss();
        if(!TextUtils.isEmpty(s)){
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
                if(jsonObject.has("data")){
                    JsonObject dataObject = jsonObject.get("data").getAsJsonObject();
                    activityName = dataObject.get("activityName").getAsString();
                    eventVenue = dataObject.get("eventVenue").getAsString();
                    eventDate = dataObject.get("eventDate").getAsString();
                    description = dataObject.get("description").getAsString();
                    eventendDate = dataObject.get("eventendDate").getAsString();
                    points = dataObject.get("points").getAsString();
                    attend = dataObject.get("attend").getAsString();

                    startTime = eventDate.split(" ")[1];
                    endTime = eventendDate.split(" ")[1];
                    splittedTime = startTime.split(":")[0] + ":" + startTime.split(":")[1];
                    splittedEndTime = endTime.split(":")[0] + ":" + endTime.split(":")[1];
                    SimpleDateFormat target = new SimpleDateFormat("h:mm a");
                    SimpleDateFormat source = new SimpleDateFormat("HH:mm");
                    formattedTime = target.format(source.parse(splittedTime));
                    formattedEndTime = target.format(source.parse(splittedEndTime));

                    date = eventDate.split(" ")[0];

                    year = date.split("-")[0];

                    month = date.split("-")[1];

                    day = date.split("-")[2];
                    switch(month){
                        case "01":
                            month = "Jan";
                            break;
                        case "02":
                            month = "Feb";
                            break;
                        case "03":
                            month = "Mar";
                            break;
                        case "04":
                            month = "April";
                            break;
                        case "05":
                            month = "May";
                            break;
                        case "06":
                            month = "Jun";
                            break;
                        case "07":
                            month = "Jul";
                            break;
                        case "08":
                            month = "Aug";
                            break;
                        case "09":
                            month = "Sep";
                            break;
                        case "10":
                            month = "Oct";
                            break;
                        case "11":
                            month = "Nov";
                            break;
                        case "12":
                            month = "Dec";
                            break;

                    }

                    tv_date.setText(month + "\n" + day);
                    tv_title.setText(activityName);
                    tv_venue.setText(eventVenue);
                    tv_time.setText(formattedTime + " : " +formattedEndTime);
                    tv_description.setText(description);
                    tv_point.setText(points + " Points");
                    if(attend.equals("1")){
                        btnAttend.setVisibility(View.INVISIBLE);
                    }



                }
            }catch (Exception err){
                Toast.makeText(this, ""+err, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void StickerAnim(View view) {
        if (view == findViewById(R.id.vsticker_btn)) {
            savePdf();
        }
    }

    public void savePdf() {
        Intent i = getIntent();
        String id = i.getExtras().getString("activityId");
        SharedPreferences sharedPreferences = getSharedPreferences("mobile.thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);
        String studentId = sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, "");

        AttendedEvent.OkHttpHandler okHttpHandler = new AttendedEvent.OkHttpHandler();

        okHttpHandler.execute(stickerURL, id, studentId);
    }

    public void printSticker(String s) {

        if(!TextUtils.isEmpty(s)){

            try {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

                JsonObject dataObject = jsonObject.get("data").getAsJsonObject();

//                Log.d("ScanSuccess", "Data Check: " + dataObject);

                String attendID = dataObject.get("attendId").getAsString();
                String studattendId = dataObject.get("studattendId").getAsString();
                String eventId = dataObject.get("eventId").getAsString();
                String yearLevel = dataObject.get("yearLevel").getAsString();
                printSticker = dataObject.get("printSticker").getAsString();
                refereceNo = dataObject.get("referenceNo").getAsString();

                if (printSticker.equals("0")) {
                    // DOWNLOAD PDF
                    Document mDoc = new Document();
                    String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mFileName + ".pdf";

                    try {
                        PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));

                        mDoc.open();

                        mDoc.setPageSize(PageSize.LETTER);
                        mDoc.addCreationDate();
                        mDoc.addAuthor("Thomasian Journey");
                        mDoc.addCreator("Thomasian Journey");
                        mDoc.addTitle(activityName);

                        Font titlefont = new Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK);
                        Chunk titlefontchunk = new Chunk("Event Title Here", titlefont);
                        Paragraph titlefontparagraph = new Paragraph(titlefontchunk);
                        titlefontparagraph.setAlignment(Element.ALIGN_CENTER);
                        mDoc.add(titlefontparagraph);

                        // Creating image by file name
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sticker);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Image img = null;
                        byte[] byteArray = stream.toByteArray();
                        try {
                            img = Image.getInstance(byteArray);
//                img.scalePercent(7f);
                            img.scaleAbsolute(54f, 76f);
                            img.setAlignment(Image.MIDDLE);
                        } catch (BadElementException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mDoc.add(img);

                        mDoc.close();

                        PdfReader reader = new PdfReader(mFilePath);
                        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+activityName+" Sticker.pdf"));
                        Font f = new Font(Font.FontFamily.HELVETICA, 1.5f, Font.NORMAL, BaseColor.BLACK);

                        // EVENT ID
                        PdfContentByte cb = stamper.getOverContent(1);
                        ColumnText ct = new ColumnText(cb);
                        ct.setSimpleColumn(285f, 742.5f, 485f, 762.5f);
                        Paragraph pz = new Paragraph(new Phrase(20, "242", f));
                        ct.addElement(pz);
                        ct.go();

                        // EVENT NAME
                        ct.setSimpleColumn(288f, 737.75f, 488, 757.75f);
                        pz = new Paragraph(new Phrase(20, activityName, f));
                        ct.addElement(pz);
                        ct.go();

                        // EVENT VENUE
                        ct.setSimpleColumn(282.5f, 733f, 482.5f, 753f);
                        pz = new Paragraph(new Phrase(20, eventVenue, f));
                        ct.addElement(pz);
                        ct.go();

                        // EVENT DATE
                        ct.setSimpleColumn(280.5f, 728.5f, 480.5f, 748.5f);
                        pz = new Paragraph(new Phrase(20, month + " " + day + ", " + year, f));
                        ct.addElement(pz);
                        ct.go();

                        // EVENT TIME
                        ct.setSimpleColumn(281f, 724f, 481f, 744f);
                        pz = new Paragraph(new Phrase(20, formattedTime + " - " + formattedEndTime, f));
                        ct.addElement(pz);
                        ct.go();

                        SharedPreferences sharedPreferences = getSharedPreferences("mobile.thomasianJourney.main.register.USER_CREDENTIALS", Context.MODE_PRIVATE);
                        String studentnumber = sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_NO, "");
                        String studentname = sharedPreferences.getString(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_NAME, "");

                        // STUDENT NO.
                        ct.setSimpleColumn(290, 711f, 490f, 731f);
                        pz = new Paragraph(new Phrase(20, studentnumber, f));
                        ct.addElement(pz);
                        ct.go();

                        // STUDENT NAME
                        ct.setSimpleColumn(282f, 706.25f, 482f, 726.25f);
                        pz = new Paragraph(new Phrase(20, studentname, f));
                        ct.addElement(pz);
                        ct.go();

                        // REFERENCE NO.
                        ct.setSimpleColumn(296f, 695.75f, 496f, 715.75f);
                        pz = new Paragraph(new Phrase(20, refereceNo, f));
                        ct.addElement(pz);
                        ct.go();

                        stamper.close();
                        reader.close();

                        File file = new File(mFilePath);
                        file.delete();
                        if(file.exists()){
                            file.getCanonicalFile().delete();
                            if(file.exists()){
                                getApplicationContext().deleteFile(file.getName());
                            }
                        }

                        Toast.makeText(this, "Sticker has been downloaded to " + Environment.getExternalStorageDirectory().getAbsolutePath() + ".", Toast.LENGTH_LONG).show();
                    }

                    catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    Toast.makeText(this, "You have already downloaded the sticker for this event", Toast.LENGTH_LONG).show();
                }
            }

            catch (Exception err) {
                Toast.makeText(this, "No Event Data Found", Toast.LENGTH_SHORT).show();
                Log.d("ScanSuccess", "Error Message: " + err);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted. You may now download your sticker.", Toast.LENGTH_LONG).show() ;
                }

                else {
                    Toast.makeText(this, "Please make sure you have enabled Storage permissions on your device.", Toast.LENGTH_LONG).show() ;
                }
            }
        }
    }
}
