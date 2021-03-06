package project.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.User;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController


@RequestMapping("/projects/")
public class RestProjectController  {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;
    private static final String PROJECT_DETAILS_REL = "seeProjectDetails";

    @Autowired
    public RestProjectController(ProjectService projectService, UserService userService,  TaskService taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }

    /**
     * This method returns a ResponseEntity that contains all the active projects from the project service with a link to open each project
     * @return ResponseEntity with all the active projects
     */
    @PreAuthorize("hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<List<Project>> getActiveProjects() {

        List<Project> activeProjects = this.projectService.getActiveProjects();
        for (Project project : activeProjects) {
            Link selfLink = linkTo(methodOn(RestProjectController.class).getActiveProjects()).withSelfRel().withType(RequestMethod.GET.name());
            project.add(selfLink);
        }
        return ResponseEntity.ok().body(activeProjects);
    }


    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and #userId == principal.id or hasRole('ROLE_ADMIN') ")
    @RequestMapping(value= "{userId}/myProjects", method = RequestMethod.GET)
    public ResponseEntity<List<Project>> getUserProjects(@PathVariable int userId) {

        List<Project> userProjects = this.projectService.getProjectsFromUser(userService.getUserByID(userId));

        for (Project project : userProjects) {
            Link selfLink = linkTo(methodOn(RestProjectController.class).getUserProjects(userId)).withSelfRel().withType(RequestMethod.GET.name());
            project.add(selfLink);
        }

        for (Project project : userProjects) {
        Link projectDetailsLink = linkTo(methodOn(RestProjectController.class).getProjectDetails(project.getProjectId())).withRel("projectDetails").withType(RequestMethod.GET.name());
        project.add(projectDetailsLink);
    }
        return ResponseEntity.ok().body(userProjects);
    }

    /**
     * This method returns a ResponseEntity that contains the project details
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and @projectService.isUserActiveInProject(@userService.getUserByID(principal.id),@projectService.getProjectById(#projectId)) " +
            "or hasRole('ROLE_COLLABORATOR') and principal.id==@projectService.getProjectById(#projectId).projectManager.userID " +
            "or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value= "{projectId}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProjectDetails(@PathVariable int projectId) {

        Project project = this.projectService.getProjectById(projectId);
        Link selfRef = linkTo(methodOn(RestProjectController.class).getProjectDetails(project.getProjectId())).withSelfRel().withType(RequestMethod.GET.name());
        project.add(selfRef);
        Link updateProjectLink = linkTo(methodOn(RestProjectController.class).updateProject(project, projectId)).withRel("updateProject").withType(RequestMethod.PATCH.name());
        project.add(updateProjectLink);
        Link calculateCostLink = linkTo(methodOn(RestProjectController.class).getProjectCost(project.getProjectId())).withRel("calculateCost").withType(RequestMethod.GET.name());
        project.add(calculateCostLink);


        return ResponseEntity.ok().body(project);
    }

    /**
     * This method updates the params of the project (such manager and cost calculation method)
     *
     * @param projectUpdates
     * @param projectId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id==@projectService.getProjectById(#projectId).projectManager.userID " +
            "or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "{projectId}" , method = RequestMethod.PATCH)
    public ResponseEntity<Project> updateProject(@RequestBody Project projectUpdates, @PathVariable int projectId){
        Project project = projectService.getProjectById(projectId);
        projectService.updateProjectData(projectUpdates, project);
        if(projectUpdates.getAvailableCalculationMethods()!=null || projectUpdates.getCalculationMethod()!=null) {
            taskService.calculateReportEffortCost(project);
        }

        Link reference = linkTo(methodOn(RestProjectController.class).getProjectDetails(project.getProjectId())).withRel(PROJECT_DETAILS_REL).withType(RequestMethod.GET.name());
        project.add(reference);

        return ResponseEntity.ok().body(project);
    }


    /**
     * This controller's method uses GET to get the cost of the project through the calculation method defined previously.
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id==@projectService.getProjectById(#projectId).projectManager.userID " +
            "or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{projectId}/cost", method = RequestMethod.GET)
    public ResponseEntity<Project> getProjectCost(@PathVariable int projectId) {
        Project project = this.projectService.getProjectById(projectId);

        project.setProjectCost(taskService.getTotalCostReportedToProjectUntilNow(project));

        Link reference = linkTo(methodOn(RestProjectController.class).getProjectDetails(project.getProjectId())).withRel(PROJECT_DETAILS_REL).withType(RequestMethod.GET.name());
        project.add(reference);

        return ResponseEntity.ok().body(project);
    }


    /**
     * Creates a Project with the parameters Name, Description and Project Manager if the response body only has this
     * info. In case the Response Body has an EffortUnit and/or a Budget, the project will be created with the all the
     * Response body information.
     */
    @PreAuthorize("hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "" , method = RequestMethod.POST)
    public ResponseEntity<Project> createProject(@RequestBody Project projectDTO) {
        User projectManager = userService.getUserByEmail(projectDTO.getProjectManager().getEmail());

        Project proj = projectService.createProject(projectDTO.getName(), projectDTO.getDescription(), projectManager);

        if (projectDTO.getEffortUnit() != null){
                proj.setEffortUnit(projectDTO.getEffortUnit());
            }

        if (projectDTO.getBudget() > 0) {
            proj.setProjectBudget(projectDTO.getBudget());
        }

        this.projectService.addProjectToProjectContainer(proj);

        Link reference = linkTo(methodOn(RestProjectController.class).getProjectDetails(proj.getProjectId())).withRel(PROJECT_DETAILS_REL).withType(RequestMethod.GET.name());
        proj.add(reference);
        Link updateProjectLink = linkTo(methodOn(RestProjectController.class).updateProject(proj, proj.getProjectId())).withRel("updateProject").withType(RequestMethod.PATCH.name());
        proj.add(updateProjectLink);
        Link calculateCostLink = linkTo(methodOn(RestProjectController.class).getProjectCost(proj.getProjectId())).withRel("calculateCost").withType(RequestMethod.GET.name());
        proj.add(calculateCostLink);

        return ResponseEntity.ok().body(proj);
    }
    /**
     * This method updates the params of the project (such manager and cost calculation method)
     *
     * @param projectId
     * @return List of all active task collaborators in a task from the project
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id==@projectService.getProjectById(#projectId).projectManager.userID " +
            "or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{projectId}/activeTeam", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectCollaborator>> getActiveProjectTeam(@PathVariable int projectId) {

        Project project = projectService.getProjectById(projectId);

        List <ProjectCollaborator> activeProjectTeam = projectService.getActiveProjectTeam(project);

        return ResponseEntity.ok().body(activeProjectTeam);
    }

}






