package com.example.ool_mobile.service.api;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.EmployeeRepository;
import com.example.ool_mobile.service.api.setup.ApiInfo;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.JwtInterceptor;
import com.example.ool_mobile.service.api.setup.TokenStorage;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PhotoshootApiTest {

    PhotoshootApi api;

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TokenStorage storage = new TokenStorage(context);

        ApiProvider client = new ApiProviderBuilder()
                .interceptor(new JwtInterceptor(storage))
                .context(context)
                .tokenStorage(storage)
                .build();

        api = client.getPhotoshootApi();

        new EmployeeRepository(
                client.getUserApi(),
                client.getEmployeeApi(),
                storage
        ).login(ApiInfo.DEFAULT_USER_LOGIN, ApiInfo.DEFAULT_USER_PASSWORD)
                .subscribe();
    }

    @Test
    public void listAll() {

        List<Photoshoot> result = api.listAll().blockingGet();

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void listFromCurrentEmployee() {

        List<Photoshoot> result = api.listFromCurrentEmployee().blockingGet();

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void addsPhotoshootsAndGetsById() {

        Photoshoot photoshoot = ImmutablePhotoshoot.builder()
                .resourceId(UUID.randomUUID())
                .startTime(new Date())
                .durationMinutes(30)
                .orderId(1)
                .address("Some street")
                .build();

        Photoshoot output = api.addPhotoshoot(photoshoot).blockingGet();

        Photoshoot given = api.getPhotoshootWithId(output.resourceId()).blockingGet();

        Photoshoot result = ImmutablePhotoshoot.builder().from(given).images(null).build();


        assertThat(result, is(equalTo(output)));
    }
}