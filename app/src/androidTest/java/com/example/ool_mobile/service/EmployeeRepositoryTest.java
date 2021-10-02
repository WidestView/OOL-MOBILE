package com.example.ool_mobile.service;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.service.api.setup.ApiInfo;
import com.example.ool_mobile.service.api.setup.ApiProvider;
import com.example.ool_mobile.service.api.setup.ApiProviderBuilder;
import com.example.ool_mobile.service.api.setup.JwtInterceptor;
import com.example.ool_mobile.service.api.setup.TokenStorage;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class EmployeeRepositoryTest {

    EmployeeRepository repository;

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TokenStorage storage = new TokenStorage(context);

        ApiProvider client = new ApiProviderBuilder()
                .interceptor(new JwtInterceptor(storage))
                .tokenStorage(storage)
                .context(context)
                .build();

        repository = new EmployeeRepository(
                client.getUserApi(),
                client.getEmployeeApi(),
                storage
        );
    }


    @Test
    public void getsCurrentEmployee() {

        Boolean result = repository.login(
                ApiInfo.DEFAULT_USER_LOGIN, ApiInfo.DEFAULT_USER_PASSWORD
        ).blockingGet();

        assertThat(result, is(notNullValue()));

        assumeThat(result, is(true));

        Employee employee = repository.getCurrentEmployee().blockingGet();

        assertThat(employee, is(notNullValue()));

        assertThat(employee.cpf(), is(notNullValue()));
        assertThat(employee.name(), is(notNullValue()));
        assertThat(employee.socialName(), is(not("")));
        assertThat(employee.birthDate(), is(notNullValue()));
        assertThat(employee.phone(), is(notNullValue()));
        assertThat(employee.email(), is(notNullValue()));
        assertThat(employee.accessLevel(), is(notNullValue()));
        assertThat(employee.occupationId(), is(notNullValue()));
        assertThat(employee.gender(), is(notNullValue()));
        assertThat(employee.rg(), is(notNullValue()));
    }
}