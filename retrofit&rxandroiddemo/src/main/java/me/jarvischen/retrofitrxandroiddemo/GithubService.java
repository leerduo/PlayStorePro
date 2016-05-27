package me.jarvischen.retrofitrxandroiddemo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by chenfuduo on 2016/5/26.
 */
public interface GithubService {
    @GET("users/{username}")
    Observable<UserInfo> getGithubUserInfo(@Path("username") String userName);
}
