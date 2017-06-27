package com.example.user.sqlitetest;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.sqlitetest.dbDAO.databaseDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText etAuthorName,etPublishDate,etTitle,etQueryResult;
    private Button btnInsert,btnQueryAll;
    private databaseDAO dbDAO;
    private static boolean mDeBounce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAuthorName = (EditText) findViewById(R.id.etAuthorName);
        etPublishDate = (EditText) findViewById(R.id.etPublishDate);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etQueryResult = (EditText) findViewById(R.id.etQueryResult);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnQueryAll = (Button) findViewById(R.id.btnQueryAll);

        etQueryResult.setFocusable(false);
        etQueryResult.setClickable(false);
        etQueryResult.setFocusableInTouchMode(false);

        dbDAO = new databaseDAO(this);
        final InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues insertSQL = new ContentValues();
                // "AuthorName NVARCHAR(100),AuthDataTime DATETIME,Title NVARCHAR(100));"
                if(etAuthorName.getText().length()!=0
                        &&etPublishDate.getText().length()!=0
                        &&etTitle.getText().length()!=0) {
                    insertSQL.put("AuthorName", etAuthorName.getText().toString());
                    insertSQL.put("AuthDataTime", etPublishDate.getText().toString());
                    insertSQL.put("Title", etTitle.getText().toString());
                    dbDAO.insert(insertSQL);
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else Toast.makeText(MainActivity.this,"Input Error",Toast.LENGTH_LONG).show();
            }
        });

        ListView lvQueryResult = (ListView) findViewById(R.id.lvQueryResult);
        ArrayList<String> dataSet = new ArrayList<>();
        String[] result_row = dbDAO.getAll_f2().split(";");
        etQueryResult.setText(result_row[0]+result_row[0]+"");
        Collections.addAll(dataSet, result_row);
        final tableAdapter mAdapter = new tableAdapter(dataSet,this);
        lvQueryResult.setAdapter(mAdapter);
        btnQueryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //etQueryResult.setText(dbDAO.getAll());
                mAdapter.getData().clear();
                String[] result_row = dbDAO.getAll_f2().split(";");
                mAdapter.addAll(result_row);
                mAdapter.notifyDataSetChanged();
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        final Handler mHandler = new Handler();
        etPublishDate.setFocusable(false);
        etPublishDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN&&mDeBounce) {
                    showDTpicker();
                    mDeBounce = false;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           mDeBounce = true;
                        }
                    },500);
                }
            return false;
            }
        });
        etPublishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDTpicker();

            }
        });

    }
    private void showDTpicker()
    {
        final AlertDialog.Builder dtPicker=new AlertDialog.Builder(this);
        dtPicker.setTitle("Please Choose the Publish date");
        final DatePicker picker = new DatePicker(this);
        dtPicker.setView(picker);
        dtPicker.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do something
                Calendar dtSelected = Calendar.getInstance();
                dtSelected.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                etPublishDate.setText(formater.format(dtSelected.getTime()));
            }
        });
        dtPicker.show();

    }
}
