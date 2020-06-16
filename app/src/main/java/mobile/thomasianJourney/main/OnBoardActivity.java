package mobile.thomasianJourney.main;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobile.thomasianJourney.main.home.HomeActivity;
import mobile.thomasianJourney.main.register.RegisterFirst;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    Button obconbutton;

    private TextView[] mDots;

    private  SlideAdapter sliderAdapter;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        obconbutton = findViewById(R.id.obcontbutton);
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SlideAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);



        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);


    }

    public void contAnim(View v)
    {
        onfirst();
    }
    public void onfirst()
    {

            //place your dialog code here to dislay the dialog
            LayoutInflater inflater= LayoutInflater.from(this);
            View view=inflater.inflate(R.layout.privacypolicy, null);
            TextView textview=(TextView)view.findViewById(R.id.privacy);
            textview.setText(R.string.privacy);

            new AlertDialog.Builder(OnBoardActivity.this)
                    .setTitle("        P r i v a c y       P o l i c y")
                    .setView(view)
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSharedPreferences("mobile.thomasianJourney.main" +
                                    ".register.USER_CREDENTIALS", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply(); //
                            Intent intent;
                            if (isSharedPreferencesPresent()) {
                                intent = new Intent(OnBoardActivity.this, HomeActivity.class);
                            } else {
                                intent = new Intent(OnBoardActivity.this, RegisterFirst.class);
                            }
                            startActivity(intent);
                            finish();
                        }
                    }).show();



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

    public void addDotsIndicator(int position){

        mDotLayout.removeAllViews();

        mDots = new TextView[5];

        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));


            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0) {

            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }


    };

    public void onClick(View v) {



    }
}
