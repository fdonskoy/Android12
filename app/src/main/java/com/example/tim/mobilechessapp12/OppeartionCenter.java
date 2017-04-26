package com.example.tim.mobilechessapp12;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.GridLayout;
import android.widget.ImageButton;

public class OppeartionCenter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String firstSelected = null;
    public static String secondSelected = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oppeartion_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeBoard();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_game_btn) {
            Log.d("My APP", "new game");
        } else if (id == R.id.old_game_btn) {
            Log.d("My APP", "old games");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void  initializeBoard(){
        GridLayout currentTile = (GridLayout) findViewById(R.id.chessBoard);

        //add an onClick to every square
        for(int i = 0; i < currentTile.getChildCount(); i++){
            ImageButton currentSquare = (ImageButton) currentTile.getChildAt(i);
            currentSquare.setOnClickListener(new View.OnClickListener()   {
                public void onClick(View v) {
                    if (OppeartionCenter.firstSelected == null) {
                        OppeartionCenter.firstSelected = getResources().getResourceEntryName(v.getId());
                    } else if (OppeartionCenter.firstSelected != null) {
                        OppeartionCenter.secondSelected = getResources().getResourceEntryName(v.getId());
                    } else if (firstSelected.equals(getResources().getResourceEntryName(v.getId()))) {
                        OppeartionCenter.firstSelected = null;
                        OppeartionCenter.secondSelected = null;
                    }
                    System.out.println("done");
                }
            });

            String id = getResources().getResourceEntryName(currentSquare.getId());
            switch(id){
                case "a1":
                case "h1":
                    currentSquare.setImageResource(R.drawable.white_rook);
                    break;
                case "b1":
                case "g1":
                    currentSquare.setImageResource(R.drawable.white_knight);
                    break;
                case "c1":
                case "f1":
                    currentSquare.setImageResource(R.drawable.white_bishop);
                    break;
                case "d1":
                    currentSquare.setImageResource(R.drawable.white_queen);
                    break;
                case "e1":
                    currentSquare.setImageResource(R.drawable.white_king);
                    break;
                case "a2":
                case "b2":
                case "c2":
                case "d2":
                case "e2":
                case "f2":
                case "g2":
                case "h2":
                    currentSquare.setImageResource(R.drawable.white_pawn);
                    break;
                case "a7":
                case "b7":
                case "c7":
                case "d7":
                case "e7":
                case "f7":
                case "g7":
                case "h7":
                    currentSquare.setImageResource(R.drawable.black_pawn);
                    break;
                case "a8":
                case "h8":
                    currentSquare.setImageResource(R.drawable.black_rook);
                    break;
                case "b8":
                case "g8":
                    currentSquare.setImageResource(R.drawable.black_knight);
                    break;
                case "c8":
                case "f8":
                    currentSquare.setImageResource(R.drawable.black_bishop);
                    break;
                case "d8":
                    currentSquare.setImageResource(R.drawable.black_queen);
                    break;
                case "e8":
                    currentSquare.setImageResource(R.drawable.black_king);
                    break;
                default:
                    break;
            }
        }
    }
}
