package org.iti.wuzzuf.Controller;

import org.apache.log4j.lf5.util.StreamUtils;
import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.iti.wuzzuf.POJO.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
public class JobController {

    @Autowired
    private  JobDaoImpl JobService;

    @GetMapping({"/", "/job"})
    public String welcome() {
        StringBuilder html = new StringBuilder();
        html.append("<h1 text-align='center'> welcome </h1>");
        return html.toString();
    }

    @GetMapping("/job/readJobs")
    public String readJobs(){
        return JobService.readJobs();
    }

    @GetMapping("/job/summary")
    public String getDataSummary(){
        return JobService.getDataSummary();
    }

    @GetMapping("/job/structure")
    public String showStructure(){
        return JobService.showStructure();
    }

    @GetMapping("/job/print")
    public String printTabular(){
        return  JobService.printDataTabular();
    }

    @GetMapping("/job/filter")
    public String filterData() { return JobService.filterData(); }

    @GetMapping("/job/countCompaney")
    public String countJobsForCompany()
    {
        return JobService.countJobsForCompany();
    }

    @GetMapping(value = "/job/piechartcompany",  produces = IMAGE_JPEG_VALUE)
    public void PieChartCompanies(HttpServletResponse response) throws IOException {

        JobService.piePlot();
        ClassPathResource imgFile = new ClassPathResource("PieChartCompanies.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping(value = "/job/barplotjob",  produces = IMAGE_JPEG_VALUE)
    public void BarPlotJobs(HttpServletResponse response) throws IOException {

        JobService.barPlot();
        ClassPathResource imgFile = new ClassPathResource("BarPlotJobs.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping(value = "/job/barplotarea",  produces = IMAGE_JPEG_VALUE)
    public void BarPlotAreas(HttpServletResponse response) throws IOException {

        JobService.barPlotAreas();
        ClassPathResource imgFile = new ClassPathResource("BarPlotAreas.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping("/job/countTitles")
    public String getMostPopularTitles() { return JobService.getMostPopularTitles(); }

    @GetMapping("/job/countAreas")
    public String getMostPopularAreas() { return JobService.getMostPopularAreas(); }

    @GetMapping("/job/countSkills")
    public String mostRequiredSkill() { return JobService.mostRequiredSkill(); }

    @GetMapping("/job/yearexp")
    public String Factorize_year() {  return JobService.Factorize_column(); }





}
