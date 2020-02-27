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

        iconSize:     [90, 90], // size of the icon
    });

    var bikeBusyIcon = L.icon({
        iconUrl: 'resources/icons/scooter_busy.png',

        iconSize:     [90, 90], // size of the icon
    });

    var bikeOOSIcon = L.icon({
        iconUrl: 'resources/icons/scooter_oos.png',
        iconSize:     [90, 90], // size of the icon
    });

    addBike(center, "idle");

    function addBikeToMap(coords, state) {
        bikeIcon = bikeIdleIcon;
        switch(state) {
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
        marker = L.marker(coords,{icon: bikeIcon});
        marker.addTo(map)
        markers.push(marker)
    }

    function fetchBikes(coords) {

    }

    function clearBikeMarkers() {
        for(var i = 0; i < markers.length; i++){
            map.removeLayer(markers[i]);
        }
    }
</script>