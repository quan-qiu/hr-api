package com.vtyc.hrapispringboot1.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import com.vtyc.hrapispringboot1.resources.*;

@Component
@ApplicationPath("/rest_api")
public class JerseryConfiguration extends ResourceConfig {
    public JerseryConfiguration(){

       // packages("com.vtyc.hrapispringboot1.resources");
        register(HelloWorld.class);
        register(HrCardController.class);
        register(HrQueryController.class);
        register(MessageResource.class);
    }
}
