package org.musailov.tictactoe.models;

import lombok.Data;


@Data
public class Game {
    private String gameId;

    private TicToe playerTurn;
    private GameStatus gameStatus;

    private Player player1;
    private Player player2;

    private int[][] board;
    private TicToe winner;

}
