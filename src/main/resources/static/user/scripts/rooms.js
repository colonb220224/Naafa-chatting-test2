$(document).ready(function(){

    const sockJs = new SockJS("/stomp/chat");
    //1. SockJS를 내부에 들고있는 stomp를 내어줌
    const stomp = Stomp.over(sockJs);

    //2. connection이 맺어지면 실행
    stomp.connect({}, function (){
        stomp.subscribe("/sub/chat/roomList", function(chat){
            const content = JSON.parse(chat.body);
            const name = content.name;

            let str = "<li>";
            str += "<a href='/chat/room(roomId=${room.roomId}'>" + name + "</a>";
            str += "</li>";
            $("#msgArea").append(str);
        });
    });

    $("#btn-create").on("click", function (e){
        e.preventDefault();
        const name = $("input[name='name']").val();
        if(name == "")
            alert("Please write the name.")
        else
            stomp.send('/pub/chat/roomList', {}, JSON.stringify({name : name}))
    });
});
