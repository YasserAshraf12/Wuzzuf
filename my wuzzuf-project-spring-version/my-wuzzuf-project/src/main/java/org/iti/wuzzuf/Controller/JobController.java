package org.iti.wuzzuf.Controller;

import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    private  JobDaoImpl JobService;

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

    @GetMapping("/countJobs")
    public List<Group> countJobsForCompany()
    {
        return JobService.countJobsForCompany();
    }

}
