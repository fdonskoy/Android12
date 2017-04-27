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
import android.content.res.Resources;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Chess.logic.chess.CurrentGame;
import Chess.logic.chess.TypeOfMove;

public class OppeartionCenter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String firstSelected = null;
    public static String secondSelected = null;
    public static ImageButton firstSelectedTile;
    public static ImageButton secondSelectedTile;

    public static GridLayout currentBoardDisplay;
    public static Resources resources;

    public static CurrentGame currentGame;

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

        try{
            File file = new File(getApplicationContext().getFilesDir(), "data.dat");
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);
                currentGame = (CurrentGame)oi.readObject();
                oi.close();
                fi.close();
            } catch (Exception e) {
                e.printStackTrace();currentGame = new CurrentGame();
            }

        } catch(Exception e){
            return;
        }

        resources = getResources();
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

    public void initializeBoard(){
        GridLayout board = (GridLayout) findViewById(R.id.chessBoard);
        currentBoardDisplay = board;
        //add an onClick to every square
        for(int i = 0; i < board.getChildCount(); i++){
            ImageButton currentSquare = (ImageButton) board.getChildAt(i);
            currentSquare.setOnClickListener(new View.OnClickListener()   {
                public void onClick(View v) {
                    if (OppeartionCenter.firstSelected == null) {
                        OppeartionCenter.firstSelectedTile = (ImageButton) v;
                        OppeartionCenter.firstSelected = getResources().getResourceEntryName(v.getId());
                    } else if (OppeartionCenter.firstSelected != null) {
                        OppeartionCenter.secondSelectedTile = (ImageButton) v;
                        OppeartionCenter.secondSelected = getResources().getResourceEntryName(v.getId());

                        makeMove();

                        OppeartionCenter.firstSelectedTile = null;
                        OppeartionCenter.secondSelectedTile = null;
                        OppeartionCenter.firstSelected = null;
                        OppeartionCenter.secondSelected = null;
                    } else if (firstSelected.equals(getResources().getResourceEntryName(v.getId()))) {
                        OppeartionCenter.firstSelectedTile = null;
                        OppeartionCenter.secondSelectedTile = null;
                        OppeartionCenter.firstSelected = null;
                        OppeartionCenter.secondSelected = null;
                    }
                    System.out.println("done");
                }
            });

            /*String id = getResources().getResourceEntryName(currentSquare.getId());
            switch(id){
                case "a1":
                case "h1":
                    currentSquare.setImageResource(R.drawable.white_rook);
                    currentSquare.setTag(R.drawable.white_rook);
                    break;
                case "b1":
                case "g1":
                    currentSquare.setImageResource(R.drawable.white_knight);
                    currentSquare.setTag(R.drawable.white_knight);
                    break;
                case "c1":
                case "f1":
                    currentSquare.setImageResource(R.drawable.white_bishop);
                    currentSquare.setTag(R.drawable.white_bishop);
                    break;
                case "d1":
                    currentSquare.setImageResource(R.drawable.white_queen);
                    currentSquare.setTag(R.drawable.white_queen);
                    break;
                case "e1":
                    currentSquare.setImageResource(R.drawable.white_king);
                    currentSquare.setTag(R.drawable.white_king);
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
                    currentSquare.setTag(R.drawable.white_pawn);
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
                    currentSquare.setTag(R.drawable.black_pawn);
                    break;
                case "a8":
                case "h8":
                    currentSquare.setImageResource(R.drawable.black_rook);
                    currentSquare.setTag(R.drawable.black_rook);
                    break;
                case "b8":
                case "g8":
                    currentSquare.setImageResource(R.drawable.black_knight);
                    currentSquare.setTag(R.drawable.black_knight);
                    break;
                case "c8":
                case "f8":
                    currentSquare.setImageResource(R.drawable.black_bishop);
                    currentSquare.setTag(R.drawable.black_bishop);
                    break;
                case "d8":
                    currentSquare.setImageResource(R.drawable.black_queen);
                    currentSquare.setTag(R.drawable.black_queen);
                    break;
                case "e8":
                    currentSquare.setImageResource(R.drawable.black_king);
                    currentSquare.setTag(R.drawable.black_king);
                    break;
                default:
                    break;
            }*/
        }


        redrawBoard();
    }


    public void makeMove(){
        try{
            TypeOfMove madeMove = currentGame.makeMove(firstSelected + " " + secondSelected);

            if(madeMove == TypeOfMove.VALID){
                secondSelectedTile.setImageResource((Integer)firstSelectedTile.getTag());
                secondSelectedTile.setTag(firstSelectedTile.getTag());

                firstSelectedTile.setImageResource(android.R.color.transparent);
                firstSelectedTile.setTag(android.R.color.transparent);
            } else if(madeMove == TypeOfMove.EN_PASSANT || madeMove == TypeOfMove.PROMOTION || madeMove == TypeOfMove.CASTLE_LEFT || madeMove == TypeOfMove.CASTLE_RIGHT){// redraw 2 files
                redrawBoard();
            }

            File file = new File(getApplicationContext().getFilesDir(), "data.dat");

            try {
                FileOutputStream fileOut =
                        new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(currentGame);
                out.close();
                fileOut.close();
                System.out.printf("Serialized data is saved in /tmp/employee.ser");
                Toast.makeText(getApplicationContext(), "test saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //currentGame.writeGame("currentGame");
        }catch(Exception e){
            Log.e("AppCompatActivity",Log.getStackTraceString(e));
            return;
        }
    }



    public static void redrawBoard(){
        for(int i = 0; i < currentBoardDisplay.getChildCount(); i++){
            ImageButton currentSquare = (ImageButton) currentBoardDisplay.getChildAt(i);
            String id = resources.getResourceEntryName(currentSquare.getId());

            switch (currentGame.getCurrentBoard().pieceAtString(id)){
                case "wP":
                    currentSquare.setImageResource(R.drawable.white_pawn);
                    currentSquare.setTag(R.drawable.white_pawn);
                    break;
                case "wR":
                    currentSquare.setImageResource(R.drawable.white_rook);
                    currentSquare.setTag(R.drawable.white_rook);
                    break;
                case "wN":
                    currentSquare.setImageResource(R.drawable.white_knight);
                    currentSquare.setTag(R.drawable.white_knight);
                    break;
                case "wB":
                    currentSquare.setImageResource(R.drawable.white_bishop);
                    currentSquare.setTag(R.drawable.white_bishop);
                    break;
                case "wQ":
                    currentSquare.setImageResource(R.drawable.white_queen);
                    currentSquare.setTag(R.drawable.white_queen);
                    break;
                case "wK":
                    currentSquare.setImageResource(R.drawable.white_king);
                    currentSquare.setTag(R.drawable.white_king);
                    break;
                case "bP":
                    currentSquare.setImageResource(R.drawable.black_pawn);
                    currentSquare.setTag(R.drawable.black_pawn);
                    break;
                case "bR":
                    currentSquare.setImageResource(R.drawable.black_rook);
                    currentSquare.setTag(R.drawable.black_rook);
                    break;
                case "bN":
                    currentSquare.setImageResource(R.drawable.black_knight);
                    currentSquare.setTag(R.drawable.black_knight);
                    break;
                case "bB":
                    currentSquare.setImageResource(R.drawable.black_bishop);
                    currentSquare.setTag(R.drawable.black_bishop);
                    break;
                case "bQ":
                    currentSquare.setImageResource(R.drawable.black_queen);
                    currentSquare.setTag(R.drawable.black_queen);
                    break;
                case "bK":
                    currentSquare.setImageResource(R.drawable.black_king);
                    currentSquare.setTag(R.drawable.black_king);
                    break;
                default:
                    currentSquare.setImageResource(android.R.color.transparent);
                    currentSquare.setTag(android.R.color.transparent);
                    break;
            }
        }
        System.out.println("Board redrawn");
    }

}
