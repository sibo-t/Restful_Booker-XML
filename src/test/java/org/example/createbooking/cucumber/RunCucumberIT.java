package org.example.createbooking.cucumber;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue= "org/example/createbooking/steps",
        tags="@createbooking"
)

public class RunCucumberIT {

}
