<#include "/header.ftl">
<main role="main">
<div id="markerData" hidden><#if markersString??>${markersString}<#else></#if></div>
<div id="fenceData" hidden><#if fenceString??>${fenceString}<#else></#if></div>
<div id="pathData" hidden><#if pathString??>${pathString}<#else></#if></div>
<div id="circleData" hidden><#if circleString??>${circleString}<#else></#if></div>

<div id="freemarker_location" hidden><#if location??>${location}<#else></#if></div>
<div id="freemarker_layers" hidden><#if layers??>${layers}<#else></#if></div>

<div id="mapDiv" style="height:100%;"></div>
<#include "/scripts/render.js">
<#if page == 'home'><#include "/scripts/layers.js"></#if>
</main>
<#include "/footer.ftl">