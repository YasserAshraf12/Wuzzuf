package org.iti.wuzzuf.DAO;

import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;

import java.util.List;

public interface JobDao {

    List<Job> readJobs();
    List<Summary> getDataSummary();
    String [] showStructure();
    List<Job> printDataTabular();

    List<Job> dropNullValues();
    List<Job> dropDuplicates();
    List<Group> countJobsForCompany();
    void piePlot();
    void getMostPopularTitles();
    void barPlot();
    void getMostPopularAreas();
    void barPlotAreas();
    void mostRequiredSkill();

}
