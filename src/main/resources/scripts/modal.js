<script>

    var timelineUrl = null;
    var timelineObjectId = null;
    var timelineItems = []
    var timelineLastObjTime = null;

    var IS_TIMELINE_MODAL = false;

    function showTimeline(url, id, title) {
        // TODO set title
        if(IS_TIMELINE_MODAL) {
            $('#timelineModalTitle')[0].innerHTML = title;
        } else {
            $('#sidebar-title')[0].innerHTML = title;
        }
        fetchData(url, id);
    }

    function fetchData(url, id) {
        if(timelineUrl != url || id != timelineObjectId) {
            timelineItems = [];
            $('#timeline-view')[0].innerHTML = "";
        }
        if(sidebar) {
            sidebarClose();
        }
        timelineUrl = url;
        timelineObjectId = id;
        console.log("showTimeline() :: url : " + url + " :: id : " + id);
        data = {};
        data.id = id;
        if(timelineLastObjTime != null) {
            data.from = timelineLastObjTime
        }
        if(isLoading) {
            return;
        }
        isLoading = true;
        showLoader(true);
        var bike = null;
        httpPost(url, data, function(response) {
            renderTimeline(response.data.events);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
             showLoader(false);
         });
    }

    function renderTimeline(items) {
        if(items.length < 1) {
            return;
        }

        items.sort(function (a, b) {
            if (a.time > b.time) {
                return -1;
            }
            if (b.time > a.time) {
                return 1;
            }
            return 0;
        });

        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            var detailsElement = "<div class='border'><div class='p-2 text-monospace'>{$detail-item}</div></div>";
            var divElement = "<div class='row'><div class='col-auto text-center flex-column d-none d-sm-flex'><div class='row h-50'><div class='col border-right'>&nbsp;</div><div class='col'>&nbsp;</div></div><h5 class='m-2'><span class='badge badge-pill bg-primary'>&nbsp;</span></h5><div class='row h-50'><div class='col border-right'>&nbsp;</div><div class='col'>&nbsp;</div></div></div><div class='col py-2'><div class='card border-primary shadow'><div class='card-body'><div class='float-right text-primary'>{$time}</div><h4 class='card-title text-primary'>{$title}</h4><p class='card-text p-2 text-monospace'>{$body}</p>{$details}</div></div></div></div>";

            var detailDom = ""
            if(item.details && !jQuery.isEmptyObject(item.details)) {
                for(detailItem in item.details) {
                    detailDom += "<div>" + detailItem + " : " + item.details[detailItem] + "</div>";
                }
                detailsElement = detailsElement.replace("{$detail-item}", detailDom);
                divElement = divElement.replace("{$details}", detailsElement);
            } else {
                divElement = divElement.replace("{$details}", "");
            }
            divElement = divElement.replace("{$title}", item.header);
            divElement = divElement.replace("{$body}", item.body);
            divElement = divElement.replace("{$time}", item.timeString);

            if(IS_TIMELINE_MODAL) {
                $('#timeline-view')[0].innerHTML += divElement;
            }else {
                $('#sidebar-content')[0].innerHTML += divElement
            }
            timelineItems.push(item);
        }
        timelineLastObjTime = items[items.length -1].time;
        if(IS_TIMELINE_MODAL) {
            $('#timelineModal').modal();
            $('#timelineModal').on('hidden.bs.modal', function (e) {
                console.log("Clearing the timelone modal");
                timelineUrl = null;
                timelineObjectId = null;
                timelineItems = []
                timelineLastObjTime = null;

                $('#timeline-view')[0].innerHTML = "";
            })
        } else {
            sidebar = L.control.sidebar('sidebar', {
                position: 'left',
                autoPan: true,
                closeButton: false
            });
            map.addControl(sidebar);
            $('#sidebar')[0].hidden = false;
            sidebar.toggle();

            sidebar.on('hide', function () {
                console.log('Sidebar is now hidden.');
                timelineUrl = null;
                timelineObjectId = null;
                timelineItems = []
                timelineLastObjTime = null;

                $('#sidebar-content')[0].innerHTML = "";
                $('#sidebar')[0].hidden = true;
                sidebar = null;
            });
        }
    }

    function loadMoreTimelineItems() {
        if(timelineUrl && timelineObjectId) {
            fetchData(timelineUrl, timelineObjectId);
        }
    }

    function sidebarClose() {
        sidebar.hide();
    }

</script>