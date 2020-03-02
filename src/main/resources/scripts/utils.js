<script>
    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

    function parseBoolean(string) {
        switch (string.toLowerCase().trim()) {
            case "true": case "yes": case "1": return true;
            case "false": case "no": case "0": case null: return false;
            default: return Boolean(string);
        }
    }

    function getPoints(jPoint) {
        points = [];
        for(var i = 0; i < jPoint.length; i++) {
            coords = [];
            coords.push(jPoint[i].lat, jPoint[i].lon);
            points.push(coords);
        }
        return points;
    }

    function getToken() {
        for(count in document.cookie.split(";")) {
            var cookie = document.cookie.split(";")[count];
            if(cookie.trim().split("=")[0] == "token") {
                return cookie.trim().split("=")[1];
            }
        }
    }

    function httpPost(path, data, callback, error) {
        console.log("POST Request : " + path)

        token = getToken();
        var settings = {
          "async": true,
          "crossDomain": true,
          "url": path,
          "method": "POST",
          "headers": {
            "Content-Type": "application/json",
            "cache-control": "no-cache",
            "token": token
          },
          "processData": false,
          "data": JSON.stringify(data)
        }

        $.ajax(settings).done(function (response) {
          console.log(response);
          if(callback != null) {
            callback(response);
          }
        })
        .fail(function (jqXHR, exception) {
            // Our error logic here
            console.log("Error in httpPost");
            if(error != null) {
                error(jqXHR, exception);
            }
        });
    }

    function signOut() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
            console.log('User signed out.');
            document.cookie.split(";").forEach(function(c) { document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); });
            window.location = "/login";
        });
    }

    function onSignIn(googleUser) {
        var profile = googleUser.getBasicProfile();
        console.log("Logged in successfully as : " + profile.getEmail());
        var cookie = "token=" + gapi.auth2.getAuthInstance().currentUser.get().getAuthResponse().id_token;
        document.cookie = cookie;

        if(window.location.pathname.startsWith('/login')) {
            window.location = "/";
        }
    }

    $(document).ready(function(){
        try {
            $('[data-toggle="popover"]').popover();
        } catch(err) {
        }
    });

</script>