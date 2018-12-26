package com.vtyc.hrapispringboot1.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class HrCardInfo {

    private static final Logger logger = LoggerFactory.getLogger( HrCardInfo.class);

    @org.springframework.beans.factory.annotation.Qualifier("jdbcPrimaryTemplate")
    @Autowired
    JdbcTemplate jdbcPrimaryTemplate;

    @org.springframework.beans.factory.annotation.Qualifier("jdbcSecondaryTemplate")
    @Autowired
    JdbcTemplate jdbcSecondaryTemplate;

    public int getCountOfV_HRTOCB(){
        return jdbcPrimaryTemplate.queryForObject(" select count(*) from dbo.V_HRTOCB", Integer.class);
    }


    public SqlRowSet getCardInfo(String id){
        String sql = "SELECT id_number, name, card_number" +
                " FROM View_cardnumber where card_number=?";

        SqlRowSet cardInfo = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {id}
        );

        return cardInfo;
    }

}
