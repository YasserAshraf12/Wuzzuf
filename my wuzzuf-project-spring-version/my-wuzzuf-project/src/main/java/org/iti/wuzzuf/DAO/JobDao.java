package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;

import java.io.IOException;
import java.util.List;

public interface JobDao {

    List<Job> readJobs();
    List<Summary> getDataSummary();
    String [] showStructure();
    List<Job> printDataTabular();

    Dataset<Row> dropNullValues(Dataset<Row> data_set);
    Dataset<Row> dropDuplicates(Dataset<Row>data_set);

    List<Group> countJobsForCompany();

    void piePlot() throws IOException;
    void getMostPopularTitles();
    void barPlot() throws IOException;
    void getMostPopularAreas();
    void barPlotAreas() throws IOException;
    List<Group> mostRequiredSkill();

}
