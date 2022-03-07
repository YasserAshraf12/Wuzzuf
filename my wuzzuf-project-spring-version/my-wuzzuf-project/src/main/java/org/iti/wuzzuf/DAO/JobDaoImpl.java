package org.iti.wuzzuf.DAO;

import org.apache.spark.sql.*;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class JobDaoImpl implements JobDao{

    private final String filePath = "C:\\Users\\Top\\Desktop\\Wuzzuf_JavaML\\my wuzzuf-project-spring-version\\my-wuzzuf-project\\src\\main\\resources\\static\\Wuzzuf_Jobs.csv";

    private Dataset<Row> data = null;

    private SparkSession sparkSession = SparkSession.builder().appName ("Wuzzuf Jobs Demo").master("local[5]").getOrCreate();

    public JobDaoImpl(){
        DataFrameReader dataFrameReader = sparkSession.read();
        dataFrameReader.option("header", true);
        this.data = dataFrameReader.csv(this.filePath);
    }

    @Override
    public List<Job> readJobs() {

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
    public List<Summary> getDataSummary() {
        List<Summary> summaries = data.describe().as(Encoders.bean(Summary.class)).collectAsList();
        return summaries;
    }

    @Override
    public void showStructure() {
        data.printSchema();
    }

    @Override
    public List<Job> printDataTabular() {
        List<Job> df = data.as(Encoders.bean(Job.class)).collectAsList();
        return df;
    }

    @Override
    public List<Job> dropNullValues() {
        return data.na().drop("any").as(Encoders.bean(Job.class)).collectAsList();
    }

    @Override
    public List<Job> dropDuplicates() {
        return data.dropDuplicates().as(Encoders.bean(Job.class)).collectAsList();
    }

    @Override
    public List<Group> countJobsForCompany() {

        data.createOrReplaceTempView ("Jobs_Data");
        Dataset<Row> df =  sparkSession.sql("select Company as company, count(*) as frequency from Jobs_Data group by company order by frequency desc");

        System.out.println(df.count());
        df.show((int)df.count());

        List<Group> groups = new ArrayList<>();

        Iterator<Row> it = df.toLocalIterator();
        while (it.hasNext())
        {
            Row g = it.next();
            groups.add(new Group(g.getString(0), g.getLong(1)));
        }
        return groups;
    }

    @Override
    public void piePlot() {
        PieChart chart = new PieChartBuilder().width(800).height(600).title("Pie Chart").build();

        data.createOrReplaceTempView ("Jobs_Data");

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
    public void getMostPopularTitles() {

        data.createOrReplaceTempView ("Jobs_Data");
        sparkSession.sql("select Title, count(*) as Number_of_title from Jobs_Data group by Title order by Number_of_title desc").show(10);
    }

    @Override
    public void barPlot() {

        CategoryChart chart = new CategoryChartBuilder().width(1700).height(800).title("Histogram").xAxisTitle("Title").yAxisTitle("Frequency").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setYAxisMin(0.0);

        data.createOrReplaceTempView ("Jobs_Data");

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

    @Override
    public void getMostPopularAreas() {
        data.createOrReplaceTempView ("Jobs_Data");
        sparkSession.sql("select Location, count(*) as Number_of_area from Jobs_Data group by Location order by Number_of_area desc").show(10);
    }

    @Override
    public void barPlotAreas() {
        CategoryChart chart = new CategoryChartBuilder().width(1700).height(800).title("Histogram").xAxisTitle("Location").yAxisTitle("Frequency").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setYAxisMin(0.0);

        data.createOrReplaceTempView ("Jobs_Data");

        Dataset<Row> dt= sparkSession.sql("select cast(Location as string), cast(count(*) as int) as Number_of_area from Jobs_Data " +
                "group by Location order by Number_of_area desc limit 10");

        List<Row> temp_locations =dt.select("Location").collectAsList();
        List<Row> temp_counts =dt.select("Number_of_area").collectAsList();

        List<String> locations = new ArrayList<>();
        List<Integer> frequency = new ArrayList<>();

        for (int i = 0; i < temp_locations.size(); i++) {
            locations.add(temp_locations.get(i).getString(0));
            frequency.add(temp_counts.get(i).getInt(0));
        }

        chart.addSeries("Locations", locations, frequency);

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    @Override
    public void mostRequiredSkill() {

        data.createOrReplaceTempView ("Jobs_Data");
        List<Row> dt = sparkSession.sql("select Skills from Jobs_Data").collectAsList();

        List<String> skills = new ArrayList<>();

        for(Row row : dt){
            String [] skills_tmp = String.valueOf(row).split(", ");
            for (int i = 0; i < skills_tmp.length; i++){
                skills.add(skills_tmp[i]);
            }
        }

        Dataset<String> df_skills = sparkSession.createDataset(skills, Encoders.STRING());

        df_skills.createOrReplaceTempView ("Jobs_Skills");
        Dataset<Row> result = sparkSession.sql("select value, count(*) as Number_of_Skills from Jobs_Skills group by value order by Number_of_Skills desc");

        result.show((int) result.count());
    }
}
