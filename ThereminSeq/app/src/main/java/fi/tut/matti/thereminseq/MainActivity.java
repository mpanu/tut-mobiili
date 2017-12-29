package fi.tut.matti.thereminseq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.csounds.CsoundObj;
import com.csounds.CsoundObjListener;
import com.csounds.bindings.motion.CsoundMotion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements
        CsoundObjListener {
    private final String TAG = MainActivity.class.getName();
    protected CsoundObj csoundObj = new CsoundObj(true, true);
    private ToggleButton startStopButton = null;
    private Button playBtn;
    private Button saveBtn;
    private EditText bpmTxt;
    private Button loadBtn;
    private Button saveSeqButton;

    private PitchBinding pitch = new PitchBinding();
    private List<String> fileList = new ArrayList<>();
    private List<Double> seqList = new ArrayList<>();
    private double bpm = 90;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        csoundObj.setMessageLoggingEnabled(true);
        csoundObj.addBinding(pitch);

        bpmTxt = findViewById(R.id.bpmTxt);
        loadBtn = findViewById(R.id.loadBtn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilenames();
            }
        });

        saveSeqButton = findViewById(R.id.saveSeqBtn);
        saveSeqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });


        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seqList.add(pitch.value);
            }
        });

        playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startStopButton.isChecked()){
                    startStopButton.setChecked(false);
                }

                try {
                    bpm = Double.parseDouble(bpmTxt.getText().toString());
                } catch (NumberFormatException e) {
                    bpmTxt.setText(bpm + "");
                }

                String csd = getResourceFileAsString(R.raw.score_osc);
                File f = createTempFile(csd);
                csoundObj.startCsound(f);

                String score = "";
                double beatLength = 15.0 / (double)bpm; // 4 steps per beat
                int i = 0;
                for(double pitchD : seqList){
                    String pitchStr = String.format(Locale.US,"%.2f", pitchD);
                    // instNbr startBeat durBeats ... instParams=pitch
                    score += "i 1 " + i * beatLength + " " + (beatLength+0.1) + " " + pitchStr + "\n";
                    i++;
                }
                csoundObj.sendScore(score);
                Toast.makeText(MainActivity.this, score, Toast.LENGTH_LONG).show();

            }
        });

        startStopButton = findViewById(R.id.mute);
        startStopButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    String csd = getResourceFileAsString(R.raw.accel_osc);
                    File f = createTempFile(csd);

                    CsoundMotion csoundMotion = new CsoundMotion(csoundObj);
                    csoundMotion.enableAccelerometer(MainActivity.this);

                    csoundObj.startCsound(f);
                    keepScreenOn(true);
                } else {
                    csoundObj.stop();
                    keepScreenOn(false);
                }

            }
        });
        Toast.makeText(this, checkAudioCapabilities(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
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

    private String checkAudioCapabilities() {
        String text = "";
        boolean hasLowLatencyFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY);
        boolean hasProFeature =
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO);
        if (hasLowLatencyFeature)
            text = "android.hardware.audio.low_latency indicates a continuous "
                    + "output latency of 45 ms or less.\n";
        if (hasProFeature)
            text += " android.hardware.audio.pro indicates a continuous "
                    + "round-trip latency of 20 ms or less.\n";
        if (!hasLowLatencyFeature && !hasProFeature)
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

    private void keepScreenOn(boolean yes) {
        if (yes)
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void saveFile(){
        String file = "";
        for (double d: seqList){
            file += String.format(Locale.US,"%.2f", d) + "\n";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String FILENAME = sdf.format(new Date());
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(file.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "ounou");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "cräp");
        }


    }

    private void showFilenames(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Load sequence");

        File directory = new File(getApplicationInfo().dataDir + "/files");
        File[] files = directory.listFiles();
        fileList.clear();
        for (int i = 0; i < files.length; i++)
        {
            fileList.add(files[i].getName());
        }

        String[] fileNameArr = fileList.toArray(new String[0]);
        builder.setItems(fileNameArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int idx) {
                loadPitcSeqFromFile(fileList.get(idx));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadPitcSeqFromFile(String filename){
        List<String> pitcSeqList = new ArrayList<>();
        try {
            FileInputStream fIn = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                pitcSeqList.add(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        seqList.clear();
        for(String s : pitcSeqList){
            seqList.add(Double.parseDouble(s));
        }

    }
}
