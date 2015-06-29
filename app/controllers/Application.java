package controllers;

import com.google.gson.Gson;
import models.Genre;
import models.Movie;
import models.MovieGenre;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
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
import java.util.*;
import java.util.stream.Collectors;

public class Application extends Controller {

    public Result topGenreView() {
        List<Integer> counts = new ArrayList<>();
        List<Genre> genres = Genre.find.all();
        for(Genre genre : genres) {
            counts.add(MovieGenre.find.where().eq("genre_id", genre.id).findRowCount());
        }
        return ok(topGenre.render(genres, counts));
    }

    public Result searchView() {
        System.out.println("searchVIEW");
        return ok(search.render(models.Genre.find.all(), null));
    }

    public Result searchResults() {
        DynamicForm requestData = Form.form().bindFromRequest();
        List<MovieGenre> movieGenres;
        Collection<Movie> collection=new HashSet();
        List<Genre> genres = Genre.find.all();
        BigDecimal minVoteScore;
        if (requestData==null || !StringUtils.isNumeric(requestData.get("minVoteScore")))
            minVoteScore= BigDecimal.valueOf(0);
        else
            minVoteScore=new BigDecimal(requestData.get("minVoteScore"));

        for (Genre genre : genres) {
            System.out.println("genre.id=" + genre.id);
            System.out.println(genre.name);

            if(genre.name != null
                    && requestData.get(genre.name) != null
                    && requestData.get(genre.name).equals("on")
                    && requestData.get("minVoteScore")!=null) {
                movieGenres = MovieGenre.find.where().eq("genre_id", genre.id).findList();
                collection.addAll(movieGenres.stream()
                        .map(movieGenre -> Movie.find.where()
                                .eq("id", movieGenre.movie_id)
                                .where()
                                .ge("vote_average",minVoteScore)
                                .findUnique())
                        .collect(Collectors.toList()));
            }
        }
        for (Movie movie : collection){
            if (movie!=null && movie.title!=null)
            System.out.println(movie.title);
        }
        System.out.println("searchRESULTS");
        //System.out.println(tags);
        List<Movie> movies= new ArrayList<>();
        movies.addAll(collection);
        return ok(search.apply(models.Genre.find.all(),movies));
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
