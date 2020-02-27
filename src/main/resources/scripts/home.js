<script>
    var center = [12.9160463,77.5967117]
    var map = L.map('mapDiv').setView(center, 17);
    var markers = []

    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1Ijoic3VzaGVlbGsiLCJhIjoiY2s3NHR3YjN1MDhxOTNrcGxreGM2bmxwdiJ9.h0asAA-St15DH7sCIc0drw', {
        maxZoom: 24,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox/streets-v11',
        tileSize: 512,
        zoomOffset: -1
    }).addTo(map);

    var bikeIdleIcon = L.icon({
        iconUrl: 'resources/icons/scooter_idle.png',
        iconSize:     [55, 35], // size of the icon
    });

    var bikeBusyIcon = L.icon({
        iconUrl: 'resources/icons/scooter_busy.png',
        iconSize:     [55, 35], // size of the icon
    });

    var bikeOOSIcon = L.icon({
        iconUrl: 'resources/icons/scooter_oos.png',
        iconSize:     [55, 35], // size of the icon
    });

    refreshBikes(center)

    function addBikeToMap(bike) {
        bikeIcon = bikeIdleIcon;
        switch(bike.status) {
            case "busy":
                bikeIcon = bikeBusyIcon;
                break;
            case "idle":
                bikeIcon = bikeIdleIcon;
                break;
            case "oos":
                bikeIcon = bikeOOSIcon;
                break;
        }
        marker = L.marker([bike.lat, bike.lon],{icon: bikeIcon});
        marker.addTo(map)
        if(bike != null) {
            marker.text = bike.license_plate + " / " + bike.type;
            marker.alt = bike.license_plate + " / " + bike.type;
        }
        markers.push(marker)
    }

    function fetchBikes(coords) {

    }

    function clearBikeMarkers() {
        for(var i = 0; i < markers.length; i++){
            map.removeLayer(markers[i]);
        }
    }

    function refreshBikes(coords) {
        data = {};
        data.lat = coords[0]
        data.lon = coords[1]
        data.limit = 20
        httpPost("/bikes/listing", data, function(response) {
            if(response != null) {
                bikes = response.data.bikes;
                clearBikeMarkers();
                for(var i = 0; i < bikes.length; i++) {
                    addBikeToMap(bikes[i]);
                }
            }
        })
    }

    function httpPost(path, data, callback) {
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
            callback(response)
          }
        });
    }
</script>