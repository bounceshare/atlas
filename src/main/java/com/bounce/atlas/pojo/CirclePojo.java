package com.bounce.atlas.pojo;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.HubRecord;
import com.google.common.collect.Maps;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CirclePojo {

    public PointPojo location;
    public String color = "red";
    public String fillColor = "#f03";
    public double radius = 20;
    public double fillOpacity = 0.5;
    public Map<String, Object> data;

}
