<#include "/header.ftl">
<main role="main">
<div id="markerData" hidden><#if markersString??>${markersString}<#else></#if></div>
<div id="fenceData" hidden><#if fenceString??>${fenceString}<#else></#if></div>
<div id="pathData" hidden><#if pathString??>${pathString}<#else></#if></div>
<div id="circleData" hidden><#if circleString??>${circleString}<#else></#if></div>

<div id="freemarker_pagePath" hidden><#if pagePath??>${pagePath}<#else></#if></div>
<div id="freemarker_location" hidden><#if location??>${location}<#else></#if></div>
<div id="freemarker_zoom" hidden><#if zoom??>${zoom}<#else></#if></div>
<div id="freemarker_query" hidden><#if query??>${query}<#else></#if></div>
<div id="freemarker_searchtext" hidden><#if searchText??>${searchText}<#else></#if></div>
<div id="freemarker_searchurl" hidden><#if searchUrl??>${searchUrl}<#else></#if></div>
<div id="freemarker_autorefresh" hidden><#if autoRefresh??>${autoRefresh}<#else>false</#if></div>
<div id="freemarker_editFenceUrl" hidden><#if editFenceUrl??>${editFenceUrl}<#else>false</#if></div>
<div id="freemarker_editFenceDataSchema" hidden><#if editFenceDataSchema??>${editFenceDataSchema}<#else>false</#if></div>
<div id="freemarker_searchDataSchema" hidden><#if searchDataSchema??>${searchDataSchema}<#else>false</#if></div>
<div id="freemarker_recordsData" hidden><#if recordsDataString??>${recordsDataString}<#else></#if></div>
<div id="freemarker_mapView" hidden><#if mapView>true<#else></#if></div>
<div id="freemarker_tableView" hidden><#if tableView??>true<#else></#if></div>
<div id="freemarker_isCreateAllowed" hidden><#if isCreateAllowed??>true<#else></#if></div>

<#include "/sidebar.ftl">
<div id="mapDiv" style="height:100%;" <#if !mapView> hidden </#if> ></div>
<#if recordsDataString??><div><table class="table table-striped table-bordered" id="crudTable" width="100%"></table></div><#else></#if>
<#include "/scripts/render.js">
<#include "/scripts/layers.js">
<#include "/scripts/search.js">
<#include "/scripts/modal.js">
<#include "/scripts/form.js">
<#include "/scripts/crud.js">
<!--<#if page == 'home'></#if>-->
<!--<#if autoRefresh??><#include "/scripts/layers.js"></#if>-->
<!--<#if searchPage??><#include "/scripts/search.js"><#else></#if>-->
<!--<#include "/scripts/modal.js">-->
</main>
<#include "/footer.ftl">
<#include "/modal.ftl">
<#include "/edit_fence_modal.ftl">