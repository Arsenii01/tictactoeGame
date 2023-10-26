let turns = [["", "", ""], ["", "", ""], ["", "", ""]]
let turn = "X"
let cell_match = {
    "0": [0, 0],
    "1": [0, 1],
    "2": [0, 2],
    "3": [1, 0],
    "4": [1, 1],
    "5": [1, 2],
    "6": [2, 0],
    "7": [2, 1],
    "8": [2, 2]
}

function playerTurn(id) {
    let spotTaken = $("#" + id).text();
    if (spotTaken === "") {
        makeAMove(playerType, cell_match[id][0], cell_match[id][1]);
    }
}

function makeAMove(type, xCoordinate, yCoordinate) {
    gameId = gameId ? gameId : document.getElementById("game_id").value

    console.log(type)
    console.log(turn)
    if (turn !== type) {
        return;
    }
    $.ajax({
        url: url + "/game/move",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordX": xCoordinate,
            "coordY": yCoordinate,
            "gameId": gameId
        }),
        success: function (data) {
            if (type === 'X') {
                document.getElementById("opponent_name").innerText = "Ваш соперник: " + data.player2.login
            }
            turn = data.playerTurn
            console.log(turn)

        },
        error: function (error) {
            console.log(error);
        }
    })

}

function displayResponse(data) {
    turn = data.playerTurn

    let board = data.board;
    let id = 0
    let turnCount = 0;
    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            if (board[i][j] === 1) {
                turns[i][j] = 'X'
            } else if (board[i][j] === 2) {
                turns[i][j] = 'O';
            }
            if (board[i][j] !== 0) {
                turnCount += 1
            }
            $("#" + id).text(turns[i][j]);
            id += 1;
        }
    }

    if (data.winner == null && turnCount == 9) {
        document.getElementById("winnerMessage").innerText = "Ничья!"
        let drawCount = document.getElementById("drawsСount")
        drawCount.innerText = parseInt(drawCount.innerText) + 1
    } else if (data.winner != null) {
        document.getElementById("winnerMessage").innerText = "Победил: " + data.winner
        if (playerType == data.winner) {
            let winsCount = document.getElementById("winsCount")
            winsCount.innerText = parseInt(winsCount.innerText) + 1
        } else {
            let losesCount = document.getElementById("losesCount")
            losesCount.innerText = parseInt(losesCount.innerText) + 1
        }
    }
}

$(".cell").click(function () {
    let slot = $(this).attr('id');
    playerTurn(slot);
});

function reset() {
    turns = [["", "", ""], ["", "", ""], ["", "", ""]];
    document.getElementById("winnerMessage").innerText = ""
    $(".cell").text("");
}

$("#reset").click(function () {
    reset();
});