package mobile.thomasianJourney.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import mobile.thomasianJourney.main.home.HomeActivity;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;
import okhttp3.ConnectionSpec;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ScanSuccess extends AppCompatActivity {
    private Button vhome_btn;
    private Button vport_btn;
    private LottieAnimationView LottieScan;

    public String url = "https://thomasianjourney.website/Register/eventTime";
    public String eventUrl = "https://thomasianjourney.website/Register/eventDetails";
    ProgressDialog dialog;

    public String activityName, eventVenue, eventDate, description, eventendDate, points, attend, startTime, endTime, splittedTime, splittedEndTime, formattedTime, formattedEndTime, date, year, month, day;

    public static final int STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        setContentView(R.layout.activity_scan_success);

        //ANIMATION LOTTIE
        LottieScan = findViewById(R.id.mainlottieScan);

        LottieScan.setScale(7f);
        LottieScan.setVisibility(View.VISIBLE);
        LottieScan.setAnimation(R.raw.qr);
        LottieScan.playAnimation();

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

        Intent i = getIntent();
        String id = i.getExtras().getString("activityId");

        OkHttpHandler2 okHttpHandler2 = new OkHttpHandler2();

        SharedPreferences sharedPreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);

        int studentId = sharedPreferences.getInt("studentsId", -1);

        okHttpHandler2.execute(eventUrl, id, studentId + "");

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

                }
            } catch (Exception err){
                Toast.makeText(this, ""+err, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void HomeAnim(View view) {
        if (view == findViewById(R.id.vhome_btn)) {
            //back to home
            startActivity(new Intent(this, HomeActivity.class));
            //add animation
            Animatoo.animateCard(this);
            finish();
        }
    }

    public void PortAnim(View view) {
        if (view == findViewById(R.id.vport_btn)) {
            //go to portfolio
            startActivity(new Intent(this, MenuPortfolio.class));
            //add animation
            Animatoo.animateCard(this);
            finish();
        }
    }

    public void StickerAnim(View view) {
        if (view == findViewById(R.id.vsticker_btn)) {
            savePdf();
        }
    }

    public void savePdf() {
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mFileName + ".pdf";

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));

            mDoc.open();

//            String mText = mTextET.getText().toString();

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
            pz = new Paragraph(new Phrase(20, "ABC123", f));
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

            Toast.makeText(this, "Sticker has been downloaded.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
