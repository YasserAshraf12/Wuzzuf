package org.iti.wuzzuf.DAO;

import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;

import java.io.IOException;
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
    String [] showStructure();
    void dropNullValues();
    void dropDuplicates();
    void piePlot() throws IOException;
    void barPlot() throws IOException;
    void barPlotAreas() throws IOException;



}
