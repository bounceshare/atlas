package com.bounce.atlas.pojo;

import com.bounce.utils.BounceUtils;
import com.bounce.utils.Pair;
import com.bounce.utils.dbmodels.public_.enums.BikeStatus;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarkerPojo {

    public PointPojo location;
    public String legend;
    public int count;
    public String iconUrl;
    public String title;
    public String subtext;
    public String cta;
    public Map<String, Object> data;

}
