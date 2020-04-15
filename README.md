# BOUNCE ATLAS DOCUMENTATION 

>`This documentation contains information about the fields defined in Bounce Atlas and the approaches to access it.`

## <u>I. Fields defined in Bounce Atlas</u>

### `1. Markers`:
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

>**JSON showing markers content:**
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
>**Image showing markers displayed on screen:**
><img src="https://upload.bounce.bike/null/094d29d5-6aa2-4414-8f16-ff866e2773a3" width="80%" height="80%">
![](https://upload.bounce.bike/null/094d29d5-6aa2-4414-8f16-ff866e2773a3)
![](https://i.imgur.com/FajnbXt.png)


### `2. Events or Timeline`:
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
>**JSON showing event parameters:** 

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

>**Image showing event when its displayed:** 
![](https://i.imgur.com/ZvBavpR.png)



### `3. Paths`:
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

>**JSON showing path content**: 
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

>**Image showing a path line and infobox(data) when it is displayed**
><img src="https://i.imgur.com/6ydCwMn.png" width="80%" height="80%">
![](https://i.imgur.com/6ydCwMn.png)



### `4. Fences`:
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

>JSON showing fence parameters: 

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
>Image showing fence when its displayed: 
><img src="https://i.imgur.com/gaxZjy8.png" width="80%" height="80%">
![](https://i.imgur.com/gaxZjy8.png)


### `5. Circles`:
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

>JSON showing fence parameters: 
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

>Image showing circle when its displayed: 
![](https://i.imgur.com/s0wwafW.png)


## <u>II Configuration for adding your backend service to the atlas</u>

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


* **Example :**

>JSON showing parameters Asset safety service of Bounce
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

## <u>III. Approaches to communicate with the backend</u>

### `Approach 1`:
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
[Here](https://bitbucket.org/wicked-ride/bounce-atlas/src/master/src/main/resources/sample_response.json) is the sample valid response for the refernce

>**NOTE**: Only once the configuation is done at the atlas config this approaches works


### `Approach 2`:

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
**NOTE**: Backend can only send "events" JSON Array. If it sends, 


>**Image of entire screen if events are shown in sidebar:**
>
><img src="https://i.imgur.com/Kg807rf.png" width="100%" height="100%">
![](https://i.imgur.com/Kg807rf.png)


>**Image of entire screen if events are shown at center:**
>
><img src="https://i.imgur.com/Ij7UuuF.png" width="100%" height="100%">
![](https://i.imgur.com/Ij7UuuF.png)

## <u>IV Debugging</u>

### `1. Test with JSON`:
Developers can test if the response renders info on the map by pasting the JSON data at Options->Render Atlas Json


