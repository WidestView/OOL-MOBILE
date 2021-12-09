package com.example.ool_mobile.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class QrMessageHandlerTest {

    private QrMessageHandler messageHandler;

    private QrMessageHandler.Result.Visitor visitor;

    @Before
    public void before() {
        messageHandler = new QrMessageHandler();

        visitor = Mockito.mock(QrMessageHandler.Result.Visitor.class);
    }

    @Test
    public void parsesQrString() {

        int id = 1;

        messageHandler.parseQrString(QrMessageHandler.PREFIX + newValidJson(id))
                .accept(visitor);

        Mockito.verify(visitor).visitSuccess(id);
    }

    @Test
    public void parseQrStringWithoutPrefix() {

        int id = 1;

        messageHandler.parseQrString(newValidJson(id)).accept(visitor);

        Mockito.verify(visitor).visitSuccess(id);

    }

    @Test
    public void reportsInvalidPrefix() {

        String wrongPrefix = "potatoBanana";

        int id = 1;

        messageHandler.parseQrString(wrongPrefix + newValidJson(id)).accept(visitor);

        Mockito.verify(visitor).visitInvalidQr();

    }

    @Test
    public void reportsInvalidJson() {

        messageHandler.parseQrString("SomethingThatIsNotJson")
                .accept(visitor);

        Mockito.verify(visitor).visitInvalidQr();

    }

    @Test
    public void reportsJsonWithNullId() {

        messageHandler.parseQrString(
                String.format(
                        "{%s: %s, %s: %s}",
                        enclose("type"), enclose("equipment"),
                        enclose("id"), "null"
                )
        ).accept(visitor);

        Mockito.verify(visitor).visitInvalidQr();

    }

    @Test
    public void reportsJsonWithoutId() {

        QrMessageHandler.Result.Visitor visitor = Mockito.mock(QrMessageHandler.Result.Visitor.class);

        messageHandler.parseQrString(
                String.format("{%s: %s}",
                        enclose("type"), enclose("equipment"))
        ).accept(visitor);


        Mockito.verify(visitor).visitInvalidQr();

    }

    @Test
    public void reportsJsonWithInvalidId() {

        QrMessageHandler.Result.Visitor visitor = Mockito.mock(QrMessageHandler.Result.Visitor.class);

        messageHandler.parseQrString(
                String.format("{%s: %s, %s: %f}", enclose("type"), enclose("equipment"),
                        enclose("id"), 10.5f)
        ).accept(visitor);

        Mockito.verify(visitor).visitInvalidQr();

    }

    @Test
    public void reportsUnknownType() {

        QrMessageHandler.Result.Visitor visitor = Mockito.mock(QrMessageHandler.Result.Visitor.class);

        messageHandler.parseQrString(
                String.format("{%s: %s, %s: %s}",
                        enclose("type"), enclose("banana"),
                        enclose("id"), "10")
        )
                .accept(visitor);

        Mockito.verify(visitor).visitUnsupportedQr();

    }

    private String newValidJson(int id) {
        return String.format(
                "{%s: %s,%s: %s}",
                enclose("type"), enclose("equipment"),
                enclose("id"), enclose(String.valueOf(id))
        );
    }

    private String enclose(String s) {
        return "\"" + s + "\"";

    }


}