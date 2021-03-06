/**
 *
 */
package project.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.dto.UserDTO;
import project.model.Profile;
import project.model.User;
import project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests all methods in UserService
 *
 * @author Group3
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userContainer;

    User user1;
    User user2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        initMocks(this);


        Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(user1);


        // instantiate users
        user1 = userContainer.createUser("Daniel", "daniel@gmail.com", "001", "collaborator", "910000000", "Rua",
                "2401-00", "Porto", "Porto", "Portugal");
        user2 = userContainer.createUser("João", "joaogmail.com", "001", "Admin", "920000000", "Rua", "2401-00",
                "Porto", "Porto", "Portugal");

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

        user1 = null;
        user2 = null;

    }

    /**
     * Test method call of testCreateUserWithDTO
     */
    @Test
    public final void shouldCreateUserWithDTO() {
        // given a user DTO for user1
        UserDTO userDto = new UserDTO(user1);
        userDto.setPassword("123");

        // when the .save method is mocked
        Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(user1);

        // then the create User with DTO method must call the save method once for user1

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("321");

        userContainer.createUserWithDTO(userDto);
        Mockito.verify(userRepositoryMock, Mockito.times(2)).save(user1);
    }

    /**
     * Test method call addUserToUserRepository
     */
    @Test
    public final void shouldAddUserToUserRepositoryX() {

        // when(userRepositoryMock.save(any(User.class))).thenReturn(user1);

        userContainer.addUserToUserRepositoryX(user1);

        verify(userRepositoryMock, times(2)).save(user1);
    }


    /**
     * Test method call updateUserData
     */
    @Test
    public final void shouldUpdateUserRepository() {

        // given two users in the database, with save method called once for each of them


        // when the same method is called again, it must NOT call the repository's save method as the user already exists
        // (mocked exists by email method to return true
        Mockito.when(userRepositoryMock.existsByEmail(user1.getEmail())).thenReturn(true);
        userContainer.addUserToUserRepositoryX(user1);

        // then, save must only have been called once, for user 1
        verify(userRepositoryMock, times(1)).save(user1);


        // to update the user, updateUser method must be called instead. When called, it will call the save method regardless of ther user existing
        // then, save must have been called a total of two times for user 1
        userContainer.updateUser(user1);
        verify(userRepositoryMock, times(2)).save(user1);
    }

    /**
     * Test method call delete user from Repository
     */
    @Test
    public final void shouldDeleteUserFromRepository() {

        // GIVEN an empty database
        Mockito.when(userRepositoryMock.existsByEmail(user1.getEmail())).thenReturn(false);


        // WHEN "deleteUser" method is called for user 1
        userContainer.deleteUser(user1.getEmail());

        // THEN the repository method "deleteByEmail" must NOT be called
        verify(userRepositoryMock, times(0)).deleteByEmail(user1.getEmail());

        // AND WHEN user1 is added to the database
        userContainer.addUserToUserRepositoryX(user1);
        Mockito.when(userRepositoryMock.existsByEmail(user1.getEmail())).thenReturn(true);

        // THEN the repository method "deleteByEmail" must be invoked once when calling deleteUser
        userContainer.deleteUser(user1.getEmail());
        verify(userRepositoryMock, times(1)).deleteByEmail(user1.getEmail());

    }


    /**
     * Test method call getAllUsersFromUserContainer.
     */
    @Test
    public final void shouldGetAllUsersFromUserContainer() {

        List<User> listToCompare = new ArrayList<>();
        listToCompare.add(user1);

        List<User> userInDB = new ArrayList<>();
        userInDB.add(user1);

        when(userRepositoryMock.findAll()).thenReturn(userInDB);

        assertEquals(listToCompare, userContainer.getAllUsersFromUserContainer());
    }

    /**
     * Test method call of testGetUserByEmail
     */
    @Test
    public final void shouldGetUserByEmail() {

        when(userRepositoryMock.findByEmail("daniel@gmail.com")).thenReturn(Optional.of(user1));

        assertEquals(user1, userContainer.getUserByEmail("daniel@gmail.com"));
    }

    /**
     * Test method call of testGetAllActiveCollaboratorsFromRepository
     */
    @Test
    public final void shouldGetAllActiveCollaboratorsFromRepository() {
        List<User> list = new ArrayList<>();
        user1.setUserProfile(Profile.COLLABORATOR);
        user2.setUserProfile(Profile.COLLABORATOR);
        list.add(user1);
        list.add(user2);

        when(userRepositoryMock.findAll()).thenReturn(list);

        assertEquals(list, userContainer.getAllActiveCollaboratorsFromRepository());

    }

    /**
     * Test method call of testSearchUsersByPartsOfEmail
     */
    @Test
    public final void shouldSearchUsersByPartsOfEmail() {

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        when(userRepositoryMock.findAll()).thenReturn(list);

        assertEquals(list, userContainer.searchUsersByPartsOfEmail("gmail"));
    }

    /**
     * Test method call of testSearchUsersByProfile
     */
    @Test
    public final void shouldSearchUsersByProfile() {

        List<User> listOfUsersByProfile = new ArrayList<>();
        user1.setUserProfile(Profile.COLLABORATOR);
        listOfUsersByProfile.add(user1);

        when(userRepositoryMock.findAllByUserProfile(Profile.COLLABORATOR)).thenReturn(listOfUsersByProfile);

        assertEquals(listOfUsersByProfile, userContainer.searchUsersByProfile(Profile.COLLABORATOR));
    }

    /**
     * Test method call of testIsEmailAddressValid
     */
    @Test
    public final void checkIfEmailAddressIsValid() {
        assertEquals(userContainer.isEmailAddressValid(user1.getEmail()), true);
    }

    /**
     * Test method call of testIsEmailAddressValid
     */
    @Test
    public final void checkIfEmailAddressIsNotValid() {
        assertEquals(userContainer.isEmailAddressValid(user2.getEmail()), false);
    }

}
