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
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Component("GenreDbStorage")
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Genre> rowMapper = new RowMapper() {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    };

    @Override
    public List<Genre> getAll() {
         return jdbcTemplate.query("SELECT id, name FROM GENRES", rowMapper);
    }

    private final String findReq = "SELECT id, name FROM GENRES, FILMGENRES "+
            "WHERE FILMGENRES.genreId=GENRES.id AND FILMGENRES.filmId=?";
    @Override
    public List<Genre> getByFilm(int filmId) {
        try {
            return jdbcTemplate.query(findReq, new Object[]{filmId}, rowMapper);
        } catch (DataAccessException e) {
            throw new NotFoundException("Genres for film #"+filmId+" not found");
        }
    }

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, name FROM GENRES WHERE id=?",
                    new Object[]{id}, rowMapper);
        } catch (DataAccessException e) {
            throw new NotFoundException("Genre #"+id+" not found");
        }
    }

    @Override
    public Genre insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO GENRES (name) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
            ps.setString(1, genre.getName());
            return ps;
        }, keyHolder);
        genre.setId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        int cnt = jdbcTemplate.update("UPDATE GENRES SET name=? WHERE id=?",
                genre.getName(), genre.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("Genre not found");
        }
        return genre;
    }

    @Override
    public Genre delete(Genre genre) {
        int cnt = jdbcTemplate.update("DELETE FROM GENRES WHERE id=?",
                genre.getId()
        );
        if (cnt == 0) {
            throw new NotFoundException("Genre not found");
        }
        return genre;
    }
}
