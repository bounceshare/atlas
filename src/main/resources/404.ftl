<#include "/header.ftl">
<main role="main">
<div id="markerData" hidden><#if markersString??>${markersString}<#else></#if></div>
<div id="fenceData" hidden><#if fenceString??>${fenceString}<#else></#if></div>
<div id="pathData" hidden><#if pathString??>${pathString}<#else></#if></div>
<div id="circleData" hidden><#if circleString??>${circleString}<#else></#if></div>

<div id="freemarker_location" hidden><#if location??>${location}<#else></#if></div>
<div id="freemarker_zoom" hidden><#if zoom??>${zoom}<#else></#if></div>
<div id="freemarker_query" hidden><#if query??>${query}<#else></#if></div>
<div id="freemarker_searchtext" hidden><#if searchText??>${searchText}<#else></#if></div>
<div id="freemarker_searchurl" hidden><#if searchUrl??>${searchUrl}<#else></#if></div>
<div id="freemarker_autorefresh" hidden><#if autoRefresh??>${autoRefresh}<#else>false</#if></div>

<!--<div class="page-wrap d-flex flex-row align-items-center">-->
<!--<div class="container">-->
    <!--<div class="row justify-content-center">-->
        <!--<div class="col-md-12 text-center">-->
            <!--<span class="display-1 d-block">404</span>-->
            <!--<div class="mb-4 lead">The page you are looking for was not found.</div>-->
        <!--</div>-->
    <!--</div>-->
<!--</div>-->
<!--</div>-->

<div class="container">
<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
            <div class="card-body">
                <div class="text-center">
                    <img src="${logo}" width="200" height="200" alt="bounce.png"></img>
                </div>
                <br/>
                <div class="row justify-content-center">
                    <div class="col-md-12 text-center">
                        <span class="display-1 d-block">404</span>
                        <div class="mb-4 lead">The page you are looking for was not found.</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</main>