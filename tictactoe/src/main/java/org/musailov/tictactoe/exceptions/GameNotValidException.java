package org.musailov.tictactoe.exceptions;

public class GameNotValidException extends Exception {
    private String message;
    public GameNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
