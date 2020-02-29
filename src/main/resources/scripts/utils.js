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

    function httpPost(path, data, callback) {
        console.log("POST Request : " + path)
        var settings = {
          "async": true,
          "crossDomain": true,
          "url": path,
          "method": "POST",
          "headers": {
            "Content-Type": "application/json",
            "cache-control": "no-cache"
          },
          "processData": false,
          "data": JSON.stringify(data)
        }

        $.ajax(settings).done(function (response) {
          console.log(response);
          if(callback != null) {
            callback(response);
          }
        });
    }

</script>