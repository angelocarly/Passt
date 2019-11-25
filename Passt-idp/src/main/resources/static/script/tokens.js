document.addEventListener('DOMContentLoaded', function () {
    var x = document.getElementsByClassName("revokeByJtiButton");
    var i;
    for (i = 0; i < x.length; i++) {
        var value = x[i].value;
        
        x[i].addEventListener('click', function click() {
            var jti = value;
            $.ajax({
                type: "POST",
                url: '/client/revoke',
                contentType: 'application/json',
                data: JSON.stringify({'jti': jti}),
                error: function (err) {
                    console.log(err);
                }
            })
        })
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('revokeAllButton').addEventListener('click', function click() {
        $.ajax({
            type: "POST",
            url: '/client/revokeall',
            error: function (err) {
                console.log(err);
            }
        })
    })
});
