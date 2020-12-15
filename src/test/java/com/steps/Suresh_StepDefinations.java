package com.steps;




import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;


import javax.json.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suresh_StepDefinations {

    List<Map<String,String>> arr=new ArrayList<>();
    Response response;
    @Then("^I validate the response from the api with datatable$")
    public void iValidateTheResponseFromTheApiWithDatatable(DataTable table) {
        arr=table.asMaps(String.class,String.class);
        response=RestAssured.get("https://5fd233eb8cee610016adf0ea.mockapi.io/data/List");
        for(Map<String, String> row:table.asMaps(String.class,String.class)){
            JsonPath JSONResponseBody = response.body().jsonPath();
           List<String> nameList= JSONResponseBody.getList("name");
           Assert.assertTrue(nameList.contains(row.get("Name")));
        }
    }


}
