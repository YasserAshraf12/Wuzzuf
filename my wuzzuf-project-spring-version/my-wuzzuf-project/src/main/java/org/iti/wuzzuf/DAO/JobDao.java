package org.iti.wuzzuf.DAO;

import org.iti.wuzzuf.POJO.Group;

import java.io.IOException;
import java.util.List;

public interface JobDao {

    String readJobs();
    String printDataTabular();
    String filterData();
    String countJobsForCompany();
    List<Group> getMostPopularTitles();
    List<Group> getMostPopularAreas();
    List<Group> mostRequiredSkill();
    String getDataSummary();
    String [] showStructure();
    void dropNullValues();
    void dropDuplicates();
    void piePlot() throws IOException;
    void barPlot() throws IOException;
    void barPlotAreas() throws IOException;



}
