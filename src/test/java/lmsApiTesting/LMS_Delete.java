package lmsApiTesting;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lmsUtils.ExcelUtils;
import lmsUtils.LMSAppConstants;

public class LMS_Delete {

	@DataProvider(name = "LMSdataProvider")
	public String[] Should_Delete_Job_Data() throws Exception {
		String[] testObject = ExcelUtils.getProgramIDFromExcelSheet(LMSAppConstants.excel_DELETE_SheetName);
		return testObject;
	}

	@Test(dataProvider = "LMSdataProvider")
	void Should_Pass_Delete_Program(String ProgramID) {

		// https://lms-admin-rest-service.herokuapp.com/programs/11225

		// RestAssured.baseURI = "https://lms-admin-rest-service.herokuapp.com/";
		// RestAssured.baseURI = LMSAppConstants.Const_LMS_API_URL;

		Response response = RestAssured.given().auth().basic(LMSAppConstants.Const_Username, LMSAppConstants.Const_Pwd)
				// .queryParam("ProgramID", ProgramID)
				.delete(LMSAppConstants.Const_LMS_API_URL + LMSAppConstants.Const_LMS_API_Delete_Method + "/"
						+ ProgramID);

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Request not Deleted");
	}

	@Test
	void Should_Fail_Delete_Job() {

		Response response = RestAssured.given().auth().basic(LMSAppConstants.Const_Username, LMSAppConstants.Const_Pwd)
				// .queryParam("program Id", "0")
				.delete(LMSAppConstants.Const_LMS_API_URL + LMSAppConstants.Const_LMS_API_Delete_Method + "/0");

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 500, "Job Exists");

	}

}
