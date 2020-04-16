<script>

    function editRecord(pageId, id) {
        console.log("editRecord() : " + pageId + " , " + id);
    }

    function deleteRecord(pageId, id) {
        console.log("deleteRecord() : " + pageId + " , " + id);
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
        $('#crudTable').DataTable({
            data: recordData,
            columns: columns,
            ordering: false,
            info:     false,
            searching: false,
            lengthChange: false
        });
    }

    renderRecords(JSON.parse($('#freemarker_recordsData')[0].innerText));
</script>