package com.example.jinziqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    public enum Player {X,O};

    public class Cell{
        private Player value;

        public Player getValue(){
            return value;
        }

        public void setValue(Player value){
            this.value = value;
        }

    }

    private Cell[][] cells = new Cell[3][3];
    private Player winner;
    private GameState state;
    private Player currentTurn;
    private enum GameState {IN_PROGRESS,FINISHED};

    private ViewGroup buttonGroup;
    private View winnerPlayerViewGroup;
    private TextView winnerPlayerLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        winnerPlayerLabel = (TextView)findViewById(R.id.winnerPlayerLabel);
        winnerPlayerViewGroup = findViewById(R.id.winnerPlayerViewGroup);
        buttonGroup = (ViewGroup)findViewById(R.id.buttonGroup);
        restart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jingziqi,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_reset){
            restart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onCellClicked(View v){
        Button button = (Button) v;
        String tag = button.getTag().toString();
        int row = Integer.valueOf(tag.substring(0,1));
        int col = Integer.valueOf(tag.substring(1,2));
        Log.e(TAG,"click row =="+row + ", col == "+ col);
        Player playerThatMoved = mark(row,col);
        if(playerThatMoved !=null){
            button.setText(playerThatMoved.toString());
            if(getWiner()!=null){
                winnerPlayerLabel.setText(playerThatMoved.toString());
                winnerPlayerViewGroup.setVisibility(View.VISIBLE);
            }
        }
    }

    private Player getWiner() {
        return winner;
    }

    public void restart(){
        clearCells();
        winner = null;
        currentTurn = Player.X;
        state = GameState.IN_PROGRESS;

        winnerPlayerViewGroup.setVisibility(View.GONE);
        winnerPlayerLabel.setText("");

        for (int i = 0 ; i < buttonGroup.getChildCount();  i++){
            ((Button)buttonGroup.getChildAt(i)).setText("");
        }

    }

    private void clearCells() {
        for (int i=0; i<3;i++){
            for (int j=0;j<3;j++){
                cells[i][j] = new Cell();
            }
        }
    }


    public Player mark(int row, int col){
        Player playerThatMoved = null;
        if(isValid(row, col)){
            cells[row][col].setValue(currentTurn);
            playerThatMoved = currentTurn;

            if(isWinningMoveByPlayer(currentTurn,row,col)){
                state = GameState.FINISHED;
                winner = currentTurn;
            }else {
                //切换另一个棋手
                flipCurrentTurn();
            }
        }
        return playerThatMoved;
    }

    private boolean isValid(int row, int col) {
        if(state == GameState.FINISHED){
            return false;
        }else if(isOutOfBounds(row) || isOutOfBounds(col)) {
            return false;
        } else if(isCellValueAlreadySet(row,col)){
            return false;
        } else {
            return true;
        }
    }

    private boolean isOutOfBounds(int ids){
        return ids< 0 || ids>2;
    }

    private boolean isCellValueAlreadySet(int row,int col){
        return cells[row][col].getValue() !=null;
    }


    private boolean isWinningMoveByPlayer(Player player,int currentRow,int currentCol){
        return (
                cells[currentRow][0].getValue() == player //3行
                &&cells[currentRow][1].getValue() == player
                && cells[currentRow][2].getValue() == player
                || cells[0][currentCol].getValue() == player //3列
                && cells[1][currentCol].getValue() == player
                && cells[2][currentCol].getValue() == player
                || currentCol == currentRow
                && cells[0][0].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][2].getValue() == player
                || currentCol + currentRow == 2
                && cells[0][2].getValue() == player
                && cells[1][1].getValue() == player
                && cells[2][0].getValue() == player

        );

    }


    private void flipCurrentTurn(){
        currentTurn = currentTurn == Player.X ? Player.O : Player.X ;
    }

}
