package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.*;
import org.iti.wuzzuf.POJO.Job;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

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
        PieChart chart = new PieChartBuilder().width(800).height(600).title("Pie Chart").build();

        data.createOrReplaceTempView ("Jobs_Data");

        SQLContext sqlContext = sparkSession.sqlContext();
        Dataset<Row> dt= sparkSession.sql("select cast(Company as string), cast(count(*) as int) as Number_of_jobs from Jobs_Data " +
                "group by Company order by Number_of_jobs desc limit 10");


        List<Row> companies =dt.select("Company").collectAsList();
        List<Row> counts =dt.select("Number_of_jobs").collectAsList();

        for (int i = 0; i < companies.size(); i++) {
            chart.addSeries(companies.get(i).getString(0), counts.get(i).getInt(0));
        }

        new SwingWrapper<PieChart>(chart).displayChart();
    }

    @Override
    public void getMostPopularTitles(Dataset<Row> data) {
        data.createOrReplaceTempView ("Jobs_Data");
        SQLContext sqlContext = sparkSession.sqlContext();
        sqlContext.sql("select Title, count(*) as Number_of_title from Jobs_Data group by Title order by Number_of_title desc").show(10);
    }

    @Override
    public void barPlot(Dataset<Row> data) {

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Histogram").xAxisTitle("Title").yAxisTitle("Frequency").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);

        data.createOrReplaceTempView ("Jobs_Data");

        SQLContext sqlContext = sparkSession.sqlContext();
        Dataset<Row> dt= sparkSession.sql("select cast(Title as string), cast(count(*) as int) as Number_of_title from Jobs_Data " +
                "group by Title order by Number_of_title desc limit 10");

        List<Row> temp_titles =dt.select("Title").collectAsList();
        List<Row> temp_counts =dt.select("Number_of_title").collectAsList();

        List<String> titles = new ArrayList<>();
        List<Integer> frequency = new ArrayList<>();

        for (int i = 0; i < temp_titles.size(); i++) {
            titles.add(temp_titles.get(i).getString(0));
            frequency.add(temp_counts.get(i).getInt(0));
        }

        chart.addSeries("Titles", titles, frequency);

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }


}
