package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.form.CheckResult;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.FormTime;

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
                .orderId(input.orderId().trim())
                .address(input.address().trim())
                .startTime(input.startTime())
                .endTime(input.endTime())
                .date(input.date())
                .build();

        List<FormCheck<PhotoshootViewModel.Event>> checks = getChecks(data);

        boolean error = false;

        for (FormCheck<PhotoshootViewModel.Event> check : checks) {

            if (check.performCheck() == CheckResult.Failure) {
                events.onNext(check.getError());
                error = true;
            }
        }

        if (error) {
            return null;
        }

        return ImmutablePhotoshoot.builder()
                .resourceId(UUID.randomUUID())
                .address(data.address())
                .orderId(Integer.parseInt(data.orderId()))
                .startTime(getDate(data))
                .durationMinutes(getDuration(data))
                .build();
    }

    @NotNull
    private List<FormCheck<PhotoshootViewModel.Event>> getChecks(PhotoshootInput data) {
        return Arrays.asList(
                FormCheck.failIf(
                        () -> data.address().isEmpty(),
                        PhotoshootViewModel.Event.EmptyAddress
                ),
                FormCheck.succeedIf(
                        () -> {
                            try {
                                Integer.parseInt(data.orderId());
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        },
                        PhotoshootViewModel.Event.InvalidOrder
                ),
                FormCheck.failIf(
                        () -> data.orderId().isEmpty(),
                        PhotoshootViewModel.Event.EmptyOrder
                ),
                FormCheck.failIf(
                        () -> data.startTime() == null,
                        PhotoshootViewModel.Event.EmptyStartTime
                ),
                FormCheck.failIf(
                        () -> data.endTime() == null,
                        PhotoshootViewModel.Event.EmptyEndTime
                ),
                FormCheck.failIf(
                        () -> data.date() == null,
                        PhotoshootViewModel.Event.EmptyDate
                ),
                FormCheck.failIf(
                        () -> {
                            FormTime end = data.endTime();
                            FormTime start = data.startTime();

                            return end != null &&
                                    start != null &&
                                    end.totalMinutes() < start.totalMinutes();
                        },
                        PhotoshootViewModel.Event.InvalidTimeRange
                )

        );
    }

    private int getDuration(PhotoshootInput data) {
        return (int) ((data.endTime().totalMinutes()) - data.startTime().totalMinutes());
    }

    @NotNull
    private Date getDate(PhotoshootInput data) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date(data.date()));

        calendar.add(Calendar.HOUR, (int) data.startTime().getHour());
        calendar.add(Calendar.MINUTE, (int) data.startTime().getMinute());

        return calendar.getTime();
    }
}
