<script>

    var table = null;

    function editRecord(pageId, id) {
        console.log("editRecord() : " + pageId + " , " + id);
        data = {};
        data.pagePath = $('#freemarker_pagePath')[0].innerText;
        data.id = id;
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost("/records/form", data, function(response) {
            showLoader(false);
            renderForm(response.data.form);
        }, function(jqXHR, exceptiom) {
            showLoader(false);
        });
    }

    function deleteRecord(pageId, id) {
        console.log("deleteRecord() : " + pageId + " , " + id);
    }

    function clearRecords() {
        if(table != null) {
            table.clear().draw();
            table.destroy();
            table = null;
        }
    }

    function renderRecords(recordData) {
        var columnNames = recordData[0];

        var columns = [];
        for (column in columnNames) {
            if(columnNames[column].length < 1) {
                var obj = {}
                obj.render = function ( data, type, row, meta ) {
                    var options = JSON.parse(data);
                    var links = "";
                    if("edit" in options) {
                        var params = options.edit;
                        links += "<b><a href='#' onclick='editRecord(" + params + ")'>Edit</a></b> "
                    }
                    if("delete" in options) {
                        links += "<b><a href='#' onclick='deleteRecord(" + params + ")'>Delete</a></b> "
                    }
                    return links;
                };
                columns.push(obj);
            } else {
                var obj = {};
                obj.title = columnNames[column];
                columns.push(obj);
            }
        }

        recordData.shift(1);

        table = $('#crudTable').DataTable({
            data: recordData,
            columns: columns,
            ordering: false,
            info:     false,
            searching: false,
            lengthChange: false
        });
    }

    var recordsStr = $('#freemarker_recordsData')[0].innerText;
    if(recordsStr && recordsStr.length > 0) {
        renderRecords(JSON.parse($('#freemarker_recordsData')[0].innerText));
    }
</script>