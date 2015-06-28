package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
public class Genre extends Model {
    @Id
    public int id;
    public String name;

    public static Finder<String,Genre> find = new Finder<>(Genre.class);
}
