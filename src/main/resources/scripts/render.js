<script>

    var genericMarkerObjs = [];
    var genericFenceObjs = [];
    var genericPathObjs = [];
    var genericCircleObjs = [];

    var DEFAULT_CENTRE = [];
    var DEFAULT_RADIUS = 2500;

    var map = null;
    var markerClusterGroup = null;
    var fencesGroup = null;
    var circlesGroup = null;
    var pathsGroup = null;

    var markers = []
    var fences = []
    var paths = []
    var circles = []

    var isLoading = false;
    var isEditMode = false;

    var refresh = true;
    var query = "";
    var sidebar = null;

//    var drawnObjs = [];

    function bootstrap() {
        console.log("bootstrap()")

        setupMap();
        updateMarkers();
        renderMarkers();

        updateFences();
        renderFences(getFences().length > 0);

        updatePaths();
        renderPaths(getPaths().length > 0);

        updateCircles();
        renderCircles(getCircles().length > 0);

        if(getMarkers().length > 0) {
            map.fitBounds(markerClusterGroup.getBounds());
        }
    }

    function renderMarkers() {
        try{
            console.log("renderMarkers()")
            if(genericMarkerObjs.length > 0 && genericMarkerObjs[0].count > 0) {
                markerClusterGroup = L.markerClusterGroup({
                    iconCreateFunction: function(cluster) {
                        var legend = 0;
                        var markers = cluster.getAllChildMarkers();
                        for (var i = 0; i < markers.length; i++) {
                            legend += markers[i].count;
                        }
                        return L.divIcon({ html: '<div class="text-center" style="order: 1; width: 50; position: relative; background-color: #fff; border-radius: 5px; border-width: 2px; border-style: solid; border-color: #444; padding: 3px; white-space: nowrap;">' + legend +'</div>',
                             className: 'bg-transparent'
                        });
                    }
                });
            } else {
                markerClusterGroup = L.markerClusterGroup({
                    iconCreateFunction: function(cluster) {
                        var legend = 0;
                        var markers = cluster.getAllChildMarkers();
                        legend = markers.length;
                        return L.divIcon({ html: '<div class="text-center" style="order: 1; width: 50; position: relative; background-color: #fff; border-radius: 5px; border-width: 2px; border-style: solid; border-color: #444; padding: 3px; white-space: nowrap;">' + legend +'</div>',
                             className: 'bg-transparent'
                        });
                    }
                });
            }
            for(var i = 0; i < genericMarkerObjs.length; i++) {
                var markerData = genericMarkerObjs[i];
                var legend = markerData.legend;
                if(!legend) {
                    legend = markerData.title;
                }
                var markerIcon = L.divIcon({
                   className: 'bg-transparent text-center',
                   html: '<span class="text-center" style="order: 1; position: relative; background-color: #fff; border-radius: 5px; border-width: 2px; border-style: solid; border-color: #444; padding: 3px; white-space: nowrap;">' + legend +'</span><img src="' + markerData.iconUrl + '" style="width: 50; height: 50;">',
                   iconUrl: markerData.iconUrl,
                   iconSize:     [50, 50], // size of the icon
                   iconAnchor: [25, 50]
                });

                if(markerData.location.lat == null || markerData.location.lon == null) {
                    continue;
                }

                var marker = L.marker([markerData.location.lat, markerData.location.lon],{icon: markerIcon});

                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";

                popupInfo += "<b>" + markerData.title + "</b><br/>" + markerData.subtext + "<br/>";
                var formData = {};
                if(markerData.data) {
                    for(var key in markerData.data) {
                        popupInfo += "<div>" + key + " : " + markerData.data[key] + "</div>";
                        formData[key] = markerData.data[key];
                    }
                }
                var drawId = uuid();
                if(isEditMode) {
                    popupInfo += getEditModePopupInfo(drawId);
                }
                popupInfo += "</div></div><br/>";
                marker.bindPopup(popupInfo, {autoClose: false});
                marker.text = markerData.title + " / " + markerData.subtext;
                marker.alt = markerData.title + " / " + markerData.subtext;
                marker.shape = "Marker";
                marker.formData = formData;
                marker.drawId = drawId;
                if(markerData.count > 0) {
                    marker.count = markerData.count;
                }
                markers.push(marker);
            }
            markerClusterGroup.addLayers(markers);
            map.addLayer(markerClusterGroup);
        } catch(err) {
            console.log(err);
        }
    }

    function renderFences(fitToBounds) {
        try{
            console.log("renderFences()");
            fencesGroup = L.layerGroup();
            var layerAdded = null;
            for(var i = 0; i < genericFenceObjs.length; i++) {
                var fenceData = genericFenceObjs[i];
                var points = getPoints(fenceData.points);
                var fence = L.polygon(points, {fillColor: fenceData.fillColor, fillOpacity: fenceData.fillOpacity, color: fenceData.color});
                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
                var formData = {};
                if(fenceData.data) {
                    for(var key in fenceData.data) {
                        popupInfo += "<div>" + key + " : " + fenceData.data[key] + "</div>";
                        formData[key] = fenceData.data[key];
                    }
                }
                var drawId = uuid();
                if(isEditMode) {
                    popupInfo += getEditModePopupInfo(drawId);
                }
                popupInfo += "</div></div><br/>";
                fence.bindPopup(popupInfo, {autoClose: false});
                fence.shape = "Fence";
                fence.formData = formData;
                fence.drawId = drawId;
                fences.push(fence);
                fencesGroup.addLayer(fence);
                layerAdded = fence;
            }
            console.log("fitToBounds : " + fitToBounds);
            if(layerAdded && fitToBounds) {
                map.fitBounds(layerAdded.getBounds(), {padding: [50,50]});
            }
            fencesGroup.addTo(map);
        } catch(err) {
            console.log(err);
        }
    }

    function renderPaths(fitToBounds) {
        try{
            console.log("renderPaths()");
            pathsGroup = L.layerGroup();

            var layerAdded = null;
            for(var i = 0; i < genericPathObjs.length; i++) {
                var pathData = genericPathObjs[i];
                var points = getPoints(pathData.points);
                var lineWeight = 5
                if(pathData.lineWeight) {
                    weight = pathData.lineWeight;
                }
                var path = L.polyline(points, {color: pathData.color, weight: lineWeight});
                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
                var formData = {};
                if(pathData.data) {
                    for(var key in pathData.data) {
                        popupInfo += "<div>" + key + " : " + pathData.data[key] + "</div>";
                        formData[key] = pathData.data[key];
                    }
                }
                var drawId = uuid();
                if(isEditMode) {
                    popupInfo += getEditModePopupInfo(drawId);
                }
                popupInfo += "</div></div><br/>";
                path.bindPopup(popupInfo, {autoClose: false});
                path.shape = "Line";
                path.formData = formData;
                path.drawId = drawId;
                paths.push(path);
                pathsGroup.addLayer(path);
                layerAdded = path;
            }
            pathsGroup.addTo(map);
            if(layerAdded && fitToBounds) {
                map.fitBounds(layerAdded.getBounds(), {padding: [50,50]});
            }
        }catch(err) {
            console.log(err);
        }
    }

    function renderCircles(fitToBounds) {
        try{
            console.log("renderCircles()");
            circlesGroup = L.layerGroup();
            layerAdded = null;
            for(var i = 0; i < genericCircleObjs.length; i++) {
                var circleData = genericCircleObjs[i];
                point = [circleData.location.lat, circleData.location.lon];
                var circle = L.circle(point, {fillColor: circleData.fillColor, fillOpacity: circleData.fillOpacity, color: circleData.color, radius: circleData.radius});
                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
                var formData = {};
                if(circleData.data) {
                    for(var key in circleData.data) {
                        popupInfo += "<div>" + key + " : " + circleData.data[key] + "</div>";
                        formData[key] = circleData.data[key];
                    }
                }
                var drawId = uuid();
                if(isEditMode) {
                    popupInfo += getEditModePopupInfo(drawId);
                }
                popupInfo += "</div></div><br/>";
                circle.bindPopup(popupInfo, {autoClose: false});
                circle.shape = "Circle";
                circle.formData = formData;
                circle.drawId = drawId;
                circles.push(circle);
                circlesGroup.addLayer(circle);
                layerAdded = circle;
            }
            if(layerAdded && fitToBounds) {
    //            map.fitBounds(layerAdded.getBounds());
                console.log("Don't support fitBounds on circles")
            }
            circlesGroup.addTo(map);
        } catch(err) {
            console.log(err);
        }
    }

    function updateMarkers() {
        markerData = $('#markerData')[0].innerText;
        if(markerData.length > 0) {
            arr = JSON.parse($('#markerData')[0].innerText);
            genericMarkerObjs = arr;
        }
    }

    function updateFences() {
        fenceData = $('#fenceData')[0].innerText;
        if(fenceData.length > 0) {
            arr = JSON.parse($('#fenceData')[0].innerText);
            genericFenceObjs = arr;
        }
    }

    function updatePaths() {
        pathData = $('#pathData')[0].innerText;
        if(pathData.length > 0) {
            arr = JSON.parse($('#pathData')[0].innerText);
            genericPathObjs = arr;
        }
    }

    function updateCircles() {
        circleData = $('#circleData')[0].innerText;
        if(circleData.length > 0) {
            arr = JSON.parse($('#circleData')[0].innerText);
            genericCircleObjs = arr;
        }
    }

    function clearMarkers() {
        map.removeLayer(markerClusterGroup);
        markerClusterGroup.clearLayers();
        markers = [];
        markerClusterGroup = null;
    }

    function clearFences() {
        map.removeLayer(fencesGroup);
        fencesGroup.clearLayers();
        fences = [];
        fencesGroup = null;
    }

    function clearCircles() {
        map.removeLayer(circlesGroup);
        circlesGroup.clearLayers();
        circles = [];
        circlesGroup = null;
    }

    function clearPaths() {
        map.removeLayer(pathsGroup);
        pathsGroup.clearLayers();
        paths = [];
        pathsGroup = null;
    }

    function clearAllLayers() {
        map.eachLayer(function(layer){
            if(layer.pm && typeof layer.pm.isPolygon == 'function'){
                map.removeLayer(layer);
            }
        });
    }

    function getMarkers() {
        return genericMarkerObjs;
    }

    function getFences() {
        return genericFenceObjs;
    }

    function getPaths() {
        return genericPathObjs;
    }

    function getCircles() {
        return genericCircleObjs;
    }

    function bootboxPromptRenderJSON() {
        bootbox.prompt({
            title: "Please add your atlas json",
            inputType: 'textarea',
            callback: function (result) {
                if(result) {
                    var atlasObj = JSON.parse(result);
                    invalidateMap(atlasObj.markers, atlasObj.fences, atlasObj.circles, atlasObj.paths, atlasObj.events, atlasObj.form, atlasObj.isSidebar, true, false, atlasObj);
                }
            }
        });
    }

    function createLayersFromJson(geoJsonData) {
        return L.geoJson(geoJsonData, {
          pointToLayer: (feature, latlng) => {
            if (feature.properties.shape && feature.properties.shape == 'Circle') {
              return new L.Circle(latlng, feature.properties.radius);
            } else {
              return new L.Marker(latlng);
            }
          },
        });
    };

    function bootboxPromptRenderGeoJSON() {
        bootbox.prompt({
            title: "Please add your geojson",
            inputType: 'textarea',
            callback: function (result) {
                if(result) {
                    var geoJSONObj = JSON.parse(result);
                    clearMarkers();
                    clearAllLayers();
                    if(Array.isArray(geoJSONObj)) {
                        renderGeoJsonArray(geoJSONObj, true);
                    } else {
                        renderGeoJsonObjects(geoJSONObj, true);
                    }
                }
            }
        });
    }

    function bootboxPromptRenderKML() {
            bootbox.prompt({
                title: "Please add your KML Contents",
                inputType: 'textarea',
                callback: function (result) {
                    if(result) {
                        var geoJSONObj = toGeoJSON.kml((new DOMParser()).parseFromString(result, 'text/xml'))
                        console.log("GeoJSONObj");
                        console.log(JSON.stringify(geoJSONObj));
                        clearMarkers();
                        clearAllLayers();
                        renderGeoJsonObjects(geoJSONObj, true);
                    }
                }
            });
        }

    function renderGeoJsonArray(geojson, fitBounds = true) {
        var geolayers = createLayersFromJson(geojson);
        if(geojson.length < 1) {
            return;
        }
        for(var i = 0; i < geojson.length; i++) {
            renderGeoJsonObjects(geojson[i], fitBounds);
        }
        if(fitBounds) {
            map.fitBounds(geolayers.getBounds());
        }
    }

    function renderGeoJsonObjects(geoJSONObj, fitBounds = true) {
        if(markerClusterGroup == null) {
            markerClusterGroup = L.markerClusterGroup({
                iconCreateFunction: function(cluster) {
                    var legend = 0;
                    var markers = cluster.getAllChildMarkers();
                    legend = markers.length;
                    return L.divIcon({ html: '<div class="text-center" style="order: 1; width: 50; position: relative; background-color: #fff; border-radius: 5px; border-width: 2px; border-style: solid; border-color: #444; padding: 3px; white-space: nowrap;">' + legend +'</div>',
                         className: 'bg-transparent'
                    });
                }
            });
        }
        var geoJSONLayers = createLayersFromJson(geoJSONObj);
        var properties = geoJSONObj.properties;
        var removeLayers = [];
        for(var i=0;i<geoJSONLayers.getLayers().length;i++) {
            var geoLayer = geoJSONLayers.getLayers()[i];
            var shape = geoLayer.feature.properties.shape;
            if(!shape) {
                shape = geoLayer.feature.geometry.type;
            }
            switch(shape) {
                case "Point":
                    removeLayers.push(geoLayer);
                    var markerIcon = L.divIcon({
                       className: 'bg-transparent text-center',
                       html: '<img src="/resources/icons/marker_blue.png" style="width: 50; height: 50;">',
                       iconUrl: '/resources/icons/marker_blue.png',
                       iconSize:     [50, 50] // size of the icon
                    });
                    var marker = L.marker([geoLayer.getLatLng().lat, geoLayer.getLatLng().lng],{icon: markerIcon});
                    var formData = {};
                    var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
                     if(properties) {
                        for(var key in properties) {
                            if(typeof properties[key] == 'object') {
                                continue;
                            }
                            popupInfo += "<div>" + key + " : " + properties[key] + "</div>";
                            formData[key] = properties[key];
                        }
                    }
                    var drawId = uuid();
                    if(isEditMode) {
                        popupInfo += getEditModePopupInfo(drawId);
                    }
                    popupInfo += "</div></div><br/>";
                    marker.bindPopup(popupInfo, {autoClose: false});
                    marker.shape = "Marker";
                    marker.formData = formData;
                    marker.drawId = drawId;
                    if(markerData.count > 0) {
                        marker.count = markerData.count;
                    }
                    markers.push(marker);
                    break;
                case "Line":
                    break;
                case "Rectangle":
                    shape = "Fence";
                    break;
                case "Cicle":
                    break;
                case "Polygon":
                    shape = "Fence";
                    break;
            }
            if(shape == "Point") continue;
            var drawId = uuid();
            geoLayer.shape = shape;
            geoLayer.drawId = drawId;
            if(properties) {
                var formData = {};
                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
                 if(properties) {
                    for(var key in properties) {
                        if(typeof properties[key] == 'object') {
                            continue;
                        }
                        popupInfo += "<div>" + key + " : " + properties[key] + "</div>";
                        formData[key] = properties[key];
                    }
                }
                if(isEditMode) {
                    popupInfo += getEditModePopupInfo(drawId);
                }
                popupInfo += "</div></div><br/>";
                geoLayer.bindPopup(popupInfo, {autoClose: false});
                geoLayer.formData = formData;
            }
        }
        if(removeLayers.length > 0) {
            for(var i=0;i<removeLayers.length;i++) {
                geoJSONLayers.removeLayer(removeLayers[i]);
            }
        }
        if(markers != null && markers.length > 0) {
            markerClusterGroup.addLayers(markers);
            map.addLayer(markerClusterGroup);
        }
        if(geoJSONLayers.getLayers().length > 0) {
            geoJSONLayers.addTo(map);
            if(fitBounds) {
                map.fitBounds(geoJSONLayers.getBounds());
            }
        }
    }

    function getEditModePopupInfo(drawId){
        popupInfo = "<div>" + "Edit Data" + " : " + "<a href='#' onclick=showFenceModal('" + drawId + "');>Click Here</a>" + "</div>";
        popupInfo += "<div>" + "Update changes" + " : " + "<a href='#' onclick=updateSelectedGeom('" + drawId + "');>Click Here</a>" + "</div>";
        popupInfo += "<div>" + "Delete" + " : " + "<a href='#' onclick=deleteSelectedGeom('" + drawId + "');>Click Here</a>" + "</div>";
        return popupInfo;
    }

    function getDrawnObjects() {
        var drawnObjects = [];
        map.eachLayer(function(layer){
            if(layer.pm && typeof layer.pm.isPolygon == 'function'){
                var drawId = layer.drawId;
                var drawObj = {};
                drawObj.formData = layer.formData;
                drawObj.shape = layer.shape;
                switch(layer.shape) {
                    case "Circle":
                        drawObj.coords = [];
                        drawObj.coords.push(layer.getLatLng().lat + "," + layer.getLatLng().lng);
                        drawObj.drawId = drawId;
                        drawObj.options = {};
                        drawObj.options.radius = layer.getRadius();
                        drawnObjects.push(drawObj)
                        break;
                    case "Fence":
                        drawObj.coords = [];
                        for(var i = 0; i < layer.getLatLngs()[0].length; i++) {
                            drawObj.coords.push(layer.getLatLngs()[0][i].lat + "," +  layer.getLatLngs()[0][i].lng);
                        }
                        drawObj.drawId = drawId;
                        drawnObjects.push(drawObj);
                        break;
                    case "Line":
                        drawObj.coords = [];
                        for(var i = 0; i < layer.getLatLngs().length; i++) {
                            drawObj.coords.push(layer.getLatLngs()[i].lat + "," +  layer.getLatLngs()[i].lng);
                        }
                        drawObj.drawId = drawId;
                        drawnObjects.push(drawObj);
                        break;
                    case "Marker":
                        drawObj.coords = [];
                        drawObj.coords.push(layer.getLatLng().lat + "," + layer.getLatLng().lng);
                        drawObj.drawId = drawId;
                        drawnObjects.push(drawObj);
                        break;
                }

            }
        });
        return drawnObjects;
    }

    function updateSelectedGeom(drawId) {
        bootbox.confirm("Are you sure you want to update it on the server?", function(result){
            if(result){
                var drawObj = getDrawnObject(drawId);
                editFenceUrl = $('#freemarker_editFenceUrl')[0].innerText;
                console.log("Submitting fence data : " + editFenceUrl);

                if(!editFenceUrl || editFenceUrl.length < 1) {
                    console.log("Wrong invocation of search api");
                    return;
                }
                console.log("updateSelectedGeom() : " + JSON.stringify(drawObj));
                data = {};
                data.drawnObj = drawObj;
                data.action = 'update';
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
        });
    }

    function deleteSelectedGeom(drawId) {
        bootbox.confirm("Are you sure you want to delete this?", function(result){
            if(result){
                var drawObj = getDrawnObject(drawId);
                editFenceUrl = $('#freemarker_editFenceUrl')[0].innerText;
                console.log("Submitting fence data : " + editFenceUrl);
                if(!editFenceUrl || editFenceUrl.length < 1) {
                    console.log("Wrong invocation of search api");
                    return;
                }
                console.log("deleteSelectedGeom() : " + JSON.stringify(drawObj));
                data = {};
                data.drawnObj = drawObj;
                data.action = 'delete';
                if(isLoading) {
                    return;
                }
                showLoader(true);
                httpPost(editFenceUrl, data, function(response) {
                    invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.form, response.data.isSidebar, true, response.data.autoRefresh, response.data);
                    showLoader(false);
                    map.eachLayer(function(layer){
                        if(layer.pm && typeof layer.pm.isPolygon == 'function' && layer.drawId==drawId)
                            layer.remove();
                    });
                }, function(jqXHR, exceptiom) {
                    showLoader(false);
                });
            }
        });
    }

    function getDrawnObject(drawId) {
        var drawObj = {};
        map.eachLayer(function(layer){
            if(layer.pm && typeof layer.pm.isPolygon == 'function' && layer.drawId==drawId){
                drawObj.formData = layer.formData;
                drawObj.shape = layer.shape;
                switch(layer.shape) {
                    case "Circle":
                        drawObj.coords = [];
                        drawObj.coords.push(layer.getLatLng().lat + "," + layer.getLatLng().lng);
                        drawObj.drawId = layer.drawId;
                        drawObj.options = {};
                        drawObj.options.radius = layer.getRadius();
                        break;
                    case "Fence":
                        drawObj.coords = [];
                        for(var i = 0; i < layer.getLatLngs()[0].length; i++) {
                            drawObj.coords.push(layer.getLatLngs()[0][i].lat + "," +  layer.getLatLngs()[0][i].lng);
                        }
                        drawObj.drawId = layer.drawId;
                        break;
                    case "Line":
                        drawObj.coords = [];
                        for(var i = 0; i < layer.getLatLngs().length; i++) {
                            drawObj.coords.push(layer.getLatLngs()[i].lat + "," +  layer.getLatLngs()[i].lng);
                        }
                        drawObj.drawId = layer.drawId;
                        break;
                    case "Marker":
                        drawObj.coords = [];
                        drawObj.coords.push(layer.getLatLng().lat + "," + layer.getLatLng().lng);
                        drawObj.drawId = layer.drawId;
                        break;
                }
            }
        });
        return drawObj;
    }

    function addEditFenceControls() {
        map.pm.addControls({
          position: 'topleft',
          cutPolygon: false,
          editMode: true,
          dragMode: true,
          drawCircleMarker: false,
          pinningOption: false,
          snappingOption: false
        });

        isEditMode = true;

        map.on('pm:create', function(e) {
            var drawId = uuid();
            console.log(e);
            var coords = [];
            switch(e.shape) {
                case "Circle":
                    coords.push(e.layer.getLatLng().lat + "," + e.layer.getLatLng().lng);
                    e.layer.shape = e.shape;
                    break;
                case "Rectangle":
                case "Polygon":
                    for(var i = 0; i < e.layer.getLatLngs()[0].length; i++) {
                        coords.push(e.layer.getLatLngs()[0][i].lat + "," +  e.layer.getLatLngs()[0][i].lng);
                    }
                    e.layer.shape = "Fence";
                    break;
                case "Line":
                    for(var i = 0; i < e.layer.getLatLngs().length; i++) {
                        coords.push(e.layer.getLatLngs()[i].lat + "," +  e.layer.getLatLngs()[i].lng);
                    }
                    e.layer.shape = e.shape;
                    break;
                case "Marker":
                    coords.push(e.layer.getLatLng().lat + "," + e.layer.getLatLng().lng);
                    e.layer.shape = e.shape;
                    break;
            }

            e.layer.drawId = drawId
            var editFenceDataSchema = $('#freemarker_editFenceDataSchema')[0].innerText;

            var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
            popupInfo += "<div>" + "Coords" + " : " + JSON.stringify(coords, null, 3) + "</div>";
            if(editFenceDataSchema) {
                popupInfo += getEditModePopupInfo(drawId);
            }
            popupInfo += "</div></div><br/>";
            e.layer.bindPopup(popupInfo, {autoClose: false});
        });
    }

    function setupMap() {
        var loc = $('#freemarker_location')[0].innerText;
        var zoom = $('#freemarker_zoom')[0].innerText;
        var autoRefresh = $('#freemarker_autorefresh')[0].innerText;
        var editFence = parseBoolean($('#freemarker_isEditControlSupported')[0].innerText);
        var geojson = $('#freemarker_geojson')[0].innerText;
        var tileserverurl = $('#freemarker_tileserverurl')[0].innerText;
        var tileserverid = $('#freemarker_tileserverid')[0].innerText;

        refresh = parseBoolean(autoRefresh);

        if(refresh) {
            $('#refreshCheckbox').prop('checked', true);
        }

        if(loc.length > 0) {
            DEFAULT_CENTRE = [parseFloat(loc.split(",")[0]), parseFloat(loc.split(",")[1])];
        }
        if(zoom.length < 1) {
            zoom = 18;
        }
        tempQuery = $('#freemarker_query')[0].innerText;
        if(tempQuery.length > 0) {
            query = tempQuery;
        }

        map = L.map('mapDiv').setView(DEFAULT_CENTRE, zoom);
        map.on('moveend', onMapEvent);
        isLoading = false;

//        L.tileLayer(tileserverurl, {
//            maxZoom: 22,
//            id: tileserverid,
//            subdomains:['mt0','mt1','mt2','mt3']
//        }).addTo(map);

        L.gridLayer
        .googleMutant({
            type: "roadmap", // valid values are 'roadmap', 'satellite', 'terrain' and 'hybrid'
            styles: googleMapsStylesJSON
        })
        .addTo(map);


        if(editFence) {
            addEditFenceControls();
        }
        if(geojson && geojson.length > 0) {
            geojson = JSON.parse(geojson);
            renderGeoJsonArray(geojson, true);
        }
    }

    function onMapEvent(event) {
        // If move is set to true. hit api to fetch data and set it using js. Only for home page
    }

    function invalidateMap(tMarkers, tFences, tCircles, tPaths, events, form, isSidebar = true, fitToBounds = false, toRefresh = refresh, data = null) {
        console.log("fitToBounds : " + fitToBounds);
        if(tMarkers != null) {
            $('#markerData')[0].innerText = JSON.stringify(tMarkers);
        }
        if(tFences != null) {
            $('#fenceData')[0].innerText = JSON.stringify(tFences);
        }
        if(tCircles != null) {
            $('#circleData')[0].innerText = JSON.stringify(tCircles);
        }
        if(tPaths != null) {
            $('#pathData')[0].innerText = JSON.stringify(tPaths);
        }

        clearMarkers();
        clearFences();
        clearPaths();
        clearCircles();

        updateMarkers();
        updateFences();
        updateCircles();
        updatePaths();

        renderMarkers();
        renderFences(fitToBounds);
        renderCircles(fitToBounds);
        renderPaths(fitToBounds);

        if(tMarkers != null && tMarkers.length > 0 && fitToBounds) {
            map.fitBounds(markerClusterGroup.getBounds(), {padding: [50,50]});
        }

        if(events != null) {
            renderTimeline(events, isSidebar);
        }

        if(form != null) {
            renderForm(form);
        }

        refresh = toRefresh;

        $('#refreshCheckbox').prop('checked', refresh);

        if(data) {
            if(data.editMode == true) {
                addEditFenceControls();
            }
        }
    }

    function getMapRadiusInMeters() {
        var mapBoundNorthEast = map.getBounds().getNorthEast();
        var mapDistance = mapBoundNorthEast.distanceTo(map.getCenter());
        return mapDistance;
    }

    bootstrap();

</script>