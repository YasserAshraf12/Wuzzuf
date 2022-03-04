package org.iti.wuzzuf;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.iti.wuzzuf.DAO.JobDao;
import org.iti.wuzzuf.DAO.JobDaoImpl;

public class App {


    public static void main(String[] args) {

        JobDao jobDao = new JobDaoImpl();
        String fileName = "C:\\Users\\Top\\Desktop\\Wuzzuf_JavaML\\my-wuzzuf-project\\src\\main\\resources\\Wuzzuf_Jobs.csv";
        //List<Job> jobs = jobDao.readJobs(fileName);

        /*for(Job j : jobs){
            System.out.println(j.toString());
        }*/

        Dataset<Row> df =  jobDao.readCSVFileSpark(fileName);
        jobDao.countJobsForCompany(df);

    }
}