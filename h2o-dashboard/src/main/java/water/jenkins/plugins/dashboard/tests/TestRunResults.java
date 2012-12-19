package water.jenkins.plugins.dashboard.tests;

import hudson.model.Run;
import hudson.tasks.junit.CaseResult;

import java.util.ArrayList;

public class TestRunResults {
  
  public static final TestRunResults EMPTY_RESULT = new TestRunResults(null);

  private ArrayList<CaseResult> okTests      = new ArrayList<CaseResult>();
  private ArrayList<CaseResult> failedTests  = new ArrayList<CaseResult>();
  private ArrayList<CaseResult> skippedTests = new ArrayList<CaseResult>(); 
  private ArrayList<CaseResult> tests        = new ArrayList<CaseResult>();
  private Run run;
  
  public TestRunResults(Run run) {
    this.run = run;    
  }

  public void addTest(CaseResult cr) {
    tests.add(cr);     
    if (cr.isPassed()) {
      okTests.add(cr);
    } else if (cr.isSkipped()) {
      skippedTests.add(cr);
    } else {
      failedTests.add(cr);
    }
  } 
  
  public ArrayList<CaseResult> getTests() {
    return tests;
  }
  
  public ArrayList<CaseResult> getFailedTests() {
    return failedTests;
  }
  
  public ArrayList<CaseResult> getOkTests() {
    return okTests;
  }
  public ArrayList<CaseResult> getSkippedTests() {
    return skippedTests;
  }
  
  
}
