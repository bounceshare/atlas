<#include "/header.ftl">
<main role="main">
    <div id="markerData" hidden><#if markersString??>${markersString}<#else></#if></div>
    <div id="fenceData" hidden><#if fenceString??>${fenceString}<#else></#if></div>
    <div id="pathData" hidden><#if pathString??>${pathString}<#else></#if></div>
    <div id="circleData" hidden><#if circleString??>${circleString}<#else></#if></div>

    <div id="mapDiv" style="height:100%;"></div>
    <#include "/scripts/home_deprecated.js">
</main>
<#include "/footer.ftl">