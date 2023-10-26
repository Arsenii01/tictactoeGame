package org.musailov.tictactoe.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicToe {
    X(1), O(2);

    private int value;
}
