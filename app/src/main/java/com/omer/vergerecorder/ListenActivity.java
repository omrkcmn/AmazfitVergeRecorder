package com.omer.vergerecorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListenActivity extends AppCompatActivity {

    private List<String> songList = new ArrayList<String>();

    private ListView songListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        songListView = findViewById(R.id.lstSongs);
        getAllSongs();


        /*songListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListenActivity.this);
                builder.setMessage("Merhaba");

                builder.setPositiveButton("OK",null);
                builder.setNegativeButton("No",null);

                return true;
            }
        });*/

    }

    public void getAllSongs() {
        File dir = new File("/Records/");
        //File dir = Environment.getExternalStorageDirectory();

        String pattern = ".3gp";
        File listFile[] = dir.listFiles();
        if(listFile != null)
        {
            for(File file:dir.listFiles())
            {
                if(file.getName().endsWith(pattern)){
                    songList.add(file.getName());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, songList);
            songListView.setAdapter(adapter);
        }
    }
}
