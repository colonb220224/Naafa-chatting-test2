$(document).ready(function(){

    // var roomName = [[${room.name}]];
    // var roomId = [[${room.roomId}]];
    // var username = [[${#authentication.principal.username}]];

    // console.log(roomName + ", " + roomId + ", " + username);

    var username = "유저";
    var urlParams = new URL(location.href).searchParams;
    var roomId = urlParams.get('roomId');

    var sockJs = new SockJS("/stomp/chat");
    //1. SockJS를 내부에 들고있는 stomp를 내어줌
    var stomp = Stomp.over(sockJs);

    //2. connection이 맺어지면 실행
    stomp.connect({}, function (){
        console.log("STOMP Connection")

        //4. subscribe(path, callback)으로 메세지를 받을 수 있음
        stomp.subscribe("/sub/chat/room/" + roomId, function (chat) {
            // for(let i=0; i<res.data.length; i++) {
            //     sentenceSetHtml += `
            //                     <li>
            //                         <span>·</span>${res.data[i].SENTENCE}
            //                     </li>
            //           `
            // }
            // sentenceSetHtml += `</ul></div>`
            // $('#sentenceAppend').html(sentenceSetHtml)
            console.log(chat)
            var content = JSON.parse(chat.body);

            var writer = content.writer;
            var message = content.message;
            var str = '';

            // if(writer === username){
            //     str = "<div class='col-6'>";
            //     str += "<div class='alert alert-secondary'>";
            //     str += "<b>" + writer + " : " + message + "</b>";
            //     str += "</div></div>";
            //     $("#msgArea").append(str);
            // }
            // else{
                str = "<div class='col-6'>";
                str += "<div class='alert alert-warning'>";
                str += "<b>" + writer + " : " + message + "</b>";
                str += "</div></div>";
                $("#msgArea").append(str);
            // }

            // $("#msgArea").append(str);
        });

        //3. send(path, header, message)로 메세지를 보낼 수 있음
        //stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, writer: username}))
        var roomName = document.getElementById("roomName")
        stomp.send('/pub/chat/message', {}, JSON.stringify({type:'ENTER', roomId: roomId, writer: username, roomName : roomName.value}))
    });

    $("#button-send").on("click", function(){
        var msg = document.getElementById("msg");
        var roomName = document.getElementById("roomName")

        console.log(username + ":" + msg.value);
        stomp.send('/pub/chat/message', {}, JSON.stringify({type: 'TALK', roomId: roomId, message: msg.value, writer: username, roomName : roomName.value}));
        msg.value = '';
    });
});