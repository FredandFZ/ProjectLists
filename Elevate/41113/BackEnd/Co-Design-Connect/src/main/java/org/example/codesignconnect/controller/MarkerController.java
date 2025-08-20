package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Marker;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markers")
public class MarkerController {
    @Autowired
    private MarkerService markerService;

    @GetMapping
    public Result getAllByProjectId(Integer projectId) {
        return Result.success(markerService.findAllByProjectId(projectId));
    }

    @PostMapping("/create")
    public Result create(@RequestBody Marker marker) {
        markerService.insert(marker);
        return Result.success(marker);
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Marker marker, @PathVariable Integer id) {
        marker.setId(id);
        markerService.update(marker);
        return Result.success();
    }

    @DeleteMapping("/{projectId}/{id}")
    public Result delete(@PathVariable Integer projectId, @PathVariable Integer id) {
        markerService.delete(id);
        return Result.success();
    }
}
