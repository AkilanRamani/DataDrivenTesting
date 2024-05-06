package DataDrivenTesting;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.JSONObject;

public class PostAPITest {

	@Test(dataProvider = "pricedata")
	public void post_programs(String producttype, String type ,String base , String groupcode , String bulktype , 
			String bulkcode ,String bulksp , String spbasedon ,String applyfor , String refOut ,String refArea , 
			String catCode ,String catValueCode ,String itemCode ,String isChildItems ,String hsstock , String catName) {

		RestAssured.baseURI = "http://10.63.39.201:8181/RayMedi_HQ/ ";
		RestAssured.basePath = "rest/trans/priceupdate/get/cs-items";
//Creating Json object to send data along with post request
		JSONObject requestparams = new JSONObject();

// requestparams.put("online", "true");
		requestparams.put("productType",producttype );
		requestparams.put("type",type );
		requestparams.put("base", base);
		requestparams.put("groupCode",groupcode );
		requestparams.put("bulkType", bulktype);
		requestparams.put("bulkCode", bulkcode);
		requestparams.put("bulkSp", bulksp);
		requestparams.put("spBasedOn", spbasedon);
		requestparams.put("applyFor",applyfor );
		requestparams.put("refOutlet", refOut);
		requestparams.put("refArea", refArea);
		requestparams.put("catCode",catCode );
		requestparams.put("catValueCode", catValueCode);
		requestparams.put("itemCode",itemCode );
		requestparams.put("isChildItems", isChildItems);
		requestparams.put("hstock", hsstock);
		requestparams.put("catName", catName);
		
		
// requestparams.put("programId", "601");
		Response resp_prog_details = RestAssured.given().cookie("JSESSIONID","6F0467E47AAE37937DEE088F3A4BC18D.jvm1;")
				.header("Content-Type", "application/json").body(requestparams.toString()).when().post().then()
				.assertThat().statusCode(200).log().all().extract().response();
//Asserting the status code is success
		int statusCode = resp_prog_details.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		System.out.println("The response code is " + statusCode);
		String responseBody = resp_prog_details.getBody().asPrettyString();
		JsonPath jsonpath = resp_prog_details.jsonPath();
		String successmessage = jsonpath.get("status");
		
		String msg = jsonpath.get("message");
		
//Asserting the correct values are posted
		Assert.assertEquals(responseBody.contains(itemCode), true);
		Assert.assertEquals(responseBody.contains(successmessage), true);
		Assert.assertEquals(responseBody.contains(msg), true);
	}

	@DataProvider(name = "pricedata")
	public String[][] get_prog_data() throws IOException {
		String path = System.getProperty("user.dir") + "/src/test/java/DataDrivenTesting/CsPriceData.xlsx";
		int rownum = XLUtils.getRowCount(path, "Sheet1");
		int colnum = XLUtils.getCellCount(path, "Sheet1", 1);
		String progdata[][] = new String[rownum][colnum];
		for (int i = 1; i <= rownum; i++) {
			for (int j = 0; j < colnum; j++) {
				progdata[i - 1][j] = XLUtils.getCellData(path, "Sheet1", i, j);
			}
		}
		return progdata;
	}
}
