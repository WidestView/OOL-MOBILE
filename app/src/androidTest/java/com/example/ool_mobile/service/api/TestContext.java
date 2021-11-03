package com.example.ool_mobile.service.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.service.api.setup.ApiInfo;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.JwtInterceptor;
import com.example.ool_mobile.service.api.setup.TokenStorage;

public class TestContext {

    private final ApiProvider provider;

    private final TokenStorage storage;

    private TestContext() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        storage = new TokenStorage(context);

        provider = new ApiProviderBuilder()
                .interceptor(new JwtInterceptor(storage))
                .context(context)
                .tokenStorage(storage)
                .build();
    }

    @NonNull
    public ApiProvider getApiProvider() {
        return provider;
    }

    public void login() {

        new EmployeeRepository(
                provider.getUserApi(),
                provider.getEmployeeApi(),
                storage
        ).login(ApiInfo.DEFAULT_USER_LOGIN, ApiInfo.DEFAULT_USER_PASSWORD)
                .blockingSubscribe();
    }

    public static TestContext create() {
        return new TestContext();
    }
}
