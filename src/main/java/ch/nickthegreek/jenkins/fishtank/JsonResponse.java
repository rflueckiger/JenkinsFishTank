package ch.nickthegreek.jenkins.fishtank;

import java.util.List;

public class JsonResponse {

    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}

