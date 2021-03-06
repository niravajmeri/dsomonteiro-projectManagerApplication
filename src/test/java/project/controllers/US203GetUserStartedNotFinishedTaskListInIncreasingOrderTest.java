package project.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import project.model.*;
import project.model.taskstateinterface.OnGoing;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import java.util.Calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "project.services", "project.model", "project.controllers" })
public class US203GetUserStartedNotFinishedTaskListInIncreasingOrderTest {
	@Autowired
	UserService userContainer;
	@Autowired
	ProjectService projectContainer;
	@Autowired
	TaskService taskService;
	@Autowired
	US203GetUserStartedNotFinishedTaskListInIncreasingOrderController tasksFiltersController;

	User user1, user2, user3;
	Project project1;
	ProjectCollaborator projCollab1, projCollab2, projCollab3;
	Task task1, task2, task3, task4, task5, task6;

	@Before
	public void setUp() {

		// create users in UserContainer
		user2 = userContainer.createUser("João", "user2@gmail.com", "001", "Manager", "930025000", "rua doutor antónio",
				"7689-654", "porto", "porto", "portugal");
		user1 = userContainer.createUser("Juni", "user3@gmail.com", "002", "Code Monkey", "930000000",
				"rua engenheiro joão", "789-654", "porto", "porto", "portugal");

		// change profiles of users from VISITOR (default) to COLLABORATOR
		user2.setUserProfile(Profile.COLLABORATOR);
		user1.setUserProfile(Profile.COLLABORATOR);

		userContainer.addUserToUserRepositoryX(user2);
		userContainer.addUserToUserRepositoryX(user1);

		// create project 1 in company 1
		project1 = projectContainer.createProject("name3", "description4", user2);

		// create an estimated Task Start Date
		Calendar estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.add(Calendar.YEAR, -2);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 25);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create an estimated Task Dead line Date, from earliest to most latest
		Calendar taskDeadlineDateTest1 = Calendar.getInstance();
		taskDeadlineDateTest1.add(Calendar.YEAR, 2);
		taskDeadlineDateTest1.set(Calendar.MONTH, Calendar.JANUARY);

		Calendar taskDeadlineDateTest2 = Calendar.getInstance();
		taskDeadlineDateTest2.add(Calendar.YEAR, 2);
		taskDeadlineDateTest2.set(Calendar.MONTH, Calendar.FEBRUARY);

		Calendar taskDeadlineDateTest3 = Calendar.getInstance();
		taskDeadlineDateTest3.add(Calendar.YEAR, 2);
		taskDeadlineDateTest3.set(Calendar.MONTH, Calendar.MARCH);

		// create a Date before to the previous Dead line created in order to result in
		// an expired Task
		Calendar taskExpiredDeadlineDateTest = Calendar.getInstance();
		taskExpiredDeadlineDateTest.add(Calendar.YEAR, -2);
		taskExpiredDeadlineDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		taskExpiredDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskExpiredDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);

		int taskEffortAndBudget = 10;

		// create tasks in project 1
		// tasks 1 and 2 have the earliest, non expired deadlines
		task1 = taskService.createTask("Do this", project1);
        task1.setStartDateAndState(estimatedTaskStartDateTest);
		task1.setTaskDeadline(taskDeadlineDateTest1);
		task1.setTaskBudget(taskEffortAndBudget);
		task1.setEstimatedTaskEffort(taskEffortAndBudget);

		task2 = taskService.createTask("Do that", project1);
        task2.setStartDateAndState(estimatedTaskStartDateTest);
		task2.setTaskDeadline(taskDeadlineDateTest2);
		task2.setTaskBudget(taskEffortAndBudget);
		task2.setEstimatedTaskEffort(taskEffortAndBudget);

		// task 3 is expired
		task3 = taskService.createTask("[Expired] Merge everything", project1);
        task3.setStartDateAndState(estimatedTaskStartDateTest);
		task3.setTaskDeadline(taskExpiredDeadlineDateTest);
		task3.setTaskBudget(taskEffortAndBudget);
		task3.setEstimatedTaskEffort(taskEffortAndBudget);

		// task 4 expires after task 2
		task4 = taskService.createTask("Do this", project1);
        task4.setStartDateAndState(estimatedTaskStartDateTest);
		task4.setTaskDeadline(taskDeadlineDateTest3);
		task4.setTaskBudget(taskEffortAndBudget);
		task4.setEstimatedTaskEffort(taskEffortAndBudget);

		// task 5 is expired
		task5 = taskService.createTask("Do this", project1);
        task5.setStartDateAndState(estimatedTaskStartDateTest);
		task5.setTaskDeadline(taskExpiredDeadlineDateTest);
		task5.setTaskBudget(taskEffortAndBudget);
		task5.setEstimatedTaskEffort(taskEffortAndBudget);

		// and task 6 has no deadline or start date
		task6 = taskService.createTask("Testing a task with deadlinenull", project1);
		task6.setTaskBudget(taskEffortAndBudget);
		task6.setEstimatedTaskEffort(taskEffortAndBudget);

		// add costPerEffort to users in project 1, resulting in a Project Collaborator
		// for each one
		projCollab1 = projectContainer.createProjectCollaborator(user1, project1, 250);
		projCollab2 = projectContainer.createProjectCollaborator(user2, project1, 120);

		// set the state of the tasks

		// task 2 and 3 are ongoing, 3 is expired
		task2.addProjectCollaboratorToTask(projCollab1);
        task2.setStartDateAndState(Calendar.getInstance());

		task3.addProjectCollaboratorToTask(projCollab1);
        task3.setStartDateAndState(Calendar.getInstance());

		// task 4 is ongoing
		task4.addProjectCollaboratorToTask(projCollab1);
        task4.setStartDateAndState(Calendar.getInstance());

		// task 5 is finished
		task5.addProjectCollaboratorToTask(projCollab1);
        task5.setStartDateAndState(Calendar.getInstance());
		task5.markTaskAsFinished();

		// task 6 is planned due to no deadline
		task6.addProjectCollaboratorToTask(projCollab1);

		task2.setTaskState(new OnGoing());
		task2.setCurrentState(StateEnum.ONGOING);
		task3.setTaskState(new OnGoing());
		task3.setCurrentState(StateEnum.ONGOING);
		task4.setTaskState(new OnGoing());
		task4.setCurrentState(StateEnum.ONGOING);

		taskService.saveTask(task1);
		taskService.saveTask(task2);
		taskService.saveTask(task3);
		taskService.saveTask(task4);
		taskService.saveTask(task5);
		taskService.saveTask(task6);

		projectContainer.updateProjectCollaborator(projCollab1);
		projectContainer.updateProjectCollaborator(projCollab2);

	}

	/**
	 * 
	 * US203v02 - Como colaborador, eu pretendo consultar a minha lista de tarefas
	 * iniciadas mas não concluidas de modo a saber o que tenho para fazer hoje. Se
	 * tiverem data limite quero-as ordenadas por ordem crescente de data limite
	 */

	@Test
	public void testGetUserStartedNotFinishedTaskList() {
		assertTrue(task3.isProjectCollaboratorActiveInTaskTeam(projCollab1));

		assertTrue("OnGoing".equals(task3.viewTaskStateName()));
		assertTrue(task3.getStartDate() != null);
		assertTrue(task3.getTaskDeadline() != null);

		// asserts the list contains five tasks, and the first two are the ones with the
		// earliest deadline
		assertEquals(3, tasksFiltersController.getUserStartedNotFinishedTaskListInIncreasingOrder(user1).size());
		assertTrue(
				task3.equals(tasksFiltersController.getUserStartedNotFinishedTaskListInIncreasingOrder(user1).get(0)));
		assertTrue(
				task2.equals(tasksFiltersController.getUserStartedNotFinishedTaskListInIncreasingOrder(user1).get(1)));
		assertTrue(
				task4.equals(tasksFiltersController.getUserStartedNotFinishedTaskListInIncreasingOrder(user1).get(2)));
	}
}