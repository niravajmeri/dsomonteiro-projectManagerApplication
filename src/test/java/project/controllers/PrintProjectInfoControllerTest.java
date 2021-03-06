package project.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import project.model.*;
import project.model.costcalculationinterface.CostCalculationFactory;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackages = {"project.services", "project.controllers", "project.model", "project.security", "project.dto"})
public class PrintProjectInfoControllerTest {

	User user1;
	User joaoPM;
	ProjectCollaborator collab1, collab2;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

	@Autowired
	ProjectService projectContainer;
	@Autowired
	UserService userContainer;
	@Autowired
	TaskService taskService;

	Project project, project1;
	Calendar startDate, finishDate;
	Task task1, task2, task3;

    String first, last, average, currently, expectedOptions;

	@Autowired
	PrintProjectInfoController controller;

	@Before
	public void setUp() {

		user1 = userContainer.createUser("Daniel", "daniel@gmail.com", "001", "collaborator", "910000000", "Rua",
				"2401-00", "Test", "Testo", "Testistan");

		// create user admin
		joaoPM = userContainer.createUser("João", "joao@gmail.com", "001", "Admin", "920000000", "Rua", "2401-00",
				"Test", "Testo", "Testistan");

		// creates project repository
		// projectContainer = myCompany.getProjectsContainer();

		// Creates one Project
		project = projectContainer.createProject("Projeto de gestão", "Este projeto está focado na gestão.", joaoPM);

		project.setProjectBudget(1000);
		project.setProjectStatus(3);

		// add project to project repository
		projectContainer.updateProject(project);

		// add start date to project
		Calendar startDate = Calendar.getInstance();
		startDate.set(2017, Calendar.JANUARY, 2, 12, 31, 00);
		project.setStartdate(startDate);

		// add finish date to project
		Calendar finishDate = Calendar.getInstance();
		finishDate.set(2017, Calendar.FEBRUARY, 2, 12, 31, 00);
		project.setFinishdate(finishDate);

		// create project collaborators
		collab1 = projectContainer.createProjectCollaborator(user1, project, 2);
		collab2 = projectContainer.createProjectCollaborator(joaoPM, project, 3);

		// set user as collaborator
		user1.setUserProfile(Profile.COLLABORATOR);
		joaoPM.setUserProfile(Profile.COLLABORATOR);

		// create three tasks
		task1 = taskService.createTask("First task", project);
		task2 = taskService.createTask("Second task", project);
		task3 = taskService.createTask("Third task", project);

		// add project's collaborators to tasks
		task1.addProjectCollaboratorToTask(collab1);
		task2.addProjectCollaboratorToTask(collab1);
		task3.addProjectCollaboratorToTask(collab2);

		taskService.saveTask(task1);
		taskService.saveTask(task2);
		taskService.saveTask(task3);


		first ="[1] - The user's first cost [ENABLED]\n";
		last ="[2] - The user's last cost [ENABLED]\n";
		average="[3] - Average between all of the user's costs [ENABLED]\n";
		currently="(Currently: 1)";
		expectedOptions=first+last+average+currently;


		// Instantiates de controllers
		controller.setProject(project);



	}

	@After
	public void clear() {

		user1 = null;
		joaoPM = null;
		collab1 = null;
		collab2 = null;

		project = null;
		project1 = null;
		startDate = null;
		finishDate = null;
		task1 = null;
		task2 = null;
		task3 = null;

	}

	/**
	 * Tests if the method of controllers gets the project's name
	 */
	@Test
	public void testPrintProjectNameInfo() {

		assertEquals(controller.printProjectNameInfo(), "Projeto de gestão");
	}

	/**
	 * Tests if the method of controllers gets the project's ID
	 */
	@Test
	public void testPrintProjectIDCodeInfo() {

		String projectID = String.valueOf(project.getIdCode());

		assertEquals(controller.printProjectIDCodeInfo(), projectID);
	}

	/**
	 * Tests if the method of controllers gets the project's status
	 */
	@Test
	public void testPrintProjectStatusInfo() {

		assertEquals(controller.printProjectStatusInfo(), "3");
	}

	/**
	 * Tests if the method of controllers gets the project's description
	 */
	@Test
	public void testPrintProjectDescriptionInfo() {

		assertEquals(controller.printProjectDescriptionInfo(), "Este projeto está focado na gestão.");
	}

	/**
	 * Tests if the method of controllers gets the project's start date
	 */
	@Test
	public void testPrintProjectStartDateInfo() {

		assertEquals(controller.printProjectStartDateInfo(), "Mon, 2 Jan 2017 12:31:00");
	}

	/**
	 * Tests if the method of controllers gets the project's finish date
	 */
	@Test
	public void testPrintProjectFinishDateInfo() {

		assertEquals(controller.printProjectFinishDateInfo(), "Thu, 2 Feb 2017 12:31:00");
	}

	/**
	 * Tests if the method of controllers gets the project's manager
	 */
	@Test
	public void testPrintProjectManagerInfo() {

		assertEquals(controller.printProjectManagerInfo(), "João");
	}

	/**
	 * Tests if the method of controllers gets the project's team
	 */
	@Test
	public void testPrintProjectTeamInfo() {

		assertEquals(controller.printProjectTeamInfo(), "Daniel [ACTIVE], João [ACTIVE]");
	}

	/**
	 * Tests if the method of controllers gets the project's budget
	 */
	@Test
	public void testPrintProjectBudgetInfo() {

		assertEquals(controller.printProjectBudgetInfo(), "1000.0"); 
	}

	/**
	 * Tests if the method of controllers gets project's available cost calculation methods
	 * as well as the present calculation method
	 */
	@Test
	public void testPrintProjectCostCalculationInfo() {
		// given a new project

		// when no action is taken,
		// then the cost calculation should return the default values

		assertEquals(expectedOptions, controller.printCostCalculationMethods());

		List<CostCalculationFactory.Method> expected = project.listAvaliableCalculationMethods();
		expected.remove(CostCalculationFactory.Method.CI);

		List<Integer> expectedCodes = expected.stream().map(CostCalculationFactory.Method::getCode).collect(Collectors.toList());

		project.createAvailableCalculationMethodsString(expectedCodes);
		project.setCalculationMethod(CostCalculationFactory.Method.CF);
		projectContainer.updateProject(project);
		controller.setProject(project);

        first ="[1] - The user's first cost [DISABLED]\n";
        currently="(Currently: 2)";

        expectedOptions=first+last+average+currently;

        // when First Collaborator Cost option has been disabled
        // then the controller should print that option as [Disabled], and current cost as 2
        assertEquals(2, expected.size());
        assertFalse(project.isCalculationMethodAllowed(CostCalculationFactory.Method.CI.getCode()));
        assertEquals(expectedOptions, controller.printCostCalculationMethods());


    }


	/**
	 * Tests if the method of controllers gets the project's task list
	 */
	@Test
	public void testGetProjectTaskList() {

		// create a list of Strings with ID and description of task, to compare in
		// assert
		Integer projectID = project.getIdCode();

		List<String> toCompare = new ArrayList<>();
		toCompare.add("[" + projectID + ".1] First task");
		toCompare.add("[" + projectID + ".2] Second task");
		toCompare.add("[" + projectID + ".3] Third task");

		assertEquals(controller.getProjectTaskList(), toCompare);
	}

	/**
	 * Tests if the method of controllers gets the project's task list IDs
	 */
	@Test
	public void testGetTasksIDs() {

		Integer projectID = project.getIdCode();

		// create a list of Strings with ID of task, to compare in assert
		List<String> toCompare = new ArrayList<>();
		toCompare.add(projectID + ".1");
		toCompare.add(projectID + ".2");
		toCompare.add(projectID + ".3");

		assertEquals(controller.getTasksIDs(), toCompare);
	}

	/**
	 * Tests if the method of controllers gets the project's tasks
	 */
	@Test
	public void testGetTasks() {

		// create a list of tasks, to compare in assert
		List<Task> toCompare = new ArrayList<>();
		toCompare.add(task1);
		toCompare.add(task2);
		toCompare.add(task3);

		assertEquals(controller.getTasks(), toCompare);
	}

	@Test
	public void getProjectNameByID() {

		Integer projectID = project.getIdCode();

		// create controllers
		// controllers = new PrintProjectInfoController(projectID);
		// controllers.projService=this.projectContainer;
		// controllers.taskService=this.taskService;
		// controllers.setProject();

		String projectName = controller.printProjectNameInfo();

		assertEquals("Projeto de gestão", projectName);

	}

	@Test
	public void testPrintProjectInfoController() {

		// Creates one Project
		project1 = projectContainer.createProject("Projeto de voluntariado",
				"Este projeto está focado em solidariedade.", user1);

		// Instantiates de controllers
		controller.setProject(project1);

		assertEquals(controller.printProjectNameInfo(), "Projeto de voluntariado");

		// add user to project team
		projectContainer.createProjectCollaborator(user1, project1, 10);
		ProjectCollaborator joaoPMcolab = projectContainer.createProjectCollaborator(joaoPM, project1, 10);

		// remove user from project team
		joaoPMcolab.setStatus(false);
		joaoPMcolab.setFinishDate(Calendar.getInstance());
		assertFalse(joaoPMcolab.isProjectCollaboratorActive());

		projectContainer.updateProject(project1);
		projectContainer.updateProjectCollaborator(joaoPMcolab);

		assertEquals(controller.printProjectTeamInfo(), "Daniel [ACTIVE], João [INACTIVE]");

	}

	@Test
	public void testConstructorAndSetter(){


		// Creates one Project
		Project project2 = projectContainer.createProject("Projeto de teste",
				"Este projeto está focado em solidariedade.", user1);

		System.out.println(project2.getProjectId());
		System.out.println(project2.getIdCode());



		controller.setProjectByProjID(project2.getProjectId());


		assertEquals(controller.printProjectNameInfo(), "Projeto de teste");

		String projectName = "" + "\n" + "PROJECT " + "PROJETO DE TESTE" + "\n";
		String line = "__________________________________________\n";
		String idAndStatus = "ID: " + project2.getProjectId() + "\n" + "STATUS: " + controller.printProjectStatusInfo() + "\n";
		String description = "DESCRIPTION: " + controller.printProjectDescriptionInfo() + "\n";
		String dates = "START DATE: " + controller.printProjectStartDateInfo() + "\n " + "FINISH DATE: " + controller.printProjectFinishDateInfo() + "\n";
		String projectManager = "PROJECT MANAGER: " + controller.printProjectManagerInfo() + "\n";
		String projectTeam = "PROJECT TEAM: " + controller.printProjectTeamInfo() + "\n";
		String budget = "PROJECT BUDGET: " + controller.printProjectBudgetInfo() + "\n";
		String tasks = "TASKS OF " + controller.printProjectNameInfo().toUpperCase() + ":" + "\n";

		String expected = projectName + line + idAndStatus + description + dates + projectManager + projectTeam + budget + tasks;

		assertEquals(expected, controller.getAllProjectInfo());



	}

	}


