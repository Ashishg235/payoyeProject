package gaur.ashish.com.payoyeproject.modal;

/**
 * Created by Ashish Gaur on 5/13/2016.
 */
public class MovieFields {
    public static String title, actors, runtime, year, imdbRating, released, genre, plot, poster;

    public static boolean nullOrNot() {
        String check = getActors() + getGenre() + getImdbRating() + getPlot() + getReleased() + getRuntime() + getTitle() + getYear();
        if (check != null) {
            return false;
        } else {
            return true;
        }
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        MovieFields.title = title;
    }

    public static String getActors() {
        return actors;
    }

    public static void setActors(String actors) {
        MovieFields.actors = actors;
    }

    public static String getRuntime() {
        return runtime;
    }

    public static void setRuntime(String runtime) {
        MovieFields.runtime = runtime;
    }

    public static String getYear() {
        return year;
    }

    public static void setYear(String year) {
        MovieFields.year = year;
    }

    public static String getImdbRating() {
        return imdbRating;
    }

    public static void setImdbRating(String imdbRating) {
        MovieFields.imdbRating = imdbRating;
    }

    public static String getReleased() {
        return released;
    }

    public static void setReleased(String released) {
        MovieFields.released = released;
    }

    public static String getGenre() {
        return genre;
    }

    public static void setGenre(String genre) {
        MovieFields.genre = genre;
    }

    public static String getPlot() {
        return plot;
    }

    public static void setPlot(String plot) {
        MovieFields.plot = plot;
    }

    public static String getPoster() {
        return poster;
    }

    public static void setPoster(String poster) {
        MovieFields.poster = poster;
    }
}
