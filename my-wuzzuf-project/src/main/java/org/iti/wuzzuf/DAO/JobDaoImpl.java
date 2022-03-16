package org.iti.wuzzuf.DAO;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.*;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.regexp_replace;

@Service
public class JobDaoImpl implements JobDao{

    private final String filePath = "C:\\Users\\Top\\Desktop\\my-wuzzuf-project\\src\\main\\resources\\dataset\\Wuzzuf_Jobs.csv";

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
        return data.describe().as(Encoders.bean(Summary.class)).collectAsList();
    }

    @Override
    public List<String> showStructure() {
        String[] data_structure =  data.schema().treeString().split("\\|--");
        return Arrays.asList(data_structure);
    }

    @Override
    public String printDataTabular() {
        List<Job> df = data.as(Encoders.bean(Job.class)).collectAsList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<style>table, th, td {\n" +
                "  border: 1px solid black; " +
                "text-align: center; \n" +
                "}" +
                ".head{ background-color: black; color: white; }</style><h1>Welcome</h1></hr><table><tr>");
        String[] colnames = data.columns();

        for(String name : colnames){
            stringBuilder.append("<th class='head'>" + name + "</th>");
        }
        stringBuilder.append("</tr>");


        for(Job job : df)
        {
            stringBuilder.append("<tr><td>" + job.getTitle() + "</td>");
            stringBuilder.append("<td>" + job.getCompany() + "</td>");
            stringBuilder.append("<td>" + job.getLocation() + "</td>");
            stringBuilder.append("<td>" + job.getType() + "</td>");
            stringBuilder.append("<td>" + job.getLevel() + "</td>");
            stringBuilder.append("<td>" + job.getYearsExp() + "</td>");
            stringBuilder.append("<td>" + job.getCountry() + "</td>");
            stringBuilder.append("<td>" + job.getSkills() + "</td>");
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");

        return stringBuilder.toString();

    }

    @Override
    public void dropNullValues() {
        data =  data.na().drop("any");
    }

    @Override
    public void dropDuplicates() {
        data = data.dropDuplicates();
    }

    @Override
    public List<Job> filterData(){
        dropNullValues();
        dropDuplicates();
        return data.as(Encoders.bean(Job.class)).collectAsList();
    }

    @Override
    public List<Group> countJobsForCompany() {

        data.createOrReplaceTempView ("Jobs_Data");
        Dataset<Row> df =  sparkSession.sql("select Company as alias, count(*) as frequency from Jobs_Data group by alias order by frequency desc");

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
    public void piePlot() throws IOException {
        PieChart chart = new PieChartBuilder().width(1000).height(800).title("Pie Chart").build();
        data.createOrReplaceTempView ("Jobs_Data");

        Dataset<Row> dt= sparkSession.sql("select cast(Company as string), cast(count(*) as int) as Number_of_jobs from Jobs_Data " +
                "group by Company order by Number_of_jobs desc limit 10");


        List<Row> companies =dt.select("Company").collectAsList();
        List<Row> counts =dt.select("Number_of_jobs").collectAsList();

        for (int i = 0; i < companies.size(); i++) {
            chart.addSeries(companies.get(i).getString(0), counts.get(i).getInt(0));
        }

        String file_name="target/classes/PieChartCompanies.jpg";
        File f=new File(file_name);
        if(f.exists()) {
            f.delete();
        }
        BitmapEncoder.saveJPGWithQuality(chart, file_name, 0.95f);
        //new SwingWrapper<PieChart>(chart).displayChart();
    }

    @Override
    public List<Group> getMostPopularTitles() {

        data.createOrReplaceTempView ("Jobs_Data");
        Dataset<Row> df  = sparkSession.sql("select Title as alias, count(*) as frequency from Jobs_Data group by alias order by frequency desc");

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
    public void barPlot() throws IOException {

        CategoryChart chart = new CategoryChartBuilder().width(1000).height(800).title("Histogram").xAxisTitle("Title").yAxisTitle("Frequency").build();
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

        String file_name="target/classes/BarPlotJobs.jpg";
        File f=new File(file_name);
        if(f.exists()) {
            f.delete();
        }
        BitmapEncoder.saveJPGWithQuality(chart, file_name, 0.95f);
    }

    @Override
    public List<Group> getMostPopularAreas() {
        data.createOrReplaceTempView ("Jobs_Data");
        Dataset<Row> df = sparkSession.sql("select Location as alias, count(*) as frequency from Jobs_Data group by alias order by frequency desc");

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
    public void barPlotAreas() throws IOException {
        CategoryChart chart = new CategoryChartBuilder().width(1000).height(800).title("Histogram").xAxisTitle("Location").yAxisTitle("Frequency").build();
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

        String file_name="target/classes/BarPlotAreas.jpg";
        File f=new File(file_name);
        if(f.exists()) {
            f.delete();
        }
        BitmapEncoder.saveJPGWithQuality(chart, file_name, 0.95f);
    }

    @Override
    public void charts() throws IOException {
        piePlot();
        barPlot();
        barPlotAreas();
    }

    @Override
    public List<Group> mostRequiredSkill() {

        data.createOrReplaceTempView ("Jobs_Data");
        List<Row> dt = sparkSession.sql("select Skills from Jobs_Data").collectAsList();

        List<String> skills = new ArrayList<>();

        for(Row row : dt){
            String [] skills_tmp = String.valueOf(row).split(", ");
            for (int i = 0; i < skills_tmp.length; i++){
                skills_tmp[i] = skills_tmp[i].replace("[","").trim();
                skills_tmp[i] = skills_tmp[i].replace("]","").trim();
                skills.add(skills_tmp[i]);
            }
        }

        Dataset<String> df_skills = sparkSession.createDataset(skills, Encoders.STRING());

        df_skills.createOrReplaceTempView ("Jobs_Skills");
        Dataset<Row> df = sparkSession.sql("select value as alias, count(*) as frequency from Jobs_Skills group by alias order by frequency desc");

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
    public List<Job> Factorize_column() {
        data = data.withColumn("YearsExp", regexp_replace(col("YearsExp"), " Yrs of Exp", ""));
        data = data.withColumn("YearsExp", regexp_replace(col("YearsExp"), "null", "0"));

        StringIndexer indexer = new StringIndexer().setStringOrderType("frequencyAsc")
                .setInputCol("YearsExp")
                .setOutputCol("YearsExp_index");


        data = indexer.fit(data).transform(data);
        data = data.drop("YearsExp");
        data = data.withColumnRenamed("YearsExp_index", "YearsExp");
        Dataset<Row> data2 = data.select("Title", "Company", "Location", "Type", "Level", "YearsExp", "Country", "Skills");
        List<Row> df = data2.collectAsList();
        List<Job> jobs = new ArrayList<>();


        for (Row row : df) {
            Job j = new Job();
            j.setTitle(String.valueOf(row.get(0)));
            j.setCompany(String.valueOf(row.get(1)));
            j.setLocation(String.valueOf(row.get(2)));
            j.setType(String.valueOf(row.get(3)));
            j.setLevel(String.valueOf(row.get(4)));
            j.setYearsExp(String.valueOf(row.get(5)));
            j.setCountry(String.valueOf(row.get(6)));
            j.setSkills(String.valueOf(row.get(7)));
            jobs.add(j);
        }
        return jobs;
    }
}
