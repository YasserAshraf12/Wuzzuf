package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.iti.wuzzuf.POJO.Job;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobDao {

    ResponseEntity<List<Job>> readJobs();

    Dataset<Row> readCSVFileSpark(String filePath);
    Dataset<Row> getDataSummary();
    void showStructure(Dataset<Row> data);
    void printDataTabular(Dataset<Row> data, int n);
    Dataset<Row> dropNullValues(Dataset<Row> data);
    Dataset<Row> dropDuplicates(Dataset<Row> data);
    void countJobsForCompany(Dataset<Row> data);
    void piePlot(Dataset<Row> data);
    void getMostPopularTitles(Dataset<Row> data);
    void barPlot(Dataset<Row> data);
    void getMostPopularAreas(Dataset<Row> data);
    void barPlotAreas(Dataset<Row> data);
    void mostRequiredSkill(Dataset<Row> data);
}
