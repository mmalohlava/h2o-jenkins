package water.jenkins.plugins.dashboard.tests;

import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.model.Run;
import hudson.tasks.junit.TestResultAction;
import hudson.tasks.junit.CaseResult;

public class TestUtils {
  
  protected static TestRunResults getTestRunResults(Run run, TestResultAction tra) {
    TestRunResults trr = new TestRunResults(run);
    for (CaseResult cr : tra.getFailedTests()) {
      trr.addFailedTest(cr);      
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
       return new TestRunResults(run);
    }
    
    return new TestRunResults(run);
 }
}
