package fi.tut.matti.thereminseq;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.csounds.CsoundObj;
import com.csounds.CsoundObjListener;
import com.csounds.bindings.motion.CsoundMotion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity implements
        CsoundObjListener {
    private final String TAG = MainActivity.class.getName();
    protected CsoundObj csoundObj = new CsoundObj(true,true);
    ToggleButton startStopButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        csoundObj.setMessageLoggingEnabled(true);

        startStopButton = findViewById(R.id.mute);
        startStopButton
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            String csd = getResourceFileAsString(R.raw.hardware_test);
                            File f = createTempFile(csd);

                            CsoundMotion csoundMotion = new CsoundMotion(csoundObj);
                            csoundMotion.enableAccelerometer( MainActivity.this);

                            csoundObj.startCsound(f);
                            keepScreenOn(true);
                        } else {
                            csoundObj.stop();
                            keepScreenOn(false);
                        }

                    }
                });

        Toast.makeText(this, checkAudioCapabilities(), Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        csoundObj.stop();

    }

    protected String getResourceFileAsString(int resId) {
        StringBuilder str = new StringBuilder();

        InputStream is = getResources().openRawResource(resId);
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;

        try {
            while ((line = r.readLine()) != null) {
                str.append(line).append("\n");
            }
        } catch (IOException ios) {

        }

        return str.toString();
    }

    protected File createTempFile(String csd) {
        File f = null;

        try {
            f = File.createTempFile("temp", ".csd", this.getCacheDir());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(csd.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return f;
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

    @Override
    public void csoundObjStarted(CsoundObj csoundObj) {

    }

    @Override
    public void csoundObjCompleted(CsoundObj csoundObj) {

    }

    private void keepScreenOn(boolean yes){
        if(yes)
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
