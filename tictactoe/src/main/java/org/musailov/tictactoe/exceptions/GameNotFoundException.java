package org.musailov.tictactoe.exceptions;

public class GameNotFoundException extends Exception{
    private String message;
    public GameNotFoundException(String message) {
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
