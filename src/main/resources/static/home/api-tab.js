$(function(){
    $(".serve_nav").find("a").each(function(){
        $(this).click(function(){
            $(this).parent().find("a").each(function(){
                $(this).removeClass("on");
                $("div[rel="+this.id+"]").hide();
            });
            $(this).addClass("on");
            $("div[rel="+this.id+"]").show();
        });

    });
});