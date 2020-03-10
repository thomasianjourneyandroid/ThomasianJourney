package mobile.thomasianJourney.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import mobile.thomasianJourney.main.home.HomeActivity;
import mobile.thomasianJourney.main.vieweventsfragments.R;


public class ScanSuccess extends AppCompatActivity {
    private Button vhome_btn;
    private Button vport_btn;
    private LottieAnimationView LottieScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);

        //ANIMATION LOTTIE
        LottieScan = findViewById(R.id.mainlottieScan);

        LottieScan.setScale(7f);
        LottieScan.setVisibility(View.VISIBLE);
        LottieScan.setAnimation(R.raw.qr);
        LottieScan.playAnimation();
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
            startActivity(new Intent(this, Portfolio.class));
            //add animation
            Animatoo.animateCard(this);
            finish();
        }
    }
}
