var Placeholder = {

    addMessage:function (message) {
        var alertTemplate = Handlebars.compile($("#template-alert-message").html());
        $("#message-holder").html(alertTemplate({message:message}));
        $("#alert-message").delay(2000).fadeOut(400, function() { $(this).remove(); })
    }
};

$(document).ready(function () {
    var feedbackMessage = $(".messageblock");
    if (feedbackMessage.length > 0) {
        Placeholder.addMessage(feedbackMessage.text());
    }
});




