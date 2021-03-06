package project.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.User;
import project.restcontroller.RestProjCollabController;
import project.services.ProjectService;
import project.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
public class RestProjCollabControllerTest {

    @Mock
    private ProjectService projectServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private Project projectMock;

    @InjectMocks
    private RestProjCollabController victim;

    private MockMvc mvc;
    private JacksonTester<ProjectCollaborator> jacksonProjectCollaborator;
    private JacksonTester<List<ProjectCollaborator>> jacksonProjectCollaboratorList;
    private User uDaniel;
    private User uInes;
    private ProjectCollaborator pcDaniel;
    private ProjectCollaborator pcInes;
    List<ProjectCollaborator> projectTeam;



    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(victim).build();
        uDaniel = new User("Daniel", "daniel@gmail.com", "01", "Arquitecto", "967387654");
        uInes = new User("Ines", "ines@gmail.com", "02", "Veterinaria", "917897458");
        projectMock = new Project("Project teste", "Teste ao controller", uDaniel);
        projectMock.setProjectId(1);
        pcDaniel = new ProjectCollaborator(uDaniel, 10);
        pcInes = new ProjectCollaborator(uInes, 20);
        pcDaniel.setProject(projectMock);
        pcInes.setProject(projectMock);
        projectTeam = new ArrayList<>();
        projectTeam.add(pcDaniel);
        projectTeam.add(pcInes);

    }

    @After
    public void tearDown() {
        victim = null;
        pcInes = null;
        jacksonProjectCollaborator = null;
        jacksonProjectCollaboratorList = null;
    }


    /**
     * GIVEN a project ID
     * WHEN we perform a get request to url /projects/<projectId>/team
     * THEN we receive a valid message and a list of project collaborators
     * @throws Exception
     */
    @Test
    public void shouldReturnTheProjectTeam() throws Exception {

        //GIVEN a project id
        int projectId = 1;

        //WHEN we performs a get request to url /projects/<projectId>/team
        when(projectServiceMock.getProjectById(projectId)).thenReturn(projectMock);
        when(projectServiceMock.getActiveProjectTeam(projectMock)).thenReturn(projectTeam);
        MockHttpServletResponse response = mvc.perform(get("/projects/" + projectId + "/team").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //THEN we receive a valid message and a list of project collaborators
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jacksonProjectCollaboratorList.write(projectTeam).getJson(), response.getContentAsString());
        verify(projectServiceMock, times(1)).getActiveProjectTeam(projectMock);

    }

    /**
     * GIVEN a project collaborator id
     * WHEN we perform a get request to url /projects/<projectId>/team/<projectCollaboratorId>
     * THEN we receive a valid message and the details of the projectCollaborator
     * @throws Exception
     */
    @Test
    public void shouldReturnTheProjectCollaboratorDetails() throws Exception {


        //GIVEN a project collaborator id
        int projectCollaboratorId = 1;

        //When we we perform a get request to url /projects/<projectId>/team/1
        when(projectServiceMock.getProjectCollaboratorById(projectCollaboratorId)).thenReturn(pcDaniel);
        MockHttpServletResponse response = mvc.perform(get("/projects/1/team/1").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //THEN we receive a valid message and a list of project collaborators
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jacksonProjectCollaborator.write(pcDaniel).getJson(), response.getContentAsString());
        verify(projectServiceMock, times(1)).getProjectCollaboratorById(projectCollaboratorId);

    }

    /**
     * GIVEN a project ID
     * WHEN we perform a post request to url /projects/<projectId>/team
     * THEN we we receive status OK and the new Project Collaborator created
     * @throws Exception
     */
    @Test
    public void shouldCreateProjectCollaborator() throws Exception{

        //GIVEN a project ID
        int projectId = 1;

        //WHEN we perform a post request to url /projects/<projectId>/team
        when(projectServiceMock.getProjectById(projectId)).thenReturn(projectMock);
        when(userServiceMock.getUserByEmail(uDaniel.getEmail())).thenReturn(uDaniel);
        when(projectServiceMock.createProjectCollaboratorWithEmail(any(String.class),any(Integer.class), any(Double.class))).thenReturn(pcDaniel);

        MockHttpServletResponse response = mvc.perform(post("/projects/" + projectId + "/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProjectCollaborator.write(pcDaniel).getJson())).andReturn().getResponse();


        //THEN we receive status OK and the project info updated
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }


    /**
     * GIVEN a project Id
     * WHEN we perform a post request to url /projects/<projectId>/team with an invalid request body
     * THEN we we receive status BAD-REQUEST and no projectCollaborator will be created
     * @throws Exception
     */
    @Test
    public void shouldNotCreateProjectCollaborator() throws Exception{

        //GIVEN a project ID
        int projectId = 2;

        //WHEN we perform a post request to url /projects/<projectId>/team with a incomplete request body
        when(projectServiceMock.getProjectById(projectId)).thenReturn(projectMock);
        when(userServiceMock.getUserByEmail(uDaniel.getEmail())).thenReturn(null);
        MockHttpServletResponse response = mvc.perform(post("/projects/" + projectId + "/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProjectCollaborator.write(pcDaniel).getJson())).andReturn().getResponse();


        //THEN we receive status OK and the project info updated
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        verify(projectServiceMock, times(0)).createProjectCollaboratorWithEmail(uDaniel.getEmail(), projectId, 10);
    }


    /**
     * GIVEN a project ID and a projectCollabId
     * WHEN we perform a put request to url /projects/<projectId>/team/<projectCollabId>
     * THEN we we receive status OK and the Project Collaborator is deactivated
     * @throws Exception
     */
    @Test
    public void shouldDeactivateProjectCollaborator() throws Exception{

        //GIVEN a project ID and a projectCollaboratorID
        int projectId = 1;

        //WHEN we perform a put request to url /projects/<projectId>/team
        when(projectServiceMock.getProjectById(projectId)).thenReturn(projectMock);
        when(userServiceMock.getUserByEmail(uDaniel.getEmail())).thenReturn(uDaniel);
        when(projectServiceMock.getProjectCollaboratorById(pcDaniel.getProjectCollaboratorId())).thenReturn(pcDaniel);
        when(projectServiceMock.createProjectCollaboratorWithEmail(any(String.class),any(Integer.class), any(Double.class))).thenReturn(pcDaniel);


        MockHttpServletResponse response = mvc.perform(delete("/projects/" + projectId + "/team"+"/"+pcDaniel.getProjectCollaboratorId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProjectCollaborator.write(pcDaniel).getJson())).andReturn().getResponse();


        //THEN we receive status OK and the project info updated
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    /**
     * GIVEN a project ID
     * WHEN we perform a get request to url /projects/<projectId>/team/usersAvailable
     * THEN we we receive status NOT_ACCEPTABLE when All users are in the project
     *
     * AND WHEN one of the collaborators is NOT active in the project
     * THEN we recieve status OK and the response entity must contain a list of one user
     *
     * @throws Exception
     */
    @Test
    public void shouldGetAvailableCollaborators() throws Exception{

        //GIVEN a project ID and a projectCollaboratorID
        int projectId = 1;

        List<User> activeCollabs = new ArrayList<>();
        activeCollabs.add(uDaniel);
        activeCollabs.add(uInes);

        //WHEN we perform a put request to url /projects/<projectId>/team
        when(projectServiceMock.getProjectById(projectId)).thenReturn(projectMock);
        when(userServiceMock.getUserByEmail(uDaniel.getEmail())).thenReturn(uDaniel);
        when(projectServiceMock.getProjectCollaboratorById(pcDaniel.getProjectCollaboratorId())).thenReturn(pcDaniel);
        when(projectServiceMock.createProjectCollaboratorWithEmail(any(String.class),any(Integer.class), any(Double.class))).thenReturn(pcDaniel);

        when(userServiceMock.getAllActiveCollaboratorsFromRepository()).thenReturn(activeCollabs);
        when(projectServiceMock.isUserActiveInProject(uDaniel, projectMock)).thenReturn(true);
        when(projectServiceMock.isUserActiveInProject(uInes, projectMock)).thenReturn(true);


        MockHttpServletResponse response = mvc.perform(get("/projects/" + projectId + "/team/usersAvailable").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();


        //THEN
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getStatus());

        //AND WHEN
        when(projectServiceMock.isUserActiveInProject(uInes, projectMock)).thenReturn(false);
        response = mvc.perform(get("/projects/" + projectId + "/team/usersAvailable").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // THEN
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, victim.usersAvailableToAdd(1).getBody().size());

    }

}
