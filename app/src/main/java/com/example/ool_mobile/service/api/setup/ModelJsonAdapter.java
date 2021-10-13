package com.example.ool_mobile.service.api.setup;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.Employee;
import com.example.ool_mobile.model.Equipment;
import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.model.ImmutableEmployee;
import com.example.ool_mobile.model.ImmutableEquipment;
import com.example.ool_mobile.model.ImmutableEquipmentDetails;
import com.example.ool_mobile.model.ImmutableEquipmentKind;
import com.example.ool_mobile.model.ImmutableOccupation;
import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.ImmutablePhotoshootImage;
import com.example.ool_mobile.model.Occupation;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.model.PhotoshootImage;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ModelJsonAdapter {

    @FromJson
    @NonNull
    public UUID UUIDFromJson(@NonNull String text) {
        return UUID.fromString(text);
    }

    @ToJson
    @NonNull
    public String UUIDtoJson(@NonNull UUID uuid) {
        return uuid.toString();
    }

    @FromJson
    @NonNull
    public Date dateFromJson(@NonNull String text) {
        return ApiDate.parse(text);
    }

    @ToJson
    @NonNull
    public String dateToJson(@NonNull Date date) {
        return ApiDate.format(date);
    }

    @FromJson
    @NonNull
    public Photoshoot photoshootFromJson(@NonNull PhotoshootData photoshootData) {

        return ImmutablePhotoshoot.builder()
                .resourceId(photoshootData.id)
                .address(photoshootData.address)
                .durationMinutes(photoshootData.durationMinutes)
                .orderId(photoshootData.orderId)
                .startTime(photoshootData.start)
                .images(photoshootData.images)
                .build();
    }

    @ToJson
    @NonNull
    public PhotoshootData photoshootToJson(@NonNull Photoshoot photoshoot) {

        PhotoshootData data = new PhotoshootData();
        data.id = photoshoot.resourceId();
        data.address = photoshoot.address();
        data.durationMinutes = photoshoot.durationMinutes();
        data.address = photoshoot.address();
        data.start = photoshoot.startTime();
        data.durationMinutes = photoshoot.durationMinutes();
        data.orderId = photoshoot.orderId();

        return data;
    }

    @FromJson
    @NonNull
    public PhotoshootImage photoshootImage(@NonNull PhotoshootImageData data) {

        return ImmutablePhotoshootImage
                .builder()
                .id(data.id)
                .onPortifolio(data.onPortifolio)
                .photoshootId(data.photoshootId)
                .photoshoot(data.photoshoot)
                .build();
    }

    @ToJson
    @NonNull
    public PhotoshootImageData photoshootImageToJson(@NonNull PhotoshootImage image) {

        PhotoshootImageData imageData = new PhotoshootImageData();
        imageData.id = image.id();
        imageData.onPortifolio = image.onPortifolio();
        imageData.photoshoot = image.photoshoot();
        imageData.photoshootId = image.photoshootId();

        return imageData;
    }

    @FromJson
    @NonNull
    public Employee employeeFromJson(@NonNull EmployeeData output) {

        Objects.requireNonNull(output);

        return ImmutableEmployee.builder()
                .cpf(output.cpf)
                .name(output.name)
                .socialName(output.socialName)
                .birthDate(ApiDate.parse(output.birthDate))
                .phone(output.phone)
                .email(output.email)
                .accessLevel(output.accessLevel)
                .occupationId(output.occupationId)
                .occupation(output.occupation)
                .gender(output.gender)
                .rg(output.rg)
                .build();
    }

    @ToJson
    @NonNull
    public EmployeeData employeeToJson(@NonNull Employee employee) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @FromJson
    public Occupation occupationFromJson(@Nullable OccupationData occupationData) {

        if (occupationData == null) {
            return null;
        }

        return ImmutableOccupation.builder()
                .id(occupationData.id)
                .description(occupationData.description)
                .name(occupationData.name)
                .build();
    }


    @ToJson
    @NonNull
    public OccupationData occupationToJson(@NonNull Occupation occupation) {
        throw new UnsupportedOperationException();
    }

    @ToJson
    @NonNull
    public EquipmentKindData equipmentKindToJson(@NonNull EquipmentKind equipmentKind) {
        EquipmentKindData data = new EquipmentKindData();
        data.id = equipmentKind.getId();
        data.name = equipmentKind.getName();
        data.description = equipmentKind.getDescription();

        return data;
    }

    @FromJson
    @NonNull
    public EquipmentKind equipmentKindFromJson(@NonNull EquipmentKindData equipmentKindData) {
        return ImmutableEquipmentKind.builder()
                .id(equipmentKindData.id)
                .name(equipmentKindData.name)
                .description(equipmentKindData.description)
                .build();
    }


    @ToJson
    @NonNull
    public EquipmentData equipmentToJson(@NonNull Equipment equipment) {
        EquipmentData data = new EquipmentData();
        data.id = equipment.getId();
        data.available = equipment.isAvailable();
        data.detailsId = equipment.getDetailsId();
        data.details = equipment.getDetails();

        return data;
    }

    @FromJson
    @NonNull
    public Equipment equipmentFromJson(@NonNull EquipmentData equipmentData) {
        return ImmutableEquipment.builder()
                .id(equipmentData.id)
                .isAvailable(equipmentData.available)
                .details(equipmentData.details)
                .detailsId(equipmentData.detailsId)
                .build();
    }


    @ToJson
    @NonNull
    public EquipmentDetailsData equipmentDetailsToJson(@NonNull EquipmentDetails equipmentDetails) {
        EquipmentDetailsData data = new EquipmentDetailsData();
        data.id = equipmentDetails.getId();
        data.name = equipmentDetails.getName();
        data.equipments = null;
        data.price = equipmentDetails.getPrice();
        data.typeId = equipmentDetails.getKindId();
        data.type = equipmentDetails.getKind();

        return data;
    }

    @FromJson
    @NonNull
    public EquipmentDetails equipmentDetailsFromJson(@NonNull EquipmentDetailsData equipmentDetailsData) {
        return ImmutableEquipmentDetails.builder()
                .id(equipmentDetailsData.id)
                .name(equipmentDetailsData.name)
                .price(equipmentDetailsData.price)
                .kind(equipmentDetailsData.type)
                .kindId(equipmentDetailsData.typeId)
                .build();
    }

    private static class EquipmentData {

        public int id;

        public boolean available;

        public int detailsId;

        public EquipmentDetails details;
    }

    private static class EquipmentKindData {

        public int id;

        public String name;

        public String description;
    }

    private static class EquipmentDetailsData {

        public int id;

        public String name;

        public double price;

        public int typeId;

        public EquipmentKind type;

        @Nullable
        public List<Equipment> equipments;
    }


    private static class PhotoshootData {

        public UUID id;

        public int orderId;

        public String address;

        public Date start;

        public int durationMinutes;

        @Nullable
        public List<PhotoshootImage> images;
    }

    // TODO: 26-09-2021 00:46 - Continue from here
    //  decide how to fetch employee images
    //  add contentActivity viewmodel
    //  load current employee pic
    //  look for some visitor pattern generator library at awesome java code generation

    @SuppressLint("UnknownNullness")
    private static class PhotoshootImageData {
        public UUID id;

        public UUID photoshootId;

        @Nullable
        public Photoshoot photoshoot;

        public boolean onPortifolio;
    }

    @SuppressLint("UnknownNullness")
    private static class EmployeeData {

        public String cpf;

        public String name;

        public String socialName;

        public String birthDate;

        public String phone;

        public String email;

        public int accessLevel;

        public int occupationId;

        @Nullable
        public Occupation occupation;

        public String gender;

        public String rg;
    }

    @SuppressLint("UnknownNullness")
    static class OccupationData {
        public int id;

        public String name;

        public String description;
    }
}
