package com.example.signapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;



public class SettingsMain extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

        Bundle settings = getIntent().getExtras();

        final EditText signText = (EditText) findViewById(R.id.editText01);
	    signText.setText(settings.getString("signText"));

        final EditText fontSize = (EditText) findViewById(R.id.editSignature);
        fontSize.setText(String.valueOf(settings.getInt("fontSize")));

        final Spinner color = (Spinner) findViewById(R.id.spinnerColor);
        String[] values= getResources().getStringArray(R.array.values);
        ArrayList<String> arrColors = new ArrayList<String>();

        for(String s : values)
        {
            arrColors.add(s);
        }

        color.setSelection(arrColors.indexOf(String.valueOf(settings.getInt("color"))));


        Button save = (Button)findViewById(R.id.button_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
                String sql_update = "UPDATE Daten SET signText ="+signText+" color="+color+" fontSize="+fontSize + "where id=1;";
                db.execSQL(sql_update);
                db.close();
            }
        });


    }
}