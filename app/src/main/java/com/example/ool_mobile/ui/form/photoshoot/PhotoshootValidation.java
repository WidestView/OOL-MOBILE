package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.FormTime;
import com.example.ool_mobile.ui.util.form.ValidationResult;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observer;

class PhotoshootValidation {

    @NonNull
    private final Observer<PhotoshootViewModel.Event> events;

    public PhotoshootValidation(@NonNull Observer<PhotoshootViewModel.Event> events) {
        Objects.requireNonNull(events, "events is null");

        this.events = events;
    }

    @Nullable
    public Photoshoot normalize(@NonNull final PhotoshootInput input) {

        final PhotoshootInput data = ImmutablePhotoshootInput.builder()
                .orderId(input.getOrderId().trim())
                .address(input.getAddress().trim())
                .startTime(input.getStartTime())
                .endTime(input.getEndTime())
                .date(input.getDate())
                .build();

        List<FormCheck<PhotoshootViewModel.Event>> checks = getChecks(data);

        boolean error = false;

        for (FormCheck<PhotoshootViewModel.Event> check : checks) {

            if (check.performCheck() == ValidationResult.Failure) {
                events.onNext(check.getError());
                error = true;
            }
        }

        if (error) {
            return null;
        }

        return ImmutablePhotoshoot.builder()
                .resourceId(UUID.randomUUID())
                .address(data.getAddress())
                .orderId(Integer.parseInt(data.getOrderId()))
                .startTime(getDate(data))
                .durationMinutes(getDuration(data))
                .build();
    }

    @NotNull
    private List<FormCheck<PhotoshootViewModel.Event>> getChecks(PhotoshootInput data) {
        return Arrays.asList(
                FormCheck.failIf(
                        () -> data.getAddress().isEmpty(),
                        PhotoshootViewModel.Event.EmptyAddress
                ),
                FormCheck.succeedIf(
                        () -> {
                            try {
                                Integer.parseInt(data.getOrderId());
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        },
                        PhotoshootViewModel.Event.InvalidOrder
                ),
                FormCheck.failIf(
                        () -> data.getOrderId().isEmpty(),
                        PhotoshootViewModel.Event.EmptyOrder
                ),
                FormCheck.failIf(
                        () -> data.getStartTime() == null,
                        PhotoshootViewModel.Event.EmptyStartTime
                ),
                FormCheck.failIf(
                        () -> data.getEndTime() == null,
                        PhotoshootViewModel.Event.EmptyEndTime
                ),
                FormCheck.failIf(
                        () -> data.getDate() == null,
                        PhotoshootViewModel.Event.EmptyDate
                ),
                FormCheck.failIf(
                        () -> {
                            FormTime end = data.getEndTime();
                            FormTime start = data.getStartTime();

                            return end != null &&
                                    start != null &&
                                    end.totalMinutes() < start.totalMinutes();
                        },
                        PhotoshootViewModel.Event.InvalidTimeRange
                )

        );
    }

    private int getDuration(PhotoshootInput data) {

        Objects.requireNonNull(data.getEndTime(), "endTime");
        Objects.requireNonNull(data.getStartTime());

        return (int) ((data.getEndTime().totalMinutes()) - data.getStartTime().totalMinutes());
    }

    @NotNull
    private Date getDate(PhotoshootInput data) {

        Calendar calendar = Calendar.getInstance();

        Objects.requireNonNull(data.getDate(), "input date is null");
        Objects.requireNonNull(data.getStartTime(), "input start time is null");

        calendar.setTime(new Date(data.getDate()));

        calendar.add(Calendar.HOUR, (int) data.getStartTime().getHour());
        calendar.add(Calendar.MINUTE, (int) data.getStartTime().getMinute());

        return calendar.getTime();
    }
}
