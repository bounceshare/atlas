<script>

    $('#searchBar')[0].placeholder = "Booking Id";

    function search(isMapEvent) {
        isMapEvent = false;
        searchQuery = $('#searchBar')[0].value;
        if(searchQuery != null && searchQuery.length > 0) {
            getBookingData(searchQuery);
        }
        $('#searchBar')[0].value = "";
    }

    function getBookingData(searchQuery) {
        console.log("getBookingData() : " + searchQuery);
        data = {};
        data.searchQuery = searchQuery
        if(isLoading) {
            return;
        }
        isLoading = true;
        showLoader(true);
        var bike = null;
        httpPost("/apis/booking/search", data, function(response) {
            invalidateMap(response.data.markers, null, null, response.data.paths, true);
            showLoader(false);
        });
    }


</script>