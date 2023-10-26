package org.musailov.tictactoe.dto;

import lombok.Data;
import org.musailov.tictactoe.models.Player;

@Data
public class ConnectRequest {
    private String gameId;
    private Player player;
}
