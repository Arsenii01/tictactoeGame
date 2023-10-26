package org.musailov.tictactoe.storage;

import org.musailov.tictactoe.models.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {
    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void addGame(Game newGame) {
        games.put(newGame.getGameId(), newGame);
    }


}
