package com.example.tim.mobilechessapp12;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.content.res.Resources;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Chess.logic.chess.Board;
import Chess.logic.chess.Color;
import Chess.logic.chess.CurrentGame;
import Chess.logic.chess.TypeOfMove;

public class ReplayCenter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String firstSelected = null;
    public static String secondSelected = null;
    public static ImageButton firstSelectedTile;
    public static ImageButton secondSelectedTile;

    public static GridLayout currentBoardDisplay;
    public static Resources resources;

    public static CurrentGame currentGame;
    private CurrentGame replay;
    public static String loadGame = null;
    GridLayout grid = null;

    int place = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Button resign = (Button)findViewById(R.id.resign);
*/
        Toast.makeText(getApplicationContext(), "Game selected: " + loadGame, Toast.LENGTH_LONG).show();
        AutoCompleteTextView t = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        loadGame = loadGame.trim();
        t.setText(loadGame);
        t.setEnabled(false);

        resources = getResources();
        try {
            initializeBoard();
            if (!readIt(loadGame)) {
                Toast.makeText(getApplicationContext(), "Couldn't load " + loadGame, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Loaded " + loadGame, Toast.LENGTH_LONG).show();
                replay = new CurrentGame();
                replay = currentGame;
                replay.currentBoard = new Board();
            }
            grid = (GridLayout) findViewById(R.id.chessBoard);
        }
        catch (Exception e) {
            return;
        }

    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

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

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void initializeBoard() throws Exception{
        currentGame = new CurrentGame();
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
            replay.listMoves.get(place).substring(0,2);
            replay.listMoves.get(place).substring(3,5);
            //Toast.makeText(getApplicationContext(), "Extracted " + replay.listMoves.get(place).substring(0,2) + " and " + replay.listMoves.get(place).substring(3,5), Toast.LENGTH_SHORT).show();
            firstSelectedTile = findTile(replay.listMoves.get(place).substring(0,2));
            secondSelectedTile = findTile(replay.listMoves.get(place).substring(3,5));

            secondSelectedTile.setImageResource((Integer)firstSelectedTile.getTag());
            secondSelectedTile.setTag(firstSelectedTile.getTag());

            firstSelectedTile.setImageResource(android.R.color.transparent);
            firstSelectedTile.setTag(android.R.color.transparent);

        }catch(Exception e){
            Log.e("AppCompatActivity",Log.getStackTraceString(e));
            return;
        }
    }

    private ImageButton findTile(String tileID){

        ImageButton z = null;
        for(int i=0; i< grid.getChildCount(); i++) {
            ImageButton child = (ImageButton)grid.getChildAt(i);
            if (getResources().getResourceEntryName(child.getId()).equals(tileID)){
                z = child;
            }
        }

        return z;
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

    //need to cycle through all files
    private boolean readIt(String title) throws Exception{
        File file = null;
        File[] listOfFiles = getFilesDir().listFiles();
        for (File f: listOfFiles) {
            if (f.getName().equals(title)) {
                file = f;
                break;
            }
        }
        if (file == null) {
            return false;
        }
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream oi = new ObjectInputStream(fi);
        currentGame = (CurrentGame)oi.readObject();
        oi.close();
        fi.close();

        return true;
    }

    public void nextMove(View v) throws Exception{
        if (place > replay.listMoves.size() -1 ) {
            place = 0;
            initializeBoard();
            return;
        }

        if (place == replay.listMoves.size()-1){
            String last = replay.listMoves.get(place);
            if (last.equals("resign")) {
                if (currentGame.currentBoard.turn.equals(Color.Black)) {
                    Toast.makeText(getApplicationContext(), "White Resigned. Black wins!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Black Resigned. White wins!", Toast.LENGTH_SHORT).show();
                }
                place++;
                return;
            }
            else if (last.equals("draw")) {
                Toast.makeText(getApplicationContext(), "Draw was accepted", Toast.LENGTH_SHORT).show();
                place++;
                return;
            }
            else if (currentGame.currentBoard.whiteKing.checkmate() ) {
                Toast.makeText(getApplicationContext(), "Checkmate! Black wins!", Toast.LENGTH_SHORT).show();
            }
            else if (currentGame.currentBoard.blackKing.checkmate()) {
                Toast.makeText(getApplicationContext(), "Checkmate! White wins!", Toast.LENGTH_SHORT).show();
            }
            makeMove();
            place++;
            return;
        }
        makeMove();
        //Toast.makeText(getApplicationContext(), replay.listMoves.get(place), Toast.LENGTH_SHORT).show();
        place++;
    }

}