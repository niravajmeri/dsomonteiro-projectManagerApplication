package project.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.dto.TaskAction;
import project.model.Task;
import project.model.User;
import project.services.TaskService;
import project.services.UserService;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/users/{userId}/tasks/")
public class RestUserTasksController {

    UserService userService;
    TaskService taskService;

    @Autowired
    public RestUserTasksController(TaskService taskService, HttpServletRequest request, UserService userService) {

        this.userService = userService;
        this.taskService = taskService;
    }
    /**
     * This method returns all the tasks of a given user. for each task found a specific link is
     * added with the possible actions and the details of the task itself
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id== #userId or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable Integer userId) {

        User user = userService.getUserByID(userId);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Task> taskFromUser = taskService.getUserTasks(user);

        for(Task task: taskFromUser) {


                for (String action : task.getTaskState().getActions()) {

                    Link taskLinkn = TaskAction.getLinks(task.getProject().getProjectId(), task.getTaskID()).get(action);
                    task.add(taskLinkn);
                }



        }

        return ResponseEntity.ok().body(taskFromUser);
    }

    /**
     * This method returns the result obtained from the sum of the working times of a
     * given user in his tasks finished in the last month
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "totaltimespent/completed/lastmonth", method = RequestMethod.GET)
    public ResponseEntity<Double> getTotalTimeSpentOnTasksCompletedLastMonth(@PathVariable Integer userId) {

        User user = userService.getUserByID(userId);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Double totalTime = taskService.getTotalTimeOfFinishedTasksFromUserLastMonth(user);

        return ResponseEntity.ok().body(totalTime);

    }

    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id == #userId or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "finished", method = RequestMethod.GET)
    public List<Task> getUserFinishedTasks(@PathVariable Integer userId) {
        List<Task> taskList = taskService.getAllFinishedUserTasksInDecreasingOrder(this.userService.getUserByID(userId));
        for(Task task: taskList) {
            Link reference = linkTo(getClass(), userId).slash("task").slash(task.getDbTaskId()).withSelfRel();
            task.add(reference);
        }
        return taskList;
    }


    /**
     * THis method returns user tasks finished previous month
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id == #userId or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "lastmonthfinished", method = RequestMethod.GET)
    public ResponseEntity<List<Task>>  getLastMonthFinishedTasks (@PathVariable Integer userId) {

        List<Task> taskList = taskService.getFinishedUserTasksFromLastMonthInDecreasingOrder(this.userService.getUserByID(userId));

        ResponseEntity <List<Task>> response;

        response  = new ResponseEntity<>(taskList, HttpStatus.OK);

        return response;
    }

    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id == #userId or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "pending", method = RequestMethod.GET)
    public List<Task> getPendingTasks(@PathVariable Integer userId) {

        User user = userService.getUserByID(userId);


        List<Task> taskList = taskService.getStartedNotFinishedUserTaskList(user);
        List<Task> userTasks = new ArrayList<>();

        for(Task task: taskList) {
            if (task.getActiveTaskCollaboratorByEmail(user.getEmail()) != null) {

                Link reference = linkTo(getClass(), userId).slash("task").slash(task.getDbTaskId()).withSelfRel();
                task.add(reference);
                userTasks.add(task);

            }
        }

        return userTasks;
    }



    /**
     * This method returns the unfinished tasks of a given user sorted by deadline
     *
     * @param userId The User ID to search it's tasks
     * @return Sorted pending task list by deadline
     */

    @PreAuthorize("hasRole('ROLE_COLLABORATOR') and principal.id == #userId or hasRole('ROLE_DIRECTOR') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "sortedbydeadline", method = RequestMethod.GET)
    public List<Task> getSortedTaskListByDeadline(@PathVariable Integer userId) {

        User user = userService.getUserByID(userId);

        List<Task> ongoingTaskList = taskService.getStartedNotFinishedUserTaskList(user);

        List<Task> userOngoingTasks = new ArrayList<>();


        for(Task task: ongoingTaskList) {
            if (task.getActiveTaskCollaboratorByEmail(user.getEmail()) != null) {

                Link reference = linkTo(getClass(), userId).slash("task").slash(task.getDbTaskId()).withSelfRel();

                task.add(reference);

                userOngoingTasks.add(task);

            }
        }

        userOngoingTasks = taskService.sortTaskListByDeadline(userOngoingTasks);

        return userOngoingTasks;
    }



}