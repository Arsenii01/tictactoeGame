package org.musailov.tictactoe.services;


import lombok.extern.slf4j.Slf4j;
import org.musailov.tictactoe.exceptions.GameNotFoundException;
import org.musailov.tictactoe.exceptions.GameNotValidException;
import org.musailov.tictactoe.models.*;
import org.musailov.tictactoe.storage.GameStorage;
import org.springframework.stereotype.Service;

import java.lang.management.ThreadInfo;
import java.util.UUID;

@Service
@Slf4j
public class GameService {

    private final int[][] winningCombinations = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };


    public Game createGame(Player player1) {
        Game game = new Game();
        game.setGameStatus(GameStatus.CREATED);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayerTurn(TicToe.X);
        game.setPlayer1(player1);
        game.setBoard(new int[3][3]);
        GameStorage.getInstance().addGame(game);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws GameNotFoundException, GameNotValidException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameNotFoundException("Игры с таким ID нет!");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game.getPlayer2() != null) {
            throw new GameNotValidException("Игра уже занята или уже закончилась!");
        }

        game.setPlayer2(player2);
        game.setGameStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().addGame(game);
        return game;
    }

    public Game makeMove(GameMove gameMove) throws GameNotFoundException, GameNotValidException {
        if (!GameStorage.getInstance().getGames().containsKey(gameMove.getGameId())) {
            throw new GameNotFoundException("Игры с таким ID нет!");
        }
        Game game = GameStorage.getInstance().getGames().get(gameMove.getGameId());
        if (game.getGameStatus().equals(GameStatus.FINISHED)) {
            throw new GameNotValidException("Игра уже закончилась!");
        }
        if (!game.getPlayerTurn().equals(gameMove.getType())) {
            return game;
        }
        int [][] board = game.getBoard();
        board[gameMove.getCoordX()][gameMove.getCoordY()] = gameMove.getType().getValue();

        // Изменяем игрока, которому принадлежит ход
        if (gameMove.getType().getValue() == 1) {
            game.setPlayerTurn(TicToe.O);
        } else {
            game.setPlayerTurn(TicToe.X);
        }

        // Перевод поля в массив из одной строки из единиц и двоек
        int [] boardInLine = new int[9];
        int index = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                boardInLine[index] = board[i][j];
                index += 1;
            }
        }
        StringBuffer s = new StringBuffer();
        for (int x: boardInLine) {
            s.append(x);
        }
        log.info("board: {}", s);


        boolean xWinner = checkWinner(boardInLine, TicToe.X);
        boolean oWinner = checkWinner(boardInLine, TicToe.O);

        if (xWinner) {
            game.setWinner(TicToe.X);
        } else if (oWinner) {
            game.setWinner(TicToe.O);
        }
        return game;
    }


    // Проверка на победителя
    public boolean checkWinner(int[] boardInLine, TicToe ticToe) {
        for (int[] i: winningCombinations) {
            if (boardInLine[i[0]] == ticToe.getValue() && boardInLine[i[1]] == ticToe.getValue()
                    && boardInLine[i[2]] == ticToe.getValue()) {
                return true;
            }
        }
        return false;
    }
}
