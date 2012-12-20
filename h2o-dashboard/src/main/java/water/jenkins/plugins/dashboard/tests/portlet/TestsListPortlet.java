/**
 * 
 */
package water.jenkins.plugins.dashboard.tests.portlet;

import hudson.Extension;
import hudson.model.TopLevelItem;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.ArrayList;
import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import water.jenkins.plugins.dashboard.tests.TestRunResults;
import water.jenkins.plugins.dashboard.tests.TestUtils;

/**
 * @author Michal Malohlava
 *
 */
public class TestsListPortlet extends DashboardPortlet {

  @DataBoundConstructor
  public TestsListPortlet(String name) {
    super(name);
  }
  
  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {      
      return "Tests radiator view";
    }    
  }
  
  @SuppressWarnings("rawtypes")
  public TestRunResults getJobTestLastRunResults(TopLevelItem item) {    
    if (item instanceof Job) {        
      Job job = (Job) item;
      Run run = job.getLastCompletedBuild();
      if (run != null) {
        return TestUtils.getTestResult(run);
      }
    }
    return TestRunResults.EMPTY_RESULT;
  }
 
  @SuppressWarnings("rawtypes")
  public Collection<TestRunResults> getAllTest(Collection<TopLevelItem> allJobs) {
    ArrayList<TestRunResults> tests = new ArrayList<TestRunResults>();
    
    for(TopLevelItem item : allJobs) {
      if (item instanceof Job) {        
        Job job = (Job) item;
        Run run = job.getLastCompletedBuild();
        if (run!=null) {
          tests.add(TestUtils.getTestResult(run));
        }
      }      
    }
    
    return tests;    
  }
  
}
