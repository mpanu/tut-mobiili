package fi.tut.matti.laskukone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addLaskeButtonHandler((Button)findViewById(R.id.plussabutton), "+");
        addLaskeButtonHandler((Button)findViewById(R.id.miinusbutton), "-");
        addLaskeButtonHandler((Button)findViewById(R.id.kertobutton), "x");
        addLaskeButtonHandler((Button)findViewById(R.id.jakobutton), "/");
    }

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
