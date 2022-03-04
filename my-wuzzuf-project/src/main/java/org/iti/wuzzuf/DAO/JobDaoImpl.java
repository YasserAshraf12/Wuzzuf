package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.iti.wuzzuf.POJO.Job;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao{

    @Override
    public List<Job> readJobs(String filePath) {

        List<Job> jobs = new ArrayList<>();

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            if (line == null) {
                System.out.println("File is Empty!");
                System.exit(0);
            }

            do{
                line = br.readLine();
                String [] lines = line.split(",");

                Job job = new Job(lines);
                jobs.add(job);
            }while(line != null);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    @Override
    public Dataset<Row> readCSVFileSpark(String filePath) {
        final SparkSession sparkSession = SparkSession.builder().appName("Wuzzuf Spark Demo").master("local[3]")
        .getOrCreate();

        final DataFrameReader dataFrameReader = sparkSession.read ();

        dataFrameReader.option ("header", "true");
        final Dataset<Row> csvDataFrame = dataFrameReader.csv (filePath);

        return csvDataFrame;
    }

    @Override
    public void getDataSummary(Dataset<Row> data) {
        data.describe().show();
    }

    @Override
    public void showStructure(Dataset<Row> data) {
        data.printSchema();
    }

    @Override
    public void printDataTabular(Dataset<Row> data, int n) {
        data.show(10);
    }

    @Override
    public Dataset<Row> dropNullValues(Dataset<Row> data) {
        return data.na().drop("any");
    }

    @Override
    public Dataset<Row> dropDuplicates(Dataset<Row> data) {
        return data.dropDuplicates();
    }

    @Override
    public void countJobsForCompany(Dataset<Row> data) {
        data.groupBy("Company").count().foreach(x -> System.out.println(x));
    }
}
