package project.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import project.dto.TaskDTO;
import project.model.*;
import project.model.costcalculationinterface.CostCalculationFactory;
import project.model.taskstateinterface.*;
import project.repository.ProjCollabRepository;
import project.repository.ProjectsRepository;
import project.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TaskServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private Task taskMock;

    @Mock
    private Task task2Mock;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectsRepository projectsRepository;

    @Mock
    private ProjCollabRepository projectCollaboratorRepository;

    @Mock
    private ProjectCollaborator projectCollaboratorMock;

    @Mock
    private TaskCollaborator taskCollaboratorMock;

    @Mock
    private ProjectService projectService;

    private Calendar startDate;
    private Project project;
    private User user;
    private User user2;
    private Task notAmock;
    private ProjectCollaborator projectCollaborator;
    private ProjectCollaborator projectCollaborator2;
    private ProjectCollaborator projectCollaborator3;
    private TaskCollaborator taskCollaborator;

    @InjectMocks
    private TaskService victim = new TaskService(taskRepository);

    private void mocksGetTaskRepository() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(any(Project.class))).thenReturn(taskList);
        when(taskRepository.findAll()).thenReturn(taskList);
    }

    private void mocksGetProjectTasks() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(any(Project.class))).thenReturn(taskList);
    }

    public void mocksGetAllFinishedTasksFromUser() {
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(taskRepository.findAll()).thenReturn(taskListMock);

        List<TaskCollaborator> listTaskCollaborator = new ArrayList<>();
        listTaskCollaborator.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(listTaskCollaborator);

        // when the current state is finished, the task is added to list
        when(taskMock.getCurrentState()).thenReturn(StateEnum.FINISHED);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
    }

    @Before
    public void setUp() {
        initMocks(this);

        this.projectService = new ProjectService(projectsRepository, projectCollaboratorRepository, userService);

        this.startDate = Calendar.getInstance();
        this.startDate.set(Calendar.YEAR, 2017);
        this.startDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
        this.startDate.set(Calendar.DAY_OF_MONTH, 25);
        this.startDate.set(Calendar.HOUR_OF_DAY, 14);

        this.user = new User("name", "email", "idNumber", "function", "phone");
        this.user2 = new User("Nuno", "nuno@emai.com", "1012", "tester", "987-23");
        this.project = projectService.createProject("testing", "Test", user);
        this.projectCollaborator = new ProjectCollaborator(user, 10);
        this.projectCollaborator2 = new ProjectCollaborator(user, 10);
        this.projectCollaborator3 = new ProjectCollaborator(user2, 20);
        this.taskCollaborator = new TaskCollaborator(new ProjectCollaborator(user, 10));

    }

    @After
    public void tearDown() {
        userService = null;
        taskMock = null;
        task2Mock = null;
        taskRepository = null;
        projectsRepository = null;
        projectCollaboratorRepository = null;
        projectCollaboratorMock = null;
        taskCollaboratorMock = null;
        projectService = null;
        startDate = null;
        project = null;
        user = null;
        user2 = null;
        notAmock = null;
        projectCollaborator = null;
        projectCollaborator2 = null;
        projectCollaborator3 = null;
        taskCollaborator = null;
    }

    /**
     * Tests the construction of an instance of Task
     */
    @Test
    public void testCreateTask() {
        when(taskRepository.save(taskMock)).thenReturn(taskMock);
        victim.createTask("description", project);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    /**
     * Tests the getFinishedTasksFromUser() method to verify if the tasks from a
     * certain user, marked as finished, are returned.
     */
    @Test
    public void testGetAllFinishedTasksFromUser() {

        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(taskRepository.findAll()).thenReturn(taskListMock);

        List<TaskCollaborator> listTaskCollaborator = new ArrayList<>();
        listTaskCollaborator.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(listTaskCollaborator);

        // WHEN
        // the current state is finished
        when(taskMock.getCurrentState()).thenReturn(StateEnum.FINISHED);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        // THEN
        // the task is added to list
        assertEquals(expectedTaskList, victim.getAllFinishedTasksFromUser(user));
    }

    /**
     * Tests the getFinishedTasksFromUser() method to verify if the tasks from a
     * certain user, not marked as finished, is not returned.
     */
    @Test
    public void shouldNotGetAllFinishedTasksFromUserWhenCurrentStateIsNotFInished() {
        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(taskRepository.findAll()).thenReturn(taskListMock);

        List<TaskCollaborator> listTaskCollaborator = new ArrayList<>();
        listTaskCollaborator.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(listTaskCollaborator);

        // WHEN
        // the current state isn't finished
        List<Task> expectedTaskList = new ArrayList<>();
        when(taskMock.getCurrentState()).thenReturn(StateEnum.CREATED);
        expectedTaskList.remove(taskMock);

        // THEN
        // the task isn't added to list
        assertEquals(expectedTaskList, victim.getAllFinishedTasksFromUser(user));
    }

    /**
     * Tests the getUnfinishedUserTaskList() method to verify if the tasks from a
     * certain user, marked as unfinished, are returned.
     */
    @Test
    public void testGetUnfinishedUserTaskList() {
        this.mocksGetTaskRepository();

        when(taskMock.getCurrentState()).thenReturn(StateEnum.CREATED);
        List<TaskCollaborator> taskCollaboratorsList = new ArrayList<>();
        taskCollaboratorsList.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(taskCollaboratorsList);
        when(taskCollaboratorMock.getProjectCollaboratorFromTaskCollaborator()).thenReturn(projectCollaborator);
        when(projectCollaboratorMock.getCollaborator()).thenReturn(user);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getUnfinishedUserTaskList(user));
    }

    /**
     * Tests the getStartedNotFinishedUserTaskList() method to verify if the tasks
     * from a certain user, marked as Started but the state "unfinished" are
     * returned.
     */
    @Test
    public void testGetStartedNotFinishedUserTaskList() {
        this.mocksGetTaskRepository();

        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.viewTaskStateName()).thenReturn("Canceled");
        when(taskMock.getStartDate()).thenReturn(startDate);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getStartedNotFinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    @Test
    public void testSortTaskListDecreasingOrder() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2017);
        when(taskMock.getFinishDate()).thenReturn(calendar1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2016);
        when(task2Mock.getFinishDate()).thenReturn(calendar2);

        List<Task> result = new ArrayList<>();
        result.add(task2Mock);
        result.add(taskMock);

        List<Task> orderedListToCompare = new ArrayList<>();
        orderedListToCompare.add(taskMock);
        orderedListToCompare.add(task2Mock);

        assertEquals(orderedListToCompare, victim.sortFinishTaskListDecreasingOrder(result));
    }

    @Test
    public void testSortTaskListByDeadline() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2017);
        when(taskMock.getTaskDeadline()).thenReturn(calendar1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2016);
        when(task2Mock.getTaskDeadline()).thenReturn(calendar2);

        List<Task> result = new ArrayList<>();
        result.add(taskMock);

        result.add(task2Mock);

        List<Task> orderedListToCompare = new ArrayList<>();
        orderedListToCompare.add(task2Mock);
        orderedListToCompare.add(taskMock);

        assertEquals(orderedListToCompare, victim.sortTaskListByDeadline(result));
    }

    @Test
    public void testGetTotalTimeOfFinishedTasksFromUserLastMonth() {
        this.mocksGetAllFinishedTasksFromUser();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        when(taskMock.getFinishDate()).thenReturn(calendar);

        when(projectCollaboratorMock.getUserFromProjectCollaborator()).thenReturn(user);
        when(taskMock.getTimeSpentByProjectCollaboratorOntask(any(ProjectCollaborator.class))).thenReturn(10.0);

        List<ProjectCollaborator> projectCollaboratorsList = new ArrayList<>();
        when(projectCollaboratorMock.getCollaborator()).thenReturn(user);
        projectCollaboratorsList.add(projectCollaboratorMock);
        when(projectCollaboratorRepository.findAll()).thenReturn(projectCollaboratorsList);

        assertEquals(10.0, victim.getTotalTimeOfFinishedTasksFromUserLastMonth(user), 0.1);
    }

    @Test
    public void testGetAverageTimeOfFinishedTasksFromUserLastMonth() {

        when(projectCollaboratorMock.getCollaborator()).thenReturn(user);

        List<ProjectCollaborator> projectCollaboratorsList = new ArrayList<>();
        projectCollaboratorsList.add(projectCollaborator);

        when(projectCollaboratorRepository.findAll()).thenReturn(projectCollaboratorsList);
        when(taskMock.getTimeSpentByProjectCollaboratorOntask(any(ProjectCollaborator.class))).thenReturn(10.0);
        when(task2Mock.getTimeSpentByProjectCollaboratorOntask(any(ProjectCollaborator.class))).thenReturn(20.0);

        List<TaskCollaborator> taskCollaboratorsList = new ArrayList<>();
        taskCollaboratorsList.add(taskCollaborator);

        when(taskMock.getTaskTeam()).thenReturn(taskCollaboratorsList);
        when(task2Mock.getTaskTeam()).thenReturn(taskCollaboratorsList);
        when(taskCollaboratorMock.getProjectCollaboratorFromTaskCollaborator()).thenReturn(projectCollaborator);
        // when(projectCollaboratorMock.getCollaborator()).thenReturn(user);
        when(task2Mock.getCurrentState()).thenReturn(StateEnum.FINISHED);
        when(taskMock.getCurrentState()).thenReturn(StateEnum.FINISHED);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);

        when(taskMock.getFinishDate()).thenReturn(calendar1);
        when(task2Mock.getFinishDate()).thenReturn(calendar1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);
        when(taskRepository.findAll()).thenReturn(taskList);

        assertEquals(15.0, victim.getAverageTimeOfFinishedTasksFromUserLastMonth(user), 0.1);
    }

    @Test
    public void testGetFinishedUserTasksFromLastMonthInDecreasingOrder() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 25);
        calendar1.add(Calendar.MONTH, -1);

        when(taskMock.getFinishDate()).thenReturn(calendar1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 24);
        calendar2.add(Calendar.MONTH, -1);

        when(task2Mock.getFinishDate()).thenReturn(calendar2);

        when(taskMock.getCurrentState()).thenReturn(StateEnum.FINISHED);
        when(task2Mock.getCurrentState()).thenReturn(StateEnum.FINISHED);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);
        when(taskRepository.findAll()).thenReturn(taskList);

        List<TaskCollaborator> listTaskCollaborator = new ArrayList<>();
        listTaskCollaborator.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(listTaskCollaborator);
        when(task2Mock.getTaskTeam()).thenReturn(listTaskCollaborator);

        List<Task> orderedListToCompare = new ArrayList<>();
        orderedListToCompare.add(taskMock);
        orderedListToCompare.add(task2Mock);

        List<Task> trueList = victim.getFinishedUserTasksFromLastMonthInDecreasingOrder(user);

        assertEquals(orderedListToCompare, trueList);
    }

    /**
     * Tests the getAllFinishedUserTasksInDecreasingOrder() method, to verify f a
     * list with the finished tasks of a certain user by decreasing order of date is
     * returned.
     */
    @Test
    public void testGetAllFinishedUserTasksInDecreasingOrder() {

        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(taskRepository.findAll()).thenReturn(taskListMock);

        List<TaskCollaborator> listTaskCollaborator = new ArrayList<>();
        listTaskCollaborator.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(listTaskCollaborator);

        // when the current state is finished, the task is added to list
        when(taskMock.getCurrentState()).thenReturn(StateEnum.FINISHED);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        assertEquals(expectedTaskList, victim.getAllFinishedUserTasksInDecreasingOrder(user));
    }

    @Test
    public void testGetStartedNotFinishedUserTasksInIncreasingDeadlineOrder() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);
        when(taskRepository.findAll()).thenReturn(taskList);

        List<TaskCollaborator> taskCollaboratorList = new ArrayList<>();
        taskCollaboratorList.add(taskCollaborator);
        when(taskMock.getTaskTeam()).thenReturn(taskCollaboratorList);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2017);
        when(taskMock.getTaskDeadline()).thenReturn(calendar2);

        when(task2Mock.getTaskTeam()).thenReturn(taskCollaboratorList);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2016);
        when(task2Mock.getTaskDeadline()).thenReturn(calendar1);

        when(taskMock.getCurrentState()).thenReturn(StateEnum.ONGOING);
        when(task2Mock.getCurrentState()).thenReturn(StateEnum.ONGOING);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2Mock);
        expectedList.add(taskMock);
        assertEquals(expectedList, victim.getStartedNotFinishedUserTasksInIncreasingDeadlineOrder(user));
    }

    @Test
    public void testSaveTask() {
        when(taskRepository.save(taskMock)).thenReturn(taskMock);
        assertEquals(taskMock, victim.saveTask(taskMock));
    }

    /**
     * Tests the getUnfinishedTasksFromProjectCollaborator() method to verify if a
     * list of all unfinished tasks of a certain project is returned.
     */
    @Test
    public void testGetUnFinishedTasksFromProjectCollaborator() {
        this.mocksGetTaskRepository();

        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getUnfinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the getStartedNotFinishedTasksFromProjectCollaborator() method to
     * verify if a list of started, but not yes finished tasks, assigned to a
     * certain project collaborator, is returned.
     */
    @Test
    public void shouldGetStartedNotFinishedTasksFromProjectCollaborator() {

        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(victim.getAllTasksFromProjectCollaborator(projectCollaborator)).thenReturn(taskListMock);

        // WHEN
        // condition is other.isTaskFinished() is false
        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.viewTaskStateName()).thenReturn("Cancelled");
        when(taskMock.getStartDate()).thenReturn(startDate);
        when(taskMock.isProjectCollaboratorInTaskTeam(projectCollaborator)).thenReturn(true);
        List<Task> expectedTaskList = new ArrayList<>();

        // THEN
        // the task is removed from list
        assertEquals(expectedTaskList, victim.getStartedNotFinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the getStartedNotFinishedTasksFromProjectCollaborator() method to
     * verify if a list of started, but not yes finished tasks, assigned to a
     * certain project collaborator, is returned.
     */
    @Test
    public void shouldGetStartedNotFinishedTasksFromProjectCollaboratorWhenTaskIsCancelled() {

        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(victim.getAllTasksFromProjectCollaborator(projectCollaborator)).thenReturn(taskListMock);

        // WHEN condition is "Cancelled".equals(other.viewTaskStateName()) is false
        List<Task> expectedTaskList = new ArrayList<>();
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.viewTaskStateName()).thenReturn("Finished");
        when(taskMock.getStartDate()).thenReturn(startDate);
        when(taskMock.isProjectCollaboratorInTaskTeam(projectCollaborator)).thenReturn(true);

        // THEN
        // the task is removed from list
        assertEquals(expectedTaskList, victim.getStartedNotFinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the getStartedNotFinishedTasksFromProjectCollaborator() method to
     * verify if a list of started, but not yes finished tasks, assigned to a
     * certain project collaborator, is returned.
     */
    @Test
    public void shouldGetStartedNotFinishedTasksFromProjectCollaboratorWhenTaskIsNotStarted() {

        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(victim.getAllTasksFromProjectCollaborator(projectCollaborator)).thenReturn(taskListMock);

        // WHEN
        // condition is other.getStartDate() == null is false
        List<Task> expectedTaskList = new ArrayList<>();
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.viewTaskStateName()).thenReturn("Cancelled");
        when(taskMock.getStartDate()).thenReturn(null);
        when(taskMock.isProjectCollaboratorInTaskTeam(projectCollaborator)).thenReturn(true);

        // THEN
        // the task is removed from list
        assertEquals(expectedTaskList, victim.getStartedNotFinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the getStartedNotFinishedTasksFromProjectCollaborator() method to
     * verify if a list of started, but not yes finished tasks, assigned to a
     * certain project collaborator, is returned.
     */
    @Test
    public void shouldNotGetStartedNotFinishedTasksFromProjectCollaborator() {

        // GIVEN
        // a list of tasks from a user
        List<Task> taskListMock = new ArrayList<>();
        taskListMock.add(taskMock);
        when(victim.getAllTasksFromProjectCollaborator(projectCollaborator)).thenReturn(taskListMock);

        // WHEN
        // all conditions are false
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.viewTaskStateName()).thenReturn("Finished");
        when(taskMock.getStartDate()).thenReturn(startDate);
        when(taskMock.isProjectCollaboratorInTaskTeam(projectCollaborator)).thenReturn(true);

        // THEN
        // the task is NOT removed from list
        assertEquals(expectedTaskList, victim.getStartedNotFinishedTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the getFinishedTaskListofUserInProject() method to verify if a list of
     * only the finished tasks of a certain user in a project is returned.
     */
    @Test
    public void testFinishedTaskListOfUserInProject() {
        this.mocksGetTaskRepository();

        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getFinishedTaskListOfUserInProject(projectCollaborator));
    }

    /**
     * Tests the getFinishedTasksFromProjectCollaboratorInGivenMonth() method to
     * verify if a list of all tasks finished x months ago (this month) by a certain
     * project collaborator is returned.
     */
    @Test
    public void shouldGetFinishedTasksFromProjectCollaboratorInGivenMonthThis() {

        // GIVEN
        // a list of tasks from ProjectCollab
        Calendar calendar = Calendar.getInstance();
        this.mocksGetTaskRepository();

        // WHEN
        // ProjectCollab is in task
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.getFinishDate()).thenReturn(calendar);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        // THEN
        // one can GetFinishedTasksFromProjectCollaboratorInGivenMonthThis
        assertEquals(expectedTaskList,
                victim.getFinishedTasksFromProjectCollaboratorInGivenMonth(projectCollaborator, 0));
    }

    /**
     * Tests the getFinishedTasksFromProjectCollaboratorInGivenMonth() method to
     * verify if a list of all tasks finished x months ago (the last) by a certain
     * project collaborator is returned.
     */
    @Test
    public void shouldGetFinishedTasksFromProjectCollaboratorInGivenMonthTheLast() {

        // GIVEN
        // a list of tasks from ProjectCollab
        Calendar calendar = Calendar.getInstance();
        this.mocksGetTaskRepository();

        // WHEN
        // ProjectCollab is in task
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.getFinishDate()).thenReturn(calendar);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        // THEN
        // one can GetFinishedTasksFromProjectCollaboratorInGivenMonthTheLast
        assertEquals(expectedTaskList,
                victim.getFinishedTasksFromProjectCollaboratorInGivenMonth(projectCollaborator, -1));
    }

    /**
     * Tests the getFinishedTasksFromProjectCollaboratorInGivenMonth() method to
     * verify if a list of all tasks finished x months ago by a certain project
     * collaborator is returned.
     */
    @Test
    public void shouldNotGetFinishedTasksFromProjectCollaborator() {

        // GIVEN
        // a list of tasks from ProjectCollab
        Calendar calendar = Calendar.getInstance();
        this.mocksGetTaskRepository();

        // WHEN
        // ProjectCollab is in task
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.getFinishDate()).thenReturn(calendar);
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        when(taskMock.getFinishDate()).thenReturn(calendar);

        // THEN
        // one shouldNotGetFinishedTasksFromProjectCollaborator
        assertEquals(new ArrayList<>(),
                victim.getFinishedTasksFromProjectCollaboratorInGivenMonth(projectCollaborator, 0));
    }

    /**
     * Tests the isTaskinTaskRepository() method to verify if a certain task exists
     * in the task list.
     */
    @Test
    public void isTaskInTaskContainer() {
        // GIVEN
        // a task Repository
        this.mocksGetTaskRepository();

        // WHEN a task is in task Repository
        // Then the method returns true
        assertTrue(victim.isTaskInTaskRepository(taskMock));
    }

    /**
     * Tests the isTaskinTaskRepository() method to verify if a certain task exists
     * in the task list.
     */
    @Test
    public void isNotTaskInTaskContainer() {

        // GIVEN
        // a task Repository
        this.mocksGetTaskRepository();

        // WHEN a task is not in task Repository
        // Then the method returns false
        assertFalse(victim.isTaskInTaskRepository(new Task()));
    }

    /**
     * Tests the getAllTasksFromProjectCollaborator() method to verify if a list of
     * all the tasks of a certain user, which is a project collaborator, is
     * returned.
     */
    @Test
    public void testGetAllTasksFromProjectCollaborator() {

        // GIVEN
        // a task Repository
        this.mocksGetTaskRepository();

        // WHEN asked to return all tasks from a ProjCollab
        when(taskMock.isProjectCollaboratorInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        // THEN the method returns the list of all tasks from ProjCollab
        assertEquals(expectedTaskList, victim.getAllTasksFromProjectCollaborator(projectCollaborator));
    }

    /**
     * Tests the isCollaboratorActiveOnAnyTask() method to verify if a given user
     * had any task assigned to him.
     */
    @Test
    public void isCollaboratorActiveOnAnyTask() {
        // GIVEN
        // a task Repository
        this.mocksGetTaskRepository();

        // WHEN a Collaborator is active on a task
        when(taskMock.isProjectCollaboratorActiveInTaskTeam(any(ProjectCollaborator.class))).thenReturn(true);

        // THEN the method returns true
        assertTrue(victim.isCollaboratorActiveOnAnyTask(projectCollaborator));
    }

    /**
     * Tests the isCollaboratorActiveOnAnyTask() method to verify if a given user
     * had any task assigned to him.
     */
    @Test
    public void isNotCollaboratorActiveOnAnyTask() {

        // GIVEN
        // a task Repository
        this.mocksGetTaskRepository();
        // WHEN a Collaborator is NOT active on a task
        when(taskMock.isProjectCollaboratorActiveInTaskTeam(any(ProjectCollaborator.class))).thenReturn(false);

        // THEN the method returns false
        assertFalse(victim.isCollaboratorActiveOnAnyTask(projectCollaborator));
    }

    /**
     * Tests the getProjectTasksWithoutCollaboratorAssigned() method to verify if a
     * list with all the tasks without collaborators assigned is returned.
     */
    @Test
    public void shouldGetAllTasksWithoutCollaboratorsAssigned() {
        this.mocksGetProjectTasks();

        when(taskMock.isTaskTeamEmpty()).thenReturn(true);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectTasksWithoutCollaboratorsAssigned(project));
    }

    /**
     * Tests the getProjectTasksWithoutCollaboratorAssigned() method to verify if a
     * list with all the tasks without collaborators assigned is returned.
     */
    @Test
    public void shouldNotGetAllTasksWithoutCollaboratorsAssigned() {
        this.mocksGetProjectTasks();

        when(taskMock.isTaskTeamEmpty()).thenReturn(true);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        when(taskMock.isTaskTeamEmpty()).thenReturn(false);
        when(taskMock.doesTaskTeamHaveActiveUsers()).thenReturn(false);
        assertEquals(expectedTaskList, victim.getProjectTasksWithoutCollaboratorsAssigned(project));
    }

    /**
     * Tests the getProjectTasksWithoutCollaboratorAssigned() method to verify if a
     * list with all the tasks without collaborators assigned is returned.
     */
    @Test
    public void testGetAllTasksWithoutCollaboratorsAssigned() {
        this.mocksGetProjectTasks();

        when(taskMock.isTaskTeamEmpty()).thenReturn(true);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        when(taskMock.isTaskTeamEmpty()).thenReturn(false);
        when(taskMock.doesTaskTeamHaveActiveUsers()).thenReturn(true);
        expectedTaskList.remove(taskMock);
        assertEquals(expectedTaskList, victim.getProjectTasksWithoutCollaboratorsAssigned(project));
    }

    /**
     * Tests the getProjectFinishedTasks() method to verify is a list with all
     * finished tasks from a certain project is returned.
     */
    @Test
    public void testGetProjectFinishedTasks() {
        this.mocksGetProjectTasks();

        when(taskMock.isTaskFinished()).thenReturn(true);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        assertEquals(expectedTaskList, victim.getProjectFinishedTasks(project));
    }

    /**
     * Tests the getFinishedTasksInDecreasingOrder() method, to verify if a list of
     * all tasks finished from a certain project in decreasing order is returned.
     */
    @Test
    public void testGetProjectFinishedTasksInDecreasingOrder() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);

        Calendar calendar = Calendar.getInstance();
        when(taskMock.getFinishDate()).thenReturn(calendar);
        when(taskMock.isTaskFinished()).thenReturn(true);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);
        when(task2Mock.getFinishDate()).thenReturn(calendar2);
        when(task2Mock.isTaskFinished()).thenReturn(true);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        expectedTaskList.add(task2Mock);

        assertEquals(expectedTaskList, victim.getProjectFinishedTasksInDecreasingOrder(project));
    }

    /**
     * Tests the get ProjectUnfinishedTasks() method to verify if a list with all
     * unfinished tasks in a certain project is returned.
     */
    @Test
    public void testGetUnFinishedTasks() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        this.mocksGetProjectTasks();

        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.viewTaskStateName()).thenReturn("OnGoing");
        when(taskMock.getStartDate()).thenReturn(calendar2);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectUnFinishedTasks(project));
    }

    /**
     * Tests the getProjectOnGoingTasks() method to verify if a list with all the
     * OnGoing tasks is returned.
     */
    @Test
    public void testGetProjectOnGoingTasks() {
        this.mocksGetProjectTasks();

        TaskStateInterface onGoing = new OnGoing();
        when(taskMock.getTaskState()).thenReturn(onGoing);
        when(taskMock.viewTaskStateName()).thenReturn("OnGoing");
        when(taskMock.isTaskFinished()).thenReturn(false);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectOnGoingTasks(project));
    }

    /**
     * Tests the getProjectUnstartedTasks() method to verify if a list with all not
     * started tasks (with the stated "Created", "Planned" and/or "Ready") of a
     * certain project is returned.
     */
    @Test
    public void shouldGetProjectUnstartedTasksIfTaskCreated() {
        this.mocksGetProjectTasks();

        TaskStateInterface created = new Created();
        when(taskMock.getTaskState()).thenReturn(created);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectUnstartedTasks(project));
    }

    /**
     * Tests the getProjectUnstartedTasks() method to verify if a list with all not
     * started tasks (with the stated "Created", "Planned" and/or "Ready") of a
     * certain project is returned.
     */
    @Test
    public void shouldGetProjectUnstartedTasksIfTaskPlanned() {
        this.mocksGetProjectTasks();

        TaskStateInterface created = new Created();
        when(taskMock.getTaskState()).thenReturn(created);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        TaskStateInterface planned = new Planned();
        when(taskMock.getTaskState()).thenReturn(planned);
        assertEquals(expectedTaskList, victim.getProjectUnstartedTasks(project));
    }

    /**
     * Tests the getProjectUnstartedTasks() method to verify if a list with all not
     * started tasks (with the stated "Created", "Planned" and/or "Ready") of a
     * certain project is returned.
     */
    @Test
    public void shouldNotGetProjectUnstartedTasksIfTaskReady() {
        this.mocksGetProjectTasks();

        TaskStateInterface created = new Created();
        when(taskMock.getTaskState()).thenReturn(created);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectUnstartedTasks(project));
        TaskStateInterface ready = new Ready();
        when(taskMock.getTaskState()).thenReturn(ready);
        assertEquals(expectedTaskList, victim.getProjectUnstartedTasks(project));
    }

    /**
     * Tests the getProjectExpiredTasks() method to verify if a list of all the
     * tasks marked as unfinished but which deadline already passed, is returned.
     */
    @Test
    public void shouldGetExpiredTasks() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);

        // all conditions on if are true
        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.getTaskDeadline()).thenReturn(calendar2);

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectExpiredTasks(project));

    }

    /**
     * Tests the getProjectExpiredTasks() method to verify if a list of all the
     * tasks marked as unfinished but which deadline already passed, is returned.
     */
    @Test
    public void shouldGetExpiredTasksWithConditions() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);


        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);


        // the condition other.getTaskDeadline().before(today) is false
        when(taskMock.isTaskFinished()).thenReturn(false);
        Calendar calendar = Calendar.getInstance();
        when(taskMock.getTaskDeadline()).thenReturn(calendar);
        expectedTaskList.remove(taskMock);
        taskList.remove(taskMock);
        assertEquals(expectedTaskList, victim.getProjectExpiredTasks(project));

    }

    /**
     * Tests the getProjectExpiredTasks() method to verify if a list of all the
     * tasks marked as unfinished but which deadline already passed, is returned.
     */
    @Test
    public void shouldNottGetExpiredTasksWithConditionNoDeadLine() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);


        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);

        expectedTaskList.remove(taskMock);
        taskList.remove(taskMock);


        // the condition other.getTaskDeadline() != null is false
        when(taskMock.isTaskFinished()).thenReturn(false);
        when(taskMock.getTaskDeadline()).thenReturn(null);
        assertEquals(expectedTaskList, victim.getProjectExpiredTasks(project));

    }

    /**
     * Tests the getProjectExpiredTasks() method to verify if a list of all the
     * tasks marked as unfinished but which deadline already passed, is returned.
     */
    @Test
    public void shouldNotGetExpiredTasks() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);


        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(taskMock);


        // the condition other.getTaskDeadline().before(today) is false
        expectedTaskList.remove(taskMock);
        taskList.remove(taskMock);


        // the condition !other.isTaskFinished() is false
        when(taskMock.isTaskFinished()).thenReturn(true);
        when(taskMock.getTaskDeadline()).thenReturn(calendar2);
        assertEquals(expectedTaskList, victim.getProjectExpiredTasks(project));
    }

    @Test
    public void testGetTaskByID() {
        when(taskRepository.findByDbTaskId(12L)).thenReturn(Optional.of(taskMock));
        assertEquals(taskMock, victim.getTaskByID(12L));
    }

    /**
     * Tests the deleteTask() method to verify if a task is deleted from the Task
     * repository, if it hasn't started.
     */
    @Test
    public void shouldDeleteTaskPlanned() {
        String taskId = "T0.1";
        taskMock.setTaskID(taskId);
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));

        when(taskMock.viewTaskStateName()).thenReturn("Planned");
        assertTrue(victim.deleteTask(taskId));
    }

    /**
     * Tests the deleteTask() method to verify if a task is deleted from the Task
     * repository, if it hasn't started.
     */
    @Test
    public void shouldDeleteTaskCreated() {
        String taskId = "T0.1";
        taskMock.setTaskID(taskId);
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));


        when(taskMock.viewTaskStateName()).thenReturn("Created");
        assertTrue(victim.deleteTask(taskId));

    }

    /**
     * Tests the deleteTask() method to verify if a task is deleted from the Task
     * repository, if it hasn't started.
     */
    @Test
    public void shouldDeleteTaskPReady() {
        String taskId = "T0.1";
        taskMock.setTaskID(taskId);
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));

        when(taskMock.viewTaskStateName()).thenReturn("Ready");
        assertTrue(victim.deleteTask(taskId));

    }

    /**
     * Tests the deleteTask() method to verify if a task is deleted from the Task
     * repository, if it hasn't started.
     */
    @Test
    public void shouldDeleteTaskAssigned() {
        String taskId = "T0.1";
        taskMock.setTaskID(taskId);
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));
        when(taskMock.viewTaskStateName()).thenReturn("Assigned");
        assertTrue(victim.deleteTask(taskId));
    }

    /**
     * Tests the deleteTask() method to verify if a task is deleted from the Task
     * repository, if it hasn't started.
     */
    @Test
    public void shouldDeleteTaskFinished() {
        String taskId = "T0.1";
        taskMock.setTaskID(taskId);
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));
        when(taskMock.viewTaskStateName()).thenReturn("Finished");
        assertFalse(victim.deleteTask(taskId));
    }

    /**
     * Tests the getProjectCancelledTasks() method to verify if a list of all
     * cancelled tasks in a certain project is returned.
     */
    @Test
    public void shouldNotGetProjectCancelledTasksIfTasksAreFinished() {
        this.mocksGetProjectTasks();

        TaskStateInterface finished = new Finished();
        when(taskMock.getTaskState()).thenReturn(finished);
        List<Task> expectedTaskList = new ArrayList<>();
        assertEquals(expectedTaskList, victim.getProjectCancelledTasks(project));
    }

    /**
     * Tests the getProjectCancelledTasks() method to verify if a list of all
     * cancelled tasks in a certain project is returned.
     */
    @Test
    public void shouldGetProjectCancelledTasksIfTasksAreCancelled() {
        this.mocksGetProjectTasks();

        List<Task> expectedTaskList = new ArrayList<>();

        TaskStateInterface cancelled = new Cancelled();
        when(taskMock.getTaskState()).thenReturn(cancelled);
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getProjectCancelledTasks(project));
    }

    /**
     * Tests the getReportedCostOfEachTask() method to verify is the cost reported
     * to each task is returned.
     */
    @Test
    public void testGetReportedCostOfEachTask() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);
        when(taskRepository.findAllByProject(project)).thenReturn(taskList);

        when(taskMock.getTaskCost()).thenReturn(10.0);

        List<String> expectedList = new ArrayList<>();
        expectedList.add("10.0, 0.0");

        assertEquals(expectedList.toString().trim(), victim.getReportedCostOfEachTask(project).toString().trim());
    }

    /**
     * Tests the various boolean condition checks to validate if a single
     * collaborator was active during the period of a report
     */
    @Test
    public void wasNotCollaboratorActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given a task report created started 3 months, and finished two months ago,
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(twoMonthsAgo);

        // when a project collaborator has its start date set to one month ago, after
        // the report
        projectCollaborator2.setStartDate(oneMonthAgo);

        // then their date intervals Don't intersect, and
        // "wasCollaboratorActiveDuringReport" must return false
        assertFalse(victim.wasCollaboratorActiveDuringReport(projectCollaborator2, testing));

    }

    /**
     * Tests the various boolean condition checks to validate if a single
     * collaborator was active during the period of a report
     */
    @Test
    public void wasCollaboratorActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given a task report created started 3 months, and finished two months ago,
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(twoMonthsAgo);

        // when a project collaborator has its start date set to one month ago, after
        // the report
        projectCollaborator2.setStartDate(oneMonthAgo);


        // when the report is updated and its date set to Today...
        testing.setDateOfUpdate(Calendar.getInstance());

        // then their date intervals now intersect, and
        // "wasCollaboratorActiveDuringReport" must return true
        assertTrue(victim.wasCollaboratorActiveDuringReport(projectCollaborator2, testing));

    }



    /**
     * Tests the various boolean condition checks to validate if a single
     * collaborator was active during the period of a report
     */
    @Test
    public void WasNOTCollaboratorActiveDuringReportTrue() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given a task report created started 3 months, and finished two months ago,
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(twoMonthsAgo);

        // when a project collaborator has its start date set to one month ago, after
        // the report
        projectCollaborator2.setStartDate(oneMonthAgo);


        // when the report is updated and its date set to Today...
        testing.setDateOfUpdate(Calendar.getInstance());


        // given then a different instance of projectCollaborator

        // when he's started and finished on the same date, before the report, their
        // start dates don't intersect
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(fourMonthsAgo);


        // then the tested method must return false
        assertFalse(victim.wasCollaboratorActiveDuringReport(projectCollaborator, testing));

    }

    /**
     * Tests the various boolean condition checks to validate if a single
     * collaborator was active during the period of a report
     */
    @Test
    public void WasCollaboratorActiveDuringReportTrue() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given a task report created started 3 months, and finished two months ago,
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(twoMonthsAgo);

        // when a project collaborator has its start date set to one month ago, after
        // the report
        projectCollaborator2.setStartDate(oneMonthAgo);


        // when the report is updated and its date set to Today...
        testing.setDateOfUpdate(Calendar.getInstance());


        // given then a different instance of projectCollaborator

        // when he's started and finished on the same date, before the report, their
        // start dates don't intersect
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(fourMonthsAgo);


        // when projectCollaborator is given a finish date of two months ago
        projectCollaborator.setFinishDate(twoMonthsAgo);


        // then heir date intervals must now interset
        assertTrue(victim.wasCollaboratorActiveDuringReport(projectCollaborator, testing));
    }

    /**
     * Tests the various boolean condition checks to validate if a single
     * collaborator was active during the period of a report
     */
    @Test
    public void WasNOTCollaboratorActiveDuringReportWithoutDatesIntersecting() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given a task report created started 3 months, and finished two months ago,
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(twoMonthsAgo);

        // when a project collaborator has its start date set to one month ago, after
        // the report
        projectCollaborator2.setStartDate(oneMonthAgo);


        // when the report is updated and its date set to Today...
        testing.setDateOfUpdate(Calendar.getInstance());


        // given then a different instance of projectCollaborator

        // when he's started and finished on the same date, before the report, their
        // start dates don't intersect
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(fourMonthsAgo);

        // when projectCollaborator is given a finish date of two months ago
        projectCollaborator.setFinishDate(twoMonthsAgo);


        // when the projectCollaborator is given a start date of two months ago, and the
        // report set to three months ago
        projectCollaborator.setStartDate(twoMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);

        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(threeMonthsAgo);

        // then heir date intervals no longer intersect, as the collaborator started
        // after the report
        assertFalse(victim.wasCollaboratorActiveDuringReport(projectCollaborator, testing));
    }

    /**
     * This method tests the ability to find all collaborators from the same user in
     * the project during the period of a single report
     */
    @Test
    public void shouldFindOneCollaboratorActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given the same collaborator started four months and ended two months ago
        // then the same collaborator started two months ago and still active
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);
        projectCollaborator2.setStartDate(twoMonthsAgo);

        List<ProjectCollaborator> allCollabs = new ArrayList<>();
        allCollabs.add(projectCollaborator);
        allCollabs.add(projectCollaborator2);

        when(taskMock.getProject()).thenReturn(project);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(allCollabs);

        // when the report was started and finished three months ago, meaning only one
        // instance of project collaborator was active
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(threeMonthsAgo);

        // then only projectCollaborator 1 was active during that period
        assertEquals(1, victim.getAllCollaboratorInstancesFromReport(testing).size());
        assertEquals(projectCollaborator, victim.getAllCollaboratorInstancesFromReport(testing).get(0));
    }

    /**
     * This method tests the ability to find all collaborators from the same user in
     * the project during the period of a single report
     */
    @Test
    public void shouldFindTwoCollaboratorsActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given the same collaborator started four months and ended two months ago
        // then the same collaborator started two months ago and still active
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);
        projectCollaborator2.setStartDate(twoMonthsAgo);

        List<ProjectCollaborator> allCollabs = new ArrayList<>();
        allCollabs.add(projectCollaborator);
        allCollabs.add(projectCollaborator2);

        when(taskMock.getProject()).thenReturn(project);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(allCollabs);

        // when the report was started and finished three months ago, meaning only one
        // instance of project collaborator was active
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(threeMonthsAgo);

        // when the report's last update is set to one month ago
        testing.setDateOfUpdate(oneMonthAgo);

        // then the active collaborators list must contain two instances
        assertEquals(2, victim.getAllCollaboratorInstancesFromReport(testing).size());
        assertEquals(projectCollaborator2, victim.getAllCollaboratorInstancesFromReport(testing).get(1));

    }

    /**
     * This method tests the ability to find all collaborators from the same user in
     * the project during the period of a single report
     */
    @Test
    public void shouldFindCollaboratorStillActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given the same collaborator started four months and ended two months ago
        // then the same collaborator started two months ago and still active
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);
        projectCollaborator2.setStartDate(twoMonthsAgo);

        List<ProjectCollaborator> allCollabs = new ArrayList<>();
        allCollabs.add(projectCollaborator);
        allCollabs.add(projectCollaborator2);

        when(taskMock.getProject()).thenReturn(project);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(allCollabs);

        // when the report was started and finished three months ago, meaning only one
        // instance of project collaborator was active
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(threeMonthsAgo);


        // when the report's last update is set to one month ago
        testing.setDateOfUpdate(oneMonthAgo);


        // when the report is set as a hypothetical future date (only to test the
        // method's boolean checks)
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.MONTH, 2);
        testing.setFirstDateOfReport(futureDate);
        testing.setDateOfUpdate(futureDate);

        // then the list must contain only collaborators who are still active (ie, no
        // finish date)
        assertEquals(1, victim.getAllCollaboratorInstancesFromReport(testing).size());
        assertEquals(projectCollaborator2, victim.getAllCollaboratorInstancesFromReport(testing).get(0));

    }


    /**
     * This method tests the ability to find all collaborators from the same user in
     * the project during the period of a single report
     */
    @Test
    public void shouldNOTFindCollaboratorActiveDuringReport() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        // given the same collaborator started four months and ended two months ago
        // then the same collaborator started two months ago and still active
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);
        projectCollaborator2.setStartDate(twoMonthsAgo);

        List<ProjectCollaborator> allCollabs = new ArrayList<>();
        allCollabs.add(projectCollaborator);
        allCollabs.add(projectCollaborator2);

        when(taskMock.getProject()).thenReturn(project);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(allCollabs);

        // when the report was started and finished three months ago, meaning only one
        // instance of project collaborator was active
        Report testing = new Report();
        testing.setTask(taskMock);
        testing.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        testing.setFirstDateOfReport(threeMonthsAgo);
        testing.setDateOfUpdate(threeMonthsAgo);


        // when the report's last update is set to one month ago
        testing.setDateOfUpdate(oneMonthAgo);

        // when the report is set as a hypothetical future date (only to test the
        // method's boolean checks)
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.MONTH, 2);
        testing.setFirstDateOfReport(futureDate);
        testing.setDateOfUpdate(futureDate);


        // when their finish date is set to present day
        projectCollaborator2.setFinishDate(Calendar.getInstance());

        // then no collaborators are active during that future report.
        assertEquals(0, victim.getAllCollaboratorInstancesFromReport(testing).size());

    }

    /**
     * This test validates if the methods to calculate report costs are all working
     * correctly
     */
    @Test
    public void shouldCalculateReportsCostUsingDefaultCalculationMethod() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        notAmock = project.createTask("This is not a mock");

        // given a project collaborator with start date set to four months ago and ended
        // 2 months ago
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);

        // when the project collaborator is removed from the project and added again
        // with cost 20,
        projectCollaborator2.setStartDate(twoMonthsAgo);
        projectCollaborator2.setCostPerEffort(20);

        // the initial report created three months and ended one month ago has a cost
        // 10, but the collaborator also had a cost 20 during that period
        Report firstReport = new Report();
        firstReport.setTask(notAmock);
        firstReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        firstReport.setFirstDateOfReport(threeMonthsAgo);
        firstReport.setDateOfUpdate(oneMonthAgo);
        firstReport.setCost(10);

        // he only creates a second report during his second instance, giving it an
        // initial cost of 20
        Report secondReport = new Report();
        secondReport.setTask(notAmock);
        secondReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator2));
        secondReport.setCost(20);
        secondReport.setFirstDateOfReport(oneMonthAgo);
        secondReport.setDateOfUpdate(Calendar.getInstance());

        List<ProjectCollaborator> collabsFromUser = new ArrayList<>();
        collabsFromUser.add(projectCollaborator);
        collabsFromUser.add(projectCollaborator2);

        List<Report> taskReports = new ArrayList<>();
        taskReports.add(firstReport);
        taskReports.add(secondReport);

        List<Task> projectTasks = new ArrayList<>();
        projectTasks.add(notAmock);

        notAmock.setProject(project);
        notAmock.setReports(taskReports);

        when(taskRepository.findAllByProject(project)).thenReturn(projectTasks);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(collabsFromUser);

        // given the project has a default calculation method of "first collaborator"
        assertEquals(10, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the report cost is calculated and updated
        victim.calculateReportEffortCost(project);

        // then it shoulde remain unchanged
        assertEquals(10, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);
    }


    /**
     * This test validates if the methods to calculate report costs are all working
     * correctly
     */
    @Test
    public void shoulCalculateReportsCostUpdatedToLastCollaborator() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        notAmock = project.createTask("This is not a mock");

        // given a project collaborator with start date set to four months ago and ended
        // 2 months ago
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);

        // when the project collaborator is removed from the project and added again
        // with cost 20,
        projectCollaborator2.setStartDate(twoMonthsAgo);
        projectCollaborator2.setCostPerEffort(20);

        // the initial report created three months and ended one month ago has a cost
        // 10, but the collaborator also had a cost 20 during that period
        Report firstReport = new Report();
        firstReport.setTask(notAmock);
        firstReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        firstReport.setFirstDateOfReport(threeMonthsAgo);
        firstReport.setDateOfUpdate(oneMonthAgo);
        firstReport.setCost(10);

        // he only creates a second report during his second instance, giving it an
        // initial cost of 20
        Report secondReport = new Report();
        secondReport.setTask(notAmock);
        secondReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator2));
        secondReport.setCost(20);
        secondReport.setFirstDateOfReport(oneMonthAgo);
        secondReport.setDateOfUpdate(Calendar.getInstance());

        List<ProjectCollaborator> collabsFromUser = new ArrayList<>();
        collabsFromUser.add(projectCollaborator);
        collabsFromUser.add(projectCollaborator2);

        List<Report> taskReports = new ArrayList<>();
        taskReports.add(firstReport);
        taskReports.add(secondReport);

        List<Task> projectTasks = new ArrayList<>();
        projectTasks.add(notAmock);

        notAmock.setProject(project);
        notAmock.setReports(taskReports);

        when(taskRepository.findAllByProject(project)).thenReturn(projectTasks);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(collabsFromUser);

        // when the project is set to Last Collaborator for each report, and the cost
        // calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CF);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 20
        assertEquals(20, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);
    }



    /**
     * This test validates if the methods to calculate report costs are all working
     * correctly
     */
    @Test
    public void shouldCalculateReportsCostUsingAverageCostFirstAndLastCollab() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        notAmock = project.createTask("This is not a mock");

        // given a project collaborator with start date set to four months ago and ended
        // 2 months ago
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);

        // when the project collaborator is removed from the project and added again
        // with cost 20,
        projectCollaborator2.setStartDate(twoMonthsAgo);
        projectCollaborator2.setCostPerEffort(20);

        // the initial report created three months and ended one month ago has a cost
        // 10, but the collaborator also had a cost 20 during that period
        Report firstReport = new Report();
        firstReport.setTask(notAmock);
        firstReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        firstReport.setFirstDateOfReport(threeMonthsAgo);
        firstReport.setDateOfUpdate(oneMonthAgo);
        firstReport.setCost(10);

        // he only creates a second report during his second instance, giving it an
        // initial cost of 20
        Report secondReport = new Report();
        secondReport.setTask(notAmock);
        secondReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator2));
        secondReport.setCost(20);
        secondReport.setFirstDateOfReport(oneMonthAgo);
        secondReport.setDateOfUpdate(Calendar.getInstance());

        List<ProjectCollaborator> collabsFromUser = new ArrayList<>();
        collabsFromUser.add(projectCollaborator);
        collabsFromUser.add(projectCollaborator2);

        List<Report> taskReports = new ArrayList<>();
        taskReports.add(firstReport);
        taskReports.add(secondReport);

        List<Task> projectTasks = new ArrayList<>();
        projectTasks.add(notAmock);

        notAmock.setProject(project);
        notAmock.setReports(taskReports);

        when(taskRepository.findAllByProject(project)).thenReturn(projectTasks);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(collabsFromUser);

        // when the project is set to First/Last Collaborator average for each report,
        // and the cost calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CIFM);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 15
        assertEquals(15, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

    }

    /**
     * This test validates if the methods to calculate report costs are all working
     * correctly
     */
    @Test
    public void shouldCalculateReportsCostAltenatingBetweenMechanismUsing3Collabs() {

        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH, -1);
        Calendar twoMonthsAgo = Calendar.getInstance();
        twoMonthsAgo.add(Calendar.MONTH, -2);
        Calendar threeMonthsAgo = Calendar.getInstance();
        threeMonthsAgo.add(Calendar.MONTH, -3);
        Calendar fourMonthsAgo = Calendar.getInstance();
        fourMonthsAgo.add(Calendar.MONTH, -4);

        notAmock = project.createTask("This is not a mock");

        // given a project collaborator with start date set to four months ago and ended
        // 2 months ago
        projectCollaborator.setStartDate(fourMonthsAgo);
        projectCollaborator.setFinishDate(twoMonthsAgo);

        // when the project collaborator is removed from the project and added again
        // with cost 20,
        projectCollaborator2.setStartDate(twoMonthsAgo);
        projectCollaborator2.setCostPerEffort(20);

        // the initial report created three months and ended one month ago has a cost
        // 10, but the collaborator also had a cost 20 during that period
        Report firstReport = new Report();
        firstReport.setTask(notAmock);
        firstReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator));
        firstReport.setFirstDateOfReport(threeMonthsAgo);
        firstReport.setDateOfUpdate(oneMonthAgo);
        firstReport.setCost(10);

        // he only creates a second report during his second instance, giving it an
        // initial cost of 20
        Report secondReport = new Report();
        secondReport.setTask(notAmock);
        secondReport.setTaskCollaborator(new TaskCollaborator(projectCollaborator2));
        secondReport.setCost(20);
        secondReport.setFirstDateOfReport(oneMonthAgo);
        secondReport.setDateOfUpdate(Calendar.getInstance());

        List<ProjectCollaborator> collabsFromUser = new ArrayList<>();
        collabsFromUser.add(projectCollaborator);
        collabsFromUser.add(projectCollaborator2);

        List<Report> taskReports = new ArrayList<>();
        taskReports.add(firstReport);
        taskReports.add(secondReport);

        List<Task> projectTasks = new ArrayList<>();
        projectTasks.add(notAmock);

        notAmock.setProject(project);
        notAmock.setReports(taskReports);

        when(taskRepository.findAllByProject(project)).thenReturn(projectTasks);

        when(projectCollaboratorRepository.findAllByProjectAndCollaborator(project, user)).thenReturn(collabsFromUser);

        // given the project has a default calculation method of "first collaborator"
        assertEquals(10, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the report cost is calculated and updated
        victim.calculateReportEffortCost(project);

        // then it shoulde remain unchanged
        assertEquals(10, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the project is set to Last Collaborator for each report, and the cost
        // calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CF);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 20
        assertEquals(20, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the project is set to First/Last Collaborator average for each report,
        // and the cost calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CIFM);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 15
        assertEquals(15, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the project is set to Collaborator cost average for each report, and the
        // cost calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CM);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 15
        assertEquals(15, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // creating a new, testing project Collaborator 3 with a cost 70, should alter
        // the value of the average collaborator cost, but not first/last

        projectCollaborator3 = new ProjectCollaborator(user, 70);
        projectCollaborator3.setProject(project);
        projectCollaborator3.setStartDate(twoMonthsAgo);
        projectCollaborator3.setFinishDate(twoMonthsAgo);

        collabsFromUser.add(projectCollaborator3);

        // when the project is set to First/Last Collaborator average for each report,
        // and the cost calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CIFM);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 15
        assertEquals(15, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

        // when the project is set to Collaborator cost average for each report, and the
        // cost calculated
        project.setCalculationMethod(CostCalculationFactory.Method.CM);
        victim.calculateReportEffortCost(project);

        // then the second report remains unchanged, and the first report becomes 33.33
        // (10+20+70)/3
        assertEquals(33.33, firstReport.getCost(), 0.01);
        assertEquals(20, secondReport.getCost(), 0.01);

    }

    @Test
    public void shouldNotGetTaskListOfWhichDependenciesCanBeCreatedIfTaskIsFinished() {
        this.mocksGetProjectTasks();
        this.mocksGetTaskRepository();

        // test case taskState is finished
        TaskStateInterface finished = new Finished();
        when(taskMock.getTaskState()).thenReturn(finished);
        List<Task> expectedTaskList = new ArrayList<>();
        assertEquals(expectedTaskList, victim.getTaskListOfWhichDependenciesCanBeCreated(project));

    }


    @Test
    public void shouldNotGetTaskListOfWhichDependenciesCanBeCreatedIfTaskIsCancelled() {
        this.mocksGetProjectTasks();
        this.mocksGetTaskRepository();

        List<Task> expectedTaskList = new ArrayList<>();

        // test case taskState is cancelled
        TaskStateInterface cancelled = new Cancelled();
        when(taskMock.getTaskState()).thenReturn(cancelled);
        assertEquals(expectedTaskList, victim.getTaskListOfWhichDependenciesCanBeCreated(project));

        // test case taskState isn't finished neither cancelled
        this.mocksGetTaskRepository();
        TaskStateInterface onGoing = new OnGoing();
        when(taskMock.getTaskState()).thenReturn(onGoing);
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getTaskListOfWhichDependenciesCanBeCreated(project));
    }

    @Test
    public void shouldGetTaskListOfWhichDependenciesCanBeCreatedIfTaskIsNeitherFinishedOrCancelled() {
        this.mocksGetProjectTasks();
        this.mocksGetTaskRepository();

        List<Task> expectedTaskList = new ArrayList<>();

        // test case taskState isn't finished neither cancelled
        this.mocksGetTaskRepository();
        TaskStateInterface onGoing = new OnGoing();
        when(taskMock.getTaskState()).thenReturn(onGoing);
        expectedTaskList.add(taskMock);
        assertEquals(expectedTaskList, victim.getTaskListOfWhichDependenciesCanBeCreated(project));
    }

    /**
     * This test confirms all the methods to get and view the lists of requests are
     * working correctly
     */

    @Test
    public void testGetAssignment_RemovalRequestsList() {

        // given a new task in project, asserts it has no requests and no active team

        notAmock = project.createTask("This is not a mock");

        List<Task> expected = new ArrayList<>();
        expected.add(notAmock);

        victim.saveTask(taskMock);

        verify(taskRepository, times(1)).save(taskMock);

        when(taskRepository.findAllByProject(project)).thenReturn(expected);

        assertEquals(victim.getAllProjectTaskAssignmentRequests(project).size(), 0);
        assertEquals(victim.viewAllProjectTaskAssignmentRequests(project).size(), 0);
        assertEquals(victim.getAllProjectTaskRemovalRequests(project).size(), 0);
        assertEquals(victim.viewAllProjectTaskRemovalRequests(project).size(), 0);

        assertFalse(notAmock.isProjectCollaboratorActiveInTaskTeam(projectCollaborator));

        // when the task assignment requests is created, asserts the lists of assignment
        // requests contain 1 entry
        notAmock.createTaskAssignmentRequest(projectCollaborator);

        assertEquals(victim.getAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.getAllProjectTaskRemovalRequests(project).size(), 0);
        assertEquals(victim.viewAllProjectTaskRemovalRequests(project).size(), 0);

        // then, deletes the request and adds the collaborator to the task, asserting all
        // and that the task team contains the project collaborator
        assertTrue(notAmock.approveTaskAssignmentRequest(projectCollaborator));
        //notAmock.addProjectCollaboratorToTask(projectCollaborator);

        assertEquals(victim.getAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.getAllProjectTaskRemovalRequests(project).size(), 0);
        assertEquals(victim.viewAllProjectTaskRemovalRequests(project).size(), 0);

        assertTrue(notAmock.isProjectCollaboratorActiveInTaskTeam(projectCollaborator));

        // when a task removal request is created for projectCollaborator
        // asserts the task removal requests contain 1 entry

        notAmock.createTaskRemovalRequest(projectCollaborator);

        assertEquals(victim.getAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.getAllProjectTaskRemovalRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskRemovalRequests(project).size(), 1);

        // then, approves the request and deletes it, asserting the list of requests all
        // contain 0 entries
        // and that projectCollaborator is no longer active on task
        assertTrue(notAmock.approveTaskRemovalRequest(projectCollaborator));
        //notAmock.removeProjectCollaboratorFromTask(projectCollaborator);

        assertFalse(notAmock.isProjectCollaboratorActiveInTaskTeam(projectCollaborator));

        assertEquals(victim.getAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskAssignmentRequests(project).size(), 1);
        assertEquals(victim.getAllProjectTaskRemovalRequests(project).size(), 1);
        assertEquals(victim.viewAllProjectTaskRemovalRequests(project).size(), 1);

    }

    @Test
    public void testGetTaskByTaskID() {
        when(taskRepository.findByTaskID("12")).thenReturn(Optional.of(taskMock));
        assertEquals(taskMock, victim.getTaskByTaskID("12"));
    }

    /**
     * GIVEN a project id WHEN the method getProjectFinishedTasksInDecOrder is
     * called THEN a list of taskDTOs must be returned in decreasing order
     */
    @Test
    public void testGetProjectFinishedTasksInDecOrder() {

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(taskMock);
        taskList.add(task2Mock);

        when(taskMock.isTaskFinished()).thenReturn(true);
        when(task2Mock.isTaskFinished()).thenReturn(true);
        when(taskMock.getFinishDate()).thenReturn(calendar);
        when(task2Mock.getFinishDate()).thenReturn(calendar2);

        List<TaskDTO> taskListDTO = new ArrayList<>();
        taskListDTO.add(new TaskDTO(taskMock));
        taskListDTO.add(new TaskDTO(task2Mock));

        // GIVEN a project id
        int projId = 1;
        when(projectsRepository.findByProjectId(projId)).thenReturn(Optional.of(project));

        // WHEN the method getProjectFinishedTasksInDecOrder is called
        when(taskRepository.findAllByProject(any(Project.class))).thenReturn(taskList);
        List<TaskDTO> finishedTasksDTO = victim.getProjectFinishedTasksDecOrder(projId);

        // THEN a list of taskDTOs must be returned in decreasing order
        assertEquals(taskListDTO, finishedTasksDTO);

    }

    /**
     * GIVEN a task id WHEN the method getTaskDTOByTaskId is called THEN a taskDTO
     * from the found task must be returned
     */
    @Test
    public void getTaskDTOByTaskIDTest() {

        // GIVEN a task id
        String taskId = "01";

        // WHEN the method getTaskDTOByTaskId is called
        when(taskRepository.findByTaskID(taskId)).thenReturn(Optional.of(taskMock));

        // THEN a taskDTO from the found task must be returned
        TaskDTO taskDTO = new TaskDTO(taskMock);
        assertEquals(taskDTO, victim.getTaskDtoByTaskId(taskId));

    }

    /**
     * GIVEN a Task that needs to be edited and a TaskDto with the information to edit the task
     * WHEN the method editTask is called
     * THEN a new TaskDto is returned
     */
    @Test
    public void shouldEditTask(){

        Task task = new Task("description", project);
        task.setEstimatedTaskStartDate(Calendar.getInstance());
        task.setTaskDeadline(Calendar.getInstance());
        task.setEstimatedTaskEffort(30.0);
        task.setTaskBudget(12.00);
        task.setStartDate(Calendar.getInstance());


        Task taskDToCreateDto = new Task("description AA", project);
        taskDToCreateDto.setEstimatedTaskStartDate(startDate);
        taskDToCreateDto.setTaskDeadline(startDate);
        taskDToCreateDto.setEstimatedTaskEffort(10.0);
        taskDToCreateDto.setTaskBudget(10.00);
        taskDToCreateDto.setStartDate(startDate);


        //GIVEN a Task that needs to be edited and a TaskDto with the information to edit the task
        when(taskRepository.findByTaskID(any(String.class))).thenReturn(Optional.of(task));

        //When the method editTask is called
        TaskDTO toCompare = victim.editTask(taskDToCreateDto);

        //THEN a new TaskDto is returned and should be equal to the taskDto received

        assertTrue(Double.compare(toCompare.getEstimatedTaskEffort(), taskDToCreateDto.getEstimatedTaskEffort()) == 0);
        assertTrue(toCompare.getEstimatedTaskStartDate().equals(taskDToCreateDto.getEstimatedTaskStartDate()));
        assertTrue(toCompare.getTaskDeadline().equals(taskDToCreateDto.getTaskDeadline()));
        assertTrue(toCompare.getStartDate().equals(taskDToCreateDto.getStartDate()));

    }

}
