package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtils;

public class TestListener extends LoggerUtils implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // delete screenshot from temp folder (if it was saved in temp folder)
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // copy screenshot from temp folder to screenshots folder
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // delete screenshot from temp folder (if it was saved in temp folder)

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
