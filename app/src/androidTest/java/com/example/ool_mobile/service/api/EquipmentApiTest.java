package com.example.ool_mobile.service.api;

import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.model.ImmutableEquipment;
import com.example.ool_mobile.model.ImmutableEquipmentDetails;
import com.example.ool_mobile.model.ImmutableEquipmentKind;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EquipmentApiTest {

    private EquipmentApi api;

    @Before
    public void setUp() {

        TestContext context = TestContext.create();

        api = context.getApiProvider().getEquipmentApi();

        context.login();
    }

    @Test
    public void listDetails() {
        assertThat(api.listDetails().blockingGet(), is(notNullValue()));
    }

    @Test
    public void addDetails() {

        EquipmentDetails details = ImmutableEquipmentDetails
                .builder()
                .name("Some details")
                .price(10.40)
                .kindId(1)
                .kind(null)
                .build();


        EquipmentDetails insertedDetails = api.addDetails(details).blockingGet();

        assertThat(insertedDetails.getId(), is(not(equalTo(0))));

        EquipmentDetails givenDetails = api.getDetailsById(insertedDetails.getId()).blockingGet();

        assertThat(givenDetails, is(notNullValue()));
    }

    @Test
    public void listEquipments() {
        assertThat(api.listDetails().blockingGet(), is(notNullValue()));
    }


    @Test
    public void addEquipment() {

        Equipment equipment = ImmutableEquipment.builder()
                .detailsId(1)
                .isAvailable(false)
                .build();

        Equipment insertedEquipment = api.addEquipment(equipment).blockingGet();

        assertThat(insertedEquipment, is(notNullValue()));

        assertThat(insertedEquipment.getId(), is(not(equalTo(0))));
    }

    @Test
    public void listKinds() {
        assertThat(api.listKinds().blockingGet(), is(notNullValue()));
    }

    @Test
    public void addEquipmentKind() {

        EquipmentKind kind = ImmutableEquipmentKind.builder()
                .name("Some test equipment kind")
                .description("Some description")
                .build();

        EquipmentKind insertedKind = api.addEquipmentKind(kind).blockingGet();

        assertThat(insertedKind, is(notNullValue()));

        assertThat(insertedKind.getId(), is(not(equalTo(0))));
    }
}