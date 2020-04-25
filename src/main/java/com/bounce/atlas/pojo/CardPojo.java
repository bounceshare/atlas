package com.bounce.atlas.pojo;

import com.bounce.atlas.utils.Constants;
import com.bounce.atlas.utils.Utils;
import com.bounce.utils.BounceUtils;
import com.bounce.utils.dbmodels.public_.tables.records.BikeStatusLogRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BookingRecord;
import com.bounce.utils.dbmodels.public_.tables.records.DeviceLatestDataRecord;
import com.bounce.utils.dbmodels.public_.tables.records.EndTripFeedbackRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CardPojo {

    public String timelineHeader;
    public String header;
    public long time;
    public String timeString;
    public String body;
    public String color = Constants.Color.BOUNCE_RED;
    public Map<String, String> details;

}
