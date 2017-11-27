/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.tut.matti.laskukone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MathLogActivity extends AppCompatActivity {
    private static final String TAG = MathLogActivity.class.getName();

    /* Field to store our TextView */
    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathlog);

        mDisplayText = (TextView) findViewById(R.id.tv_display);
        mDisplayText.setText(FileUtils.lueTiedostosta(MathLogActivity.this));

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
        FileUtils.clearLog(MathLogActivity.this);
        mDisplayText.setText("");
        return true;
    }
}