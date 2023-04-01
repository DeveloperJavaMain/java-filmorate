package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Primary
@Component("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = new RowMapper() {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFriends(new TreeSet<Integer>(
                    jdbcTemplate.queryForList(
                            "SELECT friendId FROM FRIENDS WHERE userId=?",
                            new Object[]{user.getId()}, Integer.class)));
            return user;
        }
    };

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM USERS", rowMapper);
    }

    @Override
    public User getById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM USERS WHERE id=?",
                    new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new NotFoundException("User #"+id+" not found");
        }
    }

    private void insertFriends(Set<Integer> friendIds, int userId) {
        if(friendIds.size()==0) {
            return;
        }
        List<Integer> list = List.copyOf(friendIds);
        jdbcTemplate.batchUpdate("INSERT INTO FRIENDS (userId,friendId) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setInt(2, list.get(i));
                    }
                    @Override
                    public int getBatchSize() {
                        return friendIds.size();
                    }
                }
        );
    }

    @Override
    public User insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO USERS (email, login, name, birthday) VALUES (?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());

        insertFriends(user.getFriends(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        int cnt = jdbcTemplate.update(
                "UPDATE USERS SET email=?, login=?, name=?, birthday=? WHERE id=?",
                user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("User #"+user.getId()+" not found");
        }
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE userId=?",
                new Object[]{user.getId()});
        insertFriends(user.getFriends(), user.getId());
        return user;
    }

    @Override
    public User delete(User user) {
        int cnt = jdbcTemplate.update(
                "DELETE FROM USERS WHERE id=?",
                user.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("User #"+user.getId()+" not found");
        }
        return user;
    }
}
