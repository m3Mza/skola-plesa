package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.HiphopEnrollment;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HiphopEnrollmentRowMapper extends EnrollmentRowMapper implements ResultSetHandler<List<HiphopEnrollment>> {
    
    @Override
    public List<HiphopEnrollment> handle(ResultSet rs) throws SQLException {
        List<HiphopEnrollment> results = new ArrayList<>();
        while (rs.next()) {
            HiphopEnrollment enrollment = new HiphopEnrollment();
            mapCommonFields(rs, enrollment);
            results.add(enrollment);
        }
        return results;
    }
    
    public static class SingleRowMapper extends EnrollmentRowMapper implements ResultSetHandler<HiphopEnrollment> {
        @Override
        public HiphopEnrollment handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                HiphopEnrollment enrollment = new HiphopEnrollment();
                return mapCommonFields(rs, enrollment);
            }
            return null;
        }
    }
}
