package lmsApiTesting;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map.Entry;

		import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lmsUtils.ExcelUtils;
import lmsUtils.LMSAppConstants;

public class LMS_Post {

	@DataProvider(name = "ProgramDataProvider")
	public String[][] Should_Post_Program_Data() throws Exception {
		String[][] testObject = ExcelUtils.getDataFromExcelSheet(LMSAppConstants.excel_POST_SheetName);
		return testObject;
	}

	@Test(dataProvider = "ProgramDataProvider")
	void Should_Post_New_Program(String programName, String programDescription, String online) {

		
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("programName", programName);
		hm.put("programDescription", programDescription);
		hm.put("online", online);
		 for (Entry<String , String> m : hm.entrySet()) {
			// String key = m.getKey();
			// String v = m.getValue();
		 }

		JSONObject request = new JSONObject();
		//request.put(key, v);
		request.putAll(hm);


//		JSONObject request = new JSONObject();
//		request.put("programName", programName);
//		request.put("programDescription", programDescription);
//		request.put("online", online);

		// System.out.println("request=" + request);

		RestAssured.baseURI = LMSAppConstants.Const_LMS_API_URL;

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.given().auth().basic(LMSAppConstants.Const_Username, LMSAppConstants.Const_Pwd)
				.header("Content-Type", "application/json").body(request.toJSONString()).when()
				.post(LMSAppConstants.Const_LMS_API_Post_Method).then().log().all().extract().response();

		String responseBody = response.getBody().asPrettyString();
		System.out.println(responseBody);
		// status Code Validation

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		System.out.println(statusCode);

		assertThat("Json response schema", responseBody,
				JsonSchemaValidator.matchesJsonSchemaInClasspath("LmsSchema_GETByID.json"));

		System.out.println("JSON Schema Validation is successfull");

	}
}
