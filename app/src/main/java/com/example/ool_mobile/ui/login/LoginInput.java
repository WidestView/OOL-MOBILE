package com.example.ool_mobile.ui.login;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.ool_mobile.BR;

public class LoginInput extends BaseObservable {

    @NonNull
    private String email = "";

    @NonNull
    private String password = "";

    @Bindable
    @NonNull
    public String getEmail() {
        return email;
    }

    @Bindable
    @NonNull
    public String getPassword() {
        return password;
    }

    public void setEmail(@NonNull String email) {

        if (!this.email.equals(email)) {
            this.email = email;

            notifyPropertyChanged(BR.email);
        }
    }

    public void setPassword(@NonNull String password) {

        if (!this.password.equals(password)) {
            this.password = password;

            notifyPropertyChanged(BR.password);
        }
    }
}
