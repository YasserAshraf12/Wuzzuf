package org.iti.wuzzuf.Controller;

import org.apache.log4j.lf5.util.StreamUtils;
import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;


@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private  JobDaoImpl JobService;

    @GetMapping({"/", "/job"})
    public String welcome(Map<String, Object> model) {
        model.put("message", "Hello World");
        return "welcome";
    }

    @GetMapping("/readJobs")
    public List<Job> readJobs(){
        return JobService.readJobs();
    }

    @GetMapping("/summary")
    public List<Summary> getDataSummary(){
        return JobService.getDataSummary();
    }

    @GetMapping("/structure")
    public String [] showStructure(){
        return JobService.showStructure();
    }

    @GetMapping("/print")
    public List<Job> printTabular(){
        return  JobService.printDataTabular();
    }

    @GetMapping("/filter")
    public List<Job> filterData() { return JobService.filterData(); }

    @GetMapping("/countJobs")
    public List<Group> countJobsForCompany()
    {
        return JobService.countJobsForCompany();
    }

    @GetMapping(value = "/piechartcompany",  produces = IMAGE_JPEG_VALUE)
    public void PieChartCompanies(HttpServletResponse response) throws IOException {

        JobService.piePlot();
        ClassPathResource imgFile = new ClassPathResource("PieChartCompanies.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping(value = "/barplotjob",  produces = IMAGE_JPEG_VALUE)
    public void BarPlotJobs(HttpServletResponse response) throws IOException {

        JobService.barPlot();
        ClassPathResource imgFile = new ClassPathResource("BarPlotJobs.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping(value = "/barplotarea",  produces = IMAGE_JPEG_VALUE)
    public void BarPlotAreas(HttpServletResponse response) throws IOException {

        JobService.barPlotAreas();
        ClassPathResource imgFile = new ClassPathResource("BarPlotAreas.jpg");

        response.setContentType(IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @GetMapping("/countTitles")
    public List<Group> getMostPopularTitles() { return JobService.getMostPopularTitles(); }

    @GetMapping("/countAreas")
    public List<Group> getMostPopularAreas() { return JobService.getMostPopularAreas(); }

    @GetMapping("/countSkills")
    public List<Group> mostRequiredSkill() { return JobService.mostRequiredSkill(); }

}
