<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
    <a class="navbar-brand" <#if auth??> href="/" </#if> >
    <img src="${favicon}" width="20" height="20" alt="">
    </a>
    <a class="navbar-brand" <#if auth??> href="/" </#if> >${title}</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
            aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarCollapse">
    <ul class="navbar-nav mr-auto">
        <#if tabs??>
        <#list tabs as tab>
            <li class="nav-item <#if page == '${tab.page}'> active </#if>">
                <a class="nav-link" href="#" onclick="navBarClicks('${tab.path}')">${tab.page}<#if page == '${tab.page}'> <span class="sr-only">(current)</span> </#if> </a>
            </li>
        </#list>
        </#if>

    <#if nestedTabs??>
        <#list nestedTabs as tabName, tabMap>
            <li class="nav-item dropdown <#if page == '${tabName}'> active </#if>">
            <a class="nav-link dropdown-toggle" id="dropdown-${tabName}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${tabName}</a>
            <div class="dropdown-menu" aria-labelledby="dropdown-${tabName}">
                <#list tabMap as tab>
                    <a class="dropdown-item" href="#" onclick="navBarClicks('${tab.path}')">${tab.page}</a>
                </#list>
            </div>
            </li>
        </#list>
    </#if>

    <#if auth??>
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="dropdown04" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Options</a>
            <div class="dropdown-menu" aria-labelledby="dropdown04">
                <a class="dropdown-item" href="#" onclick="bootboxPromptRenderJSON()">Render Atlas JSON</a>
                <a class="dropdown-item" href="#" onclick="bootboxPromptRenderGeoJSON()">Render GeoJSON</a>
                <a class="dropdown-item" href="#" onclick="bootboxPromptRenderKML()">Render KML Contents</a>
                <#if isAdmin??><#if isAdmin>
                    <a class="dropdown-item" href="/config">Config</a>
                </#if>
        </#if>
        <div class="dropdown-item">
            <input type="checkbox" class="form-check-input text-center" id="refreshCheckbox">Auto Refresh</input>
        </div>
        <a class="dropdown-item" href="#" onclick="signOut()">Logout</a>
                <!--<label class="form-check-label dropdown-item" for="refreshCheckbox">Auto Refresh</label>-->
        </div>
        </li>
    <#if help??>
    <li class="nav-item">
    <a class="nav-link" href="#" data-toggle="popover" title="Help" data-content="${help}">?</a>
    </li>
    <#else></#if>
    </#if>
    </ul>

    <div class="form-inline mt-2 mt-md-0" <#if searchPage??><#else>hidden</#if>>
        <div id="dropdownSearch" class="dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuSearch" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Search
        </button>
        <div class="dropdown-menu dropdown-menu-lg-right" aria-labelledby="dropdownMenuSearch">
            <form class="px-4 py-3" id="dropdownMenuSearchDiv">
            </form>
            <#if queryBuilder??><a class="dropdown-item" href="#" onclick="openQueryBuilder()">Query Builder</a></#if>
        </div>
    </div>
    </div>
        <!--<div class="form-inline mt-2 mt-md-0" <#if searchPage??><#else>hidden</#if>>-->
        <!--<input class="form-control mr-sm-2" type="text" id="searchBar" placeholder="Search" aria-label="Search"></input>-->
        <!--<button class="btn btn-outline-success my-2 my-sm-0" onclick="search()">Search</button>&nbsp;-->
        <!--</div>-->
        <!--<div class="form-inline mt-2 mt-md-0" <#if editFenceUrl??><#else>hidden</#if>>-->
        <!--<button class="btn btn-outline-success my-2 my-sm-0" onclick="fenceSubmit()">Submit</button>&nbsp;-->
        <!--</div>&nbsp;&nbsp;-->

    </div>
</nav>