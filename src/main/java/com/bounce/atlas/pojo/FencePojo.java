package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.QueryUtils;
import com.bounce.utils.Log;
import com.bounce.utils.apis.BaseApiHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class FencePojo {

    public List<PointPojo> points;
    public String color = "red";
    public String fillColor = "#f03";
    public double fillOpacity = 0.5;
    public Map<String, Object> data;

}
