<script>

    var searchText = $('#freemarker_searchtext')[0].innerText;
    $('#searchBar')[0].placeholder = searchText;

    function search(isMapEvent) {
        isMapEvent = false;
        searchQuery = $('#searchBar')[0].value;
        if(searchQuery != null && searchQuery.length > 0) {
            getSearchData(searchQuery);
        }
        $('#searchBar')[0].value = "";
    }

    function fenceSubmit() {
        editFenceUrl = $('#freemarker_editFenceUrl')[0].innerText;
        console.log("Submitting fence data : " + editFenceUrl);
    }

    function getSearchData(searchQuery) {
        var searchUrl = $('#freemarker_searchurl')[0].innerText;
        if(!searchUrl || searchUrl.length < 1) {
            console.log("Wrong invocation of search api");
            return;
        }
        console.log("getSearchData() : " + searchQuery);

        pos = map.getCenter();
        coords = [pos.lat, pos.lng];

        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = getMapRadiusInMeters();
        data.searchQuery = searchQuery
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(searchUrl, data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.isSidebar, true, response.data.autoRefresh);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }


</script>