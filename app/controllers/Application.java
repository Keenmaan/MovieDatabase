package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result topGenreView() {
        return Results.TODO;
    }

    public Result searchView() {
        return Results.TODO;
    }

    public Result movieDescriptionView(int id) {
        return Results.TODO;
    }

    public Result index() {
        return ok(index.render(models.Movie.find.all()));
    }

}
