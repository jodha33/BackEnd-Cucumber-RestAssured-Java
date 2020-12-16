package com.steps;

import com.pojos.task2.Group;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
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
   // HashMap<String,String> groupIDList=new HashMap<>();
    //HashMap<String,HashMap<String,String>> resorceIDList=new HashMap<>();
    Group groupObject;
    List<Group> groupDataList=new ArrayList<>();
    @Before("@task2")
    public void cleanUPAPIBeforeExecution(){
        String url="https://5fd233eb8cee610016adf0ea.mockapi.io/data/List";
        response=RestAssured.given().get(url);
        for(Object id: response.jsonPath().getList("id")){
            String idToDelete=id.toString();
            String urlToDelete="https://5fd233eb8cee610016adf0ea.mockapi.io/data/List/"+idToDelete;
            RestAssured.delete(urlToDelete);
            System.out.println("Successfully Deleted Record");
        }
    }

    @After("@task2")
    public void cleanUPAPIAfterExecution(){
        for(Group grp: groupDataList){
            for(String resName:grp.getResourceList().keySet()){
                String idToDelete=grp.getResourceList().get(resName);
                String urlToDelete="https://5fd233eb8cee610016adf0ea.mockapi.io/data/List/"+idToDelete;
                RestAssured.delete(urlToDelete);
                System.out.println("Successfully Deleted Record");
            }

        }
    }

    @When("^I Post the data with groupname$")
    public void iPostTheDataWithGroupname(DataTable table) {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)){
            String body="{\"name\" : \""+row.get("GroupName")+"\"}";
            System.out.println(body);
            response=RestAssured.given().body(body).post("https://5fd233eb8cee610016adf0ea.mockapi.io/data/Group");
            Assert.assertTrue(response.statusCode()==201);
            JsonPath jsonPath=response.body().jsonPath();
            //groupIDList.put(row.get("GroupName"),jsonPath.get("id").toString());
            groupObject=new Group();
            groupObject.setGroupName(row.get("GroupName"));
            groupObject.setGroupId(jsonPath.get("id").toString());
            groupDataList.add(groupObject);
        }
        System.out.println(groupDataList);
    }


    @Then("^I Post the data to resource api$")
    public void iPostTheDataToResourceApi(DataTable table) throws IOException {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)) {
            String body=new String(Files.readAllBytes(Paths.get("src/test/resources/resourceApi.json")));
            String resourceName=row.get("ResourceName");
            String id=groupDataList.get(getGroupIndexByGroupName(row.get("GroupName"))).getGroupId();

            //Use below url not temporaryURL in 53 Line
            String url="baseURL/group/"+id+"/resource";
            String temporaryURL="https://5fd233eb8cee610016adf0ea.mockapi.io/data/Resource";

            response=RestAssured.given().body(body).post(temporaryURL);
            Assert.assertTrue(response.statusCode()==201);
            JsonPath jsonPath=response.body().jsonPath();
            Assert.assertTrue(jsonPath.get("granted").toString().contains(""));
            Assert.assertTrue(jsonPath.get("reserved").toString().contains(""));

            //Uncomment Below Line
            //Assert.assertTrue(jsonPath.get("group").equals(id));
            String resourceID=jsonPath.get("id");
            System.out.println(resourceID);
            HashMap<String,String> resorceIDMap=new HashMap<>();
            resorceIDMap.put(resourceName,resourceID);

            for(Group item:groupDataList){
                if(item.getGroupName().equals(row.get("GroupName"))){
                    item.getResourceList().put(resourceName,resourceID);
                }
            }
        }
        System.out.println(groupDataList);
    }

    public int getGroupIndexByGroupName(String groupName){
        int index=-99;
        for(Group item:groupDataList) {
            if(item.getGroupName().equals(groupName)) {
                    index=groupDataList.indexOf(item);
            }
        }
        return index;
    }

    @Then("^I hit the Get resource API$")
    public void iHitTheGetResourceAPI(DataTable table) throws IOException {
        arr=table.asMaps(String.class,String.class);
        for(Map<String, String> row:table.asMaps(String.class,String.class)) {
            String resourceName=row.get("ResourceName");
            String storedResourceID=groupDataList.get(getGroupIndexByGroupName(row.get("GroupName"))).getResourceList().get(resourceName);
            //Use below url not temporaryURL in 78 Line
            String groupID=groupDataList.get(getGroupIndexByGroupName(row.get("GroupName"))).getGroupId();
            String url="baseURL/group/"+groupID+"/resource/"+storedResourceID;
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
            //Assert.assertTrue(jsonPath.get("group").toString().contains(groupID));
            Assert.assertTrue(jsonPath.get("granted").toString().contains(""));
            Assert.assertTrue(jsonPath.get("reserved").toString().contains(""));
            //Uncomment below line
            //Assert.assertTrue(jsonPath.get("id").toString().contains(storedResourceID));
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

            for(String resourceName:groupDataList.get(getGroupIndexByGroupName(row.get("GroupName"))).getResourceList().keySet()){
                String resourceID=groupDataList.get(getGroupIndexByGroupName(row.get("GroupName"))).getResourceList().get(resourceName);
                System.out.println(jsonPath.getList("id"));
                System.out.println(resourceID);
                Assert.assertTrue(jsonPath.getList("id").contains(resourceID));
                System.out.println("count "+1);
            }
        }
    }
}
