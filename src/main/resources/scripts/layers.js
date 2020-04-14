<script>

    function refreshLayers(coords, radius) {
        console.log("refreshLayers()");
        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = radius;

        data.searchQuery = query;
        if(isLoading && !refresh) {
            console.log("Not refreshing cause already loading");
            return;
        }
        showLoader(true);

        var searchUrl = $('#freemarker_searchurl')[0].innerText;

        httpPost(searchUrl, data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.isSidebar, false, response.data.autoRefresh);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
             showLoader(false);
         });
    }

    function onMapEvent(event) {
        console.log("Layers onMapEvent");
        console.log(event);

        if(isLoading || !refresh) {
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
        console.log("Empty search query. So searching wherever the map is active")
        pos = map.getCenter();
        center = [pos.lat, pos.lng];
        radius = DEFAULT_RADIUS;
        if(isMapEvent) {
            radius = getMapRadiusInMeters();
        }
        refreshLayers(center, radius);
    }

    if(refresh || query.length > 1) {
        refreshLayers(DEFAULT_CENTRE, DEFAULT_RADIUS);
    }
    map.on('moveend', onMapEvent);

</script>