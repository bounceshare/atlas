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
                markerClusterGroup = L.markerClusterGroup();
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

                var marker = L.marker([markerData.location.lat, markerData.location.lon],{icon: markerIcon});

                var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";

                popupInfo += "<b>" + markerData.title + "</b><br/>" + markerData.subtext + "<br/>";
                if(markerData.data) {
                    for(var key in markerData.data) {
                        popupInfo += "<div>" + key + " : " + markerData.data[key] + "</div>";
                    }
                }
                popupInfo += "</div></div><br/>";
                marker.bindPopup(popupInfo, {autoClose: false});
                marker.text = markerData.title + " / " + markerData.subtext;
                marker.alt = markerData.title + " / " + markerData.subtext;
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
                if(fenceData.data) {
                    for(var key in fenceData.data) {
                        popupInfo += "<div>" + key + " : " + fenceData.data[key] + "</div>";
                    }
                }
                popupInfo += "</div></div><br/>";
                fence.bindPopup(popupInfo, {autoClose: false});
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
                if(pathData.data) {
                    for(var key in pathData.data) {
                        popupInfo += "<div>" + key + " : " + pathData.data[key] + "</div>";
                    }
                }
                popupInfo += "</div></div><br/>";
                path.bindPopup(popupInfo, {autoClose: false});
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
                if(circleData.data) {
                    for(var key in circleData.data) {
                        popupInfo += "<div>" + key + " : " + circleData.data[key] + "</div>";
                    }
                }
                popupInfo += "</div></div><br/>";
                circle.bindPopup(popupInfo, {autoClose: false});
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
                    invalidateMap(atlasObj.markers, atlasObj.fences, atlasObj.circles, atlasObj.paths, atlasObj.events, atlasObj.form, atlasObj.isSidebar, true, false);
                }
            }
        });
    }

    function getDrawnObjects() {
        var drawnObjects = [];
        map.eachLayer(function(layer){
            if(layer.pm && typeof layer.pm.isPolygon == 'function'){
                var drawId = layer._leaflet_id;
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

        map.on('pm:create', function(e) {
            var drawId = e.layer._leaflet_id;
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

            var editFenceDataSchema = $('#freemarker_editFenceDataSchema')[0].innerText;

            var popupInfo = "<br/><div class='border'><div class='p-2 text-monospace'>";
            popupInfo += "<div>" + "Coords" + " : " + JSON.stringify(coords, null, 3) + "</div>";
            if(editFenceDataSchema) {
                popupInfo += "<div>" + "Edit Data" + " : " + "<a href='#' onclick=showFenceModal(" + drawId + ");>Click Here</a>" + "</div>";
            }
            popupInfo += "</div></div><br/>";
            e.layer.bindPopup(popupInfo, {autoClose: false});
        });
    }

    function setupMap() {
        var loc = $('#freemarker_location')[0].innerText;
        var zoom = $('#freemarker_zoom')[0].innerText;
        var autoRefresh = $('#freemarker_autorefresh')[0].innerText;
        var editFence = $('#freemarker_editFenceUrl')[0].innerText;

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

        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1Ijoic3VzaGVlbGsiLCJhIjoiY2s3NHR3YjN1MDhxOTNrcGxreGM2bmxwdiJ9.h0asAA-St15DH7sCIc0drw', {
            maxZoom: 18,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1
        }).addTo(map);

        if(editFence != 'false') {
            addEditFenceControls();
        }
    }

    function onMapEvent(event) {
        console.log("onMapEvent");
        console.log(event);
        // If move is set to true. hit api to fetch data and set it using js. Only for home page
    }

    function invalidateMap(tMarkers, tFences, tCircles, tPaths, events, form, isSidebar = true, fitToBounds = false, toRefresh = refresh) {
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
    }

    function getMapRadiusInMeters() {
        var mapBoundNorthEast = map.getBounds().getNorthEast();
        var mapDistance = mapBoundNorthEast.distanceTo(map.getCenter());
        return mapDistance;
    }

    bootstrap();

</script>