package water.jenkins.plugins.dashboard.tests;

import hudson.model.Run;
import hudson.tasks.junit.CaseResult;

import java.util.ArrayList;

public class TestRunResults {
  
  public static final TestRunResults EMPTY_RESULT = new TestRunResults(null);
  
  private ArrayList<String> failedTests = new ArrayList<String>();
  private Run run;
  
  public TestRunResults(Run run) {
    this.run = run;    
  }

  public void addFailedTest(CaseResult cr) {
    failedTests.add(cr.getName());            
  } 
  
  public ArrayList<String> getFailedTests() {
    return failedTests;
  }
}
