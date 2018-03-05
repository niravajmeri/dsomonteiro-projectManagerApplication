package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

;

public class ProjectsRepositoryTest {

	ProjectRepository projectRepository;
	User user1;
	User user2;
	User user3;
	User userNotCollaborator;
	User nullUser;
	ProjectCollaborator collab1;
	ProjectCollaborator collab2;
	ProjectCollaborator collab3;
	ProjectCollaborator collab4;

	TaskCollaborator taskWorker1;
	TaskCollaborator taskWorker2;
	TaskCollaborator taskWorker3;
	TaskCollaborator taskWorker4;

	Project project1;
	Project project2;
	Project project3;
	Project project4;
	Project project5;
	Project project6;

	Project projectA;
	Project projectB;
	Project projectC;

	Task task1;
	Task task2;
	Task task3;
	Task task4;
	Task task5;
	Task task6;

	List<Project> expResultProjectList;
	List<Task> expResultTaskList;
	Calendar estimatedStartDate;
	Calendar taskDeadline;
	Calendar taskDeadlineDateTest;
	Calendar taskDeadlineDateTest2;
	Calendar taskDeadlineDateTest3;
	Calendar taskDeadlineDateTest4;
	Calendar taskDeadlineDateTest5;
	Calendar taskDeadlineDateTest6;
	Calendar estimatedTaskStartDateTest;

	@Before
	public void setUp() {

		projectRepository = new ProjectRepository();
		user1 = new User("name", "email@gmail.com", "idNumber", "function", "123456789");
		user2 = new User("name2", "email2@gmail.com", "idNumber2", "function2", "987654321");
		user3 = new User("name6", "email6@gmail.com", "idNumber6", "function6", "987654271");
		userNotCollaborator = new User("name7", "notcollaborator@gmail.com", "idNumber7", "function6", "987654271");

		collab1 = new ProjectCollaborator(user1, 1);
		collab2 = new ProjectCollaborator(user2, 2);
		collab3 = new ProjectCollaborator(user3, 3);

		// Another collaborator in project 2
		collab4 = new ProjectCollaborator(user1, 4);

		taskWorker1 = new TaskCollaborator(collab1);
		taskWorker2 = new TaskCollaborator(collab2);
		taskWorker3 = new TaskCollaborator(collab3);

		// User 1 is collab4 in project 2
		taskWorker4 = new TaskCollaborator(collab4);

		project1 = new Project(0, "name3", "description3", user1);
		project2 = new Project(1, "name4", "description5", user2);
		project3 = new Project(2, "name5", "description5", user3);
		project4 = new Project(3, "project4", "description5", user3);
		project5 = new Project(4, "project5", "description5", user3);
		project6 = new Project(5, "project6", "description5", user3);

		// create a estimated Task Start Date
		estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.set(Calendar.YEAR, 2017);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 25);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);
		// create a estimated Task Dead line Date
		// last deadline
		taskDeadlineDateTest = Calendar.getInstance();
		taskDeadlineDateTest.set(Calendar.MONTH, Calendar.JANUARY);

		// first deadline
		taskDeadlineDateTest2 = Calendar.getInstance();
		taskDeadlineDateTest2.set(Calendar.HOUR_OF_DAY, 15);
		taskDeadlineDateTest2.set(Calendar.DAY_OF_MONTH, 28);
		taskDeadlineDateTest2.set(Calendar.MONTH, Calendar.FEBRUARY);
		taskDeadlineDateTest2.set(Calendar.YEAR, 2018);

		// second deadline
		taskDeadlineDateTest3 = Calendar.getInstance();
		taskDeadlineDateTest3.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest3.set(Calendar.MONTH, Calendar.FEBRUARY);
		taskDeadlineDateTest3.set(Calendar.DAY_OF_MONTH, 28);
		taskDeadlineDateTest3.set(Calendar.HOUR_OF_DAY, 16);
		// third deadline
		taskDeadlineDateTest4 = Calendar.getInstance();
		taskDeadlineDateTest4.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest4.set(Calendar.MONTH, Calendar.FEBRUARY);
		taskDeadlineDateTest4.set(Calendar.DAY_OF_MONTH, 28);
		taskDeadlineDateTest4.set(Calendar.HOUR_OF_DAY, 17);
		// fourth deadline
		taskDeadlineDateTest6 = Calendar.getInstance();
		taskDeadlineDateTest6.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest6.set(Calendar.MONTH, Calendar.FEBRUARY);
		taskDeadlineDateTest6.set(Calendar.DAY_OF_MONTH, 28);
		taskDeadlineDateTest6.set(Calendar.HOUR_OF_DAY, 18);
		// fifth deadline
		taskDeadlineDateTest5 = Calendar.getInstance();
		taskDeadlineDateTest5.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest5.set(Calendar.MONTH, Calendar.FEBRUARY);
		taskDeadlineDateTest5.set(Calendar.DAY_OF_MONTH, 28);
		taskDeadlineDateTest5.set(Calendar.HOUR_OF_DAY, 19);

		estimatedStartDate = Calendar.getInstance();
		estimatedStartDate.set(2017, Calendar.JANUARY, 14);
		taskDeadline = Calendar.getInstance();
		taskDeadline.set(2017, Calendar.NOVEMBER, 17);
		task1 = new Task(111, 222, "Task 1", 50, estimatedTaskStartDateTest, taskDeadlineDateTest, 2000);
		task2 = new Task(112, 223, "Task 1", 50, estimatedTaskStartDateTest, taskDeadlineDateTest2, 2000);
		task3 = new Task(113, 224, "Task 1", 50, estimatedTaskStartDateTest, taskDeadlineDateTest3, 2000);
		task4 = new Task(213, 224, "Task 4", 50, estimatedTaskStartDateTest, taskDeadlineDateTest4, 2000);
		task5 = new Task(289, 2245, "Task 5", 50, estimatedTaskStartDateTest, taskDeadlineDateTest5, 2000);
		task6 = new Task(584, 285, "Task 5", 50, estimatedTaskStartDateTest, taskDeadlineDateTest6, 2000);

		expResultProjectList = new ArrayList<Project>();
		expResultTaskList = new ArrayList<Task>();

		// sets the Project Counter to 0
		projectRepository.setProjCounter(0);
	}

	@After
	public void tearDown() {

		projectRepository.setProjCounter(0);
		projectRepository = null;
		user1 = null;
		user2 = null;
		user3 = null;
		project1 = null;
		project2 = null;
		project3 = null;
		project4 = null;
		project5 = null;
		project6 = null;

		projectA = null;
		projectB = null;
		projectC = null;

		taskDeadlineDateTest = null;
		taskDeadlineDateTest2 = null;
		taskDeadlineDateTest3 = null;
		taskDeadlineDateTest4 = null;
		taskDeadlineDateTest5 = null;
		taskDeadlineDateTest6 = null;

		task1 = null;
		task2 = null;
		task3 = null;
		task4 = null;
		task5 = null;
		task6 = null;

		taskWorker1 = null;
		taskWorker2 = null;
		taskWorker3 = null;
		taskWorker4 = null;

	}

	/**
	 * Tests the ProjectsRepository constructor.
	 */
	@Test
	public void test_Constructor() {

		assertEquals(expResultProjectList, projectRepository.getAllProjects());

	}

	/**
	 * Tests the CreateProject method by calling the method equals (project) to
	 * assert if the project created is equal to other project. If the equals
	 * returns TRUE means the two projects are equal, so the creatProject method
	 * worked.
	 */
	@Test
	public void testCreateProject() {

		assertTrue(project1.equals(projectRepository.createProject("name3", "description3", user1)));

	}

	/**
	 * Tests the getProjCounter and SetProjectCounter at the same time. First the
	 * project counter is set to 10 and then is asserted if the getProjCounter
	 * method outputs the value 10.
	 */
	@Test
	public void test_getProjCounter() {

		projectRepository.setProjCounter(10);

		assertEquals(10, projectRepository.getProjCounter());
	}

	/**
	 * Tests the addProjectToProjectRepository by asserting if the list within the
	 * projectRepository is equal to a new list of projects created, after adding
	 * the same project to both lists.
	 */
	@Test
	public void testAddProjectToProjectRepository() {

		projectRepository.addProjectToProjectRepository(project1);

		expResultProjectList.add(project1);

		assertEquals(expResultProjectList, projectRepository.getAllProjects());

		// Tried to add again the same project
		projectRepository.addProjectToProjectRepository(project1);

		/*
		 * The result will be the same as before, as the method doesn't allow to add
		 * projects that already exist in the projectRepository
		 */

		assertEquals(expResultProjectList, projectRepository.getAllProjects());

	}

	/**
	 * Tests the getAllProjects by asserting if the list within the
	 * projectRepository is equal to a new list of projects created.
	 */
	@Test
	public void testGetAllProjects() {

		projectRepository.addProjectToProjectRepository(project1);

		expResultProjectList.add(project1);

		assertEquals(expResultProjectList, projectRepository.getAllProjects());
	}

	/**
	 * This test compares a list (expResult) with only active projects to the list
	 * returned by the method getActiveProjects, which has to output only the active
	 * projects from the repository.
	 */
	@Test
	public void testGetActiveProjects() {

		// Adds the projects to the project repository
		projectRepository.addProjectToProjectRepository(project1);
		projectRepository.addProjectToProjectRepository(project2);
		projectRepository.addProjectToProjectRepository(project3);
		projectRepository.addProjectToProjectRepository(project4);
		projectRepository.addProjectToProjectRepository(project5);
		projectRepository.addProjectToProjectRepository(project6);

		// Sets the project status to Planning (0, Not active)
		project1.setProjectStatus(0);

		// Sets the project status to Initiation (1, Active)
		project2.setProjectStatus(1);

		// Sets the project status to Executing (2, Active)
		project3.setProjectStatus(2);

		// Sets the project status to Delivery (3, Active)
		project4.setProjectStatus(3);

		// Sets the project status to Review (4, Active)
		project5.setProjectStatus(4);

		// Sets the project status to Close (5, Active)
		project6.setProjectStatus(5);

		// Adds the projects to the expResult list to be compared to.
		expResultProjectList.add(project2);
		expResultProjectList.add(project3);
		expResultProjectList.add(project4);
		expResultProjectList.add(project5);

		assertEquals(expResultProjectList, projectRepository.getActiveProjects());
	}

	/**
	 * Tests the getUserTasks. The list returned has to be equal to the
	 * expResultTaskList created.
	 */
	@Test
	public void testGetUserTasks() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// Adds user to tasks.
		task1.addTaskCollaboratorToTask(taskWorker1);
		task3.addTaskCollaboratorToTask(taskWorker1);

		// Adds user to expResultTaskList
		expResultTaskList.add(task1);
		expResultTaskList.add(task3);

		assertEquals(expResultTaskList, projectRepository.getUserTasks(user1));

		// Clears list
		expResultTaskList.clear();

		// returns an empty list, as the user is not a collaborator
		assertEquals(expResultTaskList, projectRepository.getUserTasks(userNotCollaborator));

		/*
		 * returns an empty list, as the user is a collaborator but doesnt have any task
		 * associated to him
		 */
		assertEquals(expResultTaskList, projectRepository.getUserTasks(user2));

	}

	/**
	 * Tests the getFinishedUserTaskList method. Has to return the finished tasks
	 * from an user.
	 */
	@Test
	public void testGetFinishedUserTaskList() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.setFinishDate(taskDeadlineDateTest3);
		task1.getTaskState().changeToFinished();

		task3.getTaskState().changeToPlanned();
		task3.addProjectCollaboratorToTask(collab1);
		task3.getTaskState().changeToAssigned();
		task3.getTaskState().changeToReady();
		Calendar startDateTask3 = task3.getEstimatedTaskStartDate();
		startDateTask3.add(Calendar.DAY_OF_MONTH, 60);
		task3.setStartDate(startDatetask);
		task3.getTaskState().changeToOnGoing();
		task3.setFinishDate(taskDeadlineDateTest3);
		task3.getTaskState().changeToFinished();

		// Adds user to expResultTaskList
		expResultTaskList.add(task1);
		expResultTaskList.add(task3);

		assertEquals(expResultTaskList, projectRepository.getAllFinishedTasksFromUser(user1));

		// Clears list
		expResultTaskList.clear();

		// returns an empty list, as the user is not a collaborator
		assertEquals(expResultTaskList, projectRepository.getAllFinishedTasksFromUser(userNotCollaborator));

	}

	/**
	 * Tests the getUnifinishedUserTaskList method. Has to return a list with the
	 * unfinished tasks from an user.
	 */
	@Test
	public void testGetUnfinishedUserTaskList() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// Adds user to tasks.
		task2.addTaskCollaboratorToTask(taskWorker1);
		task3.addTaskCollaboratorToTask(taskWorker1);

		// Marks tasks as finished
		task1.setEstimatedTaskStartDate(estimatedStartDate);
		task1.setTaskDeadline(taskDeadlineDateTest);
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDateTask1 = estimatedStartDate;
		startDateTask1.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDateTask1);
		task1.getTaskState().changeToOnGoing();
		task1.markTaskAsFinished();

		// Adds user to expResultTaskList
		expResultTaskList.add(task2);
		expResultTaskList.add(task3);

		assertEquals(expResultTaskList, projectRepository.getUnfinishedUserTaskList(user1));

		// Clears list
		expResultTaskList.clear();

		// returns an empty list, as the user is not a collaborator
		assertEquals(expResultTaskList, projectRepository.getUnfinishedUserTaskList(userNotCollaborator));

	}

	/**
	 * Tests the getLastMonthFinishedUserTaskList by comparing the output of that
	 * method with a list created with tasks finished last month.
	 */
	@Test
	public void testGetLastMonthFinishedUserTaskList() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// create finished date to test
		Calendar startDateTest = Calendar.getInstance();
		startDateTest.set(Calendar.YEAR, 2017);
		startDateTest.set(Calendar.MONTH, Calendar.NOVEMBER);
		startDateTest.set(Calendar.DAY_OF_MONTH, 29);
		startDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create finished date to test
		Calendar finishDateTest = Calendar.getInstance();
		finishDateTest.add(Calendar.MONTH, -1);
		Calendar finishDateTest2 = Calendar.getInstance();
		finishDateTest2.add(Calendar.MONTH, -2);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.setFinishDate(finishDateTest);
		task1.getTaskState().changeToFinished();

		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab2);
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask2 = task2.getEstimatedTaskStartDate();
		startDateTask2.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDatetask);
		task2.getTaskState().changeToOnGoing();
		task2.setFinishDate(finishDateTest);
		task2.getTaskState().changeToFinished();

		task3.getTaskState().changeToPlanned();
		task3.addProjectCollaboratorToTask(collab1);
		task3.getTaskState().changeToAssigned();
		task3.getTaskState().changeToReady();
		Calendar startDateTask3 = task3.getEstimatedTaskStartDate();
		startDateTask3.add(Calendar.DAY_OF_MONTH, 60);
		task3.setStartDate(startDatetask);
		task3.getTaskState().changeToOnGoing();
		task3.setFinishDate(finishDateTest2);
		task3.getTaskState().changeToFinished();

		// Adds user to expResultTaskList
		expResultTaskList.add(task1);
		expResultTaskList.add(task2);

		assertEquals(expResultTaskList, projectRepository.getLastMonthFinishedUserTaskList(user1));

		// Clears list
		expResultTaskList.clear();

		// returns an empty list, as the user is not a collaborator
		assertEquals(expResultTaskList, projectRepository.getLastMonthFinishedUserTaskList(userNotCollaborator));

	}

	/**
	 * Tests the getTotalTimeLastMonthFinishedTasksByUser. The result has to be
	 * equal to the sum of time spent on every task by the user.
	 */
	@Test
	public void testGetTotalTimeLastMonthFinishedTasksByUser() {
		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);

		// Sets a startDate for the tasks

		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MONTH, -3);
		task1.setStartDate(startDate);
		task2.setStartDate(startDate);

		// Marks tasks as finished
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MONTH, -1);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.createReport(task1.getTaskTeam().get(0));
		task1.getReports().get(0).setReportedTime(5);
		task1.setFinishDate(calendar1);
		task1.getTaskState().changeToFinished();

		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab2);
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask2 = task2.getEstimatedTaskStartDate();
		startDateTask2.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDatetask);
		task2.getTaskState().changeToOnGoing();
		task2.createReport(task2.getTaskTeam().get(1));
		task2.getReports().get(0).setReportedTime(10);
		task2.setFinishDate(calendar2);
		task2.getTaskState().changeToFinished();

		assertEquals(15.0, projectRepository.getTotalTimeOfFinishedTasksFromUserLastMonth(user1), 0.000000001);
	}

	/**
	 * Tests the GetAverageTimeLastMonthFinishedTasksUser. The result has to be
	 * equal to the sum of time spent on every task by the user divided by the
	 * number of tasks.
	 */
	@Test
	public void testGetAverageTimeLastMonthFinishedTasksUser() {
		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);

		// Sets a startDate for the tasks

		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MONTH, -3);
		task1.setStartDate(startDate);
		task2.setStartDate(startDate);

		// Marks tasks as finished
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MONTH, -1);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.createReport(task1.getTaskTeam().get(0));
		task1.getReports().get(0).setReportedTime(5);
		task1.setFinishDate(calendar1);
		task1.getTaskState().changeToFinished();

		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab2);
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask2 = task2.getEstimatedTaskStartDate();
		startDateTask2.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDatetask);
		task2.getTaskState().changeToOnGoing();
		task2.createReport(task2.getTaskTeam().get(1));
		task2.getReports().get(0).setReportedTime(10);
		task2.setFinishDate(calendar2);
		task2.getTaskState().changeToFinished();
		assertEquals(7.5, projectRepository.getAverageTimeOfFinishedTasksFromUserLastMonth(user1), 0.000000001);
	}

	/**
	 * Tests the GetLastMonthFinishedUserTaskListDecreasingOrder. Compares the
	 * output of the method to a list with the projects added be decreasing order.
	 */
	@Test
	public void testGetLastMonthFinishedUserTaskListDecreasingOrder() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// create finished date to test
		Calendar startDateTest = Calendar.getInstance();
		startDateTest.set(Calendar.YEAR, 2017);
		startDateTest.set(Calendar.MONTH, Calendar.NOVEMBER);
		startDateTest.set(Calendar.DAY_OF_MONTH, 29);
		startDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create finished date to test
		Calendar finishDateTest = Calendar.getInstance();
		finishDateTest.add(Calendar.MONTH, -1);
		Calendar finishDateTest2 = Calendar.getInstance();
		finishDateTest2.add(Calendar.MONTH, -2);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.setFinishDate(finishDateTest);
		task1.getFinishDate().set(Calendar.DAY_OF_MONTH, 10);
		task1.getTaskState().changeToFinished();

		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab2);
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask2 = task2.getEstimatedTaskStartDate();
		startDateTask2.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDatetask);
		task2.getTaskState().changeToOnGoing();
		task2.setFinishDate(finishDateTest);
		task2.getFinishDate().set(Calendar.DAY_OF_MONTH, 15);
		task2.getTaskState().changeToFinished();

		task3.getTaskState().changeToPlanned();
		task3.addProjectCollaboratorToTask(collab1);
		task3.getTaskState().changeToAssigned();
		task3.getTaskState().changeToReady();
		Calendar startDateTask3 = task3.getEstimatedTaskStartDate();
		startDateTask3.add(Calendar.DAY_OF_MONTH, 60);
		task3.setStartDate(startDatetask);
		task3.getTaskState().changeToOnGoing();
		task3.setFinishDate(finishDateTest2);
		task3.getTaskState().changeToFinished();

		// Adds user to expResultTaskList
		expResultTaskList.add(task2);
		expResultTaskList.add(task1);

		assertEquals(expResultTaskList, projectRepository.getFinishedUserTasksFromLastMonthInDecreasingOrder(user1));
	}

	/**
	 * Tests the GetFinishedTaskListByDecreasingOrder. Compares the output of the
	 * method to a list with the projects added be decreasing order.
	 */
	@Test
	public void testGetFinishedTaskListByDecreasingOrder() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds tasks to project repository.
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// create finished date to test
		Calendar startDateTest = Calendar.getInstance();
		startDateTest.set(Calendar.YEAR, 2017);
		startDateTest.set(Calendar.MONTH, Calendar.NOVEMBER);
		startDateTest.set(Calendar.DAY_OF_MONTH, 29);
		startDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create finished date to test
		Calendar finishDateTest = Calendar.getInstance();
		finishDateTest.add(Calendar.MONTH, -1);

		// prepare the tasks
		task1.getTaskState().changeToPlanned();
		task1.addProjectCollaboratorToTask(collab1);
		task1.addProjectCollaboratorToTask(collab2);
		task1.getTaskState().changeToAssigned();
		task1.getTaskState().changeToReady();
		Calendar startDatetask = task1.getEstimatedTaskStartDate();
		startDatetask.add(Calendar.DAY_OF_MONTH, 60);
		task1.setStartDate(startDatetask);
		task1.getTaskState().changeToOnGoing();
		task1.setFinishDate(finishDateTest);
		task1.getFinishDate().set(Calendar.DAY_OF_MONTH, 10);
		task1.getTaskState().changeToFinished();

		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab2);
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask2 = task2.getEstimatedTaskStartDate();
		startDateTask2.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDatetask);
		task2.getTaskState().changeToOnGoing();
		task2.setFinishDate(finishDateTest);
		task2.getFinishDate().set(Calendar.DAY_OF_MONTH, 15);
		task2.getTaskState().changeToFinished();

		task3.getTaskState().changeToPlanned();
		task3.addProjectCollaboratorToTask(collab1);
		task3.getTaskState().changeToAssigned();
		task3.getTaskState().changeToReady();
		Calendar startDateTask3 = task3.getEstimatedTaskStartDate();
		startDateTask3.add(Calendar.DAY_OF_MONTH, 60);
		task3.setStartDate(startDatetask);
		task3.getTaskState().changeToOnGoing();
		task3.setFinishDate(finishDateTest);
		task3.getFinishDate().set(Calendar.DAY_OF_MONTH, 5);
		task3.getTaskState().changeToFinished();

		// Adds user to expResultTaskList
		expResultTaskList.add(task2);
		expResultTaskList.add(task1);
		expResultTaskList.add(task3);

		assertEquals(expResultTaskList, projectRepository.getAllFinishedUserTasksInDecreasingOrder(user1));
	}

	/**
	 * Tests the SortTaskListDecreasingOrder. Compares the output of the method to a
	 * list with the projects added be decreasing order.
	 */
	@Test
	public void testSortTaskListDecreasingOrder() {

		// Marks tasks as finished and sets a finish date
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2017, Calendar.NOVEMBER, 14);
		task1.setFinishDate(calendar1);
		task1.markTaskAsFinished();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2017, Calendar.NOVEMBER, 17);
		task2.setFinishDate(calendar2);
		task2.markTaskAsFinished();

		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2017, Calendar.SEPTEMBER, 17);
		task3.setFinishDate(calendar3);
		task3.markTaskAsFinished();

		// List with tasks not sorted.
		List<Task> toBeSorted = new ArrayList<Task>();
		toBeSorted.add(task1);
		toBeSorted.add(task2);
		toBeSorted.add(task3);

		// List of sorted tasks.
		expResultTaskList.add(task2);
		expResultTaskList.add(task1);
		expResultTaskList.add(task3);

		assertEquals(expResultTaskList.size(), projectRepository.sortTaskListDecreasingOrder(toBeSorted).size());
		assertEquals(expResultTaskList, projectRepository.sortTaskListDecreasingOrder(toBeSorted));
	}

	/**
	 * test to isProjectInProjectRepository.
	 */
	@Test
	public void test_isProjectInProjectRepository() {
		// before add the project, verify if the project1 is in project repository
		assertFalse(projectRepository.isProjectInProjectRepository(project1));

		// add project 1 to project repository
		projectRepository.addProjectToProjectRepository(project1);

		// verify if project1 it was add to project repository
		assertTrue(projectRepository.isProjectInProjectRepository(project1));
	}

	@Test
	public void getStartedNotFinishedUserTaskInIncreasingDeadlineOrder() {

		// Adds project to project repository
		projectRepository.addProjectToProjectRepository(project1);
		projectRepository.addProjectToProjectRepository(project2);

		// Adds user to project team
		project1.addProjectCollaboratorToProjectTeam(collab1);
		project1.addProjectCollaboratorToProjectTeam(collab2);

		// Adds user 1(collab4) - to project2
		project2.addProjectCollaboratorToProjectTeam(collab4);

		// Adds tasks to project repository 1
		project1.getTaskRepository().addProjectTask(task1);
		project1.getTaskRepository().addProjectTask(task2);
		project1.getTaskRepository().addProjectTask(task3);

		// Adds tasks to project repository 2
		project2.getTaskRepository().addProjectTask(task4);
		project2.getTaskRepository().addProjectTask(task5);
		project2.getTaskRepository().addProjectTask(task6);

		// Adds user1 to tasks in Project 1
		task1.addTaskCollaboratorToTask(taskWorker1);
		task3.addTaskCollaboratorToTask(taskWorker1);

		Calendar estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.set(Calendar.YEAR, 2017);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 25);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);

		task1.setStartDate(estimatedTaskStartDateTest);
		task2.setStartDate(estimatedTaskStartDateTest);
		task3.setStartDate(estimatedTaskStartDateTest);
		task4.setStartDate(estimatedTaskStartDateTest);
		task5.setStartDate(estimatedTaskStartDateTest);
		task6.setStartDate(estimatedTaskStartDateTest);

		// Adds user1 to tasks in Project 2
		task4.addTaskCollaboratorToTask(taskWorker4);
		task5.addTaskCollaboratorToTask(taskWorker4);

		// Marks tasks as finished
		task2.setEstimatedTaskStartDate(estimatedStartDate);
		task2.setTaskDeadline(taskDeadlineDateTest);
		task2.getTaskState().changeToPlanned();
		task2.addProjectCollaboratorToTask(collab1);
		task2.getTaskState().changeToAssigned();
		task2.getTaskState().changeToReady();
		Calendar startDateTask1 = estimatedStartDate;
		startDateTask1.add(Calendar.DAY_OF_MONTH, 60);
		task2.setStartDate(startDateTask1);
		task2.getTaskState().changeToOnGoing();
		task2.markTaskAsFinished();

		task6.setEstimatedTaskStartDate(estimatedStartDate);
		task6.setTaskDeadline(taskDeadlineDateTest);
		task6.getTaskState().changeToPlanned();
		task6.addProjectCollaboratorToTask(collab1);
		task6.getTaskState().changeToAssigned();
		task6.getTaskState().changeToReady();
		Calendar startDateTask2 = estimatedStartDate;
		startDateTask1.add(Calendar.DAY_OF_MONTH, 60);
		task6.setStartDate(startDateTask2);
		task6.getTaskState().changeToOnGoing();
		task6.markTaskAsFinished();

		// creates a new list of tasks in increasingDeadLineOrder
		List<Task> startedNotFinishedTasksInOrder = new ArrayList<>();
		startedNotFinishedTasksInOrder.add(task1);

		startedNotFinishedTasksInOrder.add(task3);
		startedNotFinishedTasksInOrder.add(task4);
		startedNotFinishedTasksInOrder.add(task5);
		System.out.println(task1.getTaskDeadline());
		assertEquals(startedNotFinishedTasksInOrder,
				projectRepository.getStartedNotFinishedUserTasksInIncreasingDeadlineOrder(user1));
		assertEquals(startedNotFinishedTasksInOrder.size(),
				projectRepository.getStartedNotFinishedUserTasksInIncreasingDeadlineOrder(user1).size());

		// clears the list
		startedNotFinishedTasksInOrder.clear();
		// The method returns an empty list, if the user is null
		assertEquals(startedNotFinishedTasksInOrder,
				projectRepository.getStartedNotFinishedUserTasksInIncreasingDeadlineOrder(nullUser));

	}

	/**
	 * Tests the GetProjectFromUser method
	 * 
	 */
	@Test
	public void testGetProjectsFromUser() {

		// Adds three project to the projectRepository
		projectRepository.addProjectToProjectRepository(project2);
		projectRepository.addProjectToProjectRepository(project5);
		projectRepository.addProjectToProjectRepository(project6);

		// add user to projects
		project5.addProjectCollaboratorToProjectTeam(collab1);
		project6.addProjectCollaboratorToProjectTeam(collab1);

		// Creates a list with the project from a user
		List<Project> projectsOfProjectManager = new ArrayList<>();
		projectsOfProjectManager.add(project5);
		projectsOfProjectManager.add(project6);

		// Asserts if the resulted list of the getProjectOfProjectManager method equals
		// the list created with the project from which the user 3 is Project Manager
		assertEquals(projectsOfProjectManager, projectRepository.getProjectsFromUser(user1));
	}

	/**
	 * Tests the getProjById method
	 * 
	 */
	@Test
	public void testGetProjectById() {

		// Adds two project to the projectRepository
		projectA = projectRepository.createProject("ProjA", "ProjectoA", user1);
		projectB = projectRepository.createProject("ProjB", "ProjectoA", user1);
		projectRepository.addProjectToProjectRepository(projectA);
		projectRepository.addProjectToProjectRepository(projectB);
		// projectRepository.addProjectToProjectRepository(projectC);

		// Asserts if the getProjById returns the expected Project
		assertEquals(projectRepository.getProjById(0), projectA);
		assertEquals(projectRepository.getProjById(1), projectB);
		assertEquals(projectRepository.getProjById(3), null);

	}

	/*
	 * Tests the getOnGoingUserTasks
	 */
	@Test
	public void getOnGoingUserReportedTasks() {

	}

}