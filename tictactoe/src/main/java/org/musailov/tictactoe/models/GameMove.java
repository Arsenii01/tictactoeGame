package org.musailov.tictactoe.models;

import lombok.Data;

@Data
public class GameMove {
    private TicToe type;

    private int coordX;
    private int coordY;
    private String gameId;
}
