package com.example.ool_mobile;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

/**
 * A rule that detects RxJava silent errors and reports them to JUnit.
 */
public class RxErrorRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                Consumer<? super Throwable> previous = RxJavaPlugins.getErrorHandler();

                AtomicReference<Throwable> result = new AtomicReference<>();

                RxJavaPlugins.setErrorHandler(result::set);

                try {
                    base.evaluate();
                } catch (Throwable error) {

                    if (previous != null) {
                        previous.accept(error);
                    }
                } finally {
                    RxJavaPlugins.setErrorHandler(previous);
                }

                if (result.get() != null) {
                    throw result.get();
                }
            }
        };
    }

}
