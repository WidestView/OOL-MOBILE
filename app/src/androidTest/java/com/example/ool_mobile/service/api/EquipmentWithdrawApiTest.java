package com.example.ool_mobile.service.api;

import org.junit.Before;
import org.junit.Test;

public class EquipmentWithdrawApiTest {

    private EquipmentWithdrawApi api;

    @Before
    public void setUp() {

        TestContext context = TestContext.create();

        api = context.getApiProvider().getWithdrawApi();

        context.login();
    }


    @Test
    public void listWithdraws() {
        api.listWithdraws().blockingSubscribe();
    }

    @Test
    public void addWithdraw() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void updateWithdraw() {
        throw new UnsupportedOperationException();
    }
}