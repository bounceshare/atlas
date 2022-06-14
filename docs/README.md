# Atlas
An OpenSource Internal Admin-Panel Framework

Atlas lets you build custom admin panels in a very easy manner. Atlas fills the void for a good quality map based admin panel. It can connect to remote PostgreSQL databases and fetch info directly and make editable UI components out of them. 

Have a table storing some PostGIS info? 
Now you can easily visualise them on the cloud using Atlas. Atlas understands GeoJSON, so you can go ahead and configure a page to render a table on PostgreSQL by simply configuring a query that returns a GeoJSON. As simple as that! Atlas even supports CRUD on PostgreSQL tables.

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
 
![GeoJSON rendered on Atlas](https://upload.bounce.bike/test/b53ee202-049c-4e2e-bddf-8b65f72dc278)

## Screenshots 

![Screenshot1](https://upload.bounce.bike/test/9ed62c9f-182b-4d5b-afa2-82e7a4bb7143)

![Screenshot2](https://upload.bounce.bike/test/2d3027f7-43a9-4ab9-8915-753d733691b3)

You can find more info from [here](https://github.com/bounceshare/atlas/wiki)

 

## Demo
In light of the 2nd wave of the Covid-19 pandemic in India, Atlas was used to build a few pages to track hospital bed availability, vaccine and test centres in Bangalore. 
[https://covid.bounceshare.com/covid19](https://covid.bounceshare.com/covid19)
