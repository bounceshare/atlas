<script>

    function bootstrap() {
        var recordData = JSON.parse($('#freemarker_recordsData')[0].innerText);
        var columnNames = recordData[0];

        var columns = [];
        for (column in columnNames) {
            var obj = {};
            obj.title = columnNames[column];
            columns.push(obj);
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

    bootstrap();
</script>