package org.musailov.tictactoe.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musailov.tictactoe.dto.ConnectRequest;
import org.musailov.tictactoe.exceptions.GameNotFoundException;
import org.musailov.tictactoe.exceptions.GameNotValidException;
import org.musailov.tictactoe.models.Game;
import org.musailov.tictactoe.models.GameMove;
import org.musailov.tictactoe.models.Player;
import org.musailov.tictactoe.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestBody Player player) {
        log.info("game created for player: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(@RequestBody ConnectRequest connectRequest) throws GameNotValidException, GameNotFoundException {
        log.info("connect request: {}", connectRequest);
        return ResponseEntity.ok(gameService.connectToGame(connectRequest.getPlayer(), connectRequest.getGameId()));
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestBody GameMove gameMove) throws GameNotValidException, GameNotFoundException {
        log.info("game move: {}", gameMove);
        Game game = gameService.makeMove(gameMove);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
