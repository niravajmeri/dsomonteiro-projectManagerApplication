package sprint.two;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.model.Company;
import project.model.Profile;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.ProjectRepository;
import project.model.Task;
import project.model.TaskRepository;
import project.model.TaskCollaborator;
import project.model.User;
import project.model.UserRepository;

/**
 * @author Group 3
 * 
 *         Test to US370
 * 
 *         US370 - As Project Manager, I want to get a list of completed tasks.
 */
public class US370 {

	Company myCompany;
	UserRepository userRepository;
	User user1;
	User user2;
	Project project;
	ProjectRepository projectRepository;
	TaskRepository taskRepository;
	Task testTask;
	Task testTask2;
	ProjectCollaborator projectCollaborator;
	TaskCollaborator taskWorker;
	TaskCollaborator taskWorker1;

	@Before
	public void setUp() {
		// create company

		myCompany = Company.getTheInstance();

		// creates an UserRepository
		userRepository = myCompany.getUsersRepository();

		// creates a ProjectRepository
		projectRepository = myCompany.getProjectsRepository();

		// creates a UserRepository
		userRepository.getAllUsersFromRepository().clear();

		// clean list ProjectRepository
		myCompany.getProjectsRepository().getAllProjects().clear();

		// clean list URepository
		myCompany.getUsersRepository().getAllUsersFromRepository().clear();

		// create user
		user1 = myCompany.getUsersRepository().createUser("Daniel", "daniel@gmail.com", "001", "collaborator",
				"910000000", "Rua", "2401-00", "Test", "Testo", "Testistan");

		// create user admin
		user2 = myCompany.getUsersRepository().createUser("João", "joao@gmail.com", "001", "Admin", "920000000", "Rua",
				"2401-00", "Test", "Testo", "Testistan");

		// add user to user list
		userRepository.addUserToUserRepository(user1);
		userRepository.addUserToUserRepository(user2);

		// set user as collaborator
		user1.setUserProfile(Profile.COLLABORATOR);
		user2.setUserProfile(Profile.COLLABORATOR);

		// create project
		project = projectRepository.createProject("name3", "description4", user2);

		// create project collaborator
		projectCollaborator = project.createProjectCollaborator(user1, 2);

		// add user to project team
		project.addProjectCollaboratorToProjectTeam(projectCollaborator);

		// create taskRepository
		taskRepository = project.getTaskRepository();

	}

	@After
	public void tearDown() {
		myCompany.clear();
		user1 = null;
		testTask = null;
		project = null;
		projectRepository = null;
		taskRepository = null;
		userRepository = null;
	}

	@Test
	public void testUS370() {
		// create a estimated Task Start Date
		Calendar estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.set(Calendar.YEAR, 2017);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.DECEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 29);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);
		// create a estimated Task Start Date
		Calendar taskDeadlineDateTest = Calendar.getInstance();
		taskDeadlineDateTest.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest.set(Calendar.MONTH, Calendar.JANUARY);
		taskDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);
		// create 2 task
		testTask = taskRepository.createTask("Test dis agen pls", 10, estimatedTaskStartDateTest, taskDeadlineDateTest,
				10);
		testTask2 = taskRepository.createTask("Test dis agen pls", 10, estimatedTaskStartDateTest, taskDeadlineDateTest,
				10);

		// Adds Tasks to TaskRepository
		taskRepository.addProjectTask(testTask);
		taskRepository.addProjectTask(testTask2);

		// create task Worker
		taskWorker = testTask.createTaskCollaborator(projectCollaborator);
		taskWorker1 = testTask2.createTaskCollaborator(projectCollaborator);

		// Adds user1 to the Task
		testTask.addTaskCollaboratorToTask(taskWorker);
		testTask2.addTaskCollaboratorToTask(taskWorker1);

		// start task
		testTask.setStartDate(estimatedTaskStartDateTest);
		testTask2.setStartDate(estimatedTaskStartDateTest);

		// finished task
		testTask.setFinishDate(taskDeadlineDateTest);
		testTask2.setFinishDate(taskDeadlineDateTest);

		// set as finished
		testTask.markTaskAsFinished();
		testTask2.markTaskAsFinished();

		// Creates a new list, and then added the tasks without any user assigned to
		// them
		List<Task> listFinishedTasks = new ArrayList<Task>();
		listFinishedTasks.add(testTask);
		listFinishedTasks.add(testTask2);

		assertEquals(listFinishedTasks, taskRepository.getFinishedTasks());

	}

}
