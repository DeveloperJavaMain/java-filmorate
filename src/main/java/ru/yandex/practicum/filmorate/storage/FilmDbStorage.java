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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Primary
@Component("FilmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;
    private MpaRateStorage mpaStorage;

    private final RowMapper<Film> rowMapper = new RowMapper() {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setLikes(new TreeSet<Integer>(
                    jdbcTemplate.queryForList("SELECT userId FROM LIKES WHERE filmId=?",
                    new Object[]{film.getId()}, Integer.class)));
            int mparateId = rs.getInt("mpaRateId");
            if (!rs.wasNull()) {
                film.setMpa(mpaStorage.getById(mparateId));
            }
            film.setGenres(new TreeSet<Genre>(genreStorage.getByFilm(film.getId())));

            return film;
        }
    };


    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("SELECT * FROM FILMS", rowMapper);
    }

    @Override
    public Film getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM FILMS WHERE id=?",
                    new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new NotFoundException("Film #"+id+" not found");
        }
    }

    private void insertLikes(Set<Integer> userIds, int filmId) {
        if (userIds.size() == 0) {
            return;
        }
        List<Integer> list = List.copyOf(userIds);
        jdbcTemplate.batchUpdate("INSERT INTO LIKES (filmId,userId) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, list.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return userIds.size();
                    }
                }
        );
    }

    private void insertGenres(Set<Genre> genres, int filmId) {
        if (genres.size() == 0) {
            return;
        }
        List<Genre> list = List.copyOf(genres);
        jdbcTemplate.batchUpdate("INSERT INTO FILMGENRES (filmId,genreId) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, list.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                }
        );
    }

    @Override
    public Film insert(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO FILMS (name, description, releaseDate, duration, mpaRateId) VALUES (?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            if (film.getMpa() != null) {
                ps.setLong(5, film.getMpa().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        insertLikes(film.getLikes(), film.getId());
        insertGenres(film.getGenres(), film.getId());
        return getById(film.getId());
    }

    @Override
    public Film update(Film film) {
        int cnt = jdbcTemplate.update(
                "UPDATE FILMS SET name=?, description=?, releaseDate=?, duration=?, mpaRateId=? WHERE id=?",
                film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                (film.getMpa() != null) ? film.getMpa().getId() : null,
                film.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("Film #"+film.getId()+"not found");
        }
        jdbcTemplate.update("DELETE FROM LIKES WHERE filmId=?",
                new Object[]{film.getId()});
        insertLikes(film.getLikes(), film.getId());
        jdbcTemplate.update("DELETE FROM FILMGENRES WHERE filmId=?",
                new Object[]{film.getId()});
        insertGenres(film.getGenres(), film.getId());
        return getById(film.getId());
    }

    @Override
    public Film delete(Film film) {
        int cnt = jdbcTemplate.update(
                "DELETE FROM FILMS WHERE id=?",
                film.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("Film #"+film.getId()+"not found");
        }
        return film;
    }
}
