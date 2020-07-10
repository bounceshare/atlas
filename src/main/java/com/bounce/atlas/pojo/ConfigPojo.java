package com.bounce.atlas.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
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
    @SerializedName("authRoles")
    @Expose
    private Map<String, List<String>> authRoles = Maps.newLinkedHashMap();

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

    public Map<String, List<String>> getAuthRoles() {
        return authRoles;
    }

    public void setAuthRoles(Map<String, List<String>> authRoles) {
        this.authRoles = authRoles;
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
        @SerializedName("searchDataSchema")
        @Expose
        private Map<String, Object> searchDataSchema;
        @SerializedName("crudConfig")
        @Expose
        private CrudConfig crudConfig;
        @SerializedName("editControl")
        @Expose
        private EditControl editControl;
        @SerializedName("geoJsonRecordConfig")
        @Expose
        private GeoJsonRecordConfig geoJsonRecordConfig;
        @SerializedName("formConfig")
        @Expose
        private FormConfig formConfig;

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

        public String getSearchDataSchema() {
            return new GsonBuilder().disableHtmlEscaping().create().toJson(searchDataSchema);
        }

        public void setSearchDataSchema(Map<String, Object> searchDataSchema) {
            this.searchDataSchema = searchDataSchema;
        }

        public CrudConfig getCrudConfig() {
            return crudConfig;
        }

        public void setCrudConfig(CrudConfig crudConfig) {
            this.crudConfig = crudConfig;
        }

        public EditControl getEditControl() {
            return editControl;
        }

        public void setEditControl(EditControl editControl) {
            this.editControl = editControl;
        }

        public GeoJsonRecordConfig getGeoJsonRecordConfig() {
            return geoJsonRecordConfig;
        }

        public void setGeoJsonRecordConfig(GeoJsonRecordConfig geoJsonRecordConfig) {
            this.geoJsonRecordConfig = geoJsonRecordConfig;
        }

        public FormConfig getFormConfig() {
            return formConfig;
        }

        public void setFormConfig(FormConfig formConfig) {
            this.formConfig = formConfig;
        }
    }

    public class FormConfig {
        @SerializedName("formUrl")
        @Expose
        private String formUrl;
        @SerializedName("formRequestData")
        @Expose
        private Map<String, Object> formRequestData;

        public String getFormUrl() {
            return formUrl;
        }

        public void setFormUrl(String formUrl) {
            this.formUrl = formUrl;
        }

        public Map<String, Object> getFormRequestData() {
            return formRequestData;
        }

        public void setFormRequestData(Map<String, Object> formRequestData) {
            this.formRequestData = formRequestData;
        }
    }

    public class CrudConfig {
        @SerializedName("isCreateAllowed")
        @Expose
        private boolean isCreateAllowed;
        @SerializedName("isEditAllowed")
        @Expose
        private boolean isEditAllowed;
        @SerializedName("isDeleteAllowed")
        @Expose
        private boolean isDeleteAllowed;
        @SerializedName("jdbcUrl")
        @Expose
        private String jdbcUrl;
        @SerializedName("dbUsername")
        @Expose
        private String dbUsername;
        @SerializedName("dbPassword")
        @Expose
        private String dbPassword;
        @SerializedName("schema")
        @Expose
        private String schema;
        @SerializedName("table")
        @Expose
        private String table;

        public List<String> getListExcludeFields() {
            return listExcludeFields;
        }

        public void setListExcludeFields(List<String> listExcludeFields) {
            this.listExcludeFields = listExcludeFields;
        }

        public List<String> getCreateExcludeFields() {
            return createExcludeFields;
        }

        public void setCreateExcludeFields(List<String> createExcludeFields) {
            this.createExcludeFields = createExcludeFields;
        }

        public List<String> getEditExcludeFields() {
            return editExcludeFields;
        }

        public void setEditExcludeFields(List<String> editExcludeFields) {
            this.editExcludeFields = editExcludeFields;
        }

        @SerializedName("listExcludeFields")
        @Expose
        private List<String> listExcludeFields = new ArrayList<>();

        @SerializedName("createExcludeFields")
        @Expose
        private List<String> createExcludeFields = new ArrayList<>();

        @SerializedName("editExcludeFields")
        @Expose
        private List<String> editExcludeFields = new ArrayList<>();

        public boolean isCreateAllowed() {
            return isCreateAllowed;
        }

        public void setCreateAllowed(boolean createAllowed) {
            isCreateAllowed = createAllowed;
        }

        public boolean isEditAllowed() {
            return isEditAllowed;
        }

        public void setEditAllowed(boolean editAllowed) {
            isEditAllowed = editAllowed;
        }

        public boolean isDeleteAllowed() {
            return isDeleteAllowed;
        }

        public void setDeleteAllowed(boolean deleteAllowed) {
            isDeleteAllowed = deleteAllowed;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getDbUsername() {
            return dbUsername;
        }

        public void setDbUsername(String dbUsername) {
            this.dbUsername = dbUsername;
        }

        public String getDbPassword() {
            return dbPassword;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = dbPassword;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }
    }

    public class EditControl {
        @SerializedName("editFenceUrl")
        @Expose
        private String editFenceUrl;
        @SerializedName("editFenceDataSchema")
        @Expose
        private Map<String, Object> editFenceDataSchema;
        @SerializedName("isEditControlSupported")
        @Expose
        private boolean isEditControlSupported;

        public boolean IsEditControlSupported() {
            return isEditControlSupported;
        }

        public void setIsEditControlSupported(boolean isEditControlSupported) {
            this.isEditControlSupported = isEditControlSupported;
        }

        public String getEditFenceUrl() {
            return editFenceUrl;
        }

        public void setEditFenceUrl(String editFenceUrl) {
            this.editFenceUrl = editFenceUrl;
        }

        public Map<String, Object> getEditFenceDataSchema() {
            return editFenceDataSchema;
        }

        public void setEditFenceDataSchema(Map<String, Object> editFenceDataSchema) {
            this.editFenceDataSchema = editFenceDataSchema;
        }
    }

    public class GeoJsonRecordConfig {
        @SerializedName("jdbcUrl")
        @Expose
        private String jdbcUrl;
        @SerializedName("dbUsername")
        @Expose
        private String dbUsername;
        @SerializedName("dbPassword")
        @Expose
        private String dbPassword;
        @SerializedName("schema")
        @Expose
        private String schema;
        @SerializedName("table")
        @Expose
        private String table;
        @SerializedName("geoJsonSqlQuery")
        @Expose
        private String geoJsonSqlQuery;

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getDbUsername() {
            return dbUsername;
        }

        public void setDbUsername(String dbUsername) {
            this.dbUsername = dbUsername;
        }

        public String getDbPassword() {
            return dbPassword;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = dbPassword;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getGeoJsonSqlQuery() {
            return geoJsonSqlQuery;
        }

        public void setGeoJsonSqlQuery(String geoJsonSqlQuery) {
            this.geoJsonSqlQuery = geoJsonSqlQuery;
        }
    }

}