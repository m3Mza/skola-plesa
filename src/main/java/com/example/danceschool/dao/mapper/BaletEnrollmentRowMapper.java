package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.BaletEnrollment;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaletEnrollmentRowMapper extends EnrollmentRowMapper implements ResultSetHandler<List<BaletEnrollment>> {
    
    @Override
    public List<BaletEnrollment> handle(ResultSet rs) throws SQLException {
        List<BaletEnrollment> results = new ArrayList<>();
        while (rs.next()) {
            BaletEnrollment enrollment = new BaletEnrollment();
            mapCommonFields(rs, enrollment);
            results.add(enrollment);
        }
        return results;
    }
    
    public static class SingleRowMapper extends EnrollmentRowMapper implements ResultSetHandler<BaletEnrollment> {
        @Override
        public BaletEnrollment handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                BaletEnrollment enrollment = new BaletEnrollment();
                return mapCommonFields(rs, enrollment);
            }
            return null;
        }
    }
}
