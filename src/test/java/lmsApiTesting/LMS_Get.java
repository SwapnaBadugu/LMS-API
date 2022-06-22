package lmsApiTesting;

import static org.hamcrest.MatcherAssert.assertThat;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lmsUtils.ExcelUtils;
import lmsUtils.LMSAppConstants;

public class LMS_Get {

	@Test
	void Should_Get_All_Programs() {

		// Navigating to the URL
		RestAssured.baseURI = LMSAppConstants.Const_LMS_API_URL;

		RequestSpecification request = RestAssured.given().auth().basic(LMSAppConstants.Const_Username,
				LMSAppConstants.Const_Pwd);

		Response response = request.get(LMSAppConstants.Const_LMS_API_Get_Method).then().extract().response();

		int statusCode = response.getStatusCode();

		// read the response body. convert it to string format.
		String responsebody = response.getBody().asString();

		// Printing the responseBody and statusCode
		System.out.println("Job details: " + responsebody);
		System.out.println("Status Code: " + statusCode);

		// Validate the GetallAPIMethod

		Reporter.log("Response Received Successfully");

		assertThat("Json response schema", responsebody,
				JsonSchemaValidator.matchesJsonSchemaInClasspath("LmsSchema_GET.json"));

		System.out.println("Json schema validation is Successful");

	}

	@DataProvider(name = "programsdataProvider")
	public String[] Should_Get_Program_Data_by_Id() throws Exception {
		String[] testObject = ExcelUtils.getProgramIDFromExcelSheet(LMSAppConstants.excel_GET_SheetName);
		return testObject;
	}

	@Test(dataProvider = "programsdataProvider")
	void Should_Get_Program_By_ID(String programId) {

		// RestAssured.baseURI = "https://lms-admin-rest-service.herokuapp.com/";

		RequestSpecification request = RestAssured.given().auth().basic(LMSAppConstants.Const_Username,
				LMSAppConstants.Const_Pwd);

		Response response = request
				.get(LMSAppConstants.Const_LMS_API_URL + LMSAppConstants.Const_LMS_API_Get_Method + "/" + programId)
				.then().extract().response();

		int statusCode = response.getStatusCode();
		System.out.println(statusCode);

		String responseBody = response.getBody().asString();

		Assert.assertEquals(statusCode, 200);
		assertThat("Json response schema", responseBody,
				JsonSchemaValidator.matchesJsonSchemaInClasspath("LmsSchema_GETByID.json"));

		System.out.println("finished!!!");

	}

}
