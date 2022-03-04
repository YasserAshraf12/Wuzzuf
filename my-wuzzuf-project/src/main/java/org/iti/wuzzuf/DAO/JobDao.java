package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.iti.wuzzuf.POJO.Job;

import java.util.List;

public interface JobDao {

    List<Job> readJobs(String filePath);
    Dataset<Row> readCSVFileSpark(String filePath);
    void getDataSummary(Dataset<Row> data);
    void showStructure(Dataset<Row> data);
    void printDataTabular(Dataset<Row> data, int n);
    Dataset<Row> dropNullValues(Dataset<Row> data);
    Dataset<Row> dropDuplicates(Dataset<Row> data);
    void countJobsForCompany(Dataset<Row> data);

}
