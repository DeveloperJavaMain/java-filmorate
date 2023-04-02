package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Component("MpaRateDbStorage")
@AllArgsConstructor
public class MpaRateDbStorage implements MpaRateStorage {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRate> rowMapper = new RowMapper() {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MpaRate mparate = new MpaRate();
            mparate.setId(rs.getInt("id"));
            mparate.setName(rs.getString("name"));
            return mparate;
        }
    };

    @Override
    public List<MpaRate> getAll() {
        return jdbcTemplate.query("SELECT id, name FROM MPARATE", rowMapper);
    }

    @Override
    public MpaRate getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, name FROM MPARATE WHERE id=?",
                    new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new NotFoundException("MpaRate #"+id+" not found");
        }
    }

    @Override
    public MpaRate insert(MpaRate mpaRate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO MPARATE (name) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ps.setString(1, mpaRate.getName());
            return ps;
        }, keyHolder);
        mpaRate.setId(keyHolder.getKey().intValue());
        return mpaRate;
    }

    @Override
    public MpaRate update(MpaRate mpaRate) {
        int cnt = jdbcTemplate.update("UPDATE MPARATE SET name=? WHERE id=?",
                mpaRate.getName(), mpaRate.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("mpaRate #"+mpaRate.getId()+" not found");
        }
        return mpaRate;
    }

    @Override
    public MpaRate delete(MpaRate mpaRate) {
        int cnt = jdbcTemplate.update("DELETE FROM MPARATE WHERE id=?",
                mpaRate.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("mpaRate #"+mpaRate.getId()+" not found");
        }
        return mpaRate;
    }
}
