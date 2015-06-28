package controllers;

import com.google.gson.Gson;
import models.Genre;
import models.MovieGenre;
import org.apache.commons.io.IOUtils;
import play.*;
import play.mvc.*;

import scala.util.parsing.json.JSONObject;
import scala.util.parsing.json.JSONObject$;
import views.html.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Application extends Controller {

    public Result topGenreView() {
        return Results.TODO;
    }

    public Result searchView() {
        return Results.TODO;
    }

    public Result movieDescriptionView(int id) {
        models.Movie movie=models.Movie.find.where().eq("id",id).findUnique();
        List<Genre> genres = new ArrayList<>();
        List<MovieGenre> movieGenres=MovieGenre.find.where().eq("movie_id",movie.id).findList();
        for (MovieGenre movieGenre : movieGenres){
            Genre g=models.Genre.find.where().eq("id", movieGenre.genre_id).findUnique();
            if (g!=null)
                genres.add(g);
        }
        return ok(movieDescription.render(
                movie,
                genres,
                getDescription(id)
        ));
    }

    public Result index() {
        return ok(index.render(getMovieList(20,200)));
    }

    public static List<models.Movie> getMovieList(int numberOfMovies, int minimumVotes){

        List<models.Movie> movies=models.Movie.find.all();
        movies=models.Movie.find.where()
                .ge("vote_count", minimumVotes)
                .orderBy("vote_average desc, release_date")
                .setMaxRows(numberOfMovies)
                .findList();

        return movies;
    }

    public static String getDescription(int id){
        models.Movie movie=models.Movie.find.where().eq("id",id).findUnique();
        String description=null;
        if (movie!=null) {
            Logger.info("movie works. " + movie.title);

            String title="";
            try {
                title = "t=" + URLEncoder.encode(movie.title, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String year = "y=" + movie.release_date.toLocalDate().getYear();

            String urlString="http://www.omdbapi.com/?"+title+"&"+year;
            try {
                InputStream in=new URL(urlString).openStream();
                try{
                    description=IOUtils.toString(in);
                    Gson gson = new Gson();
                    Properties data = gson.fromJson(description,Properties.class);
                    description=data.getProperty("Plot");
                } finally {
                    IOUtils.closeQuietly(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return description;
    }
}
