package com.example.tim.mobilechessapp12;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.content.res.Resources;
import android.widget.ListView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import Chess.logic.chess.Color;
import Chess.logic.chess.CurrentGame;
import Chess.logic.chess.TypeOfMove;

public class OppeartionCenter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String firstSelected = null;
    public static String secondSelected = null;
    public static ImageButton firstSelectedTile;
    public static ImageButton secondSelectedTile;
    public static Drawable firstSelectedColor;
    public static Drawable secondSelectedColor;


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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Button resign = (Button)findViewById(R.id.resign);



        try {
            if (!readIt("data.dat")) {
                Toast.makeText(getApplicationContext(), "No current active game found", Toast.LENGTH_SHORT).show();
                currentGame = new CurrentGame();
            }
            else {
                //it goes on to initialize the board from scratch anyway after the getresources line
                Toast.makeText(getApplicationContext(), "Found current game", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            return;
        }


        resources = getResources();
        try {
            initializeBoard();
        }
        catch (Exception e) {
        }

    }

    public void resign(View v) {
        //Toast.makeText(getApplicationContext(), "Hey we resigned so well", Toast.LENGTH_LONG).show();
        if (currentGame.finished) {
            return;
        }
        if (currentGame.currentBoard.turn.equals(Color.White)) {
            Toast.makeText(getApplicationContext(), "White resigned. Black wins!", Toast.LENGTH_LONG).show();
        }
        if (currentGame.currentBoard.turn.equals(Color.Black)) {
            Toast.makeText(getApplicationContext(), "Black resigned. White wins!", Toast.LENGTH_LONG).show();
        }
        currentGame.resign();
        saveGameTitleOrNah();

    }

    public void randomMove(View v) {
        try {
            if (currentGame.finished) {
                return;
            }

            CurrentGame temp = readCurrentGameFile("data.dat");
            TypeOfMove made = null;
            made = currentGame.makeAImove();
            Toast.makeText(getApplicationContext(), "First ele " + currentGame.currentBoard.whiteKing.team.get(0) + " and black " + currentGame.currentBoard.blackKing.team.get(0), Toast.LENGTH_SHORT).show();
            if (made != TypeOfMove.INVALID && made != null) {
                writeToUndo(temp);
                Toast.makeText(getApplicationContext(), "Random move generated", Toast.LENGTH_SHORT).show();
                redrawBoard();
                writeIt("data.dat");

                if (currentGame.currentBoard.whiteKing.checkmate() ) {
                    Toast.makeText(getApplicationContext(), "Checkmate! Black wins!", Toast.LENGTH_LONG).show();
                    saveGameTitleOrNah();
                }

                if (currentGame.currentBoard.blackKing.checkmate()) {
                    Toast.makeText(getApplicationContext(), "Checkmate! White wins!", Toast.LENGTH_LONG).show();
                    saveGameTitleOrNah();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Can't generate random move generated", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Failed to Record Random Move", Toast.LENGTH_SHORT).show();
        }

    }
    //need to put in prompt for accept draw
    public void draw(View v) {
        if (currentGame.finished) {
            return;
        }
        new AlertDialog.Builder(this)
        .setTitle("Title")
                .setMessage("Draw requested. Accept?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        currentGame.draw();
                        Toast.makeText(getApplicationContext(), "draw", Toast.LENGTH_LONG).show();
                        saveGameTitleOrNah();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void undoMove(View v) throws Exception {
        if (currentGame.finished) {
            Toast.makeText(getApplicationContext(), "Game is already over. Can't undo.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (readIt("Undo.dat")){
            writeIt("data.dat");
            Toast.makeText(getApplicationContext(), "Move undone", Toast.LENGTH_SHORT).show();
            redrawBoard();
        }
        else {
            Toast.makeText(getApplicationContext(), "Can't undo!", Toast.LENGTH_SHORT).show();
        }
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
            if (currentGame.currentBoard.turnNum != 1) {
                saveGameTitleOrNah();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.old_game_btn) {
            try {
                writeIt("data.dat");

                Intent intent = new Intent(this, SavedGames.class);
                startActivity(intent);

            }
            catch (Exception e) {
                return false;
            }

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void initializeBoard() throws Exception{
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

                        OppeartionCenter.firstSelectedColor = OppeartionCenter.firstSelectedTile.getBackground();
                        OppeartionCenter.firstSelectedTile.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bluish, null));
                    } else if (OppeartionCenter.firstSelected != null) {
                        OppeartionCenter.secondSelectedTile = (ImageButton) v;
                        OppeartionCenter.secondSelected = getResources().getResourceEntryName(v.getId());

                        OppeartionCenter.secondSelectedColor = OppeartionCenter.secondSelectedTile.getBackground();
                        OppeartionCenter.secondSelectedTile.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bluish, null));

                        if (firstSelectedTile.equals(secondSelectedTile)) {
                            OppeartionCenter.firstSelectedTile.setBackground(OppeartionCenter.firstSelectedColor);
                            OppeartionCenter.firstSelectedTile = null;
                            OppeartionCenter.secondSelectedTile = null;
                            OppeartionCenter.firstSelected = null;
                            OppeartionCenter.secondSelected = null;

                            return;
                        }
                        makeMove();

                        OppeartionCenter.firstSelectedTile.setBackground(OppeartionCenter.firstSelectedColor);
                        OppeartionCenter.secondSelectedTile.setBackground(OppeartionCenter.secondSelectedColor);

                        OppeartionCenter.firstSelectedTile = null;
                        OppeartionCenter.secondSelectedTile = null;
                        OppeartionCenter.firstSelected = null;
                        OppeartionCenter.secondSelected = null;
                        OppeartionCenter.firstSelectedColor = null;
                        OppeartionCenter.secondSelectedColor = null;
                    } else if (firstSelected.equals(getResources().getResourceEntryName(v.getId()))) {
                        OppeartionCenter.firstSelectedTile = null;
                        OppeartionCenter.secondSelectedTile = null;
                        OppeartionCenter.firstSelected = null;
                        OppeartionCenter.secondSelected = null;
                    }
                }
            });
        }

        redrawBoard();
    }


    public void makeMove(){
        if (currentGame.finished) {
            Toast.makeText(getApplicationContext(), "Current Game is already finished", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            CurrentGame temp = readCurrentGameFile("data.dat");
            TypeOfMove madeMove = currentGame.makeMove(firstSelected + " " + secondSelected);

            if(madeMove != TypeOfMove.INVALID){
                writeToUndo(temp);
            }

            if(madeMove == TypeOfMove.VALID){
                secondSelectedTile.setImageResource((Integer)firstSelectedTile.getTag());
                secondSelectedTile.setTag(firstSelectedTile.getTag());

                firstSelectedTile.setImageResource(android.R.color.transparent);
                firstSelectedTile.setTag(android.R.color.transparent);
            } else if(madeMove == TypeOfMove.EN_PASSANT || madeMove == TypeOfMove.PROMOTION || madeMove == TypeOfMove.CASTLE_LEFT || madeMove == TypeOfMove.CASTLE_RIGHT){// redraw 2 files
                redrawBoard();
            }
            else if (madeMove == TypeOfMove.INVALID) {
                Toast.makeText(getApplicationContext(), "Illegal move", Toast.LENGTH_SHORT).show();
            }
            if (currentGame.currentBoard.whiteKing.checkmate() ) {
                Toast.makeText(getApplicationContext(), "Checkmate! Black wins!", Toast.LENGTH_LONG).show();
                saveGameTitleOrNah();
            }
            else if (currentGame.currentBoard.blackKing.checkmate()) {
                Toast.makeText(getApplicationContext(), "Checkmate! White wins!", Toast.LENGTH_LONG).show();
                saveGameTitleOrNah();
            }
            else if (currentGame.currentBoard.whiteKing.inCheck() ) {
                Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_LONG).show();
            }
            else if (currentGame.currentBoard.blackKing.inCheck()) {
                Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_LONG).show();
            }

            Boolean t = writeIt("data.dat");
            if(!t){
                Toast.makeText(getApplicationContext(), "Saving Move Failed", Toast.LENGTH_SHORT).show();
            }
            //currentGame.writeGame("currentGame");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Saving Move Crashed", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveGameTitleOrNah() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a title to save the game");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String m_Text = input.getText().toString();
                try {
                    Toast.makeText(getApplicationContext(), "Saved " + m_Text , Toast.LENGTH_SHORT).show();
                    currentGame.finished = true;
                    if (writeIt(m_Text)) {
                        currentGame = new CurrentGame();
                        redrawBoard();
                        writeIt("data.dat");
                        writeIt("Undo.dat");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to write finished game " + m_Text , Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel and Start New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    currentGame = new CurrentGame();
                    redrawBoard();
                    writeIt("data.dat");
                    writeIt("Undo.dat");
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to restart new game", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });

        builder.show();
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
    private boolean readIt(String title) {
         try{
            File file = null;
            File[] listOfFiles = getFilesDir().listFiles();
            for (File f : listOfFiles) {
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
            currentGame = (CurrentGame) oi.readObject();
            oi.close();
            fi.close();
            if (currentGame.finished) {
                return false;
            }
            return true;
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Something Broke in checking for last game", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean writeIt(String title) throws Exception{
        File file = new File(getApplicationContext().getFilesDir(), title);

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            currentGame.dateSaved = dateFormat.format(date);

            out.writeObject(currentGame);
            out.close();
            fileOut.close();


            //Toast.makeText(getApplicationContext(), "test saved", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to write to " + title, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private CurrentGame readCurrentGameFile(String title){
        try
        {
            File file = null;
            File[] listOfFiles = getFilesDir().listFiles();
            for (File f: listOfFiles) {
                if (f.getName().equals(title)) {
                    file = f;
                    break;
                }
            }
            if (file == null) {
                return null;
            }

            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            CurrentGame temp = (CurrentGame)oi.readObject();
            oi.close();
            fi.close();

            return temp;
        }
        catch(Exception e){
            return null;
        }
    }

    private boolean writeToUndo(CurrentGame game) throws Exception{
        File file = new File(getApplicationContext().getFilesDir(), "Undo.dat");

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            game.dateSaved = dateFormat.format(date);

            out.writeObject(game);
            out.close();
            fileOut.close();


            //Toast.makeText(getApplicationContext(), "test saved", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to write to " + "Undo.dat", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
