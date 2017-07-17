package com.msoma.mmeks.maps;

/**
 * Created by mmeks on 2/13/15.
 */
import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl;
    private double genre;
    private double year;
    private double rating;
    private int map_id;
    //private ArrayList<String> genre;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, double year, double rating,int map_id,
                 double genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }
    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getYear() {
        return year;
    }

    public void setYear(double year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public double getGenre() {
        return genre;
    }

    public void setGenre(double genre) {
        this.genre = genre;
    }

    //public ArrayList<String> getGenre() {
    //    return genre;
    //}

   // public void setGenre(ArrayList<String> genre) {
      //  this.genre = genre;
    //}

}