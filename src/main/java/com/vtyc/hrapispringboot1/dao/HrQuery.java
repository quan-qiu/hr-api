package com.vtyc.hrapispringboot1.dao;

import com.vtyc.hrapispringboot1.resources.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.ws.rs.PathParam;

@Repository
public class HrQuery {

    private static final Logger logger = LoggerFactory.getLogger( HrQuery.class);

    @org.springframework.beans.factory.annotation.Qualifier("jdbcPrimaryTemplate")
    @Autowired
    JdbcTemplate jdbcPrimaryTemplate;

    public SqlRowSet getMyWorkersListFromWC(String tm_user_id, String work_center, String cn_shift_name) {

        String sql = "SELECT " +
                "employee_id, employee_name " +
                "FROM V_HRTOCB " +
                "WHERE " +
                "(team_leader_day_shift_id LIKE ? OR team_leader_mid_shift_id LIKE ? OR team_leader_night_shift_id LIKE ?) AND " +
                "work_center = ? AND " +
                "management_level= ? AND " +
                "job_position= ? "
                ;

        SqlRowSet workersList = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tm_user_id,tm_user_id,tm_user_id,work_center,"Direct","Worker"}
        );

        return workersList;
    }

    public SqlRowSet getMyWorkersListFromWC(String tm_user_id, String work_center, String cn_shift_name, String plant_location) {

        String sql = "SELECT " +
                "employee_id, employee_name, shift " +
                "FROM V_HRTOCB " +
                "WHERE " +
                "(team_leader_day_shift_id LIKE ? OR team_leader_mid_shift_id LIKE ? OR team_leader_night_shift_id LIKE ?) AND " +
                "work_center = ? AND " +
                "management_level= ? AND " +
                "job_position= ? AND " +
                "plant_location=?";

        SqlRowSet workersList = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tm_user_id,tm_user_id,tm_user_id,work_center,"Direct","Worker", plant_location}
        );

        return workersList;
    }


    public SqlRowSet getMyWorkCenterList(String tm_user_id, String plant_location) {
        String sql = "SELECT " +
                "distinct(work_center),shift,plant_location " +
                "FROM V_HRTOCB " +
                "WHERE " +
                "work_center is not null and work_center<>'' and (team_leader_day_shift_id LIKE ? OR team_leader_mid_shift_id LIKE ? OR team_leader_night_shift_id LIKE ?) AND " +
                "management_level LIKE ? AND " +
                "job_position LIKE ? AND " +
                "plant_location LIKE ? " +
                "GROUP BY work_center, shift, plant_location,cost_center";

        SqlRowSet workersList = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tm_user_id,tm_user_id,tm_user_id,"Direct","Worker",plant_location}
        );
        return workersList;
    }

    public SqlRowSet getShiftData(String user_id){
        String sql = "SELECT shift AS cn_shift_name " +
                "FROM V_HRTOCB " +
                "WHERE employee_id LIKE ?";
        SqlRowSet workersList = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {user_id}
        );
        return workersList;
    }

    public SqlRowSet getEmployeeNameFromHRDB(String employee_id){

        String sql = "SELECT " +
                "employee_name " +
                "FROM V_HRTOCB " +
                "WHERE " +
                "employee_id = ?";

        SqlRowSet employeeName = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {employee_id}
        );
        return employeeName;
    }

    public SqlRowSet getCnShiftName(String employee_id) {
        String sql = "SELECT shift AS cn_shift_name " +
                "FROM V_HRTOCB " +
                "WHERE employee_id = ?";

        SqlRowSet cnShiftName = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {employee_id}
        );
        return cnShiftName;
    }

    public SqlRowSet getCnShiftNameOnHoliday(String employee_id) {
        String sql = "SELECT K191C AS cn_shift_name " +
                "FROM V_K19_DLMS " +
                "WHERE A0190=? and convert(date,K1906)=?";

        SqlRowSet cnShiftNameOnHoliday = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {employee_id, Utility.refFormatNowDate()}
        );
        return cnShiftNameOnHoliday;
    }

    public SqlRowSet getTotalHoursByEmployee(String employee_id, String date){
        String sql = "SELECT A0190 id,A0101 name,A01zZ0C day_tl_id,A01zZ0D day_tl_name," +
                "A01zZ0E night_tl_id,A01zZ0F night_tl_name,A01zZ0B work_center, CONTENT dept," +
                "plant_location location,CONVERT(VARCHAR,CONVERT(DATE,D9999)) date," +
                "isnull(k3134,0) + isnull(k315P,0) + ISNULL(k316A,0) + ISNULL(k3168,0) t_hours" +
                " FROM [damao].[dbo].[V_K31_DLMS]" +
                " WHERE A0190=? and convert(date,d9999)=? ";

        SqlRowSet totalHours = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {employee_id, date}
        );
        return totalHours;
    }

    public SqlRowSet getIdsFromHrByDate( String date,String plant_location ) {
        String sql = "SELECT DISTINCT A0190 id" +
                " FROM [damao].[dbo].[V_K31_DLMS]" +
                " WHERE convert(date,d9999)=? AND A0209='direct' and plant_location=?";

        SqlRowSet idsFromHrByDate = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {date, plant_location.toUpperCase()}
        );

        return idsFromHrByDate;
    }

    public SqlRowSet listJobPositionDetail(){
        String sql = "SELECT distinct job_position_detail, job_position " +
                "FROM V_HRTOCB";

        SqlRowSet jobPosition = jdbcPrimaryTemplate.queryForRowSet(sql);

        return jobPosition;

    }

    public SqlRowSet getEmployeeInfo(String job_position_detail){

        String sql = "SELECT employee_id, employee_name, management_level " +
                "FROM V_HRTOCB " +
                "WHERE job_position_detail LIKE ?";

        SqlRowSet employeeInfo = jdbcPrimaryTemplate.queryForRowSet(sql, new Object[] {job_position_detail});

        return employeeInfo;

    }

    public SqlRowSet getAllWorkCenter(){
        String sql = "select work_center from V_HRTOCB" +
                "  where work_center is not null and work_center<>''" +
                "  group by work_center" +
                "  order by work_center";

        SqlRowSet workCenters = jdbcPrimaryTemplate.queryForRowSet(sql);

        return workCenters;
    }

    public SqlRowSet getEmployeeIdNotBelongTL(String tl_id){
        String sql = "SELECT employee_id,employee_name,team_leader_day_shift_id," +
                "team_leader_day_shift_name,team_leader_mid_shift_id, team_leader_mid_shift_name,team_leader_night_shift_id," +
                "team_leader_night_shift_name FROM V_HRTOCB AS v1" +
                " WHERE LOWER(v1.management_level)='direct'" +
                " and v1.plant_location in (select plant_location from V_HRTOCB where employee_id=?)" +
                " and v1.employee_id NOT IN" +
                " (select v2.employee_id FROM V_HRTOCB AS v2" +
                " WHERE v2.team_leader_day_shift_id=? OR v2.team_leader_mid_shift_id=? OR v2.team_leader_night_shift_id=?)";

        SqlRowSet employeeIdNotBelongTL = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tl_id,tl_id,tl_id,tl_id}
        );

        return employeeIdNotBelongTL;
    }

    public SqlRowSet getAllEmployeeIds(String tl_id){
        String sql = "SELECT employee_id,employee_name,team_leader_day_shift_id," +
                "team_leader_day_shift_name,team_leader_mid_shift_id, team_leader_mid_shift_name, team_leader_night_shift_id," +
                "team_leader_night_shift_name FROM V_HRTOCB AS v1" +
                " WHERE LOWER(v1.management_level)='direct'" +
                " and v1.plant_location in (select plant_location from V_HRTOCB where employee_id=?)";

        SqlRowSet allEmployeeIds = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tl_id}
        );

        return allEmployeeIds;
    }

    public SqlRowSet getAllTeamLeadersNotIncludeTL(String tl_id){
        String sql = "SELECT employee_id,employee_name FROM V_HRTOCB" +
                " WHERE LOWER(management_level)='indirect'" +
                " AND LOWER(job_position_detail)='team leader'" +
                " AND employee_id<>?" +
                " AND plant_location in (select plant_location from V_HRTOCB where employee_id=?)";

        SqlRowSet teamLeadersNotIncludeTL = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {tl_id,tl_id}
        );

        return teamLeadersNotIncludeTL;
    }

    public SqlRowSet getEmployeeInfoById(String employee_id){
        String sql = "SELECT employee_id,employee_name,plant_location" +
                ",cost_center,work_center,management_level,job_position" +
                ",job_position_detail,team_leader_day_shift_id" +
                ",team_leader_day_shift_name,team_leader_mid_shift_id, team_leader_mid_shift_name, team_leader_night_shift_id" +
                ",team_leader_night_shift_name,shift,workstatus " +
                "FROM V_HRTOCB " +
                "WHERE employee_id=?";

        SqlRowSet employeeInfo = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {employee_id}
                );

        return employeeInfo;
    }

    public SqlRowSet getAllAvailableEmployees(String plant_location){
        String sql ="SELECT E_department section" +
                ",Department plant_name" +
                ",EmpID user_id" +
                ",Name cn_user_name" +
                ",Position job_position" +
                ",EMAIL email" +
                ",二级部门 department" +
                ",Alias en_user_name" +
                ",Management management_level" +
                ",Immediate immediate_superior_user_id" +
                " FROM V_A01_DLMS" +
                " where Department like ?";

        SqlRowSet allAvailableEmployees = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {"%"+ plant_location +"%"}
        );

        return allAvailableEmployees;
    }

    public SqlRowSet getCostCenter(String plant_location){
        String sql = "SELECT distinct(cost_center) cost_center FROM V_HRTOCB " +
                "WHERE cost_center is not null and cost_center<>'' and plant_location=?";

        SqlRowSet costCenters = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {"%"+ plant_location +"%"}
        );

        return costCenters;
    }

    public SqlRowSet getTlByCostcenter(String cost_center,String plant_location){
        String sql = "select " +
                "distinct(case when team_leader_day_shift_id<>'' and team_leader_day_shift_id is not null then team_leader_day_shift_id end) tl_id, " +
                "team_leader_day_shift_name tl_name " +
                "from V_HRTOCB " +
                "where cost_center=? and plant_location=? " +
                "and (team_leader_day_shift_id is not null and team_leader_day_shift_id<>'') " +
                "union " +
                "select " +
                "distinct(case when team_leader_mid_shift_id<>'' and team_leader_mid_shift_id is not null then team_leader_mid_shift_id end) tl_id," +
                "team_leader_mid_shift_name tl_name " +
                "from V_HRTOCB " +
                "where cost_center=? and plant_location=? and " +
                "(team_leader_mid_shift_id is not null and team_leader_mid_shift_id<>'')"+
                "union " +
                "select " +
                "distinct(case when team_leader_night_shift_id<>'' and team_leader_night_shift_id is not null then team_leader_night_shift_id end) tl_id," +
                "team_leader_night_shift_name tl_name " +
                "from V_HRTOCB " +
                "where cost_center=? and plant_location=? and " +
                "(team_leader_night_shift_id is not null and team_leader_night_shift_id<>'')";


        SqlRowSet teamLeaders = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {cost_center, plant_location,cost_center, plant_location,cost_center, plant_location }
        );

        return teamLeaders;
    }

    public SqlRowSet getTlByCostcenterShift(String cost_center,String plant_location,String shift_type){
        String sql = "";

        if (shift_type.toLowerCase().equals("day")){
            sql = "select " +
                    "distinct(case when team_leader_day_shift_id<>'' and team_leader_day_shift_id is not null then team_leader_day_shift_id end) tl_id, " +
                    "team_leader_day_shift_name tl_name " +
                    "from V_HRTOCB " +
                    "where cost_center=? and plant_location=? " +
                    "and (team_leader_day_shift_id is not null and team_leader_day_shift_id<>'') ";
        }

        if (shift_type.toLowerCase().equals("mid")){
            sql = "select " +
                    "distinct(case when team_leader_mid_shift_id<>'' and team_leader_mid_shift_id is not null then team_leader_mid_shift_id end) tl_id, " +
                    "team_leader_mid_shift_name tl_name " +
                    "from V_HRTOCB " +
                    "where cost_center=? and plant_location=? " +
                    "and (team_leader_mid_shift_id is not null and team_leader_mid_shift_id<>'') ";
        }

        if (shift_type.toLowerCase().equals("night")){
            sql = "select " +
                    "distinct(case when team_leader_night_shift_id<>'' and team_leader_night_shift_id is not null then team_leader_night_shift_id end) tl_id, " +
                    "team_leader_night_shift_name tl_name " +
                    "from V_HRTOCB " +
                    "where cost_center=? and plant_location=? " +
                    "and (team_leader_night_shift_id is not null and team_leader_night_shift_id<>'') ";
        }

        //logger.debug(sql);
        SqlRowSet teamLeaders = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {cost_center, plant_location}
        );

        return teamLeaders;
    }

    public SqlRowSet getDepartment(String plant_location){
        String sql = "SELECT distinct(课室名称) department from dbo.v_cb_dlms " +
                "where 厂址代码=? and 成本中心<>'' ";

        SqlRowSet departments = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {plant_location}
        );

        return departments;
    }

    public SqlRowSet getDepartmentCostcenter(String plant_location, String department_name){
        String sql = "SELECT 成本中心 cost_center from dbo.v_cb_dlms " +
                "where 厂址代码=? and 课室名称=? and 成本中心<>'' ";

       // logger.info(sql);
       // logger.info(plant_location);
       // logger.info(department_name);
        SqlRowSet departments = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {plant_location, department_name}
        );

        return departments;
    }

    public SqlRowSet getAllDepartment(String plant_location){
        String sql = "select distinct(二级部门) dept from V_A01_DLMS " +
                "where department like ? ";


        SqlRowSet departments = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {"%" + plant_location + "%"}
        );

        return departments;
    }

    public SqlRowSet getShift(String plant_location){
        String sql = "select distinct shift from V_HRTOCB " +
                "where upper(management_level)='DIRECT' and upper(plant_location)=?; ";


        SqlRowSet shifts = jdbcPrimaryTemplate.queryForRowSet(sql,
                new Object[] {plant_location }
        );

        return shifts;
    }
}
