package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.model.Iteration;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class IterationController {

    @Autowired
    private IterationService iterationService;

    @PostMapping("/projects/{projectId}/iterations")
    public Result createIteration(@RequestBody Iteration iteration, @PathVariable Integer projectId) {
        log.info("Create iteration");
        iteration.setProjectId(projectId);
        return Result.success(iterationService.createIteration(iteration));
    }

    @PutMapping("/iteration")
    public Result updateIteration(@RequestBody Iteration iteration) {
        log.info("Update iteration");
        return Result.success(iterationService.updateIteration(iteration));
    }

    @DeleteMapping("/projects/{projectId}/iterations")
    public Result deleteIteration(@PathVariable Integer projectId) {
        log.info("Delete iteration");
        return Result.success(iterationService.deleteIteration(projectId));
    }

    @GetMapping("/projects/{projectId}/iterations")
    public Result getIterations(@PathVariable Integer projectId, @RequestParam Short status) {
        log.info("Get iteration");
        return Result.success(iterationService.getIterations(projectId, status));
    }

    @GetMapping("/projects/{projectId}/folders")
    public Result getFolders(@PathVariable Integer projectId) {
        return Result.success(iterationService.getFolders(projectId));
    }
}
