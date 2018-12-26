package com.vtyc.hrapispringboot1.resources;

import com.vtyc.hrapispringboot1.dao.HrCardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Component
@Path("/api")
public class HrCardController {

    @Autowired
    HrCardInfo hrCardInfo;

    @GET
    @Path("/hr1")
    @Produces(MediaType.APPLICATION_JSON)
    public int hello(){
        //Messages messages = new Messages();
        // messages.entries.add("hello world");

        // return messages;
        int num = hrCardInfo.getCountOfV_HRTOCB();

        return  num;
    }

    @GET
    @Path("/cardReader/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCardInfo(@PathParam("id") String id){
        String status = "false";
        String msg = "";

        JSONObject data = new JSONObject();

        SqlRowSet cardInfo = hrCardInfo.getCardInfo(id);

        JSONObject obj = new JSONObject();
        while (cardInfo.next()) {

            obj.put("id_number", cardInfo.getString("id_number"));
            obj.put("card_number", cardInfo.getString("card_number"));
            obj.put("name", cardInfo.getString("name"));

            status = "true";
            msg = "Data fetched successfully";
        }

        data.put("data",obj);
        data.put("status",status);
        data.put("msg", msg);
        return data.toJSONString();
    }




}
