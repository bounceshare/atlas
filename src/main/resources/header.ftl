<html>
<head>
    <title>${title}</title>
    <!-- Latest compiled and minified CSS -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="${title}"></meta>
    <meta name="google-signin-client_id" content="${googleclientid}"></meta>

    <link rel="shortcut icon" type="image/png" href="${favicon}">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous"></link>
    <link
            rel="stylesheet"
            href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css"
            integrity="sha512-xodZBNTC5n17Xt2atTPuE1HxjVMSvLVW9ocqUKLsCC5CXdbqCmblAshOMAS6/keqq/sMZMZ19scR4PsZChSR7A=="
            crossorigin=""/>

    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.1/dist/MarkerCluster.css"/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.1/dist/MarkerCluster.Default.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/files/css/leaflet-sidebar.css" />
    <link rel="stylesheet" href="/files/css/bootstrap-datetimepicker.min.css" />
    <link rel="stylesheet" href="/files/css/tailwind.min.css" />
    <link rel="stylesheet" href="https://unpkg.com/@geoman-io/leaflet-geoman-free@2.5.0/dist/leaflet-geoman.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/jQuery-QueryBuilder/dist/css/query-builder.default.min.css" />
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBJG0FV8g6IJ6kyWVy52GeLwkYeNlIRjA8" async defer></script>



    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <script
            src="https://unpkg.com/leaflet@1.7.1/dist/leaflet-src.js"
            integrity="sha512-I5Hd7FcJ9rZkH7uD01G3AjsuzFy3gqz7HIJvzFZGFt2mrCS4Piw9bYZvCgUE0aiJuiZFYIJIwpbNnDIM6ohTrg=="
            crossorigin=""
    ></script>

    <script src="https://unpkg.com/leaflet.markercluster@1.4.1/dist/leaflet.markercluster.js"></script>
    <script src="/files/scripts/leaflet-sidebar.js"></script>
    <script src="/files/scripts/underscore.js"></script>
    <script src="/files/scripts/jsv.js"></script>
    <script src="/files/scripts/jsonform.js"></script>
    <script src="/files/scripts/dot.min.js"></script>
    <script src="/files/scripts/jquery-ext.js"></script>
    <script src="/files/scripts/google_maps_styles.js"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.6.1/js/dataTables.buttons.min.js"></script>
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src="https://unpkg.com/@geoman-io/leaflet-geoman-free@2.5.0/dist/leaflet-geoman.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/5.4.0/bootbox.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.21.0/moment.min.js" type="text/javascript"></script>
    <script src="https://cdn.jsdelivr.net/npm/jQuery-QueryBuilder/dist/js/query-builder.min.js"></script>
    <script src="/files/scripts/bootstrap-datetimepicker.min.js"></script>
    <script src="/files/scripts/togeojson.js"></script>
    <script src="https://unpkg.com/leaflet.gridlayer.googlemutant@latest/dist/Leaflet.GoogleMutant.js"></script>


</head>
<body>
    <#include "/navbar.ftl">
    <br/>
    <br/>
    <div class="alert alert-success alert-dismissible fade show" id="successAlert" role="alert" hidden>
    </div>
    <div class="alert alert-danger alert-dismissible fade show" id="failureAlert" role="alert" hidden>
    </div>
    <div class="progress" id="progressBar" hidden>
        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div>
    </div>
    <div class="g-signin2" data-onsuccess="onSignIn" hidden data-width="300" data-height="50" data-longtitle="true">Sing in with google</div>
</header>
<#include "/scripts/utils.js">