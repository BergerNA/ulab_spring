package com.edu.ulab.app.repository.impl.jdbcTemplate;

import com.edu.ulab.app.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
@AllArgsConstructor
public class DbLongSequenceEntityIdInstaller {

    private final JdbcTemplate jdbcTemplate;

    public void onBeforeConvert(BaseEntity<Long> entity) {
        if (entity.getId() == null) {
            Long id = jdbcTemplate.query("SELECT nextval('sequence')",
                    rs -> {
                        if (rs.next()) {
                            return rs.getLong(1);
                        } else {
                            throw new SQLException("Unable to retrieve value from sequence.");
                        }
                    });
            entity.setId(id);
        }
    }
}
