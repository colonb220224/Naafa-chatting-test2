$(document).ready(function(){

    $(".btn-create").on("click", function (e){
        e.preventDefault();
        const name = $("input[name='name']").val();
        if(name == "")
            alert("Please write the name.")
        else
            $("form").submit();
    });
});
