<html>
<head>
    <title>${title}</title>
    <!-- Latest compiled and minified CSS -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="${title}"></meta>
    <meta name="google-signin-client_id" content="665442752379-qrreooo8o3q27svvkn1i52s3v2jr3plp.apps.googleusercontent.com"></meta>

    <link rel="shortcut icon" type="image/png" href="${favicon}">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous"></link>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
          integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
          crossorigin=""/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.1/dist/MarkerCluster.css"/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.1/dist/MarkerCluster.Default.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"
            integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew=="
            crossorigin=""></script>
    <script src="https://unpkg.com/leaflet.markercluster@1.4.1/dist/leaflet.markercluster.js"></script>
    <script src="https://apis.google.com/js/platform.js" async defer></script>

</head>
<body>
<header>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="/">
            <img src="${favicon}" width="20" height="20" alt="">
        </a>
        <a class="navbar-brand" href="/">${title}</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item <#if page == 'home'> active </#if>">
                    <a class="nav-link" href="#" onclick="navBarClicks('/')">Home <#if page == 'home'> <span class="sr-only">(current)</span> </#if> </a>
                </li>
                <li class="nav-item <#if page == 'bikes'> active </#if>">
                    <a class="nav-link" href="#" onclick="navBarClicks('/bikes')">Bikes <#if page == 'bikes'> <span class="sr-only">(current)</span> </#if> </a>
                </li>
                <li hidden class="nav-item <#if page == 'test'> active </#if>">
                    <a class="nav-link" href="#" onclick="navBarClicks('/test')">Test <#if page == 'test'> <span class="sr-only">(current)</span> </#if> </a>
                </li>
                <li class="nav-item <#if page == 'bookings'> active </#if>">
                    <a class="nav-link" href="#" onclick="navBarClicks('/bookings')">Bookings <#if page == 'bookings'> <span class="sr-only">(current)</span> </#if> </a>
                </li>
                <li class="nav-item <#if page == 'tracking'> active </#if>">
                    <a class="nav-link" href="#" onclick="navBarClicks('/tracking')">Tracking <#if page == 'tracking'> <span class="sr-only">(current)</span> </#if> </a>
                </li>
                <li class="nav-item dropdown <#if page == 'layers'> active </#if>">
                    <a class="nav-link dropdown-toggle" id="dropdown03" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Layers</a>
                    <div class="dropdown-menu" aria-labelledby="dropdown03">
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/bikes')">All Bikes</a>
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/idle')">Idle</a>
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/busy')">Busy</a>
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/oos')">OOS</a>
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/parking')">Parking Fences</a>
                        <a class="dropdown-item" href="#" onclick="navBarClicks('/layers/hubs')">Hubs</a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="dropdown04" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">More</a>
                    <div class="dropdown-menu" aria-labelledby="dropdown04">
                        <a class="dropdown-item" href="#" onclick="signOut()">Logout</a>
                        <div class="dropdown-item">
                            <input type="checkbox" class="form-check-input text-center" id="refreshCheckbox">Auto Refresh</input>
                        </div>
                        <!--<label class="form-check-label dropdown-item" for="refreshCheckbox">Auto Refresh</label>-->
                    </div>
                </li>
                <#if help??>
                <li class="nav-item">
                    <a class="nav-link" href="#" data-toggle="popover" title="Help" data-content="${help}">?</a>
                </li>
                <#else></#if>
            </ul>
            <div class="form-inline mt-2 mt-md-0">
                <input class="form-control mr-sm-2" type="text" id="searchBar" placeholder="Search" aria-label="Search"></input>
                <button class="btn btn-outline-success my-2 my-sm-0" onclick="search()">Search</button>&nbsp;
            </div>&nbsp;&nbsp;

        </div>
    </nav>
    <br/>
    <br/>
    <div class="progress" id="progressBar" hidden>
        <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div>
    </div>
    <div class="g-signin2" data-onsuccess="onSignIn" hidden data-width="300" data-height="50" data-longtitle="true">Sing in with google</div>
</header>
<#include "/scripts/utils.js">