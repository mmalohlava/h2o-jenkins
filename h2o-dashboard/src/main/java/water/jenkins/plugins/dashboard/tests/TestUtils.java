package water.jenkins.plugins.dashboard.tests;

import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.model.Run;
import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.TestResult;
import hudson.tasks.junit.TestResultAction;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.ClassResult;

public class TestUtils {
  
  protected static TestRunResults getTestRunResults(Run run, TestResultAction tra) {
    TestRunResults trr = new TestRunResults(run, tra.getUrlName());
    TestResult tr = tra.getResult();
    for (PackageResult packRes : tr.getChildren()) {
      trr.addTestPackage(packRes);            
      for (ClassResult classRes :  packRes.getChildren()) {
        for (CaseResult caser : classRes.getChildren()) {
          trr.addCaseResult(packRes, caser);          
        }
      }
    }
    
    return trr;        
  }

  public static TestRunResults getTestResult(Run run) {
    TestResultAction tra = run.getAction(TestResultAction.class);
    if (tra != null) {
       return getTestRunResults(run, tra);
    } 
    
    SurefireAggregatedReport surefireTestResults = run.getAction(SurefireAggregatedReport.class);
    if (surefireTestResults != null) {
       return TestRunResults.EMPTY_RESULT;
    }
    
    return TestRunResults.EMPTY_RESULT;
 }
}
