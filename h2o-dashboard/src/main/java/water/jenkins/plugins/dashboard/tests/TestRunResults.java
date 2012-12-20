package water.jenkins.plugins.dashboard.tests;

import hudson.model.Run;
import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.CaseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TestRunResults {
  
  public static final TestRunResults EMPTY_RESULT = new TestRunResults(null);
  
  private HashMap<PackageResult, ArrayList<CaseResult>> caseResults = new HashMap<PackageResult, ArrayList<CaseResult>>(); 
  
  private Run run;
  
  public TestRunResults(final Run run) {
    this.run = run;    
  }

  public Set<PackageResult> getPackages() {
    return caseResults.keySet();
  }
  
  public void addTestPackage(final PackageResult pack) {
    if (!caseResults.containsKey(pack)) {
      caseResults.put(pack, new ArrayList<CaseResult>());            
    }
  }  
  
  public void addCaseResult(final PackageResult pack, final CaseResult cr) {
    if (!caseResults.containsKey(pack)) {
      addTestPackage(pack);      
    } 
    caseResults.get(pack).add(cr);
  }
  
  public List<CaseResult> getCaseResults(final PackageResult pack) {
    return caseResults.get(pack);
  }
}
