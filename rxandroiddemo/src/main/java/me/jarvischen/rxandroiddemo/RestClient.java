package me.jarvischen.rxandroiddemo;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenfuduo on 2016/5/26.
 */
public class RestClient {
    private Context context;

    public RestClient(Context context) {
        this.context = context;
    }

    public List<String> getFavoriteTvShows() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return createTvShowList();
    }

    public List<String> getFavoriteTvShowsWithException() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to load.");
    }

    private List<String> createTvShowList() {
        List<String> tvShows = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            tvShows.add("爸爸去哪儿" + i);
        }
        return tvShows;
    }

    public List<String> searchForCity(String searchString){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getMatchingCities(searchString);
    }

    private List<String> getMatchingCities(String searchString) {
        if (searchString.isEmpty()) {
            return new ArrayList<>();
        }
        String[] cities = context.getResources().getStringArray(R.array.city_list);
        List<String> toReturn = new ArrayList<>();
        for (String city :
                cities) {
            if (city.toLowerCase().startsWith(searchString.toLowerCase())) {
                toReturn.add(city);
            }
        }
        return toReturn;
    }
}
