<script>

    function onMapEvent(event) {
        console.log("fences onMapEvent");
        console.log(event);
        // If move is set to true. hit api to fetch data and set it using js. Only for home page
    }

    function refreshLayers(coords, radius) {
        console.log("refreshFences()");
        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = radius;
        data.layers = layers;
        if(isLoading) {
            console.log("Not refreshing cause already loading");
            return;
        }
        showLoader(true);

        httpPost("/apis/search", data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths);
            showLoader(false);
        });
    }

    refreshLayers(DEFAULT_CENTRE, DEFAULT_RADIUS);
    // TODO onMapEvent handling and search handling

</script>