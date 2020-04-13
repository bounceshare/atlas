package com.bounce.atlas.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.http.util.TextUtils;

public class ConfigPojo {

    @SerializedName("defaultLocation")
    @Expose
    private String defaultLocation;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("favicon")
    @Expose
    private String favicon;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("zoom")
    @Expose
    private String zoom;
    @SerializedName("tabs")
    @Expose
    private List<Page> tabs = null;

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public List<Page> getTabs() {
        return tabs;
    }

    public void setTabs(List<Page> tabs) {
        this.tabs = tabs;
    }

    public class Page {

        @SerializedName("page")
        @Expose
        private String page;
        @SerializedName("pageId")
        @Expose
        private String pageId;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("searchUrl")
        @Expose
        private String searchUrl;
        @SerializedName("searchPage")
        @Expose
        private String searchPage;
        @SerializedName("searchText")
        @Expose
        private String searchText;
        @SerializedName("autoRefresh")
        @Expose
        private String autoRefresh;
        @SerializedName("help")
        @Expose
        private String help;
        @SerializedName("tabName")
        @Expose
        private String tabName;
        @SerializedName("pages")
        @Expose
        private List<Page> pages = null;
        @SerializedName("auth")
        @Expose
        private String auth;
        @SerializedName("defaultLocation")
        @Expose
        private String defaultLocation;
        @SerializedName("zoom")
        @Expose
        private int zoom;
        @SerializedName("editFenceUrl")
        @Expose
        private String editFenceUrl;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSearchUrl() {
            return searchUrl;
        }

        public void setSearchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
        }

        public String getSearchPage() {
            return searchPage;
        }

        public void setSearchPage(String searchPage) {
            this.searchPage = searchPage;
        }

        public String getSearchText() {
            return searchText;
        }

        public void setSearchText(String searchText) {
            this.searchText = searchText;
        }

        public String getAutoRefresh() {
            return autoRefresh;
        }

        public void setAutoRefresh(String autoRefresh) {
            this.autoRefresh = autoRefresh;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String getTabName() {
            return tabName;
        }

        public void setTabName(String tabName) {
            this.tabName = tabName;
        }

        public List<Page> getPages() {
            return pages;
        }

        public void setPages(List<Page> pages) {
            this.pages = pages;
        }

        public boolean isAuth() {
            if(!TextUtils.isEmpty(auth) && auth.equals("open")) {
                return false;
            }
            return true;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getDefaultLocation() {
            return defaultLocation;
        }

        public void setDefaultLocation(String defaultLocation) {
            this.defaultLocation = defaultLocation;
        }

        public int getZoom() {
            return zoom;
        }

        public void setZoom(int zoom) {
            this.zoom = zoom;
        }

        public String getEditFenceUrl() {
            return editFenceUrl;
        }

        public void setEditFenceUrl(String editFence) {
            this.editFenceUrl = editFence;
        }
    }

}