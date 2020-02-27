<script>

    $('#searchBar')[0].placeholder = "Bike Id or license plate";

    function search(isMapEvent) {
        isMapEvent = false;
        searchQuery = $('#searchBar')[0].value;
        if(searchQuery != null && searchQuery.length > 0) {
            getBikeData(searchQuery);
        }
        $('#searchBar')[0].value = "";
    }

    function getBikeData(searchQuery) {
            data = {};
            data.searchQuery = searchQuery
            if(isLoading) {
                return;
            }
            isLoading = true;
            showLoader(true);
            var bike = null;
            httpPost("/apis/bike/search", data, function(response) {
                if(response != null) {
                    bikes = response.data.bikes;
                    clearBikeMarkers();
                    for(var i = 0; i < bikes.length; i++) {
                        addBikeToMap(bikes[i]);
                        bike = bikes[i];
                    }
                    showLoader(false);
                } else {
                    showLoader(false);
                }
                isLoading = false;
                if(bike != null) {
                    center[0] = bike.lat;
                    center[1] = bike.lon;
                    map.setView(center, 17);
                }
            })
        }


</script>