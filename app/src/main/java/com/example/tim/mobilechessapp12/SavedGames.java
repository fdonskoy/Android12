package com.example.tim.mobilechessapp12;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.content.res.Resources;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Chess.logic.chess.Color;
import Chess.logic.chess.CurrentGame;
import Chess.logic.chess.TypeOfMove;

public class SavedGames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public CurrentGame currentGame;
    private List<String> myList = null;
    private String game = null;

    boolean sortTitleASC, sortTitleDESC, sortDateASC, sortDateDESC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_old_games);

        game = null;
        try {
            ListView lv = (ListView) findViewById(R.id.savedGames);
            List<String> your_array_list = savedGamesStringView();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    your_array_list );
            myList = your_array_list;
            lv.setAdapter(arrayAdapter);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to load list", Toast.LENGTH_LONG).show();
        }

        final ListView lv = (ListView) findViewById(R.id.savedGames);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String text =(String) (lv.getItemAtPosition(myItemInt));
                text = text.substring(0, text.lastIndexOf("| Date:"));
                game = text;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.oppeartion_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_game_btn) {
            Log.d("My APP", "new game");
            if (currentGame.currentBoard.turnNum != 1) {
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.old_game_btn) {
            Log.d("My APP", "old games");
            try {

                Toast.makeText(getApplicationContext(), "Clicked Nav item", Toast.LENGTH_LONG).show();
                setContentView(R.layout.load_old_games);

                ListView lv = (ListView) findViewById(R.id.savedGames);

                List<String> your_array_list = savedGamesStringView();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        your_array_list );

                lv.setAdapter(arrayAdapter);

            }
            catch (Exception e) {
                return false;
            }

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private List<String> savedGamesStringView() {
        List<String> arrayListView = new ArrayList<String>();
        try {
            File[] listOfFiles = getFilesDir().listFiles();
            for (File f: listOfFiles) {
                if (!f.getName().equals("myfile") && !f.getName().equals("data.dat") && !f.getName().equals("Undo.dat")) {
                    FileInputStream fi = new FileInputStream(f);
                    ObjectInputStream oi = new ObjectInputStream(fi);
                    currentGame = (CurrentGame)oi.readObject();
                    oi.close();
                    fi.close();

                    arrayListView.add(f.getName() + " | Date: " + currentGame.dateSaved);
                    //arrayListView.add(f.getName());
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return arrayListView;
    }


    public void replay(View v) {
        if (game == null) {
            return;
        }
        ReplayCenter.loadGame = game;
        Intent intent = new Intent(this, ReplayCenter.class);
        startActivity(intent);
        //setContentView(R.layout.replay);
    }

    public void sortTitle(View v) {
        sortDateASC = false;
        sortDateDESC = false;

        ListView lv = (ListView) findViewById(R.id.savedGames);

        class StringDateComparator implements Comparator<String>
        {
            public int compare(String lhs, String rhs)
            {
                return lhs.substring(0, lhs.lastIndexOf("| Date:")).trim().toLowerCase().compareTo(rhs.substring(0, rhs.lastIndexOf("| Date:")).trim().toLowerCase());
            }
        }

        if (!sortTitleASC){
            Collections.sort(myList, new StringDateComparator());
            sortTitleDESC = false;
            sortTitleASC = true;
        }
        else if (!sortTitleDESC) {
            Collections.reverse(myList);
            sortTitleASC = false;
            sortTitleDESC = true;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myList );

        lv.setAdapter(arrayAdapter);
    }

    public void sortDate(View v) {
        sortTitleASC = false;
        sortTitleDESC = false;

        ListView lv = (ListView) findViewById(R.id.savedGames);

        class StringDateComparator implements Comparator<String>
        {
            public int compare(String lhs, String rhs)
            {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    Date date1 = format.parse(lhs.substring(lhs.indexOf("| Date: ") + 8, lhs.length()));
                    Date date2 = format.parse(rhs.substring(rhs.indexOf("| Date: ") + 8, rhs.length()));
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Couldn't compare date", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        if (!sortDateASC){
            Collections.sort(myList, new StringDateComparator());
            sortDateDESC = false;
            sortDateASC = true;
        }
        else if (!sortDateDESC) {
            Collections.reverse(myList);
            sortDateASC = false;
            sortDateDESC = true;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myList );

        lv.setAdapter(arrayAdapter);
    }



}
