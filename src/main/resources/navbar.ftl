<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
   <span>
      <a class="btn btn-secondary" onclick="return toggleSidebar()">
         <i class="fas fa-bars"></i>
      </a>
      &nbsp;&nbsp;
      <a class="navbar-brand" <#if auth??> href="/" </#if> >
      <img src="${favicon}" width="20" height="20" alt="">
      </a>
   <a class="navbar-brand" <#if auth??> href="/" </#if> >${title}</a>
   </span>
</nav>