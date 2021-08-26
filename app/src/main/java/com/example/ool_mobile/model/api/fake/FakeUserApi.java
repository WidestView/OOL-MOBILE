package com.example.ool_mobile.model.api.fake;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.api.UserApi;

import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class FakeUserApi implements UserApi {

    @NonNull
    @Override
    public Single<Response<TokenData>> login(@NonNull LoginData login) {

        Objects.requireNonNull(login);
        Objects.requireNonNull(login.username);
        Objects.requireNonNull(login.password);

        if (login.username.equals("bob") && login.password.equals("beep")) {

            TokenData tokenData = new TokenData();
            tokenData.token = "";

            return Single.just(Response.success(tokenData));
        }

        return Single.just(
                Response.error(
                        403,
                        ResponseBody.create(MediaType.get("application/json"), "")
                )
        );
    }
}
