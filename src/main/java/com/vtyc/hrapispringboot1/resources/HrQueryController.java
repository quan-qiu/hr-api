package com.vtyc.hrapispringboot1.resources;

import com.vtyc.hrapispringboot1.dao.HrCardInfo;
import com.vtyc.hrapispringboot1.dao.HrQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

@Component
@Path("/api/hr")
public class HrQueryController {
    private static final Logger logger = LoggerFactory.getLogger( HrQueryController.class);

    private static final String SHIFT_TYPE_DAY = "DAY_SHIFT";
    private static final String SHIFT_TYPE_NIGHT = "NIGHT_SHIFT";
    private static final String SHIFT_TYPE_CZ_DAY_SHIFT = "CZ_DAY_SHIFT"; // 08:00-16:00, 8:00-20:00
    private static final String SHIFT_TYPE_CZ_MFG_NIGHT_SHIFT = "CZ_MFG_NIGHT_SHIFT"; // 20:00-04:00, 20:00-8:00
    private static final String SHIFT_TYPE_CZ_MIDDLE_SHIFT = "CZ_MIDDLE_SHIFT"; // 16:00-00:00,
    private static final String SHIFT_TYPE_CZ_ASSY_DAY_SHIFT = "CZ_ASSY_DAY_SHIFT"; // 08:00-16:30, 8:00-20:00
    private static final String SHIFT_TYPE_CZ_ASSY_NIGHT_SHIFT = "CZ_ASSY_NIGHT_SHIFT"; // 20:00-04:30, 20:00-8:00

    @Autowired
    HrQuery hrQuery;

    @GET
    @Path("/getMyWorkersListFromWC/{tm_user_id}/{work_center}/{cn_shift_name}")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public String getMyWorkersListFromWC(@PathParam("tm_user_id") String tm_user_id,
                              @PathParam("work_center") String work_center,
                              @PathParam("cn_shift_name") String cn_shift_name){

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        String status = "false";
        String msg = "Not found";

        SqlRowSet workersList = hrQuery.getMyWorkersListFromWC(tm_user_id,work_center,cn_shift_name);

        if (workersList != null){
            data = Utility.convertResultSetToJson(workersList);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("data", data);
        result.put("status",status);
        result.put("msg", msg);
        return result.toJSONString();

    }

    @GET
    @Path("/getMyWorkersListFromWC/{tm_user_id}/{work_center}/{cn_shift_name}/{plant_location}")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public String getMyWorkersListFromWC(@PathParam("tm_user_id") String tm_user_id,
                                         @PathParam("work_center") String work_center,
                                         @PathParam("cn_shift_name") String cn_shift_name,
                                         @PathParam("plant_location") String plant_location){

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet workersList = hrQuery.getMyWorkersListFromWC(tm_user_id,work_center,cn_shift_name,plant_location);

        if (workersList != null){
            data = Utility.convertResultSetToJson(workersList);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("data", data);
        result.put("status",status);
        result.put("msg", msg);
        return result.toJSONString();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getMyWorkCenterList/{tm_user_id}/{cn_shift_name}")
    public String getMyWorkCenterList(@PathParam("tm_user_id") String tm_user_id,
                                      @PathParam("cn_shift_name") String cn_shift_name) {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet workCenterList = hrQuery.getMyWorkCenterList(tm_user_id, cn_shift_name);

        if (workCenterList != null){
            data = Utility.convertResultSetToJson(workCenterList);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("data", data);
        result.put("status",status);
        result.put("msg", msg);
        return result.toJSONString();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getMyWorkCenterList/{tm_user_id}/{cn_shift_name}/{plant_location}")
    public String getMyWorkCenterList(@PathParam("tm_user_id") String tm_user_id,
                                      @PathParam("cn_shift_name") String cn_shift_name,
                                      @PathParam("plant_location") String plant_location) {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet workCenterList = hrQuery.getMyWorkCenterList(tm_user_id, cn_shift_name,plant_location);

        if (workCenterList != null){
            data = Utility.convertResultSetToJson(workCenterList);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("data", data);
        result.put("status",status);
        result.put("msg", msg);
        return result.toJSONString();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getShiftData/{user_id}")
    public String getShiftData(@PathParam("user_id") String user_id) {
        JSONArray result = new JSONArray();
        JSONObject obj_cn_shift_name = new JSONObject();
        JSONObject obj_en_shift_name = new JSONObject();
        JSONObject obj_shift_start = new JSONObject();
        JSONObject obj_shift_finish = new JSONObject();
        JSONObject obj_status = new JSONObject();
        JSONObject obj_msg = new JSONObject();

        String status = "false";
        String msg = "Not found";
        String cn_shift_name = "";
        String en_shift_name = "";
        String shift_start = "";
        int working_hours = 0;

        SqlRowSet shiftData = hrQuery.getShiftData(user_id);

        while (shiftData.next()){
            cn_shift_name = shiftData.getString("cn_shift_name");
            status = "true";
            msg = "Data fetched successfully";
        }

        if (cn_shift_name.equals("早班")) {
            en_shift_name = SHIFT_TYPE_CZ_DAY_SHIFT;
            shift_start = "08:00:00";
            working_hours = 12;
        } else if (cn_shift_name.equals("制造中夜班")) {
            en_shift_name = SHIFT_TYPE_CZ_MFG_NIGHT_SHIFT;
            shift_start = "20:00:00";
            working_hours = 12;
        } else if (cn_shift_name.equals("中班")) {
            en_shift_name = SHIFT_TYPE_CZ_MIDDLE_SHIFT;
            shift_start = "16:00:00";
            working_hours = 8;
        } else if (cn_shift_name.equals("装配常日班")) {
            en_shift_name = SHIFT_TYPE_CZ_ASSY_DAY_SHIFT;
            shift_start = "08:00:00";
            working_hours = 12;
        } else if (cn_shift_name.equals("装配中夜班")) {
            en_shift_name = SHIFT_TYPE_CZ_ASSY_NIGHT_SHIFT;
            shift_start = "20:00:00";
            working_hours = 12;
        } else {
            en_shift_name = "ERROR_NO_SHIFT";
            shift_start = "00:00:00";
            working_hours = 0;
        }

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        String date_shift_start = LocalDate.now().toString() + " " + shift_start;


        obj_cn_shift_name.put("cn_shift_name", cn_shift_name);
        obj_en_shift_name.put("en_shift_code", en_shift_name);
        try {
            obj_shift_start.put("shift_start", fmt.format(fmt.parse(date_shift_start)));

            Calendar c_shift_finish = Calendar.getInstance();
            c_shift_finish.setTime(fmt.parse(date_shift_start));
            c_shift_finish.add(Calendar.HOUR, working_hours);

            obj_shift_finish.put("shift_finish", fmt.format(c_shift_finish.getTime()));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        obj_status.put("status", status);
        obj_msg.put("msg", msg);

        result.add(0, obj_status);
        result.add(1, obj_msg);
        result.add(2, obj_cn_shift_name);
        result.add(3, obj_en_shift_name);
        result.add(4, obj_shift_start);
        result.add(5, obj_shift_finish);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getEmployeeNameFromHRDB/{employee_id}")
    public String getEmployeeNameFromHRDB(@PathParam("employee_id") String employee_id) {

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet employeeName = hrQuery.getEmployeeNameFromHRDB(employee_id);

        if (employeeName != null ){
            data = Utility.convertResultSetToJson(employeeName);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getCnShiftName/{employee_id}")
    public String getCnShiftName(@PathParam("employee_id") String employee_id) {

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet cnShiftName = hrQuery.getCnShiftName(employee_id);

        if (cnShiftName != null){
            data = Utility.convertResultSetToJson(cnShiftName);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getCnShiftNameOnHoliday/{employee_id}")
    public String getCnShiftNameOnHoliday(@PathParam("employee_id") String employee_id) {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet cnShiftNameOnHoliday = hrQuery.getCnShiftNameOnHoliday(employee_id);

        if (cnShiftNameOnHoliday != null ){
            data = Utility.convertResultSetToJson(cnShiftNameOnHoliday);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getTotalHoursByEmployee/{employee_id}/{date}")
    public String getTotalHoursByEmployee(@PathParam("employee_id") String employee_id,
                                          @PathParam("date") String date) {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet totalHours = hrQuery.getTotalHoursByEmployee(employee_id,date);

        if (totalHours != null ){
            data = Utility.convertResultSetToJson(totalHours);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getIdsFromHrByDate/{date}/{plant_location}")
    public String getIdsFromHrByDate( @PathParam("date") String date,
                                      @PathParam("plant_location") String plant_location) {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet idsFromHrByDate = hrQuery.getIdsFromHrByDate(date,plant_location);

        if (idsFromHrByDate != null ){
            data = Utility.convertResultSetToJson(idsFromHrByDate);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/listJobPositionDetail")
    public String listJobPositionDetail(){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet jobPosition = hrQuery.listJobPositionDetail();

        if (jobPosition != null ){
            data = Utility.convertResultSetToJson(jobPosition);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getEmployeeInfo/{job_position_detail}")
    public String getEmployeeInfo(@PathParam("job_position_detail") String job_position_detail){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet employeeInfo = hrQuery.getEmployeeInfo(job_position_detail);

        if (employeeInfo != null ){
            data = Utility.convertResultSetToJson(employeeInfo);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getAllWorkCenter")
    public String getAllWorkCenter(){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet workCenters = hrQuery.getAllWorkCenter();

        if (workCenters != null ){
            data = Utility.convertResultSetToJson(workCenters);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getEmployeeIdNotBelongTL/{tl_id}")
    public String getEmployeeIdNotBelongTL(@PathParam("tl_id") String tl_id){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet employeeIdNotBelongTL = hrQuery.getEmployeeIdNotBelongTL(tl_id);

        if (employeeIdNotBelongTL != null ){
            data = Utility.convertResultSetToJson(employeeIdNotBelongTL);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getAllEmployeeIds/{tl_id}")
    public String getAllEmployeeIds(@PathParam("tl_id") String tl_id){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet allEmployeeIds = hrQuery.getAllEmployeeIds(tl_id);

        if (allEmployeeIds != null ){
            data = Utility.convertResultSetToJson(allEmployeeIds);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getAllTeamLeadersNotIncludeTL/{tl_id}")
    public String getAllTeamLeadersNotIncludeTL(@PathParam("tl_id") String tl_id){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet teamLeadersNotIncludeTL = hrQuery.getAllTeamLeadersNotIncludeTL(tl_id);

        if (teamLeadersNotIncludeTL != null ){
            data = Utility.convertResultSetToJson(teamLeadersNotIncludeTL);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getEmployeeInfoById/{id}")
    public String getEmployeeInfoById(@PathParam("id") String id){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet employeeInfo = hrQuery.getEmployeeInfoById(id);

        if (employeeInfo != null ){
            data = Utility.convertResultSetToJson(employeeInfo);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getAllAvailableEmployees/{plant_location}")
    public String getAllAvailableEmployees(@PathParam("plant_location") String plant_location){

        //plant_location = "CZ"/"CQ"

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet allAvailableEmployees = hrQuery.getAllAvailableEmployees(plant_location);

        if (allAvailableEmployees != null ){
            data = Utility.convertResultSetToJson(allAvailableEmployees);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getCostCenter/{plant_location}")
    public String getCostCenter(@PathParam("plant_location") String plant_location){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet costCenters = hrQuery.getCostCenter(plant_location);

        if (costCenters != null ){
            data = Utility.convertResultSetToJson(costCenters);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/test1/{plant_location}/{cost_center}")
    public String test1(@PathParam("plant_location") String plant_location,
                        @PathParam("cost_center") String cost_center){
        SqlRowSet teamLeaders = hrQuery.getTlByCostcenter(cost_center,plant_location);
        return teamLeaders.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getTlByCostcenter/{plant_location}/{cost_center}")
    public String getTlByCostcenter(@PathParam("plant_location") String plant_location,
                                    @PathParam("cost_center") String cost_center
                                    ){

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet teamLeaders = hrQuery.getTlByCostcenter(cost_center,plant_location);

        if (teamLeaders != null ){
            data = Utility.convertResultSetToJson(teamLeaders);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getTlByCostcenterShift/{plant_location}/{cost_center}/{shift_type}")
    public String getTlByCostcenterShift(@PathParam("plant_location") String plant_location,
                                    @PathParam("cost_center") String cost_center,
                                    @PathParam("shift_type") String shift_type
                                    ){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet teamLeaders = hrQuery.getTlByCostcenterShift(cost_center,plant_location,shift_type);

        if (teamLeaders != null ){
            data = Utility.convertResultSetToJson(teamLeaders);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getDepartment/{plant_location}")
    public String getDepartment(@PathParam("plant_location") String plant_location
    ){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        SqlRowSet departments = hrQuery.getDepartment(plant_location);

        if (departments != null ){
            data = Utility.convertResultSetToJson(departments);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getDepartmentCostcenter/{plant_location}/{department}")
    public String getDepartmentCostcenter(@PathParam("plant_location") String plant_location,
                                          @PathParam("department") String department
                                         ){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

       // logger.info("--------------------------------department:" + department);

        SqlRowSet costcenters = hrQuery.getDepartmentCostcenter(plant_location,department);

        if (costcenters != null ){
            data = Utility.convertResultSetToJson(costcenters);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/getAllDepartment/{plant_location}")
    public String getAllDepartment(@PathParam("plant_location") String plant_location
    ){
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "false";
        String msg = "Not found";

        // logger.info("--------------------------------department:" + department);

        SqlRowSet departments = hrQuery.getAllDepartment(plant_location);

        if (departments != null ){
            data = Utility.convertResultSetToJson(departments);
            status = "true";
            msg = "Data fetched successfully";
        }

        result.put("status", status);
        result.put("msg", msg);
        result.put("data", data);

        return result.toJSONString();
    }
}
