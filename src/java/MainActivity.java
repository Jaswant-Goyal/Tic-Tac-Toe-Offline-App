package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameActive = true;
    private TextView statusTextView;
    private Button resetButton;

    // Winning combinations (rows, columns, diagonals)
    private final int[][] winCombos = {
            {0,0,0,1,0,2}, {1,0,1,1,1,2}, {2,0,2,1,2,2}, // rows
            {0,0,1,0,2,0}, {0,1,1,1,2,1}, {0,2,1,2,2,2}, // columns
            {0,0,1,1,2,2}, {0,2,1,1,2,0}                 // diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());

        // Initialize buttons grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        resetGame();
    }

    @Override
    public void onClick(View v) {
        if (!gameActive) return;

        // Find which button was clicked
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (v.getId() == buttons[i][j].getId()) {
                    if (board[i][j] == '\u0000') {
                        makeMove(i, j);
                    }
                    break;
                }
            }
        }
    }

    private void makeMove(int row, int col) {
        board[row][col] = currentPlayer;
        updateButtonUI(row, col);

        // Check win or tie
        if (checkWin()) {
            statusTextView.setText("Player " + currentPlayer + " Wins! 🎉");
            gameActive = false;
            highlightWinningCells();
            return;
        }

        if (isBoardFull()) {
            statusTextView.setText("Game is a Tie! 🤝");
            gameActive = false;
            return;
        }

        // Switch player
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusTextView.setText("Player " + currentPlayer + "'s Turn");
    }

    private void updateButtonUI(int row, int col) {
        Button btn = buttons[row][col];
        btn.setText(String.valueOf(currentPlayer));
        // Set different colors for X and O
        if (currentPlayer == 'X') {
            btn.setTextColor(getColor(android.R.color.holo_blue_dark));
        } else {
            btn.setTextColor(getColor(android.R.color.holo_orange_dark));
        }
    }

    private boolean checkWin() {
        for (int[] combo : winCombos) {
            int r1 = combo[0], c1 = combo[1];
            int r2 = combo[2], c2 = combo[3];
            int r3 = combo[4], c3 = combo[5];

            if (board[r1][c1] == currentPlayer &&
                    board[r2][c2] == currentPlayer &&
                    board[r3][c3] == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    private void highlightWinningCells() {
        for (int[] combo : winCombos) {
            int r1 = combo[0], c1 = combo[1];
            int r2 = combo[2], c2 = combo[3];
            int r3 = combo[4], c3 = combo[5];

            if (board[r1][c1] == currentPlayer &&
                    board[r2][c2] == currentPlayer &&
                    board[r3][c3] == currentPlayer) {
                // Highlight the three winning cells
                buttons[r1][c1].setBackgroundResource(R.drawable.cell_highlight_bg);
                buttons[r2][c2].setBackgroundResource(R.drawable.cell_highlight_bg);
                buttons[r3][c3].setBackgroundResource(R.drawable.cell_highlight_bg);
                break;
            }
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\u0000') return false;
            }
        }
        return true;
    }

    private void resetGame() {
        // Clear board array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\u0000';
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundResource(R.drawable.cell_background_selector);
            }
        }
        currentPlayer = 'X';
        gameActive = true;
        statusTextView.setText("Player X's Turn");
    }
}