package com.vtyc.hrapispringboot1.resources;

import com.vtyc.hrapispringboot1.dao.Messages;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Component
@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    @GET
    public Messages retriveMessages(){
        Messages messages = new Messages();
        messages.entries.add("hello world");

        return messages;
    }
}
