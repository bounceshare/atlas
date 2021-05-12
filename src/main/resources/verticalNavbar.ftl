<nav id="sidebar" class="sidebar-wrapper">
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
          <span>Extra</span>
        </li>
        <li>
          <a href="#">
            <i class="fa fa-book"></i>
            <span>Documentation</span>
          </a>
        </li>
        <li>
          <a href="#">
            <i class="fa fa-calendar"></i>
            <span>Calendar</span>
          </a>
        </li>
        <li>
          <a href="#">
            <i class="fa fa-folder"></i>
            <span>Examples</span>
          </a>
        </li>
      </ul>
    </div>
    <!-- sidebar-menu  -->
  </div>

</nav>