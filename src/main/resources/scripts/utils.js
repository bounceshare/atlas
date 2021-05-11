<script>

    var isSidebarOpen = true;
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

    function uuid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    }

    function getToken() {
        for(count in document.cookie.split(";")) {
            var cookie = document.cookie.split(";")[count];
            if(cookie.trim().split("=")[0] == "token") {
                return cookie.trim().split("=")[1];
            }
        }
    }

    function distance(lat1, lon1, lat2, lon2, unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            var radlat1 = Math.PI * lat1/180;
            var radlat2 = Math.PI * lat2/180;
            var theta = lon1-lon2;
            var radtheta = Math.PI * theta/180;
            var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
            if (dist > 1) {
                dist = 1;
            }
            dist = Math.acos(dist);
            dist = dist * 180/Math.PI;
            dist = dist * 60 * 1.1515;
            if (unit=="K") { dist = dist * 1.609344 }
            if (unit=="N") { dist = dist * 0.8684 }
            return dist;
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

    function setupDateTimeElement() {
        if($('input[type="datetime-local"]') && $('input[type="datetime-local"]').length > 0 ) {
            $('input[type="datetime-local"]').datetimepicker({format: "YYYY-MM-DDTHH:mm"});
        }
        if($('input[type="date"]') && $('input[type="date"]').length > 0 ) {
            $('input[type="date"]').datetimepicker({format: "YYYY-MM-DD"});
        }
        if($('input[type="datetime"]') && $('input[type="datetime"]').length > 0 ) {
            $('input[type="datetime"]').datetimepicker({format: "YYYY-MM-DDTHH:mm"});
        }
    }

    jQuery(function ($) {

    	$(".sidebar-dropdown > a").click(function () {
    		$(".sidebar-submenu").slideUp(200);
    		if (
    			$(this)
    			.parent()
    			.hasClass("active")
    		) {
    			$(".sidebar-dropdown").removeClass("active");
    			$(this)
    				.parent()
    				.removeClass("active");
    		} else {
    			$(".sidebar-dropdown").removeClass("active");
    			$(this)
    				.next(".sidebar-submenu")
    				.slideDown(200);
    			$(this)
    				.parent()
    				.addClass("active");
    		}
    	});

    	$("#close-sidebar").click(function () {
    		$(".page-wrapper").removeClass("toggled");
    	});
    	$("#show-sidebar").click(function () {
    		$(".page-wrapper").addClass("toggled");
    	});


    });

    function toggleSidebar() {

    }

    function isJson(str) {
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    }

    function toggleSidebar() {
        if(isSidebarOpen) {
            $(".page-wrapper").removeClass("toggled");
        }
        else {
            $(".page-wrapper").addClass("toggled");
        }
        isSidebarOpen = !isSidebarOpen;
    }

    $("#toggleSidebarButton").click(function(e) {
//      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });

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

        setupDateTimeElement();

        $("#wrapper").toggleClass("toggled");

        // Burger menus
        document.addEventListener('DOMContentLoaded', function() {
            // open
            const burger = document.querySelectorAll('.navbar-burger');
            const menu = document.querySelectorAll('.navbar-menu');

            if (burger.length && menu.length) {
                for (var i = 0; i < burger.length; i++) {
                    burger[i].addEventListener('click', function() {
                        for (var j = 0; j < menu.length; j++) {
                            menu[j].classList.toggle('hidden');
                        }
                    });
                }
            }

            // close
            const close = document.querySelectorAll('.navbar-close');
            const backdrop = document.querySelectorAll('.navbar-backdrop');

            if (close.length) {
                for (var i = 0; i < close.length; i++) {
                    close[i].addEventListener('click', function() {
                        for (var j = 0; j < menu.length; j++) {
                            menu[j].classList.toggle('hidden');
                        }
                    });
                }
            }

            if (backdrop.length) {
                for (var i = 0; i < backdrop.length; i++) {
                    backdrop[i].addEventListener('click', function() {
                        for (var j = 0; j < menu.length; j++) {
                            menu[j].classList.toggle('hidden');
                        }
                    });
                }
            }
        });

    });

</script>