package com.example.ool_mobile.service.api.setup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ool_mobile.service.api.EmployeeApi;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.service.api.UserApi;
import com.squareup.moshi.Moshi;

import org.immutables.value.Value;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

// todo: stop using the Immutable library for dependency injection and
//  lazy loading in this class (despite it working quite well)
//  and start using dagger.

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
public abstract class ApiProvider {

    @NonNull
    protected abstract JwtInterceptor interceptor();

    @NonNull
    protected abstract TokenStorage tokenStorage();

    @NonNull
    protected abstract Context context();

    @Value.Lazy
    @NonNull
    public OkHttpClient getOkHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor())
                .build();
    }

    @Value.Lazy
    @NonNull
    protected Retrofit getRetrofit() {

        OkHttpClient httpClient = getOkHttpClient();

        Moshi moshi = new Moshi.Builder()
                .add(new ModelJsonAdapter())
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        builder.baseUrl(ApiInfo.API_BASE_URL);
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()));
        builder.addConverterFactory(MoshiConverterFactory.create(moshi));
        builder.client(httpClient);

        return builder.build();
    }

    @Value.Lazy
    @NonNull
    public EmployeeApi getEmployeeApi() {
        return getRetrofit().create(EmployeeApi.class);
    }

    @Value.Lazy
    @NonNull
    public UserApi getUserApi() {
        return getRetrofit().create(UserApi.class);
    }

    @Value.Lazy
    @NonNull
    public PhotoshootApi getPhotoshootApi() {
        return getRetrofit().create(PhotoshootApi.class);
    }

    @Value.Lazy
    @NonNull
    public EquipmentApi getEquipmentApi() {
        return getRetrofit().create(EquipmentApi.class);
    }
}
