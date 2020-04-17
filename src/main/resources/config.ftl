<#include "/header.ftl">
<main role="main">
<br/><br/>
<script>
    $(document).ready(function(){
        obj = JSON.parse($('#configTextArea')[0].innerHTML);
        text = JSON.stringify(obj, undefined, 4);
        $('#configTextArea')[0].innerHTML = text;
    });

    function submit() {
        console.log("Submitting Config Data" + $('#configTextArea')[0].value);
        data = {};
        data.config = $('#configTextArea')[0].value;
        showLoader(true);
        httpPost("/config", data, function(response) {
            console.log("Config Updated");
            showLoader(false);
            showSuccessMessage("Config Updated");
        }, function(jqXHR, exception) {
            console.log("Config Update Failed");
            showLoader(false);
            showFailureMessage("Config Update Failed");
        });
    }
</script>
<div id="formDiv">
    <div class="container">
        <div class="col-sm-9 mx-auto">
            <form>
                <div class="form-group">
                    <label for="configTextArea"><b>Config Json</b></label>
                    <!--<h5 class="card-title text-center">Config JSON</h5>-->
                    <textarea class="form-control" id="configTextArea" rows="23">${config}</textarea><br/>
                </div>
            </form>
            <button onclick="submit()" class="btn btn-primary">Submit</button>
        </div>
    </div>
</div>
</main>