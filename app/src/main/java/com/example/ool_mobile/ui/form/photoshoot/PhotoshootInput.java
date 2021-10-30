package com.example.ool_mobile.ui.form.photoshoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.form.FormTime;

import java.util.Date;
import java.util.Objects;

public class PhotoshootInput extends BaseObservable {

    @NonNull
    private String address = "";

    @NonNull
    private String orderId = "";

    private Date date;

    @Nullable
    private FormTime startTime;

    @Nullable
    private FormTime endTime;

    public PhotoshootInput() {

    }

    public PhotoshootInput(@NonNull Photoshoot photoshoot) {

        setOrderId(String.valueOf(photoshoot.orderId()));

        setAddress(photoshoot.address());

        setDate(photoshoot.startTime());
        setStartTime(FormTime.fromDate(photoshoot.startTime()));

        setEndTime(
                FormTime.fromDateSpan(
                        photoshoot.startTime(),
                        photoshoot.durationMinutes()
                )
        );
    }

    @NonNull
    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {

        if (!Objects.equals(this.address, address)) {
            this.address = address;
            notifyPropertyChanged(BR.address);
        }
    }

    @NonNull
    @Bindable
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {

        if (!Objects.equals(this.orderId, orderId)) {
            this.orderId = orderId;
            notifyPropertyChanged(BR.orderId);
        }
    }

    @Nullable
    @Bindable
    public Date getDate() {
        return date;
    }

    public void setDate(@Nullable Date date) {

        if (!Objects.equals(this.date, date)) {
            this.date = date;
            notifyPropertyChanged(BR.date);
        }
    }

    @Bindable
    @Nullable
    public FormTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable FormTime startTime) {

        if (!Objects.equals(this.startTime, startTime)) {
            this.startTime = startTime;
            notifyPropertyChanged(BR.startTime);
        }
    }

    @Nullable
    @Bindable
    public FormTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable FormTime endTime) {

        if (!Objects.equals(this.endTime, endTime)) {
            this.endTime = endTime;
            notifyPropertyChanged(BR.endTime);
        }
    }

}
