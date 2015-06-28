package models;

import com.avaje.ebean.Model;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
public class Movie extends Model{
    @Id
    public int id;

    public String original_language;
    public String original_title;
    public Date release_date;

    @Column(precision=10,scale=8)
    public BigDecimal popularity;

    public String title;

    @Column(precision=10,scale=8)
    public BigDecimal vote_average;

    public int vote_count;

    public static Finder<String,Movie> find = new Finder<>(Movie.class);
}
