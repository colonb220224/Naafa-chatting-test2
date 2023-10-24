$(document).ready(function(){

    const username = $('#username').val()
    const urlParams = new URL(location.href).searchParams;
    const roomId = urlParams.get('roomId');

    const sockJs = new SockJS("/stomp/chat");
    //1. SockJS를 내부에 들고있는 stomp를 내어줌
    const stomp = Stomp.over(sockJs);

    //2. connection이 맺어지면 실행
    stomp.connect({}, function (){

        //4. subscribe(path, callback)으로 메세지를 받을 수 있음
        stomp.subscribe("/sub/chat/room/" + roomId, function (chat) {
            const content = JSON.parse(chat.body);
            console.log(content)
            console.log(content.writer)

            const writer = content.writer;
            const message = content.message;
            let str = '';

            if(writer === username){
                str = "<div class='col-6'>";
                str += "<div class='alert alert-secondary'>";
                str += "<b>" + writer + " : " + message + "</b>";
                str += "</div></div>";
            } else{
                str = "<div class='col-6'>";
                str += "<div class='alert alert-warning'>";
                str += "<b>" + writer + " : " + message + "</b>";
                str += "</div></div>";
            }
            $("#msgArea").append(str);
        });

        //3. send(path, header, message)로 메세지를 보낼 수 있음
        //stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, writer: username}))
        const roomName = document.getElementById("roomName")
        stomp.send('/pub/chat/message', {}, JSON.stringify({type:'ENTER', roomId: roomId, writer: username, roomName : roomName.value}))
    });

    $("#button-send").on("click", function(){
        const msg = document.getElementById("msg");
        const roomName = document.getElementById("roomName")

        stomp.send('/pub/chat/message', {}, JSON.stringify({type: 'TALK', roomId: roomId, message: msg.value, writer: username, roomName : roomName.value}));
        msg.value = '';
    });
});