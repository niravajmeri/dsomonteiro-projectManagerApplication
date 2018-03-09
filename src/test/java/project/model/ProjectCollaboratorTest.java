package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectCollaboratorTest {


    User tester, testerTwo;
    ProjectCollaborator testMoar, testEvenMoar, testCollaboratorNull, testCollaboratorNull2, testCollaboratorEmpty;
    Project project;

    @Before
    public void SetUp() {
        tester = new User("pepe", "trollolol@mail.com", "12", "hue", "12356");
        testerTwo = new User("pepe", "lolno@mail.com", "12", "hue", "12356");

        testMoar = new ProjectCollaborator(tester, 100000);
        testEvenMoar = new ProjectCollaborator(testerTwo, 100000);
        testCollaboratorNull = new ProjectCollaborator(null, 100000);
        testCollaboratorNull2 = new ProjectCollaborator(null, 100000);
        testCollaboratorEmpty = new ProjectCollaborator();
        project = new Project();
    }

    @After
    public void BreakDown() {
        tester = null;
        testerTwo = null;
        testMoar = null;
        testEvenMoar = null;
        testCollaboratorNull = null;
        testCollaboratorNull2 = null;
    }

    /**
     * This test creates a user, then a Project Collaborator using its data. First
     * tests if the Collaborator starts as In Project and if it can be changed to
     * false, and back to true
     * <p>
     * Second, tests both getters for the Collaborator's cost and personal Data
     */
    @Test
    public void testProjectCollaborator() {

        assertTrue(testMoar.isProjectCollaboratorActive());
        testMoar.setStatus(false);
        assertFalse(testMoar.isProjectCollaboratorActive());
        testMoar.setStatus(true);
        assertTrue(testMoar.isProjectCollaboratorActive());

        assertEquals(testMoar.getCollaboratorCost(), 100000);

        assertEquals(tester, testMoar.getUserFromProjectCollaborator());

    }

    @Test
    public void testIsUser() {
        assertTrue(testMoar.isUser(tester));

        assertFalse(testMoar.isUser(testerTwo));
    }

    /**
     * Tests several combinations of the Equals override
     */
    @Test
    public void testEquals() {
        assertTrue(testMoar.equals(testMoar));// same object
        ProjectCollaborator testingNonStop = null;
        assertFalse(testMoar.equals(testingNonStop));// null object
        assertFalse(testMoar.equals(testerTwo));// different classes
        assertFalse(testMoar.equals(testEvenMoar));// different user
        testingNonStop = new ProjectCollaborator(tester, 100000);
        assertTrue(testMoar.equals(testingNonStop));// same user
    }

    /**
     * Tests hashcode class
     */
    @Test
    public void testHashCode() {
        assertTrue(testMoar.hashCode() == testMoar.hashCode());
        assertFalse(tester.hashCode() == testMoar.hashCode());


        int result = 31 * 3 + tester.hashCode();
        assertEquals(result, testMoar.hashCode());


    }

    @Test
    public void testSetId() {

        testMoar.setId(11L);

        assertEquals(11L, testMoar.getId(), 0);
    }

    @Test
    public void testGetCollaborator() {

        assertEquals(tester, testMoar.getCollaborator());
    }

    @Test
    public void testSetCollaborator() {

        testMoar.setCollaborator(testerTwo);
        assertEquals(testerTwo, testMoar.getCollaborator());
    }

    @Test
    public void testIsStatus() {

        assertTrue(testMoar.isStatus());
    }

    @Test
    public void testSetStatus() {

        testMoar.setStatus(false);

        assertFalse(testMoar.isStatus());
    }

    @Test
    public void testGetCostPerEffort() {

        assertEquals(100000, testMoar.getCostPerEffort());
    }

    @Test
    public void testSetCostPerEffort() {
        testMoar.setCostPerEffort(10);

        assertEquals(10, testMoar.getCostPerEffort());
    }

    @Test
    public void testGetProject() {

        testMoar.setProject(project);
        assertEquals(project, testMoar.getProject());
    }

}
