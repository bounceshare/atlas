<script>
    var center = [12.9160463,77.5967117];
    var map = L.map('mapDiv').setView(center, 17);
    var markers = [];
    map.on('moveend', onMapEvent);
    isLoading = false;

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

    refreshBikes(center);

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
        var popupInfo = "<b>" + bike.id + "</b><br/>" + bike.license_plate + "<br/>" + bike.type;
        if(bike.status == "oos") {
            popupInfo += "<br/>" + bike.oos_reason;
        }
        marker.addTo(map).bindPopup(popupInfo);
        if(bike != null) {
            marker.text = bike.license_plate + " / " + bike.type;
            marker.alt = bike.license_plate + " / " + bike.type;
        }
        markers.push(marker);
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
        data.limit = 50
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost("/apis/listing", data, function(response) {
            if(response != null) {
                bikes = response.data.bikes;
                clearBikeMarkers();
                for(var i = 0; i < bikes.length; i++) {
                    addBikeToMap(bikes[i]);
                }
                showLoader(false);
            } else {
                showLoader(false);
            }
        })
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

    function onMapEvent(event) {
        console.log("MapEvent");
        console.log(event);
        search(true);
    }

    function search(isMapEvent) {
        if(isLoading) {
            return;
        }
        searchQuery = $('#searchBar')[0].value;
        if(isMapEvent || searchQuery == '' || searchQuery == null) {
            console.log("Empty search query. So searching wherever the map is active")
            pos = map.getCenter();
            center = [pos.lat, pos.lng];
        } else {
            if(searchQuery.includes(",")) {
                $('#searchBar')[0].value = "";
                splits = searchQuery.split(",");
                if(splits.length == 2) {
                    center[0] = parseFloat(splits[0]);
                    center[1] = parseFloat(splits[1]);
                    map.setView(center, 17);
                }
            }
        }
        refreshBikes(center);
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
</script>