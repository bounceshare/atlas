<script>

    /*
    {
        header : "Title",
        time : 12341213112,
        body : "Content",
        colot : "#dddddd"
    }
    */

    /*
    <!-- timeline item -->
    <div class="row">
        <div class="col-auto text-center flex-column d-none d-sm-flex">
            <div class="row h-50">
                <div class="col border-right">&nbsp;</div>
                <div class="col">&nbsp;</div>
            </div>
            <h5 class="m-2">
                <span class="badge badge-pill bg-success">&nbsp;</span>
            </h5>
            <div class="row h-50">
                <div class="col border-right">&nbsp;</div>
                <div class="col">&nbsp;</div>
            </div>
        </div>
        <div class="col py-2">
            <div class="card border-success shadow">
                <div class="card-body">
                    <div class="float-right text-success">Tue, Jan 10th 2019 8:30 AM</div>
                    <h4 class="card-title text-success">Day 2 Sessions</h4>
                    <p class="card-text">Sign-up for the lessons and speakers that coincide with your course
                        syllabus. Meet and greet with instructors.</p>
                    <button class="btn btn-sm btn-outline-secondary" type="button" data-target="#t2_details"
                            data-toggle="collapse">Show Details ▼
                    </button>
                    <div class="collapse border" id="t2_details">
                        <div class="p-2 text-monospace">
                            <div>08:30 - 09:00 Breakfast in CR 2A</div>
                            <div>09:00 - 10:30 Live sessions in CR 3</div>
                            <div>10:30 - 10:45 Break</div>
                            <div>10:45 - 12:00 Live sessions in CR 3</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    */

    function showTimeline(url, id) {
        console.log("showTimeline() :: url : " + url + " :: id : " + id);
        data = {};
        data.id = id;
        if(isLoading) {
            return;
        }
        isLoading = true;
        showLoader(true);
        var bike = null;
        httpPost(url, data, function(response) {
            renderTimeline(response.data.events);
            showLoader(false);
        });
    }

    function renderTimeline(items) {
        $('#timeline-view')[0].innerHTML = "";
        console.log()

        if(items.count > 60) {
            items = item.slice(0, 60);
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
            var divElement = "<div class='row'><div class='col-auto text-center flex-column d-none d-sm-flex'><div class='row h-50'><div class='col border-right'>&nbsp;</div><div class='col'>&nbsp;</div></div><h5 class='m-2'><span class='badge badge-pill bg-primary'>&nbsp;</span></h5><div class='row h-50'><div class='col border-right'>&nbsp;</div><div class='col'>&nbsp;</div></div></div><div class='col py-2'><div class='card border-primary shadow'><div class='card-body'><div class='float-right text-primary'>{$time}</div><h4 class='card-title text-primary'>{$title}</h4><p class='card-text'>{$body}</p>{$details}</div></div></div></div>";

            var detailDom = ""
            if(item.details) {
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

            $('#timeline-view')[0].innerHTML += divElement;
        }
        $('#timelineModal').modal()
    }

</script>