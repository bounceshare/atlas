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
            "Access-Control-Allow-Origin" : "*",
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
                if(jqXHR != null && jqXHR.responseJSON != null && jqXHR.responseJSON.errorReason != null) {
                    showFailureMessage(jqXHR.responseJSON.errorReason);
                }
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

    function showLoader(flag) {
        console.log("showLoader() : " + flag)
        isLoading = flag;
        if(flag) {
            $('#progressBar')[0].hidden = false;
        } else {
            $('#progressBar')[0].hidden = true;
        }
    }

    function showSuccessMessage(message) {
        $('#successAlert')[0].hidden = false;
        $('#successAlert')[0].innerHTML = message;
        setTimeout(function() {
            $('#successAlert')[0].hidden = true;
        }, 2500);
    }

    function showFailureMessage(message) {
        $('#failureAlert')[0].hidden = false;
        $('#failureAlert')[0].innerHTML = message;
        setTimeout(function() {
            $('#failureAlert')[0].hidden = true;
        }, 2500);
    }

    function navBarClicks(path) {
        console.log("navBarClicks : " + path)
        if(typeof map !== 'undefined') {
            var pos = map.getCenter();
            var position = pos.lat + "," + pos.lng;

            if(path.includes("?")) {
                path += "&p=" + position;
            }else {
                path += "?p=" + position;
            }

            path += "&z=" + map.getZoom();
        }
        console.log("navBarClicks : " + path)
        window.location = path;
    }

    $(document).ready(function(){
        try {
            $('[data-toggle="popover"]').popover();
        } catch(err) {
        }

        $('#refreshCheckbox').change(function() {
            console.log("Refresh status changed : " + this.checked)
            if(this.checked) {
                refresh = true;
            } else {
                refresh = false;
            }

        });
    });

</script>