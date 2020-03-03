<script>

    function refreshLayers(coords, radius) {
        console.log("refreshLayers()");
        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = radius;

        data.q = query;
        if(isLoading) {
            console.log("Not refreshing cause already loading");
            return;
        }
        showLoader(true);

        httpPost("/apis/search", data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
             showLoader(false);
         });
    }

    function onMapEvent(event) {
        console.log("Layers onMapEvent");
        console.log(event);

        if(isLoading) {
            return;
        }

        setTimeout(function() {
            searchLayers(true);
        }, 1500);
    }

    function searchLayers(isMapEvent) {
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
                    center = [];
                    center[0] = parseFloat(splits[0]);
                    center[1] = parseFloat(splits[1]);
                    map.setView(center, 17);
                }
            } else {
                return;
            }
        }
        radius = DEFAULT_RADIUS;
        if(isMapEvent) {
            radius = getMapRadiusInMeters();
        }
        refreshLayers(center, radius);
    }

    refreshLayers(DEFAULT_CENTRE, DEFAULT_RADIUS);
    map.on('moveend', onMapEvent);

</script>