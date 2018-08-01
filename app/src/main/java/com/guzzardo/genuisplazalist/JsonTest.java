package com.guzzardo.genuisplazalist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonTest {

    private String jsonString1 = " [\n" +
            "    {\n" +
            "        \"name\": \"Hermione Granger\",\n" +
            "        \"companyName\": \"Gryffindor\",\n" +
            "        \"isFavorite\": true,\n" +
            "        \"phone\": {\n" +
            "        \"work\": \"815-467-1244\",\n" +
            "        \"home\": \"815-467-0487\",\n" +
            "        \"mobile\": \"815-467-5007\"\n" +
            "        },\n" +
            "        \"address\": {\n" +
            "        \"street\": \"641 W Lake St\",\n" +
            "        \"city\": \"Chicago\",\n" +
            "        \"state\": \"IL\",\n" +
            "        \"country\": \"US\",\n" +
            "        \"zipCode\": \"60661\"\n" +
            "        }\n" +
            "    }\n" +
            " ]";


    private String jsonString = " [\n" +
            "    {\n" +
            "        \"name\": \"Scooby Doo\",\n" +
            "        \"id\": \"17\",\n" +
            "        \"companyName\": \"Mystery Incorporated\",\n" +
            "        \"isFavorite\": false,\n" +
            "        \"phone\": {\n" +
            "        \"mobile\": \"202-783-8287\"\n" +
            "        },\n" +
            "        \"address\": {\n" +
            "        \"street\": \"400 1st St Nw\",\n" +
            "        \"city\": \"Washington\",\n" +
            "        \"state\": \"DC\",\n" +
            "        \"country\": \"US\",\n" +
            "        \"zipCode\": \"10038\"\n" +
            "        }\n" +
            "        },\n" +
            "        {\n" +
            "        \"name\": \"Hermione Granger\",\n" +
            "        \"id\": \"5\",\n" +
            "        \"companyName\": \"Gryffindor\",\n" +
            "        \"isFavorite\": true,\n" +
            "        \"phone\": {\n" +
            "        \"work\": \"815-467-1244\",\n" +
            "        \"home\": \"815-467-0487\",\n" +
            "        \"mobile\": \"815-467-5007\"\n" +
            "        },\n" +
            "        \"address\": {\n" +
            "        \"street\": \"641 W Lake St\",\n" +
            "        \"city\": \"Chicago\",\n" +
            "        \"state\": \"IL\",\n" +
            "        \"country\": \"US\",\n" +
            "        \"zipCode\": \"60661\"\n" +
            "        }\n" +
            "    }\n" +
            " ]";


    public class EmployeeNested {
        String name;
        String id;
        String companyName;
        String isFavorite;
        Phone phone;
        Address address;
    }

    public class Address {
        String street;
        String state;
        String city;
        String country;
        String zipCode;
    }

    public class Phone {
        String work;
        String home;
        String mobile;
    }

    void testIt() {

        try {
            //Gson gson = new Gson();
            Type collectionType = new TypeToken<List<EmployeeNested>>(){}.getType();
            List<EmployeeNested> EmployeeNestedObjectList = (List<EmployeeNested>) new Gson().fromJson(jsonString, collectionType);
            System.out.println("hi there in testIt again!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String args[]) {
        System.out.println("hi there Joe!");
        JsonTest jTest = new JsonTest();
        jTest.testIt();
        System.out.println("hi there again!");
    }
}




