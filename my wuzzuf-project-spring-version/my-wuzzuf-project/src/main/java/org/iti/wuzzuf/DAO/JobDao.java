package org.iti.wuzzuf.DAO;

import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;

import java.util.List;

public interface JobDao {

    List<Job> readJobs();
    List<Job> printDataTabular();
    List<Job> filterData();
    List<Group> countJobsForCompany();
    List<Group> getMostPopularTitles();
    List<Group> getMostPopularAreas();
    List<Group> mostRequiredSkill();
    List<Summary> getDataSummary();
    void showStructure();
    void dropNullValues();
    void dropDuplicates();
    void barPlot();
    void piePlot();
    void barPlotAreas();
}
