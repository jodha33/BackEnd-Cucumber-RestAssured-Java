package com.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.hu.Ha;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task2_Stepdefs {

    List<Map<String,String>> arr=new ArrayList<>();
    Response response;
    HashMap<String,String> groupIDList=new HashMap<>();
    HashMap<String,HashMap<String,String>> resorceIDList=new HashMap<>();

    @When("^I Post the data with groupname$")
    public void iPostTheDataWithGroupname(DataTable table) {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)){
            String body="{\"name\" : \""+row.get("GroupName")+"\"}";
            System.out.println(body);
            response=RestAssured.given().body(body).post("https://5fd233eb8cee610016adf0ea.mockapi.io/data/Group");
            Assert.assertTrue(response.statusCode()==201);
            JsonPath jsonPath=response.body().jsonPath();
            groupIDList.put(row.get("GroupName"),jsonPath.get("id").toString());
        }
        System.out.println(groupIDList);
    }


    @Then("^I Post the data to resource api$")
    public void iPostTheDataToResourceApi(DataTable table) throws IOException {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)) {
            String body=new String(Files.readAllBytes(Paths.get("src/test/resources/resourceApi.json")));
            String resourceName=row.get("ResourceName");
            String id=groupIDList.get(row.get("GroupName"));

            //Use below url not temporaryURL in 53 Line
            String url="baseURL/group/"+id+"/resource";
            String temporaryURL="https://5fd233eb8cee610016adf0ea.mockapi.io/data/Resource";

            response=RestAssured.given().body(body).post(temporaryURL);
            Assert.assertTrue(response.statusCode()==201);
            JsonPath jsonPath=response.body().jsonPath();
            Assert.assertTrue(jsonPath.get("granted").toString().contains(""));
            Assert.assertTrue(jsonPath.get("reserved").toString().contains(""));

            //Uncomment Below Line
            //Assert.assertTrue(jsonPath.get("group").equals(groupIDList.get(row.get("GroupName"))));
            String resourceID=jsonPath.get("id");
            System.out.println(resourceID);
            HashMap<String,String> resorceIDMap=new HashMap<>();
            resorceIDMap.put(resourceName,resourceID);
            resorceIDList.put(row.get("GroupName"),resorceIDMap);
        }
    }

    @Then("^I hit the Get resource API$")
    public void iHitTheGetResourceAPI(DataTable table) throws IOException {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)) {
            String resourceName=row.get("ResourceName");
            //Use below url not temporaryURL in 78 Line
            String url="baseURL/group/"+groupIDList.get(row.get("GroupName"))+"/resource/"+resorceIDList.get(row.get("GroupName")).get(resourceName);
            String temporaryURL="https://5fd233eb8cee610016adf0ea.mockapi.io/data/GroupdID/123123123";

            response=RestAssured.given().get(temporaryURL).thenReturn();
            Assert.assertTrue(response.statusCode()==200);
            JsonPath jsonPath=response.body().jsonPath();

            Assert.assertTrue(jsonPath.get("type").toString().contains("core"));
            Assert.assertTrue(jsonPath.get("quantity").toString().contains("0"));
            Assert.assertTrue(jsonPath.get("manufaturer").toString().contains("string"));
            Assert.assertTrue(jsonPath.get("model").toString().contains("string"));
            Assert.assertTrue(jsonPath.get("capacity").toString().contains("0"));
            //Uncomment below line
            //Assert.assertTrue(jsonPath.get("group").toString().contains(groupIDList.get(row.get("GroupName"))));
            Assert.assertTrue(jsonPath.get("granted").toString().contains(""));
            Assert.assertTrue(jsonPath.get("reserved").toString().contains(""));
            //Uncomment below line
            //Assert.assertTrue(jsonPath.get("id").toString().contains(resorceIDList.get(row.get("GroupName")).get(resourceName)));
        }
    }

    @Then("^I hit the Get Resource List API and verify the Data$")
    public void iHitTheGetResourceListAPIAndVerifyTheData(DataTable table) {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)) {
            String groupName=row.get("GroupName");
            //Use below url not temporaryURL in 105 Line
            String url = "/group/1k3k4k56k7j7kj/resource";
            String temporaryURL = "https://5fd233eb8cee610016adf0ea.mockapi.io/data/GroupdID";

            response = RestAssured.given().get(temporaryURL);
            JsonPath jsonPath=response.body().jsonPath();
            for(String resourceName:resorceIDList.get(groupName).keySet()){
                String resourceID=resorceIDList.get(groupName).get(resourceName);
                System.out.println(jsonPath.getList("id"));
                System.out.println(resourceID);
                Assert.assertTrue(jsonPath.getList("id").contains(resourceID));
                System.out.println("count "+1);
            }
        }
    }
}
