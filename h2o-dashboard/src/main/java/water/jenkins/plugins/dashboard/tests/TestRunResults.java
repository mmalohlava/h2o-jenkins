package water.jenkins.plugins.dashboard.tests;

import hudson.model.Build;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.Run.Artifact;
import hudson.scm.ChangeLogSet;
import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.ClassResult;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunResults {
  
  public static final TestRunResults EMPTY_RESULT = new TestRunResults(null, null);
  
  private ArrayList<PackageResult> packageResults = new ArrayList<PackageResult>();
  private HashMap<PackageResult, ArrayList<CaseResult>> caseResults = new HashMap<PackageResult, ArrayList<CaseResult>>(); 
  
  /** Change set cache */
  private Map<Run, ChangeLogSet> changeSetCache = new HashMap<Run, ChangeLogSet>(); 

  final private Job job;
  final private Run run;
  final private String testResultAction;

  private int failCount;
  private int passCount;
  
  public TestRunResults(final Run run, final String testResultAction) {
    this.run = run;
    this.job = (run != null) ? run.getParent() : null;
    this.testResultAction = testResultAction;
    this.passCount = 0;
    this.failCount = 0;
  }

  public List<PackageResult> getPackages() {
    List<PackageResult> result = packageResults;    
    return result;
  }
  
  public void addTestPackage(final PackageResult pack) {
    // This is a hack to skip wrong package name
    if (skipPackage(pack)) return;
    if (!caseResults.containsKey(pack)) {
      packageResults.add(pack);
      caseResults.put(pack, new ArrayList<CaseResult>());      
    }
  }  
  
  public void addCaseResult(final PackageResult pack, final CaseResult cr) {
    // This is a hack to skip wrong package name
    if (skipPackage(pack)) return;    
    if (!caseResults.containsKey(pack)) {
      addTestPackage(pack);      
    } 
    caseResults.get(pack).add(cr);
    if (cr.isPassed()) 
      passCount++;
    else
      failCount++;
    
  }
  
  private boolean skipPackage(final PackageResult pack) {
    return pack.getName().startsWith("<nose");
  }
  
  public List<CaseResult> getCaseResults(final PackageResult pack) {
    List<CaseResult> result = caseResults.get(pack);
    Collections.sort(caseResults.get(pack));
    
    return result;
  }
  
  public String getURL(final TestResult tr) {
    StringBuffer sb = new StringBuffer();
    sb.append(tr.getOwner().getUrl()).append(testResultAction).append(tr.getUrl());
    return sb.toString();
  }   
  
  public Run getRun() {
    return this.run;
  }
  
  public Job getJob() {
    return this.job;
  }
  
  public int getFailCount() {
    return this.failCount;    
  }
  
  public int getPassCount() {
    return this.passCount;
  }
  
  public int getMaxNumBuilds(int max) {
    return run != null ? Math.min(run.getNumber(), max) : max;
  }
  
  public String getRunArtifactURL(final Run run) {
    List<Artifact> ars = run.getArtifacts();
    if (ars.size() == 0) return null;
    for (Artifact a : ars) 
    	if (a.getFileName().equals("url")) {
    		File f = a.getFile();
    		String url = null;
    		try {
    			url = readLine(f);
    		} catch (IOException _) {
    		}
    		return url;
    	}
    return run.getUrl() + "artifact/target/index.html";
  }
  
  static String readLine(File f) throws IOException {
	  BufferedReader in = null;
	  try {
		  in = new BufferedReader(new FileReader(f));
		  return in.readLine();
	  } finally { in.close(); }
  }
  
  public int getBuildNumber() {
    return run!=null ? run.getNumber() : 0;     
  }
  
  public CaseResult getCaseResultInBuild(int buildNum, PackageResult pr, CaseResult cr) {
    Run r = job.getBuildByNumber(buildNum);
    if (r == null) return null;
    
    AbstractTestResultAction tra = r.getAction(pr.getParentAction().getClass());
    if(tra!=null) {
        TestResult result = tra.findCorrespondingResult(pr.getId());
        if (result!=null && result instanceof PackageResult)
          for (ClassResult classResult : ((PackageResult) result).getChildren()) {
            CaseResult previousCR = classResult.getCaseResult(cr.getName());
            if (previousCR!=null) return previousCR;
          }
    }
    return null;
  }
  
  public ChangeLogSet getChangeLogSet(final Run r) {    
    if (r instanceof Build) {
      if (changeSetCache.containsKey(r)) {
        System.out.println("Cached TestRunResults.getChangeSet(): " + r);
        return changeSetCache.get(r);
      } else {
        Build b = (Build) r;
        ChangeLogSet set = b.getChangeSet(); 
      }
    }
    return null;
  }
}
