<div class="modal fade" id="searchModal" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="searchModalTitle">Search Options</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">

            <form class="px-4 py-3" id="dropdownMenuSearchDiv">
            </form>
            <#if queryBuilder??><a class="dropdown-item" href="#" onclick="openQueryBuilder()">Query Builder</a></#if>


        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="submitSearchFormData()">Search</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        </div>
    </div>
</div>
</div>


<script>

    function setupSearch() {
        var tableView = $('#freemarker_tableView')[0].innerText;
        if(!tableView || tableView != "true") {
            setupSearchForMapView();
        } else {
            var searchDataSchemaStr = $('#freemarker_searchDataSchema')[0].innerText;
            $('#freemarker_searchDataSchema')[0].innerText = '{"where":{"type":"string","title":"Search", "description": "Search for records by typing in the where part of the query here"}}';
            searchDataSchemaStr = $('#freemarker_searchDataSchema')[0].innerText;

            var searchDataSchema = JSON.parse(searchDataSchemaStr);
            console.log(searchDataSchema);
            $('#dropdownMenuSearchDiv').jsonForm({
                schema: searchDataSchema,
                form: [
                          "*"
                        ],
                onSubmit: function (errors, values) {
                  searchTableData(values);
                }
            });
        }
    }

    function setupSearchForMapView() {
        var searchDataSchemaStr = $('#freemarker_searchDataSchema')[0].innerText;
        if(searchDataSchemaStr == 'null') {
            var searchText = $('#freemarker_searchtext')[0].innerText;
            $('#freemarker_searchDataSchema')[0].innerText = '{"searchQuery":{"type":"string","title":"Search", "description": "$desc"}}';
            searchDataSchemaStr = $('#freemarker_searchDataSchema')[0].innerText;
            if(searchText != 'null') {
                searchDataSchemaStr = searchDataSchemaStr.replace("$desc", "Search for " + searchText);
            } else {
                searchDataSchemaStr = searchDataSchemaStr.replace("$desc", "");
            }
        }
        var searchDataSchema = JSON.parse(searchDataSchemaStr);
        console.log(searchDataSchema);
        $('#dropdownMenuSearchDiv').jsonForm({
            schema: searchDataSchema,
            form: [
                      "*"
                    ],
            onSubmit: function (errors, values) {
              submitSearchData(values);
            }
        });
    }

    function searchTableData(searchVals) {
        console.log(searchVals);
        $("body").trigger("click");
        var searchUrl = "/records/search";
        if(!searchUrl || searchUrl.length < 1) {
            console.log("Wrong invocation of search api");
            return;
        }
        console.log("searchTableData() : " + searchVals.toString());

        pos = map.getCenter();
        coords = [pos.lat, pos.lng];

        data = {};
        data.pagePath = $('#freemarker_pagePath')[0].innerText;
        for(var key in searchVals) {
            data[key] = searchVals[key];
        }
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(searchUrl, data, function(response) {
            showLoader(false);
            clearRecords();
            if(response.data.records.length > 0) {
                renderRecords(response.data.records);
            }
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }

    function openSearchModal() {
        $('#searchModal').modal();
    }

    function submitSearchFormData() {
        $("#dropdownMenuSearchDiv").submit();
    }

    function submitSearchData(values) {
        console.log(values);
        searchUsingQueryPath(values);
//        getSearchData(values);
        $("body").trigger("click");
    }

    function search(isMapEvent) {
        isMapEvent = false;
        searchQuery = $('#searchBar')[0].value;
        if(searchQuery != null && searchQuery.length > 0) {
            getSearchData(searchQuery);
        }
        $('#searchBar')[0].value = "";
    }

    // TODO get editable markers from iterating through the layers
    function fenceSubmit() {
        editFenceUrl = $('#freemarker_editFenceUrl')[0].innerText;
        console.log("Submitting fence data : " + editFenceUrl);

        if(!editFenceUrl || editFenceUrl.length < 1) {
            console.log("Wrong invocation of search api");
            return;
        }
        var drawnObjects = getDrawnObjects();
        console.log("fenceSubmit() : " + JSON.stringify(drawnObjects));

        pos = map.getCenter();
        coords = [pos.lat, pos.lng];

        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = getMapRadiusInMeters();
        data.drawnObjs = drawnObjects;
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(editFenceUrl, data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.form, response.data.isSidebar, true, response.data.autoRefresh, response.data);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }

    function searchUsingQueryPath(searchVals) {
        var path = window.location.pathname;
        console.log("searchUsingQueryPath : " + path)
        if(typeof map !== 'undefined') {
            var pos = map.getCenter();
            var position = pos.lat + "," + pos.lng;

            if(path.includes("?")) {
                path += "&p=" + position;
            }else {
                path += "?p=" + position;
            }

            path += "&z=" + map.getZoom();
        }
        path += "&q=" + JSON.stringify(searchVals);
        console.log("searchUsingQueryPath : " + path)
        window.location = path;
    }

    function getSearchData(searchVals) {
        var searchUrl = $('#freemarker_searchurl')[0].innerText;
        if(!searchUrl || searchUrl.length < 1) {
            console.log("Wrong invocation of search api");
            return;
        }
        console.log("getSearchData() : " + searchVals.toString());

        pos = map.getCenter();
        coords = [pos.lat, pos.lng];

        data = {};
        data.lat = coords[0];
        data.lon = coords[1];
        data.radius = getMapRadiusInMeters();
        for(var key in searchVals) {
            data[key] = searchVals[key];
        }
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(searchUrl, data, function(response) {
            var fitBounds = true;
            if ("fitBounds" in response.data) {
                fitBounds = response.data.fitBounds
            }
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.form, response.data.isSidebar, fitBounds, response.data.autoRefresh, response.data);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }

    setupSearch();

</script>