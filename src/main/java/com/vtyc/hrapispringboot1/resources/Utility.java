package com.vtyc.hrapispringboot1.resources;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static JSONArray convertResultSetToJson(SqlRowSet rs) {

        JSONArray result = new JSONArray();

        if (rs == null){
            return result;
        }


           SqlRowSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            while (rs.next()) {
                JSONObject obj = new JSONObject();

                for (int i = 1; i < numColumns + 1; i++) {
                    String column_name = rsmd.getColumnName(i);

                    if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rs.getLong(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.REAL) {
                        obj.put(column_name, rs.getFloat(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rs.getBoolean(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rs.getDouble(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rs.getDouble(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rs.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rs.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.CHAR) {
                        obj.put(column_name, rs.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NCHAR) {
                        obj.put(column_name, rs.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.LONGNVARCHAR) {
                        obj.put(column_name, rs.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARCHAR) {
                        obj.put(column_name, rs.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rs.getByte(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rs.getShort(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rs.getDate(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIME) {
                        obj.put(column_name, rs.getTime(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rs.getTimestamp(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIT) {
                        obj.put(column_name, rs.getBoolean(column_name));
                    }  else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
                        obj.put(column_name, rs.getBigDecimal(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DECIMAL) {
                        obj.put(column_name, rs.getBigDecimal(column_name));
                    }  else if (rsmd.getColumnType(i) == java.sql.Types.STRUCT) {
                        obj.put(column_name, rs.getObject(column_name)); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DISTINCT) {
                        obj.put(column_name, rs.getObject(column_name)); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
                    } else if (rsmd.getColumnType(i) == java.sql.Types.JAVA_OBJECT) {
                        obj.put(column_name, rs.getObject(column_name));
                    } else {
                        obj.put(column_name, rs.getString(i));
                    }
                }
                result.add(obj);
            }

        return result;
    }

    public  static String refFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String retStrFormatNowDate = sdFormatter.format(nowTime);

        return retStrFormatNowDate;
    }
}
