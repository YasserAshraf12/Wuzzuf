package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.*;
import org.iti.wuzzuf.POJO.Job;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao{

    final SparkSession sparkSession = SparkSession.builder().appName("Wuzzuf Spark Demo").master("local[3]")
            .getOrCreate();

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

        data.createOrReplaceTempView ("Jobs_Data");

        SQLContext sqlContext = sparkSession.sqlContext();
        sqlContext.sql("select Company, count(*) as Number_Of_Jobs from Jobs_Data group by Company order by Number_Of_Jobs desc").show(10);
    }

    @Override
    public void piePlot(Dataset<Row> data) {
        PieChart chart = new PieChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

        data.createOrReplaceTempView ("Jobs_Data");

        SQLContext sqlContext = sparkSession.sqlContext();
        Dataset<Row> dt = sparkSession.sql("select Company, count(*) as Number_of_jobs from Jobs_Data " +
                "group by Company order by Number_of_jobs desc limit 5");


        dt.foreach(row -> { chart.addSeries((String) row.get(0), (int)row.get(1)); });

        new SwingWrapper<PieChart>(chart).displayChart();
    }


}
