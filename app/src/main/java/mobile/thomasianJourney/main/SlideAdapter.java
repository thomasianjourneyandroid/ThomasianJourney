package mobile.thomasianJourney.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import mobile.thomasianJourney.main.vieweventsfragments.R;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Button obconbutton;
    public SlideAdapter(Context context) {

        this.context = context;
    }

    //Arrays Onboarding json
    public  int[] slide_images = {

            R.drawable.ob,
            R.drawable.ob1,
            R.drawable.ob2,
            R.drawable.ob3,
            R.drawable.ob4
    };

    public String[] slide_headings = {

            "HELLO THOMASIAN",
            "EVENTS",
            "NOTIFY",
            "SCAN QR",
            "GET STARTED"
    };

    public  String[] slide_descs ={
            "The Thomasian Journey Event Notification System" +
                    "will keep you updated with all the local and university wide events.",
            "The Thomasian Journey Event Notification System" +
                    "keeps track of all the events you've attended.",
            "Get notified to all campus events and get" +
                    "points each time you will attend.",
            "The Thomasian Journey validates the attendance" +
                    "with the use of QR Technology.",
            "Welcome to your Journey, Thomasian!" +
                    "" +
                    "\n \n Click here to get started:"

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }


    private LottieAnimationView slidelottieAnimationView;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);


//        LottieAnimationView slidelottieAnimationView = (LottieAnimationView) view.findViewById(R.id.lottieECalendar);
//        slidelottieAnimationView.setScale(3f);
        ImageView slideimgView = (ImageView) view.findViewById(R.id.onboardingimg);
        TextView slidetextView1 = (TextView) view.findViewById(R.id.onboardingtitle1);
        TextView slidetextView2 = (TextView) view.findViewById(R.id.onboardingdesc1);
        Button obconbutton = view.findViewById(R.id.obcontbutton);
//        slidelottieAnimationView.setAnimation(slide_images[position]);
        slideimgView.setImageResource(slide_images[position]);
        slidetextView1.setText(slide_headings[position]);
        slidetextView2.setText(slide_descs[position]);
        if(position == 4){
            obconbutton.setVisibility(View.VISIBLE);
        }else{

            obconbutton.setVisibility(View.INVISIBLE);
        }
        container.addView(view);

        return  view;


    }




    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}















