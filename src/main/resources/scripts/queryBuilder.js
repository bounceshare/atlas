<div class="modal fade" id="qbModal" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="qbTitle">Query Builder</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">

            <div id="qb-view">

            </div>


        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="searchQuerySubmit()">Search</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        </div>
    </div>
</div>
</div>

<script>
    function openQueryBuilder() {

        var qbFilters = JSON.parse($('#freemarker_searchQueryBuilderFilters')[0].innerText);
        $('#qb-view')[0].innerHTML = "";
        $('#qb-view').queryBuilder({
          plugins: {
            'not-group' : {icon_checked: 'fa fa-check', icon_unchecked: 'icon-check-empty'}
          },
          filters: qbFilters
        });
        $('#qbModal').modal();
    }

    function searchQuerySubmit() {
        console.log("searchQuerySubmit()");
        var sqlRaw = $('#qb-view').queryBuilder('getSQL', false, true).sql.replace(/(\r\n|\n|\r)/gm, " ");
        console.log("searchQuerySubmit() query : " + sqlRaw);
    }
</script>