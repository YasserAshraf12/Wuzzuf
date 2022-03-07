package org.iti.wuzzuf.Controller;

import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController

@RequestMapping("/job")
public class JobController {



    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", "Hello World");
        return "welcome";
    }

    @GetMapping("/readJobs")
    public List<Job> readJobs(){
        return new JobDaoImpl().readJobs();
    }

    @GetMapping("/summary")
    public List<Summary> getDataSummary(){
        return new JobDaoImpl().getDataSummary();
    }

    @GetMapping("/structure")
    public void showStructure(){
        new JobDaoImpl().showStructure();
    }

    @GetMapping("/print")
    public List<Job> printTabular(){
        return new JobDaoImpl().printDataTabular();
    }

    @GetMapping("/filter")
    public List<Job> filterData() { return new JobDaoImpl().filterData(); }

    @GetMapping("/countJobs")
    public List<Group> countJobsForCompany()
    {
        return new JobDaoImpl().countJobsForCompany();
    }

    @GetMapping("/countTitles")
    public List<Group> getMostPopularTitles() { return new JobDaoImpl().getMostPopularTitles(); }

    @GetMapping("/countAreas")
    public List<Group> getMostPopularAreas() { return new JobDaoImpl().getMostPopularAreas(); }

    @GetMapping("/countSkills")
    public List<Group> mostRequiredSkill() { return new JobDaoImpl().mostRequiredSkill(); }

}
