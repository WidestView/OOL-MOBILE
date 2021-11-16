package com.example.ool_mobile.rx;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A composite rule for TrampolineSchedulersRule, RxErrorRule and InstantTaskExecutorRule.
 */
public class RxTestRule implements TestRule {

    private final TrampolineSchedulersRule trampolineRule = new TrampolineSchedulersRule();

    private final RxErrorRule rxErrorRule = new RxErrorRule();

    private final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Override
    public Statement apply(Statement base, Description description) {
        return rxErrorRule.apply(
                trampolineRule.apply(
                        instantTaskExecutorRule.apply(base, description), description
                ), description
        );
    }
}
