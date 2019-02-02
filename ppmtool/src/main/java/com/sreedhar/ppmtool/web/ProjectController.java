package com.sreedhar.ppmtool.web;

import com.sreedhar.ppmtool.domain.Project;
import com.sreedhar.ppmtool.services.MapValidationService;
import com.sreedhar.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final MapValidationService mapValidationService;

    @Autowired
    public ProjectController(ProjectService projectService, MapValidationService mapValidationService) {
        this.projectService = projectService;
        this.mapValidationService = mapValidationService;
    }

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationService.mapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }
        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectbyId(@PathVariable String projectId) {
        return new ResponseEntity<>(projectService.findProjectByIdentifier(projectId),HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> findAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        projectService.deleteProjectByIdentifier(projectId);
        return new ResponseEntity<String>("Project with ID:"  + projectId + " was deleted", HttpStatus.OK);
    }
}
