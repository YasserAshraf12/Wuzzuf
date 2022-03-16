package org.iti.wuzzuf.Controller;

import org.apache.log4j.lf5.util.StreamUtils;
import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@Controller
public class JobController {

    @Autowired
    private  JobDaoImpl JobService = new JobDaoImpl();

    @GetMapping({"/", "/job"})
    public String welcome() {
        return "index";
    }

    @GetMapping("/job/readJobs")
    public String readJobs(Model model){
        model.addAttribute("jobs", JobService.readJobs());
        return "readJobs";
    }

    @GetMapping("/job/summary")
    public String getDataSummary(Model model){
        model.addAttribute("summaries",JobService.getDataSummary());
        return "summary";
    }

    @GetMapping("/job/structure")
    public String showStructure(Model model){
        model.addAttribute("structures", JobService.showStructure());
        return "structure";
    }

    @GetMapping("/job/print")
    public String printTabular(){
        return  JobService.printDataTabular();
    }

    @GetMapping("/job/filter")
    public String filterData(Model model) {
        model.addAttribute("jobs", JobService.filterData());
        return "filter";
    }

    @GetMapping("/job/countCompany")
    public String countJobsForCompany(Model model)
    {
        model.addAttribute("counts", JobService.countJobsForCompany());
        return "countCompany";
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
    public String getMostPopularTitles(Model model) {
        model.addAttribute("counts", JobService.getMostPopularTitles());
        return "countTitles";
    }

    @GetMapping("/job/countAreas")
    public String getMostPopularAreas(Model model) {
        model.addAttribute("counts", JobService.getMostPopularAreas());
        return "countAreas";
    }

    @GetMapping("/job/countSkills")
    public String mostRequiredSkill(Model model) {
        model.addAttribute("counts", JobService.mostRequiredSkill());
        return "countSkills";
    }

    @GetMapping("/job/charts")
    public String charts() throws IOException {
        JobService.charts();
        return "charts";
    }
    @GetMapping("/job/factorize")
    public String factorize(Model model)
    {
        model.addAttribute("jobs", JobService.Factorize_column());
        return "factorize";
    }

}
