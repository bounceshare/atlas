# Atlas
An OpenSource Internal Admin-Panel Framework

Atlas lets you build custom admin panels in a very easy manner. Atlas fills the void for a good quality map based admin panel. It can connect to remove PostgreSQL databases and fetch info directly and make editable UI components out of them. 

Have a table storing some PostGIS info? Now you can easily visualise them on the cloud using Atlas. Atlas understands GeoJSON, so you can go ahead and configure a page to render some table on PostgreSQL by simply configuring a query that returns a GeoJSON. As simple as that! Atlas even supports CRUD on PostgreSQL tables.

```yaml
{
  "page": "City Geofences",
  "pageId": "Miscellaneous",
  "path": "/layers/cities",
  "autoRefresh": "true",
  "zoom": 0,
  "editControl": {
    "editFenceUrl": "/apis/test/drawnObjs",
    "isEditControlSupported": false
  },
  "geoJsonRecordConfig": {
    "jdbcUrl": "jdbc:postgresql://<database_host:port>/database_name",
    "dbUsername": "<username>",
    "dbPassword": "<password>",
    "schema": "public",
    "table": "geofence_area",
    "geoJsonSqlQuery": "select st_asgeojson(fence) as geojson, * from geofence_area where ST_DWithin(fence::geography, ST_MakePoint($lon,$lat)::geography, $radius)"
  }
}
```
 
![GeoJSON rendered on Atlas](https://upload.bounce.bike/images/c1218a90-7a5c-488c-9799-4ae16c8e5754)

## Screenshots 

![Screenshot1](https://upload.bounce.bike/images/dc3539b7-1216-4b6a-858d-e0e409cb7b4c)

![Screenshot2](https://upload.bounce.bike/images/25ca396e-e9c7-418b-80e7-4a25adc5cde4)

You can find more info from [here](https://github.com/bounceshare/atlas/wiki)

