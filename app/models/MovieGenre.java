package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name="movie_genre")
public class MovieGenre extends Model {
    @Id
    public int id;

    public int movie_id;

    public int genre_id;
}
