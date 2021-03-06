<script>

    function onFormSubmit(formObj, values) {
        var formSchema = formObj.formSchema;
        var formValues = formObj.values;
        var postUrl = formObj.postUrl;

        console.log(values);

        $('#formModal').modal("hide");
        data = {};
        data.data = values;
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(postUrl, data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.form, response.data.isSidebar, true, response.data.autoRefresh, response.data);
            showLoader(false);
            showSuccessMessage("Form Update : Success")
        }, function(jqXHR, exception) {
            showLoader(false);
            showFailureMessage("Form Update : Fail");
        });

        // post data to the end point
    }

    function onFormPageSubmit(formObj, values) {
        var formSchema = formObj.formSchema;
        var formValues = formObj.values;
        var postUrl = formObj.postUrl;

        console.log(values);

        $('#formModal').modal("hide");
        data = {};
        data.data = values;
        if(isLoading) {
            return;
        }
        showLoader(true);
        httpPost(postUrl, data, function(response) {
            invalidateMap(response.data.markers, response.data.fences, response.data.circles, response.data.paths, response.data.events, response.data.form, response.data.isSidebar, true, response.data.autoRefresh, response.data);
            showLoader(false);
            bootbox.alert("Form Submission Successful", function() {
                location.reload();
            });
        }, function(jqXHR, exception) {
            showLoader(false);
            bootbox.alert("Form Submission Failure : " + exception);
        });

        // post data to the end point
    }

    function fetchForm(url, id) {
        isLoading = true;
        showLoader(true);
        data = {};
        data.id = id;
        httpPost(url, data, function(response) {
            renderForm(response.data.form);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
             showLoader(false);
        });
    }

    function fetchFormAndRender(url, dataObj) {
        isLoading = true;
        showLoader(true);
        data = {};
        data.data = dataObj;
        httpPost(url, data, function(response) {
            renderFormPage(response.data.form);
            showLoader(false);
        }, function(jqXHR, exceptiom) {
             showLoader(false);
        });
    }

    function renderFormPage(formObj) {
        var formSchema = formObj.formSchema;
        var formValues = formObj.values;
        var postUrl = formObj.postUrl;

        $('#formpage-form')[0].innerHTML = "";
            // show form based on schema
        $('#formpage-form').jsonForm({
            schema: formSchema,
            form: ["*"],
            value: formValues,
            form: [
              "*",
              {
                "type": "submit",
                "title": "Submit"
              }
            ],
            onSubmit: function (errors, values) {
              console.log("onSubmit()");
              onFormPageSubmit(formObj, values);
            }
        });

//        setupDateTimeElement();
    }

    function renderForm(formObj) {
        var formSchema = formObj.formSchema;
        var formValues = formObj.values;
        var postUrl = formObj.postUrl;

        $('#formview-form')[0].innerHTML = "";
            // show form based on schema
        $('#formview-form').jsonForm({
            schema: formSchema,
            form: ["*"],
            value: formValues,
            onSubmit: function (errors, values) {
              console.log("onSubmit()");
              onFormSubmit(formObj, values);
            }
          });

        setupDateTimeElement();
        $('#formModal').modal();
    }

    var formStr = $('#freemarker_formPageUrl')[0].innerText;
    if(formStr && formStr.length > 0) {
        fetchFormAndRender(formStr, $('#freemarker_formPageReqData')[0].innerText);
    }
    var formSchemaStr = $('#freemarker_formPageSchema')[0].innerText;
    if(formSchemaStr && formSchemaStr.length > 0) {
        renderFormPage(JSON.parse(formSchemaStr));
    }

</script>
<div class="modal fade" id="formModal" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="formModalTitle">Form Data</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">

            <div class="container py-2 mt-4 mb-4" id="form-view">
                <form id="formview-form"></form>
            </div>


        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="$('#formview-form').submit();">Submit</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        </div>
    </div>
</div>
</div>