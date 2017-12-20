package fi.tut.matti.vastavari;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();

    private View mainColorView;
    private View complementaryColorView;
    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;
    private TextView mainHex;
    private TextView complementHex;
    private int red, green, blue;
    private Button saveBtn;
    private Button nextBtn;
    private Button deleteBtn;
    private TextView colIdTxt;

    private ColorDataSource datasource;
    private List<int[]> colorsList;
    private int currentIdx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainColorView = findViewById(R.id.mainColor);
        complementaryColorView = findViewById(R.id.complementaryColor);
        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);

        mainHex = findViewById(R.id.mainHex);
        complementHex = findViewById(R.id.complementHex);

        colIdTxt = findViewById(R.id.idx);



        datasource = new ColorDataSource(this);

        red = 0; green = 128; blue = 255;
        setColors();

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress;
                setColors();
                clearCurrIdx();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress;
                setColors();
                clearCurrIdx();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress;
                setColors();
                clearCurrIdx();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        saveBtn = findViewById(R.id.savebtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] color = getMatchFromList();
                if(color == null) {
                    datasource.open();
                    datasource.store(red, green, blue);
                    colorsList = datasource.findAll();
                    currentIdx = colorsList.size() - 1;
                } else {
                    currentIdx = colorsList.indexOf(color);
                }
                colIdTxt.setText((currentIdx + 1) + "/" + colorsList.size());
            }
        });

        nextBtn = findViewById(R.id.nextbtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int testi = -1;
                if(colorsList == null || currentIdx == -1){
                    datasource.open();
                    colorsList = datasource.findAll();
                    if(colorsList.size() >= 1) {
                        currentIdx = 0;
                        Log.d(TAG, "indeksi nollaan");
                    }
                } else {
                    incrementIdx();
                }
                if(colorsList.size() == 0) return;
                setcolorByRowId(colorsList.get(currentIdx)[3]);
                colIdTxt.setText((currentIdx + 1) + "/" + colorsList.size());

            }
        });

        deleteBtn = findViewById(R.id.deletebtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentIdx == -1) return;

                datasource.open();
                datasource.delete(colorsList.get(currentIdx)[3]);
                colorsList = datasource.findAll();
                if(colorsList.size() == 0){
                    clearCurrIdx();
                } else {
                    setColor(colorsList.get(currentIdx));
                }
            }
        });

    }

    private void incrementIdx(){
        if(currentIdx == -1) return;
        if (currentIdx >= colorsList.size() - 1)
            currentIdx = 0;
        else
            currentIdx++;
    }

    /** return -1 if not found, columnId if found */
    private int[] getMatchFromList(){
        if(colorsList == null) return null;
        for(int[] intArr: colorsList) {
            if(intArr[0] == red && intArr[1] == green && intArr[2] == blue) {
                return intArr;
            }
        }
        return null;
    }

    private void setcolorByRowId(int id){
        int[] currColor = new int[]{128,128,128};
        for(int[] color : colorsList){
            if(id == color[3]){
                currColor = color;
                break;
            }
        }
        setColor(currColor);
    }

    private void setColor(int[] currColor){
        red = currColor[0];
        green = currColor[1];
        blue = currColor[2];
        setColors();
    }

    private void clearCurrIdx(){
        currentIdx = -1;
        colIdTxt.setText("");
    }

    private void setColors(){
        int color = (0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
        int complementaryColor = (0xff) << 24 | ((255-red) & 0xff) << 16 | ((255-green) & 0xff) << 8 | ((255-blue) & 0xff);
        mainColorView.setBackgroundColor(color);
        complementaryColorView.setBackgroundColor(complementaryColor);

        mainHex.setText(String.format("#%06X", (0xFFFFFF & color)));
        complementHex.setText(String.format("#%06X", (0xFFFFFF & complementaryColor)));

        seekBarRed.setProgress(red);
        seekBarGreen.setProgress(green);
        seekBarBlue.setProgress(blue);
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
        // automatically handle clicks on the Home/Up saveBtn, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
