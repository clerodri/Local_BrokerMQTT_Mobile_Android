package unicam.pi.mqespolversion2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import unicam.pi.mqespolversion2.databinding.ActivityMainBinding;
import unicam.pi.mqespolversion2.model.mqttService;
import unicam.pi.mqespolversion2.viewModel.MainActiviyViewModel;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SplashScreen splashScreen;

    private Intent serviceMQTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashScreen= SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        Log.e("TAG","Main Activity Create");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // = new ViewModelProvider(this).get(MainActiviyViewModel.class);
        serviceMQTT  = new Intent(this, mqttService.class);

        binding.btnStartBroker.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                startForegroundService(serviceMQTT);
                }
        });
        binding.btnStopBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceMQTT);
            }
        });

    }
}