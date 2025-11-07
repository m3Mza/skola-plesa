package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.LatinoEnrollment;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LatinoEnrollmentRowMapper extends EnrollmentRowMapper implements ResultSetHandler<List<LatinoEnrollment>> {
    
    @Override
    public List<LatinoEnrollment> handle(ResultSet rs) throws SQLException {
        List<LatinoEnrollment> results = new ArrayList<>();
        while (rs.next()) {
            LatinoEnrollment enrollment = new LatinoEnrollment();
            mapCommonFields(rs, enrollment);
            results.add(enrollment);
        }
        return results;
    }
    
    public static class SingleRowMapper extends EnrollmentRowMapper implements ResultSetHandler<LatinoEnrollment> {
        @Override
        public LatinoEnrollment handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                LatinoEnrollment enrollment = new LatinoEnrollment();
                return mapCommonFields(rs, enrollment);
            }
            return null;
        }
    }
}
