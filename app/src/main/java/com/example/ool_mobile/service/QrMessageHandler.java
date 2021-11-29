package com.example.ool_mobile.service;

import androidx.annotation.NonNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Objects;

import timber.log.Timber;

public class QrMessageHandler {

    public static final String PREFIX = "oolMobile://";

    static class QrJson {
        String type;
        Integer id;
    }

    @NonNull
    public Result parseQrString(@NonNull String qrString) {

        Objects.requireNonNull(qrString, "qrString is null");

        if (qrString.startsWith(PREFIX)) {
            qrString = qrString.substring(PREFIX.length());
        }

        return parseQrStringWithoutPrefix(qrString);
    }

    @NonNull
    private Result parseQrStringWithoutPrefix(@NonNull String qrString) {

        Objects.requireNonNull(qrString, "qrString is null");

        Moshi moshi = new Moshi.Builder()
                .build();

        JsonAdapter<? extends QrJson> adapter = moshi.adapter(QrJson.class);

        QrJson result;

        try {
            result = adapter.fromJson(qrString);
        } catch (IOException e) {
            Timber.e(e);

            return Result.UnknownQr;
        }

        if (result == null || result.type == null || result.id == null || result.id < 0) {
            return Result.UnknownQr;
        }

        if (!result.type.equals("equipment")) {
            return Result.UnsupportedQr;
        }

        return new Result.Success(result.id);
    }

    public interface Result {

        // todo: make Unknown Qr separate method

        Result UnknownQr = Visitor::visitInvalidQr;
        Result UnsupportedQr = Visitor::visitUnsupportedQr;

        class Success implements Result {

            public final int id;

            public Success(int id) {
                this.id = id;
            }

            @Override
            public void accept(@NonNull Visitor visitor) {
                visitor.visitSuccess(id);
            }
        }

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visitInvalidQr();

            void visitUnsupportedQr();

            void visitSuccess(int equipmentId);
        }
    }

}
