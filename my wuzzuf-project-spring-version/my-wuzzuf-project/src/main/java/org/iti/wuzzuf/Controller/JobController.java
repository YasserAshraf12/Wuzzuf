package org.iti.wuzzuf.Controller;

import org.iti.wuzzuf.DAO.JobDaoImpl;
import org.iti.wuzzuf.POJO.Group;
import org.iti.wuzzuf.POJO.Job;
import org.iti.wuzzuf.POJO.Summary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/job")
public class JobController {

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

    @GetMapping("countJobs")
    public List<Group> countJobsForCompany()
    {
        return new JobDaoImpl().countJobsForCompany();
    }

}
