package mobile.thomasianJourney.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import mobile.thomasianJourney.main.home.HomeActivity;
import mobile.thomasianJourney.main.register.RegisterFirst;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;

public class Splash extends AppCompatActivity {
    private TextView tv ;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();



            Thread timer = new Thread() {
                public void run () {
                    try {
                        sleep(3500) ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {

                        Intent intent;
                        if(!isFirstRun()) {


                            if (isSharedPreferencesPresent()) {
                                intent = new Intent(Splash.this, HomeActivity.class);
                            } else {
                                intent = new Intent(Splash.this, RegisterFirst.class);
                            }

                        }else{
                            intent = new Intent(Splash.this, OnBoardActivity.class);

                        }
                        startActivity(intent);
                        finish();

                    }
                }
            };
            timer.start();


    }

    public boolean isFirstRun(){
        boolean isFirstRun = getSharedPreferences("mobile.thomasianJourney.main" +
                ".register.USER_CREDENTIALS", MODE_PRIVATE).getBoolean("isFirstRun", true);
        return isFirstRun;
    }

    public boolean isSharedPreferencesPresent() {

        SharedPreferences sharedPreferences = getSharedPreferences("mobile.thomasianJourney.main" +
                ".register.USER_CREDENTIALS", Context.MODE_PRIVATE);

        return sharedPreferences.contains(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS) &&
                sharedPreferences.contains(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER) &&
                sharedPreferences.contains(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID);

    }
}
