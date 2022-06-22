package lmsApiTesting;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lmsUtils.ExcelUtils;
import lmsUtils.LMSAppConstants;
import io.restassured.http.ContentType;

public class LMS_Put {
	@DataProvider(name = "ProgramDataProvider")
	public String[][] Should_Post_Program_Data() throws Exception {
		String[][] testObject = ExcelUtils.getDataFromExcelSheet(LMSAppConstants.excel_PUT_SheetName);
		return testObject;
	}

	@Test(dataProvider = "ProgramDataProvider")
	void Should_Modify_Program_Data(String programId, String programName, String programDescription, String online) {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("programId", programId);
		hm.put("programName", programName);
		hm.put("programDescription", programDescription);
		hm.put("online", online);
		 for (Entry m : hm.entrySet());

		JSONObject request = new JSONObject();
		request.putAll(hm);
//		JSONObject request = new JSONObject();
//		request.put("programId", programId);
//		request.put("programName", programName);
//		request.put("programDescription", programDescription);
//		request.put("online", online);
//
//		System.out.println("request=" + request);

		// https://lms-admin-rest-service.herokuapp.com/programs/11225

		// RestAssured.baseURI = LMSAppConstants.Const_LMS_API_URL;

		Response response = RestAssured.given().auth().basic(LMSAppConstants.Const_Username, LMSAppConstants.Const_Pwd)
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON)
				.body(request.toJSONString()).when()
				.put(LMSAppConstants.Const_LMS_API_URL + LMSAppConstants.Const_LMS_API_Put_Method + "/" + programId)
				.then().log().all().extract().response();

		// Json schema validation
		String responseBody = response.getBody().asPrettyString();
		System.out.println(responseBody);

		assertThat("Json response schema", responseBody,
				JsonSchemaValidator.matchesJsonSchemaInClasspath("LmsSchema_GETByID.json"));

		System.out.println("JSON Schema Validation is successful");

	}

}
