package org.iti.wuzzuf;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.iti.wuzzuf.DAO.JobDao;
import org.iti.wuzzuf.DAO.JobDaoImpl;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;


public class App {


    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        JobDao jobDao = new JobDaoImpl();
        String fileName = "C:\\Users\\Top\\Desktop\\Wuzzuf_JavaML\\my-wuzzuf-project\\src\\main\\resources\\Wuzzuf_Jobs.csv";

        Dataset<Row> df =  jobDao.readCSVFileSpark(fileName);
        //jobDao.countJobsForCompany(df);

        //jobDao.piePlot(df);

        //jobDao.getMostPopularTitles(df);
        //jobDao.barPlot(df);
        //jobDao.getMostPopularAreas(df);
        //jobDao.barPlotAreas(df);
        //jobDao.mostRequiredSkill(df);


    }
}