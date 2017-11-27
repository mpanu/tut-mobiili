package fi.tut.matti.laskukone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //laskukone
        addLaskeButtonHandler((Button)findViewById(R.id.plussabutton), "+");
        addLaskeButtonHandler((Button)findViewById(R.id.miinusbutton), "-");
        addLaskeButtonHandler((Button)findViewById(R.id.kertobutton), "x");
        addLaskeButtonHandler((Button)findViewById(R.id.jakobutton), "/");

        //tyhjennä-nappi
        final Button tyhjennaBtn = findViewById(R.id.tyhjenna);
        final List<TextView> clearableViews = getViewsToClear();
        tyhjennaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(TextView clearableView : clearableViews){
                    clearableView.setText("0");
                }
            }
        });

        //logi-nappi
        final Button logButton = findViewById(R.id.logi);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = MathLogActivity.class;
                Intent startChildActivityIntent = new Intent(MainActivity.this,
                        destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });
    }

    // näytä menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //tyhjennä logi menusta
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FileUtils.clearLog(MainActivity.this);
        TextView tv = (TextView) findViewById(R.id.tv_display);
        if(tv != null)
            tv.setText("");
        return true;
    }

    /** find views that should be set to 0 with the button */
    private List<TextView> getViewsToClear(){
        List<TextView> clearableViews = new ArrayList<>();
        LinearLayout mainLayout = findViewById(R.id.mainlayout);
        for(int i = 0; i < 4; i++){ // magic number 4: +,-,x,/ <- exlude clear and log buttons (=idx 4)
            if(mainLayout.getChildAt(i) instanceof  LinearLayout){
                LinearLayout calcRow = (LinearLayout)mainLayout.getChildAt(i);
                // magic numbers 0,2,4 <- 2 input fields, and the result field
                clearableViews.add((TextView) calcRow.getChildAt(0));
                clearableViews.add((TextView) calcRow.getChildAt(2));
                clearableViews.add((TextView) calcRow.getChildAt(4));
            }
        }
        return clearableViews;
    }

    /** find related inputs and outputs by sibling indexes to reduce amount of code */
    private void addLaskeButtonHandler(Button btn, final String operation){
        final LinearLayout buttonParentLayout = (LinearLayout) btn.getParent();
        final EditText ekaText = (EditText) buttonParentLayout.getChildAt(0);
        final EditText tokaText = (EditText) buttonParentLayout.getChildAt(2);
        final TextView tulosText = (TextView) buttonParentLayout.getChildAt(4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double eka = Double.parseDouble(ekaText.getText().toString());
                double toka = Double.parseDouble(tokaText.getText().toString());
                String tulos = laske(eka,toka,operation)+"";
                tulosText.setText(tulos);
                //kirjoita logi
                FileUtils.kirjoitaTiedostoon(MainActivity.this, eka+operation+toka+"="+tulos+"\n");
            }
        });
    }

    /** stupid calculator method */
    private double laske(double eka, double toka, String operation){
        if(operation.equals("+")){
            return eka+toka;
        }else if(operation.equals("-")){
            return eka-toka;
        }else if(operation.equals("x")){
            return eka*toka;
        }else if(operation.equals("/")){
            return eka/toka;
        }
        Log.e(TAG, "unsupported math operation");
        return Double.MIN_VALUE;
    }
}
