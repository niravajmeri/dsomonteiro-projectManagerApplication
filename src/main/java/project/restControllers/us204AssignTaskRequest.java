package project.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.User;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user/{userId}/projects/{projectId}/tasks/{taskId}")
public class us204AssignTaskRequest {

    private UserService userService;
    private TaskService taskService;
    private ProjectService projectService;


    @Autowired
    public us204AssignTaskRequest(UserService userService, TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
    }


    @RequestMapping(value = "/CreateAssignmentRequest" , method = RequestMethod.POST)
    public ResponseEntity<?> createRequestAddCollabToTask (@PathVariable String taskId, @PathVariable int projectId, @PathVariable int userId,HttpServletRequest request){
        ProjectCollaborator projectCollaborator;

        Project project = projectService.getProjectById(projectId);

        Task task = taskService.getTaskByTaskID(taskId);

        User user = userService.getUserById(userId);

        if(this.projectService.isUserActiveInProject(user, project)){
            projectCollaborator = this.projectService.findActiveProjectCollaborator(user, project);
            task.createTaskAssignementRequest(projectCollaborator);
            taskService.saveTask(task);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
