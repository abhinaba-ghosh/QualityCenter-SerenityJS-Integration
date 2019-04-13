package components;

import org.pmw.tinylog.Logger;

import helpers.ALMCommonHelper;
import infrastructure.Constants;
import schemas.wrapper.TestCaseWrapper;

public class ALMTestCaseCreator extends Constants {

	public static String createTestCaseInTestPlan(String parentFolderId, String TestCaseName, String applicationName,
			String testPriority, String TestPhase, String TestType, String TestStatus, String Classification,
			String owner, String Description) throws Exception {

		String responseString = "";

		String response_from_alm = ALMCommonHelper.isResouceAlreadyAvailable(
				"tests?query={parent-id[=\"" + parentFolderId + "\"];name[=\"" + TestCaseName + "\"]}");
		if (response_from_alm.equalsIgnoreCase("ERROR_OCCURED")) {

			responseString = "ERROR_OCCURED";
		} else if (response_from_alm.equalsIgnoreCase("No_ID_FOUND")) {

			String xmlContent = TestCaseWrapper.prepareXml(parentFolderId, TestCaseName);
			String test_id = ALMCommonHelper.pushRequestMessageToALM(xmlContent, "tests");
			Logger.info("newly created test id:" + test_id);
			responseString = test_id;
		} else {
			responseString = response_from_alm;
			if (alm_test_case_operation.equalsIgnoreCase("update")) {
				String xmlContent = TestCaseWrapper.prepareXml(parentFolderId, TestCaseName);
				String test_id = ALMCommonHelper.pushRequestMessageToALM(xmlContent, "tests/" + responseString);
				Logger.info("updated test id:" + test_id);
				responseString = test_id;
			}
		}

		return responseString;
	}
}
