package fi.tut.matti.thereminseq;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getName();

    private Button btn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, checkAudioCapabilities(), Toast.LENGTH_SHORT).show();


    }

    private String checkAudioCapabilities(){
        String text = "";
        boolean hasLowLatencyFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY);
        boolean hasProFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO);
        if(hasLowLatencyFeature)
            text = "android.hardware.audio.low_latency indicates a continuous "
                    + "output latency of 45 ms or less.\n";
        if(hasProFeature)
            text += " android.hardware.audio.pro indicates a continuous "
                    +"round-trip latency of 20 ms or less.\n";
        if(!hasLowLatencyFeature && !hasProFeature)
            text = "No Android Compatibility Definition Document (CDD) compatible "
                    + "low latency capabilities reported by device.\n";
        return text;
    }
}
