package com.aqa.example;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepStarted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberStepListener implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger(CucumberStepListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, event -> {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                final PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
                String testStepMessage = testStep.getStep().getText();

                LOGGER.info(testStepMessage);
            }
        });
    }
}
