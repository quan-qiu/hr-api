package com.vtyc.hrapispringboot1.resources;

import com.vtyc.hrapispringboot1.dao.HrCardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorld {
    @Autowired
    HrCardInfo hrCardInfo;

    @GET
    public int hello(){
        //Messages messages = new Messages();
       // messages.entries.add("hello world");

       // return messages;
        int num = hrCardInfo.getCountOfV_HRTOCB();

        return  num;

    }
}
