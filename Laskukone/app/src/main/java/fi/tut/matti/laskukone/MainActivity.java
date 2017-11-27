package fi.tut.matti.laskukone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/** code has a lot of indexes (magic numbers). I know this is bad practice
 * but just wanted to avoid tedious duplicate code and to learn parent/sibling handling.
 * should have probably used a grid for the calculators and have named columns etc.
 */
public class MainActivity extends AppCompatActivity {

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
                tulosText.setText(laske(eka,toka,operation)+"");
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
        } else {
            Log.e("tag", "serious error, unsupported operation");
            return -1;
        }
    }
}
