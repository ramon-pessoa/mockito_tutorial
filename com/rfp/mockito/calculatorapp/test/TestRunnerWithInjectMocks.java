package com.rfp.mockito.calculatorapp.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*
So far, we've used annotations to create mocks.

Mockito provides various methods to create mock objects.

mock() creates mocks without bothering about the order of method
calls that the mock is going to make in due course of its action.
 */
public class TestRunnerWithInjectMocks {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MathApplicationTesterWithInjectMocks.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
