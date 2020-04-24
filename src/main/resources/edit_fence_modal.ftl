<script>

// TODO iterate through layers and add values to the layer object itself
function submitFenceData(drawId, values) {

    map.eachLayer(function(layer){
        if(layer.pm && typeof layer.pm.isPolygon == 'function' && drawId == layer._leaflet_id){
            layer.formData = {};
            for(var key in values) {
                layer.formData[key] = values[key];
            }
            console.log(layer);
            $('#editFenceModal').modal("hide");
            return;
        }
    });

}

// TODO read formData from layer
function showFenceModal(drawId) {

    var formDataVals = null;
    map.eachLayer(function(layer){
        if(layer.pm && typeof layer.pm.isPolygon == 'function' && drawId == layer._leaflet_id){
            formDataVals = layer.formData;
        }
    });

    var editFenceDataSchema = JSON.parse(($('#freemarker_editFenceDataSchema')[0].innerText));
    console.log(editFenceDataSchema);
    $('#editFence-form')[0].innerHTML = "";
    // show form based on schema
    $('#editFence-form').jsonForm({
        schema: editFenceDataSchema,
        form: ["*"],
        value: formDataVals,
        onSubmit: function (errors, values) {
          console.log(drawId + values.toString());
          submitFenceData(drawId, values);
        }
      });

    $('#editFenceModal').modal();
}

</script>
<div class="modal fade" id="editFenceModal" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="editFenceLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editFenceModalTitle">Data</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">


                <div class="container py-2 mt-4 mb-4" id="editFence-view">
                    <form id="editFence-form"></form>
                </div>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="$('#editFence-form').submit();">Done</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>