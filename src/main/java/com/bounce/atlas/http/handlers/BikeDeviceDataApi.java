package com.bounce.atlas.http.handlers;

import com.bounce.atlas.pojo.CardPojo;
import com.bounce.atlas.utils.QueryUtils;
import com.bounce.atlas.utils.RenderUtils;
import com.bounce.utils.DatabaseConnector;
import com.bounce.utils.apis.BaseApiHandler;
import com.bounce.utils.dbmodels.public_.Keys;
import com.bounce.utils.dbmodels.public_.tables.DeviceLatestData;
import com.bounce.utils.dbmodels.public_.tables.records.AxcessRecord;
import com.bounce.utils.dbmodels.public_.tables.records.BikeRecord;
import com.bounce.utils.dbmodels.public_.tables.records.DeviceLatestDataRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.AsyncResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BikeDeviceDataApi extends BaseApiHandler {

    public BikeDeviceDataApi(String inputString, AsyncResponse asyncResponse, HttpServletRequest httpRequest,
                             HttpServletResponse httpResponse) {
        super(inputString, asyncResponse, httpRequest, httpResponse);
    }

    @Override
    public void onRequest() {
        List<CardPojo> bikeDetailsCards = Lists.newArrayList();
        try {
            super.onRequest();
            int bikeId = input.optInt("id");

            BikeRecord bike = QueryUtils.getBike(bikeId);
            AxcessRecord axcessRecord = bike.fetchParent(Keys.BIKE__BIKE_AXCESS_ID_FKEY);

            DeviceLatestDataRecord deviceLatestDataRecord =
                    DatabaseConnector.getDb().getReadDbConnector().selectFrom(DeviceLatestData.DEVICE_LATEST_DATA)
                            .where(DeviceLatestData.DEVICE_LATEST_DATA.DEVICE_ID.eq(axcessRecord.getImei())).fetchAny();

            CardPojo card = RenderUtils.getCard(deviceLatestDataRecord);
            card.body = axcessRecord.getImei();
            bikeDetailsCards.add(card);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(bikeDetailsCards, new RenderUtils.CardComparator());

        Map<Object, Object> response = Maps.newHashMap();
        response.put("events", bikeDetailsCards);

        sendSuccessResponse(asyncResponse, response);
    }
}
