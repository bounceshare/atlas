# Build Command
``mvn clean install -DskipTests``

# Running Locally
Go ahead and install Jetty-Runner plugin on IntelliJ if not installed. 

#Setting up local config
``./setup_local.sh <appname>``

#Sample Config
Please go though this sample config and configure one yourself with appropriate values

![Sample config](https://upload.bounce.bike/devops/57218388-59e0-477a-95c5-7b438272e05f "Sample Config")


# Bounce Atlas Documentation

>`This documentation contains information about the fields defined in Bounce Atlas and the approaches to access it.`

## I. Fields defined in Bounce Atlas

### 1. Markers:
Markers are used to mark a point on the map, makes it easy to notice and identify.

**markers** is an array of objects, each object containing :
* **location** **:** *(Type: Object{lat: point_lat, lon: point_lon})*
Latitude and longitude of the locations where marker is to placed.

* **iconurl** **:** *(Type: String)*
Url to the icon corresponding to marker

* **title** **:** *(Type: String)*
Title of the marker

* **subtext** **:** *(Type: String)*
More about the activity on that point

* **data** **:** *(Type: JSON Array of key-value pairs)*
This contains the additional information that is shown after clicking on the marker.
Value of key-value pair also supports HTML contents.

* **Example :**

**JSON showing markers content:**
```json 
"markers": [
      {
        "location": {
          "lat": 12.930701,
          "lon": 77.5431202
        },
        "iconUrl": "https://upload.bounce.bike/asset_safety_pictures/black.png",
        "title": "Report",
        "subtext": "Reported Event : BIKE DAMAGE AND PARTS THEFT",
        "cta": "",
        "data": {
          "Timestamp": "Feb 3, 2020 11:53:46 PM",
          "Location": "<b><a href='http://maps.google.com/?q=12.930701,77.5431202' target=\"_blank\">12.930701,77.543120</a></b>"
    }
  }
]
```
**Image showing markers displayed on screen:**

![](https://i.imgur.com/ZVMQaDK.png)



### 2. Events or Timeline:
Events or Timeline is List of Cards with specific styles and parameters. It can be used to represent list of events.

**events** is an array of objects, each object containing :

* **header :** *(Type: String)*
Title of the event

* **body :** *(Type: String)*
More about the activity of that event. Supports HTML content.

* **details :** *(Type: JSON Array of key-value pairs)*
This contains additional information to be shown.
Value of key-value pair supports HTML contents

* **time :** *(Type: Long)*
It specifies the order in list to be displayed in atlas
lower the time value, card goes to bottom of the list shown in atlas
higher the time value, card goes to top of the list shown in atlas


* **timeString :** *(Type: String)*
It is a string which is shown in top right corner of the event


* **Example :**
**JSON showing event parameters:** 

```json
"events": [
  {
    "header": "1 GoodTrip",
    "time": 1,
    "timeString": "Fri Jan 10 10:02:27 IST 2020",
    "body": "",
    "color": "#28a84c",
    "details": {
      "Time difference from previous event": "13188 minutes",
      "End Location": "<b><a href='http://maps.google.com/?q=12.847227,77.677826' target=\"_blank\">12.847227,77.677826</a></b>",
      "Distance": "0.015048372126878417Km",
      "End time": "Fri Jan 10 10:02:27 IST 2020",
      "Start time": "Fri Jan 10 09:56:39 IST 2020",
      "Start Location": "<b><a href='http://maps.google.com/?q=12.8472421,77.6777258' target=\"_blank\">12.847242,77.677726</a></b>"
    }
  }
]
```

**Image showing event when its displayed:** 

![](https://i.imgur.com/HprQ8Oz.png)


### 3. Paths:
Paths are the lines which connect different points on the map.
It can be used to represent movements of the bikes

**path** is an array of objects, each object containing:
* **points** **:** *(Type: JSON Array of Object{lat: point_lat, lon: point_lon})*
Array of latitude and longitude, specifying the needed path.

* **color :** *(Type: String)*
Hex of color of the path line

* **data** **:** *(Type: JSON Array of key-value pairs)*
This contains the information that is shown after clicking on the path line.
Value of key-value pair also supports HTML contents.

* **lineweight** **:** *(Type: String)*
This defines the weight(boldness) of the line.

**JSON showing path content**: 
```json 
"paths": [
  {
    "points": [
      {
        "lat": 12.93647,
        "lon": 77.60654
      },
      {
        "lat": 12.9348,
        "lon": 77.60974
      },
      {
        "lat": 12.93216,
        "lon": 77.61011
      },
      {
        "lat": 12.92527,
        "lon": 77.60875
      }
    ],
    "color": "red",
    "data": {
      "Booking Id": 2220857,
      "Est. Time": 3318.0,
      "Status": "completed",
      "Timeline": "<b><a href='#' onclick='showTimeline(\"/apis/booking/events\", 2220857, \"Bike Event Timeline - 2220857\");'>Events</a></b>",
      "User Id": 140620,
      "Trip Created At": "2020-02-12 15:03:42.395",
      "Trip Started At": "2020-02-12 15:04:17.09",
      "Est. Distance": 16348.0,
      "Est. Cost": 222.0
    }
  }
]
```

**Image showing a path line and infobox(data) when it is displayed**

![](https://i.imgur.com/srhpmHU.png)



### 4. Fences:
Fences are polygons which can be used to represent a region on map

**fences** is an array of objects, each object containing :


* **data :** (Type: JSON Array of key-value pairs)
This contains the information to be shown on clicking on the fence.
Value of key-value pair also supports HTML contents

* **points :** (Type: JSON Array of Object{lat: point_lat, lon: point_lon})
Latitude and longitude of the locations where marker is to placed.


* **fillColor :** (Type: String)
Hex of color of the fence

* **fillOpacity :** (Type: Double)
Opacity of Color of the fence

* **Example :**

**JSON showing fence parameters: **

```json
"fences": [
      {
        "points": [
          {
            "lat": 12.9417700595218,
            "lon": 77.5988063009201
          },
          {
            "lat": 12.9435537110075,
            "lon": 77.5982345443502
          },
          {
            "lat": 12.9439164657942,
            "lon": 77.5964353294142
          },
          {
            "lat": 12.9424955974494,
            "lon": 77.5952079022452
          },
          {
            "lat": 12.9407119766051,
            "lon": 77.5957796549167
          },
          {
            "lat": 12.9403491934642,
            "lon": 77.5975788386564
          },
          {
            "lat": 12.9417700595218,
            "lon": 77.5988063009201
          },
          {
            "lat": 12.9417700595218,
            "lon": 77.5988063009201
          }
        ],
        "color": "black",
        "fillColor": "#000",
        "fillOpacity": 0.5,
        "data": {
          "Events": "<b><a href='#' onclick='showTimeline(\"https://asset-safety-service.bounce.bike/timeline?token=53F494EC8925C8FFBB24951738147\", \"KA-03-AH-8949 d\", \"Timeline\", true);'>Show Timeline</a></b>",
          "Spot name": "8961892583bffff",
          "Spot Id": "9095"
        }
      }
    ]
```
**Image showing fence when its displayed: **

![](https://i.imgur.com/3hQgsoe.png)



### 5. Circles:
Circles can be used to represent a region on map

**circles** is an array of objects, each object containing :

* **data :** (Type: JSON Array of key-value pairs)
This contains the information to be shown on clicking on the fence.
Value of key-value pair also supports HTML contents


* **radius :** (Type: String)
Radius of circle

* **location :** (Type: Object{lat: point_lat, lon: point_lon})
Center of circle

* **fillColor :** (Type: String)
Hex of Color of the fence

* **fillOpacity :** (Type: Double)
Opacity of Color of the fence

* **Example :**

**JSON showing circle parameters: **
```json
"circles": [
  {
    "location": {
      "lat": 12.938837,
      "lon": 77.591446
    },
    "color": "blue",
    "fillColor": "#a2cff5",
    "radius": 20.0,
    "fillOpacity": 0.5,
    "data": {
      "Key": "Value",
      "Another Key": "Another Value",
      "Name": "Shop in Jayanagar"
    }
  }
]
```

**Image showing circle when its displayed: **

![](https://i.imgur.com/ogQQR4U.png)



## II. Configuration for adding your backend service to the atlas

In order to add your service to the Bounce Atlas, config needs to be updated by adding the following JSON in tabs[] JSON Array.
The link for atlas config is [here](https://atlas.bounce.bike/config)
**(Since this link belongs to prod version of the atlas, be careful while updating it, otherwise it will cause problems to other services in atlas)**

Parmeters to be added are:

* **page :** *(Type: String)*
Name of the service

* **pageId :** *(Type: String)*
Unique page Id

* **path :** *(Type: String)*
Path of the front end

* **searchUrl :** *(Type: String)*
Url needs to be called to obtain data. Basic Authentication with the help of query params is recommended

* **searchPage :** *(Type: Boolean)*
If true, text input is enabled in the navigation bar

* **searchText :** *(Type: String)*
Placeholder for input in navigation bar

* **autoRefresh :** *(Type: Boolean)*
If true: Backend call is made for actions movement/zoom-in/zoom-out in map.

* **help :** *(Type: String)*
Guide for the end users

* **zoom :** *(Type: Integer)*
Default zoom level for the map when this page is rendered

* **defaultLocation :** *(Type: String)*
Default location in the format (12.132121, 77.65421) to indicate the default location when the map will be rendered

* **editFenceUrl :** *(Type: String)*
To indicate if a page will have edit fences/markers option. 

* **editFenceDataSchema :** *(Type: Map<String, Object>)*
Schema in the format specified [here](https://jsonform.github.io/jsonform/playground/index.html?example=schema-basic) for updating fence metadata.

* **searchDataSchema :** *(Type: Map<String, Object>)*
Schema in the format specified [here](https://jsonform.github.io/jsonform/playground/index.html?example=schema-basic) for customising search options in the manner of a form.

* **Examples for config :**

**JSON showing parameters Asset safety service of Bounce**
```json
{
    "page": "Asset Safety",
    "pageId": "Layers",
    "path": "/layers/asset_safety",
    "searchUrl": "https://asset-safety-service.bounce.bike/search2?token=53F494EC8925C8FFBB24951738147",
    "searchPage": "true",
    "searchText": "BikeID/Number",
    "autoRefresh": "false",
    "help": "You can search for asset safety data for a bike. The format to search for it is - <BikeId or Vehicle number (KA-51-AE-4317)>",
    "zoom": 0
}
```

**JSON showing parameters for a test page which is used to create geofences**
```json
{
    "page": "Test Draw Objects",
    "pageId": "Test Stuff",
    "path": "/draw_test",
    "autoRefresh": "false",
    "help": "Draw paths, fences, circles, markers",
    "auth": "open",
    "zoom": 5,
    "editFenceUrl": "/apis/test/drawnObjs",
    "editFenceDataSchema": {
        "name": {
            "title": "Fence Name",
            "description": "Please specify a name to identify this geo fence",
            "type": "string"
        },
        "gender": {
            "title": "Type",
            "description": "Fence type",
            "type": "string",
            "enum": [
                "Red Zone",
                "Black Zone",
                "Blue Zone"
            ]
        }
    }
}
```

## III. Approaches to communicate with the backend

### Approach 1:
Steps:

1. Select the tab corresponding to your service in the navigation bar of atlas
2. Add searchQuery in text-input located at rightmost of the navigation bar
3. Hit 'Search' or Enter

It creates an http request to searchUrl mentioned in atlas config as shown below :
```json
POST /searchUrl_path HTTP/1.1
Host: searchUrl_host
Content-Type: application/json
{
	"searchQuery" : searchQuery
}
```
The backend should return the atlas elements in this way :
```json
{
    "statusCode":200,
    "message":"SUCCESS",
    "errorReason":"",
    "data": {
        "markers": [ Markers Field Data],
        "paths": [ Paths Field Data ],
        "circles": [ Circles Field Data ],
        "fences": [ Fences Field Data ],
        "events": [ Events Field Data ]
    }
}
```
[Here](https://bitbucket.org/wicked-ride/bounce-atlas/src/master/src/main/resources/sample_response.json) is the sample valid response for the reference

>**NOTE**: Only once the configuation is done at the atlas config this approach works


### Approach 2:

There is another trigger for Timeline modal apart from Approach 1.
This works only for timeline modal

This can be done by calling showTimeline(url, id, title, isSidebar) javascript function defined inside atlas
showTimeLine() sends a post request to the url, with a body.

The parameters are specified below:
### Inputs:
* **url :** *(Type: String)*
Url to be called to obtain timeline

* **id :** *(Type: Any)*
This is used to create body to be sent in post request as below
```json
{
    "id": id 
}
```

* **title :** *(Type: String)*
Title of the Events modal

* **isSidebar :** *(Type: String) (default = false)*
Modal rendered at the left side of the screen if value is true
Otherwise rendered at center


* **Example :**

>Code snippet for HTML to be included in order to show timeline, as soon as user clicks on it.
>**NOTE**: This can be added in any of the fields which support HTML codes.
```html
<b><a 
    href='#' 
    onclick='showTimeline("https://asset-safety-service.bounce.bike/timeline?token=123", "KA-03-AH-8949", "Timeline for KA-03-AH-8949", true);'
>Events</a></b>
```

The backend should return the atlas elements in this way
```json
{
    "statusCode":200,
    "message":"SUCCESS",
    "errorReason":"",
    "data": {
        "events": [ Events Field Data ]
    }
}
```
**NOTE**: Backend can only send "events" JSON Array


**Image of entire screen if events are shown in sidebar:**
![](https://i.imgur.com/Kg807rf.png)


**Image of entire screen if events are shown at center:**
![](https://i.imgur.com/Ij7UuuF.png)

## IV Rendering and updating map
prerequisites: II. Configuration for adding your backend service to the atlas


### 1. Reading map data from DB:
Geometries can be fetched directly from Postgres database can be done by setting geoJsonRecordConfig.
The parameters are specified below:
### Inputs:
* **jdbcUrl :** *(Type: String)*
Url of database to be called to obtain Map Geometries

* **dbUsername,  dbPassword, schema, table:** *(Type: string)*
These DB credentials and table names need to be added in Config JSON

* **geoJsonSqlQuery :** *(Type: String) (default = false)*
SQL query to fetch data from database.
This has to fetch data in geojson format.

### 2. Writing map data to DB:
* isEditControlSupported should be set to true in order to get options on map to add/edit/delete
* editFenceUrl : Url of custom backend which manages add/edit/delete request
1.  **Add/Edit/Delete :** *(Type: String)*

**Parameters**:
* **shape** : It can be Fence, Line, Marker
* **formData** : According to editFenceDataSchema specified
* **action** : update (for both add/editing geometries)/ delete
* **coords**: Json array of LatLngs

On add/edit/delete a HTTP request is sent to with body as this JSON format:
```json
{
  "action": "update",
  "drawnObj": {
    "formData": {
      "updated_on": "May 8, 2020 2:11:17 PM",
      "created_on": "Jul 5, 2019 10:19:54 AM",
      "name": "Bangalore",
      "active": true,
      "id": 1,
      "geo_id": 1
    },
    "shape": "Fence",
    "coords": [
      "13.056615,77.473915",
      "13.0522572,77.482048",
      "13.0466692,77.479738",
      "13.042932,77.474046",
      "13.0307426,77.4738811",
      "13.068031,77.521002",
      "13.082752,77.503246",
      "13.0668803,77.4859124"
    ],
    "drawId": "a5a53ac6-3cf2-4e8c-a09f-708c47214b2f"
  },
}
```
* **Example :**
**JSON showing circle parameters: **
```json
{
                    "page": "Cities",
                    "pageId": "Layers",
                    "path": "/layers/cities",
                    "autoRefresh": "true",
                    "zoom": 0,
                    
                    "editControl": {
                        "editFenceUrl": "http://localhost:8081/apis/cities/alter",
                        "editFenceDataSchema": {
                            "id": {
                                "title": "id",
                                "description": "Id of the geo fence",
                                "readonly": true,
                                "type": "string"
                            },
                            "name": {
                                "title": "Fence Name",
                                "description": "Please specify a name to identify this geo fence",
                                "type": "string"
                            },
                            "category": {
                                "title": "category",
                                "description": "Please specify if bike or cycle",
                                "type": "string",
                                "enum": [
                                    "Bike",
                                    "Cycle"
                                ]
                            },
                            "active": {
                                "title": "active",
                                "description": "Please specify if area is active",
                                "type": "string",
                                "enum": [
                                    "true",
                                    "false"
                                ]
                            },
                            "geo_id": {
                                "title": "geo_id",
                                "description": "Please specify geo_id (1 to 16)",
                                "type": "string"
                            },
                            "updated_on": {
                                "title": "updated_on",
                                "description": "Last updated time of the geo fence",
                                "readonly": true,
                                "type": "string"
                            },
                            "created_on": {
                                "title": "created_on",
                                "description": "Created time of the geo fence",
                                "readonly": true,
                                "type": "string"
                            }
                        },
                        "isEditControlSupported": true
                    },
                    "geoJsonRecordConfig": {
                        "jdbcUrl": "jdbc:postgresql://127.0.0.1:4567/metro_one",
                        "dbUsername": "wickedrideAdmin",
                        "dbPassword": "rkf_ZqSidB0506Ty",
                        "schema": "public",
                        "table": "bounce_service_area",
                        "geoJsonSqlQuery": "select st_asgeojson(fence) as geojson, * from bounce_service_area where ST_DWithin(fence::geography, ST_MakePoint($lon,$lat)::geography, $radius)"
                    }
                }
```
Screenshots:

![](https://i.imgur.com/oe7Wkdc.jpg)



## V Debugging

### 1. Test with JSON:
Developers can test if the response renders info on the map by pasting the JSON data at Options->Render Atlas Json
