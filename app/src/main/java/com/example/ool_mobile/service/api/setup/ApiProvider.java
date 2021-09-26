package com.example.ool_mobile.service.api.setup;

import androidx.annotation.NonNull;

import com.example.ool_mobile.service.api.EmployeeApi;
import com.example.ool_mobile.service.api.UserApi;

import org.immutables.value.Value;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

// todo: stop using the Immutable library for dependency injection and
//  lazy loading in this class (despite it working quite well)

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
public abstract class ApiProvider {

    @NonNull
    protected abstract JwtInterceptor interceptor();

    @Value.Lazy
    @NonNull
    protected Retrofit getRetrofit() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor())
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        builder.baseUrl("http://192.168.0.10:5000/api/");
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        builder.addConverterFactory(MoshiConverterFactory.create());
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
}
