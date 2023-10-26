const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;
function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/move");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}

function create_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        document.getElementById("gameIdMessage").innerText = "Пожалуйста, введите логин!"
    } else {
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                reset();
                connectToSocket(gameId);
                document.getElementById("gameIdMessage").innerText = "Код комнаты: " + data.gameId;

            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function connectToSpecificGame() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        document.getElementById("gameIdMessage").innerText = "Пожалуйста, введите логин"
    } else {
        document.getElementById("gameIdMessage").innerText = ""
        gameId = document.getElementById("game_id").value;
        if (gameId == null || gameId === '') {
            document.getElementById("gameIdMessage").innerText = "Пожалуйста, введите id комнаты"
        }
        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "player": {
                    "login": login
                },
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = "O";
                reset();
                connectToSocket(gameId);
                document.getElementById("opponent_name").innerText = "Ваш соперник: " + data.player1.login
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}