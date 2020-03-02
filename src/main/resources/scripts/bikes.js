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
        console.log("getBikeData() : " + searchQuery);
        data = {};
        data.searchQuery = searchQuery
        if(isLoading) {
            return;
        }
        isLoading = true;
        showLoader(true);
        var bike = null;
        httpPost("/apis/bike/search", data, function(response) {
            invalidateMap(response.data.markers, null, null, null, true);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }


</script>