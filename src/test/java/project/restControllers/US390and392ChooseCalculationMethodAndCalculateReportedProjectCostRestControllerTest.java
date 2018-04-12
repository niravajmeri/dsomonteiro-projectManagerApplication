package project.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import project.restcontroller.US390and392ChooseCalculationMethodAndCalculateReportedProjectCostRestController;
import project.services.ProjectService;
import project.services.TaskService;

import javax.print.attribute.standard.Media;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(MockitoJUnitRunner.class)
public class US390and392ChooseCalculationMethodAndCalculateReportedProjectCostRestControllerTest {
    @Mock
    private TaskService taskServiceMock;
    @Mock
    private ProjectService projectServiceMock;
    @Mock
    private Project projectMock;

    @InjectMocks
    private US390and392ChooseCalculationMethodAndCalculateReportedProjectCostRestController victim;

    private MockMvc mvc;
    private Integer projectId;
    private JacksonTester<Project> jacksonProject;
    private User userRui;
    private ProjectCollaborator projectCollaborator;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(victim).build();
        userRui = new User("Rui", "rui@gmail.com", "02", "Simples colaborador", "638192945");
        projectId = 1;
        this.projectCollaborator = new ProjectCollaborator(userRui,  2);
        projectCollaborator.setProject(projectMock);
    }

    @Test
    public void testUpdateCalculationMethod() throws Exception{
        //given the project is running
        when(projectServiceMock.getProjectById(any(Integer.class))).thenReturn(new Project("Project", "description", userRui));
        Mockito.doNothing().when(projectServiceMock).updateProject(any(Project.class));

        //when
        MockHttpServletResponse response = mvc.perform(put("/projects/" + projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"calculationMethod\":3}")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testGetProjectById() throws Exception {
        Project projectTest = new Project("Project", "description", userRui);
        //given
        when(projectServiceMock.getProjectById(any(Integer.class))).thenReturn(projectTest);

        //when
        MockHttpServletResponse response = mvc.perform(get("/projects/" + projectId).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jacksonProject.write(projectTest).getJson(), response.getContentAsString());
        verify(projectServiceMock, times(1)).getProjectById(projectId);
    }

    @Test
    public void testGetProjectCost() throws Exception {
        //given the project is running
        when(projectServiceMock.getProjectById(any(Integer.class))).thenReturn(projectMock);
        when(taskServiceMock.getTotalCostReportedToProjectUntilNow(projectMock)).thenReturn(7.0);

        //when we perform a get request to url /projects/<projectId>/cost
        MockHttpServletResponse response = mvc.perform(get("/projects/" + projectId + "/cost").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //then we receive a valid message
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"projectCost\":7.0}", response.getContentAsString());
        verify(projectServiceMock, times(1)).getProjectById(projectId);
    }
}