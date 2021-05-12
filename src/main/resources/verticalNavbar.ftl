<nav id="sideNavbar" class="sidebar-wrapper">
  <div class="sidebar-content">
    <div class="sidebar-brand">
      <a href="#">Atlas</a>
      <div id="close-sidebar">
        <i class="fas fa-times"></i>
      </div>
    </div>


    <!-- sidebar-search  -->
    <div class="sidebar-menu">
      <ul>
        <li class="header-menu">
          <span>Pages</span>
        </li>

        <#if tabs??>
        <#list tabs as tab>
          <li>
            <a href="#" onclick="navBarClicks('${tab.path}')">
              <i class="fa fa-atlas <#if page == '${tab.page}'>icon-color-active</#if>"></i>
              <span>${tab.page}</span>
            </a>
          </li>
        </#list>
        </#if>

        <li class="header-menu">
          <span>Collections</span>
        </li>


        <#if nestedTabs??>
        <#list nestedTabs as tabName, tabMap>
        <li class="sidebar-dropdown">
          <a id="dropdown-${tabName}" href="#">
            <i class="fa fa-folder <#if page == '${tabName}'>icon-color-active</#if>"></i>
            <span>${tabName}</span>
          </a>
          <div class="sidebar-submenu">
            <#list tabMap as tab>
            <ul>
              <li>
                <a href="#" onclick="navBarClicks('${tab.path}')">${tab.page}</a>
              </li>
            </ul>
            </#list>
          </div>
        </li>
        </#list>
        </#if>

        <li class="header-menu">
          <span>Advanced</span>
        </li>
        <#if searchPage??>
        <li>
          <a onclick="openSearchModal()" href="#">
            <i class="fa fa-search"></i>
            <span>Search</span>
          </a>
        </li>
        </#if>
        <li>
          <a href="#">
            <input style="width: 1.1rem;height: 1.1rem;" type="checkbox" class="fa" id="refreshCheckbox"></input> &nbsp;&nbsp;
            <span>Auto Refresh</span>
          </a>
        </li>

        <#if auth??>
        <li class="header-menu">
          <span>Options</span>
        </li>
        <li>
          <a onclick="bootboxPromptRenderJSON()" href="#">
            <i class="fa fa-code"></i>
            <span>Render Atlas JSON</span>
          </a>
        </li>
        <li>
          <a onclick="bootboxPromptRenderGeoJSON()" href="#">
            <i class="fa fa-code"></i>
            <span>Render GeoJSON</span>
          </a>
        </li>
        <li>
          <a onclick="bootboxPromptRenderKML()" href="#">
            <i class="fa fa-map"></i>
            <span>Render KML</span>
          </a>
        </li>
        <#if isAdmin??><#if isAdmin>
        <li>
          <a href="/config">
            <i class="fa fa-cogs"></i>
            <span>Config</span>
          </a>
        </li>
        </#if>
        </#if>
        <li>
          <a onclick="signOut()" href="#">
            <i class="fa fa-sign-out-alt"></i>
            <span>Sign Out</span>
          </a>
        </li>
        </#if>

        <li class="header-menu">
          <span>Open Source</span>
        </li>
        <li>
          <a href="https://github.com/bounceshare/atlas">
            <i class="fa fa-code-branch"></i>
            <span>Fork it on Github</span>
          </a>
        </li>
      </ul>
    </div>
    <!-- sidebar-menu  -->
  </div>



<!--<div class="sidebar-footer">-->
<!--<a href="https://github.com/bounceshare/atlas">-->
  <!--<i class="fa fa-github"></i>-->
  <!--<span>Github</span>-->
<!--</a>-->
<!--</div>-->

</nav>