package project.restControllers;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.dto.UserDTO;
import project.model.*;
import project.repository.ProjCollabRepository;
import project.repository.ProjectsRepository;
import project.repository.TaskRepository;
import project.repository.UserRepository;
import project.restcontroller.RestRequestController;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)

public class RestRequestControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectsRepository projectsRepository;

    @Mock
    private ProjCollabRepository projCollabRepository;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private ProjectService projectService;
    @InjectMocks
    private TaskService taskService;

    private RestRequestController controller;

    private User userPM;
    private User userTwo;
    private int userTwoId;
    private User userThree;
    private String userThreeEmail;
    private User userFour;
    private String userFourEmail;
    private Project projectOne;
    private Integer projectId;
    private Task taskOne;
    private String taskIdOne;
    private Task taskTwo;
    private String taskIdTwo;
    private ProjectCollaborator projCollabTwo;
    private ProjectCollaborator projCollabThree;

    private UserDTO userDTOTwo;


    @Before
    public void setUp(){

        // create userPM
        userPM = userService.createUser("Ana", "ana@gmail.com", "01", "collaborator", "221238442", "Rua Porto",
                "4480", "Porto", "Porto", "Portugal");

        // create userTwo
        userTwo = userService.createUser("Joao", "joao@gmail.com", "02", "collaborator", "221238447", "Rua Porto",
                "5480", "Porto", "Porto", "Portugal");
        userTwoId = 2;

        // create userThree
        userThree = userService.createUser("Rui", "rui@gmail.com", "03", "collaborator", "221378449", "Rua Porto",
                "4480", "Porto", "Porto", "Portugal");
        userThreeEmail = "rui@gmail.com";

        // create userThree
        userFour = userService.createUser("Rita", "rita@gmail.com", "04", "collaborator", "221378448", "Rua Porto",
                "6480", "Porto", "Porto", "Portugal");
        userFourEmail = "rita@gmail.com";

        // create a project with userPM as manager
        projectOne = projectService.createProject("Restful", "Implement API Rest", userPM);
        projectId = 1;


        // create 2 tasks
        taskOne = taskService.createTask("Rest Controller", projectOne);
        taskIdOne = "1.1";

        taskTwo = taskService.createTask("Rest Controller Test", projectOne);
        taskIdTwo = "1.2";

        // add users to projectOne creating a project collaborator for each
        projCollabTwo = projectService.createProjectCollaborator(userTwo, projectOne, 20);
        projCollabThree = projectService.createProjectCollaborator(userThree, projectOne, 60);

        // initialing the rest controller
        controller = new RestRequestController(userService, taskService, projectService);

        // building mockMVC
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        //creating an expected list of project collaborators from projectOne
        List<ProjectCollaborator> projCollabsList = new ArrayList<>();
        projCollabsList.add(projCollabTwo);
        projCollabsList.add(projCollabThree);

        //creating an expected list of project collaborators of userTwo
        List<ProjectCollaborator> userTwoProjCollab = new ArrayList<>();
        userTwoProjCollab.add(projCollabTwo);

        when(userRepository.findByEmail("joao@gmail.com")).thenReturn(Optional.of(userTwo));
        when(userRepository.findByEmail(userThreeEmail)).thenReturn(Optional.of(userThree));
        when(userRepository.findByEmail(userFourEmail)).thenReturn(Optional.of(userFour));
        when(userRepository.findByUserID(userTwoId)).thenReturn(Optional.of(userTwo));
        when(projectsRepository.findByProjectId(projectId)).thenReturn(Optional.of(projectOne));
        when(projCollabRepository.findAllByCollaborator(userTwo)).thenReturn(userTwoProjCollab);
        when(projCollabRepository.findAllByProject(projectOne)).thenReturn(projCollabsList);
        when(taskRepository.findByTaskID(taskIdOne)).thenReturn(Optional.of(taskOne));

        userDTOTwo = new UserDTO(userTwo);



    }
    @After
    public void tearDown() {
        controller = null;
        userPM = null;
        userTwo = null;
        userTwoId = 0;
        userThree = null;
        userThreeEmail = null;
        projectOne = null;
        projectId = null;
        taskOne = null;
        taskIdOne = null;
        taskTwo = null;
        taskIdTwo = null;
        projCollabTwo = null;
        projCollabThree = null;
    }

    @Test
    public void controllerInitializedCorrectly() {
        assertNotNull(controller);
    }

    /**
     * Given
     * TaskOne in projectOne, with no collaborators, neither requests
     *
     * When
     * Creating assignment request to taskOne from userTwo
     *
     * Then
     * It is expected to be successfully created
     *
     */
    @Test
    public void canCreateAnAssignmentRequest() {

        //Given

        Gson gson = new Gson();
        String userDTOjson = gson.toJson(userDTOTwo);



        //When
        ResponseEntity<TaskTeamRequest> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

/*        MvcResult resultTwo = mockMvc.perform(
                                                        post("/projects/" + projectId + "/tasks/" + taskIdOne + "/requests/assignmentRequest")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(userDTOjson))
                .andReturn();*/

//        MockHttpServletResponse test = mockMvc.perform(post("/projects/" + projectId + "/tasks/" + taskIdOne + "/requests/assignmentRequest")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(userDTOjson))
//                .andReturn().getResponse();


        //Then
        TaskTeamRequest assignRequest = new TaskTeamRequest();
        assignRequest.setType(RequestType.ASSIGNMENT);
        assignRequest.setProjCollab(projCollabTwo);
        assignRequest.setTask(taskOne);

        //ResponseEntity<TaskTeamRequest> expected = new ResponseEntity<>(assignRequest, HttpStatus.CREATED);

        //assertEquals(expected,result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(assignRequest, result.getBody());
        //assertEquals(HttpStatus.CREATED.value(), resultTwo.getResponse().getStatus());

    }

    /**
     * Given
     * Assignment Request created for userTwo
     *
     * When
     * Creating again assignment request that already exists for userTwo
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateAnAssignmentRequest() {



        //Given
        /*
        mockMvc.perform(
                post("/projects/" + projectId + "/tasks/" + taskIdOne + "/CreateAssignmentRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userEmail", userTwoId))
                .andReturn().getResponse();
        */
        //or could be created calling the method of the rest controller
        controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

        //When
        ResponseEntity<?> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * Adding userTwo to taskOne
     *
     * When
     * Creating assignment request for userTwo but already is added to taskOne
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateAnAssignmentRequestTwo() {



        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);

        //When
        ResponseEntity<?> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * Assignment Request created for userTwo in taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for all existing requests in taskOne
     *
     * Then
     * Expects an OK message and a list of existing requests
     *
     */
    @Test
    public  void retrieveAllRequest() {


        // Given
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskAssignmentRequest(projCollabTwo);
        taskOne.createTaskRemovalRequest(projCollabThree);

        // When
        ResponseEntity<List<TaskTeamRequest>> result = controller.getAllRequests(taskIdOne, projectId);

        // Then
        List <TaskTeamRequest> expectedList = new ArrayList<>();
        TaskTeamRequest assign = new TaskTeamRequest();
        assign.setType(RequestType.ASSIGNMENT);
        assign.setProjCollab(projCollabTwo);
        assign.setTask(taskOne);
        TaskTeamRequest removal = new TaskTeamRequest();
        removal.setType(RequestType.REMOVAL);
        removal.setProjCollab(projCollabThree);
        removal.setTask(taskOne);

        expectedList.add(assign);
        expectedList.add(removal);

        ResponseEntity<List<TaskTeamRequest>> expected = new ResponseEntity<>(expectedList, HttpStatus.OK);

        assertEquals(expected,result);

    }

    /**
     * Given
     * Adding userThree to taskOne
     * Assignment Request created for userTwo in taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for all existing ASSIGNMENT requests in taskOne
     *
     * Then
     * Expects an OK message and a list of existing ASSIGNMENT requests
     *
     */
    @Test
    public  void retrieveAllAssignmentRequest() {

        // Given

        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskAssignmentRequest(projCollabTwo);
        taskOne.createTaskRemovalRequest(projCollabThree);

        // When

        ResponseEntity<List<TaskTeamRequest>> result = controller.getAllFilteredRequests("assignment", taskIdOne, projectId);

        // Then

        List <TaskTeamRequest> expectedList = new ArrayList<>();

        TaskTeamRequest assign = new TaskTeamRequest();
        assign.setType(RequestType.ASSIGNMENT);
        assign.setProjCollab(projCollabTwo);
        assign.setTask(taskOne);

        expectedList.add(assign);

        ResponseEntity<List<TaskTeamRequest>> expected = new ResponseEntity<>(expectedList, HttpStatus.OK);

        assertEquals(expected,result);

    }

    /**
     * Given
     * Adding userThree to taskOne
     * Adding userThree to taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for all existing REMOVAL requests in taskOne
     *
     * Then
     * Expects an OK message and a list of existing REMOVAL requests
     *
     */
    @Test
    public  void retrieveAllRemovalRequest() {

        // Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskRemovalRequest(projCollabThree);

        // When

        ResponseEntity<List<TaskTeamRequest>> result = controller.getAllFilteredRequests("removal", taskIdOne,  projectId);

        // Then

        List <TaskTeamRequest> expectedList = new ArrayList<>();

        TaskTeamRequest removal = new TaskTeamRequest();
        removal.setType(RequestType.REMOVAL);
        removal.setProjCollab(projCollabThree);
        removal.setTask(taskOne);

        expectedList.add(removal);

        ResponseEntity<List<TaskTeamRequest>> expected = new ResponseEntity<>(expectedList, HttpStatus.OK);

        //assertEquals(expected.getStatusCode(),result.getStatusCode());
        assertEquals(expected,result);


    }

    /**
     * Given
     * Adding userThree to taskOne
     * Assignment Request created for userTwo in taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for an existing request in taskOne
     *
     * Then
     * Expects an OK message and the specific request
     *
     */
    @Test
    public  void retrieveOneRequest() {


        // Given
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskAssignmentRequest(projCollabTwo);
        taskOne.createTaskRemovalRequest(projCollabThree);

        taskOne.getPendingTaskTeamRequests().get(0).setTaskRequestDbId(1);
        taskOne.getPendingTaskTeamRequests().get(1).setTaskRequestDbId(2);
        //int removalID = taskOne.getPendingTaskRemovalRequests().get(0).getDbId();

        int assignID = taskOne.getPendingTaskTeamRequests().get(0).getDbId();
        int removalID = taskOne.getPendingTaskTeamRequests().get(1).getDbId();

        List <TaskTeamRequest>expectedList = taskOne.getPendingTaskTeamRequests();
        System.out.println(expectedList);


        //System.out.println(assignID);
        //System.out.println(removalID);

        // When
        ResponseEntity<TaskTeamRequest> resultAssign = controller.getRequestDetails(assignID, taskIdOne, projectId);
        ResponseEntity<TaskTeamRequest> resultRemoval = controller.getRequestDetails(removalID, taskIdOne, projectId);

        // Then
        TaskTeamRequest assignRequest = new TaskTeamRequest();
        assignRequest.setType(RequestType.ASSIGNMENT);
        assignRequest.setProjCollab(projCollabTwo);
        assignRequest.setTask(taskOne);

        TaskTeamRequest removalRequest = new TaskTeamRequest();
        removalRequest.setType(RequestType.REMOVAL);
        removalRequest.setProjCollab(projCollabThree);
        removalRequest.setTask(taskOne);


        ResponseEntity<TaskTeamRequest> expectedAssign = new ResponseEntity<>(assignRequest, HttpStatus.OK);

        assertEquals(expectedAssign,resultAssign);

        ResponseEntity<TaskTeamRequest> expectedRemoval = new ResponseEntity<>(removalRequest, HttpStatus.OK);

        assertEquals(expectedRemoval,resultRemoval);

    }

    /**
     * Given
     * Adding userThree to taskOne
     * Assignment Request created for userTwo in taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for an invalid request in taskOne
     *
     * Then
     * Expects a NOT_FOUND message
     *
     */
    @Test
    public  void retrieveOneRequestTwo() {


        // Given
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskAssignmentRequest(projCollabTwo);
        taskOne.createTaskRemovalRequest(projCollabThree);

        int assignID = taskOne.getPendingTaskAssignmentRequests().get(0).getDbId();
        int removalID = taskOne.getPendingTaskRemovalRequests().get(0).getDbId();
        int invalidID = 1000000000;

        // When
        ResponseEntity<TaskTeamRequest> resultInvalid = controller.getRequestDetails(invalidID, taskIdOne, projectId);

        // Then
        ResponseEntity<TaskTeamRequest> expectedInvalid = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        assertEquals(expectedInvalid ,resultInvalid);



    }

    /**
     * Given
     * TaskOne in projectOne
     * Cancel task
     *
     * When
     * Creating assignment request to taskOne from user
     *
     * Then
     * Expects a METHOD_NOT_ALLOWED message
     *
     */
    @Test
    public void canNotCreateAnAssignmentRequestInvalidTask() {

        //Given


        taskOne.setEstimatedTaskStartDate(Calendar.getInstance());
        taskOne.setTaskDeadline(Calendar.getInstance());
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.setTaskBudget(2.0);
        taskOne.setEstimatedTaskEffort(30.1);
        taskOne.setStartDateAndState(Calendar.getInstance());
        taskOne.cancelTask();
        System.out.println(taskOne.getTaskState());

        //When
        ResponseEntity<TaskTeamRequest> resultInvalid = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);


        //Then
        ResponseEntity<TaskTeamRequest> expectedInvalid = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        assertEquals(expectedInvalid ,resultInvalid);


    }

    /**
     * Given
     * Adding userTwo to taskOne
     *
     * When
     * Creating removal request for userTwo but already is added to taskOne
     *
     * Then
     * It is expected to be successfully created
     *
     */
    @Test
    public void canCreateARemovalRequestTwo() {


        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);

        //When
        ResponseEntity<?> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);

        //Then
        TaskTeamRequest removalRequest = new TaskTeamRequest();
        removalRequest.setType(RequestType.REMOVAL);
        removalRequest.setProjCollab(projCollabTwo);
        removalRequest.setTask(taskOne);


        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(removalRequest, result.getBody());

    }

    /**
     * Given
     * Adding userTwo to taskOne
     * Removal Request created for userTwo
     *
     * When
     * Creating again removal request that already exists for userTwo
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateARemovalRequest() {



        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);
        controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);

        //When
        ResponseEntity<?> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * TaskOne in projectOne, with no collaborators, neither requests
     *
     * When
     * Creating removal request for userTwo that is not assigned in taskOne
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateARemovalRequestTwo() {


        //Given
        controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);

        //When
        ResponseEntity<?> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * TaskOne in projectOne
     * Adding userTwo to taskOne
     * Cancel task
     *
     * When
     * Creating assignment request to taskOne from user
     *
     * Then
     * Expects a METHOD_NOT_ALLOWED message
     *
     */
    @Test
    public void canNotCreateARemovalRequestInvalidTask() {

        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);


        taskOne.setEstimatedTaskStartDate(Calendar.getInstance());
        taskOne.setTaskDeadline(Calendar.getInstance());
        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.setTaskBudget(2.0);
        taskOne.setEstimatedTaskEffort(30.1);
        taskOne.setStartDateAndState(Calendar.getInstance());
        taskOne.cancelTask();
        //System.out.println(taskOne.getTaskState());

        //When
        ResponseEntity<TaskTeamRequest> resultInvalid = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);


        //Then
        ResponseEntity<TaskTeamRequest> expectedInvalid = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        assertEquals(expectedInvalid ,resultInvalid);


    }


    /**
     * Given
     * Adding userThree to taskOne
     * Assignment Request created for userTwo in taskOne
     * Removal Request created for userThree in taskOne
     *
     * When
     * Asking for not exiting request type in taskOne
     *
     * Then
     * Expects an OK message and a list of existing ASSIGNMENT requests
     *
     */
    @Test
    public  void retrieveInvalidFilteredRequest() {

        // Given

        taskOne.addProjectCollaboratorToTask(projCollabThree);
        taskOne.createTaskAssignmentRequest(projCollabTwo);
        taskOne.createTaskRemovalRequest(projCollabThree);

        // When

        ResponseEntity<List<TaskTeamRequest>> result = controller.getAllFilteredRequests("invalid", taskIdOne, projectId);

        // Then


        ResponseEntity<?> expected = new ResponseEntity<>(HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * UserFour not in project
     *
     * When
     * Creating assignment request for userFour
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateAnAssignmentRequestNoProjectCollaborator() {

        //Given
        UserDTO userDTOFour = new UserDTO("Rita", "rita@gmail.com", "04", "collaborator", "221378448",  "", "");
        userDTOFour.setPassword("123456");

        //When
        ResponseEntity<?> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOFour);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);


    }

    /**
     * Given
     * UserFour not in project
     *
     * When
     * Creating removal request for userFour
     *
     * Then
     * Expects a FORBIDDEN message
     *
     */
    @Test
    public void canNotCreateARemovalRequestNoProjectCollaborator() {

        //Given
        UserDTO userDTOFour = new UserDTO("Rita", "rita@gmail.com", "04", "collaborator", "221378448",  "", "");
        userDTOFour.setPassword("123456");


        //When
        ResponseEntity<?> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOFour);

        //Then
        ResponseEntity<?> expected = new ResponseEntity<>( HttpStatus.FORBIDDEN);

        assertEquals(expected,result);

    }

    /**
     * Given
     * TaskOne in projectOne, with no collaborators, neither requests
     * Assignment Request created for userTwo
     *
     * When
     * Approving assignment request to taskOne from userTwo
     *
     * Then
     * Expects a OK message
     *
     */
    @Test
    public void canApproveAnAssignmentRequest() {

        //Given
        assertEquals(0, taskOne.getTaskTeam().size());
        assertEquals(0, taskOne.getPendingTaskTeamRequests().size());
        ResponseEntity<TaskTeamRequest> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

        TaskTeamRequest assignRequest = taskOne.getPendingTaskTeamRequests().get(0);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(assignRequest, result.getBody());


        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.approveRequest(assignRequest.getDbId(), taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.OK, resultTwo.getStatusCode());
        assertEquals(1, taskOne.getTaskTeam().size());


    }

    /**
     * Given
     * Adding userTwo to taskOne
     * Creating removal request for userTwo that is already in taskOne
     *
     * When
     * Approving removal request to taskOne from userTwo
     *
     * Then
     * Expects an OK message
     *
     */
    @Test
    public void canApproveAnRemovalRequest() {

        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);
        assertEquals(1, taskOne.getTaskTeam().size());
        TaskCollaborator existent= taskOne.getTaskCollaboratorByEmail(userTwo.getEmail());
        assertNull(existent.getFinishDate());

        assertEquals(0, taskOne.getPendingTaskTeamRequests().size());
        ResponseEntity<TaskTeamRequest> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);
        assertEquals(1, taskOne.getPendingTaskTeamRequests().size());

        TaskTeamRequest removeRequest = taskOne.getPendingTaskTeamRequests().get(0);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(removeRequest, result.getBody());


        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.approveRequest(removeRequest.getDbId(), taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.OK, resultTwo.getStatusCode());
        assertEquals(1, taskOne.getTaskTeam().size());
        assertNotNull(existent.getFinishDate());


    }

    /**
     * Given
     * Invalid request id
     *
     * When
     * Approving request in taskOne from userTwo
     *
     * Then
     * Expects an NOT FOUND message
     *
     */
    @Test
    public void canNotApproveARequest() {

        //Given
        int invalidRequestID = 999;

        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.approveRequest(invalidRequestID, taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, resultTwo.getStatusCode());


    }

    /**
     * Given
     * TaskOne in projectOne, with no collaborators, neither requests
     * Assignment Request created for userTwo
     *
     * When
     * Rejecting assignment request to taskOne from userTwo
     *
     * Then
     * Expects a OK message
     *
     */
    @Test
    public void canRejectAnAssignmentRequest() {

        //Given
        assertEquals(0, taskOne.getTaskTeam().size());
        assertEquals(0, taskOne.getPendingTaskTeamRequests().size());
        ResponseEntity<TaskTeamRequest> result = controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);

        TaskTeamRequest assignRequest = taskOne.getPendingTaskTeamRequests().get(0);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(assignRequest, result.getBody());
        assertNull(assignRequest.getRejectDate());



        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.rejectRequest(assignRequest.getDbId(), taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.OK, resultTwo.getStatusCode());
        assertEquals(0, taskOne.getTaskTeam().size());
        assertNotNull(assignRequest.getRejectDate());



    }

    /**
     * Given
     * Adding userTwo to taskOne
     * Creating removal request for userTwo that is already in taskOne
     *
     * When
     * Rejecting removal request to taskOne from userTwo
     *
     * Then
     * Expects an OK message
     *
     */
    @Test
    public void canRejectARemovalRequest() {

        //Given
        taskOne.addProjectCollaboratorToTask(projCollabTwo);
        assertEquals(1, taskOne.getTaskTeam().size());
        TaskCollaborator existent= taskOne.getTaskCollaboratorByEmail(userTwo.getEmail());
        assertNull(existent.getFinishDate());

        assertEquals(0, taskOne.getPendingTaskTeamRequests().size());
        ResponseEntity<TaskTeamRequest> result = controller.createRemovalRequest(taskIdOne, projectId, userDTOTwo);
        assertEquals(1, taskOne.getPendingTaskTeamRequests().size());

        TaskTeamRequest removeRequest = taskOne.getPendingTaskTeamRequests().get(0);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(removeRequest, result.getBody());
        assertNull(removeRequest.getRejectDate());



        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.rejectRequest(removeRequest.getDbId(), taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.OK, resultTwo.getStatusCode());
        assertEquals(1, taskOne.getTaskTeam().size());
        assertNull(existent.getFinishDate());

        assertNotNull(removeRequest.getRejectDate());


    }

    /**
     * Given
     * Invalid request id
     *
     * When
     * Rejecting request in taskOne from userTwo
     *
     * Then
     * Expects an NOT FOUND message
     *
     */
    @Test
    public void canNotRejectARequest() {

        //Given
        int invalidRequestID = 999;

        //When
        ResponseEntity<TaskTeamRequest> resultTwo = controller.rejectRequest(invalidRequestID, taskIdOne, projectId);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, resultTwo.getStatusCode());


    }



    @Test
    public void hasRequestByGivenUser() {

        //GIVEN
        // That no task request was yet created

        //WHEN
        // We perform the method to hasRequestByUser to check it's result
        ResponseEntity<TaskTeamRequest> result = controller.hasRequestByUser(userTwoId, taskIdOne, projectId);

        //THEN
        //The response will be a HTTPStatus.NOT_FOUND

        ResponseEntity<TaskTeamRequest> notFound =  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertEquals(result, notFound);


        //GIVEN
        //That we perform an assignement request with userTwo
        controller.createAssignmentRequest(taskIdOne, projectId, userDTOTwo);



        //WHEN
        //We get the result if it has a request by GivenUser (UserID is 0 due to mocks)

        int userId = 0;
        
        ResponseEntity<TaskTeamRequest> result2 = controller.hasRequestByUser(userId, taskIdOne, projectId);

        //THEN
        ResponseEntity<TaskTeamRequest> okExpected =  new ResponseEntity<>(HttpStatus.OK);
        assertEquals(result2.getStatusCode(), okExpected.getStatusCode());



        //WHEN
        //There are requests, but not by the given user

        ResponseEntity<TaskTeamRequest> result3 = controller.hasRequestByUser(4, taskIdOne, projectId);

        //THEN
        //The method will return a NOT_FOUND status
        ResponseEntity<TaskTeamRequest> notFoundExpected =  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertEquals(result3.getStatusCode(), notFoundExpected.getStatusCode());






    }






    }