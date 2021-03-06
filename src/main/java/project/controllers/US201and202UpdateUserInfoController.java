package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import project.model.Address;
import project.model.User;
import project.services.UserService;

import java.util.List;

@Controller
public class US201and202UpdateUserInfoController {

	/**
	 * This method controls the update of user's name by calling the method setName
	 * that set this parameter of user's data.
	 *
	 * @param user
	 * @param name
	 */

	@Autowired
	private UserService userContainer;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


	public US201and202UpdateUserInfoController() {
		//Empty constructor created for JPA integration tests

	}

	public void updateUserName(User user, String name) {

		user.setName(name);

	}

	/**
	 * This method controls the update of user's email by calling the method
	 * setEmail that set this parameter of user's data.
	 *
	 * @param user
	 * @param email
	 */
	public void updateUserEmail(User user, String email) {
		user.setEmail(email);
	}

	public boolean isEmailValid(String email) {

		return userContainer.isEmailAddressValid(email);

	}

	public boolean isEmailAlreadyInUse(String email) {

		return userContainer.getUserByEmail(email) != null;
	}

	/**
	 * This method controls the update of user's password by calling the method
	 * setPassword that set this parameter of user's data.
	 *
	 * @param user
	 * @param password
	 */
	public void updateUserPassword(User user, String password) {

        user.setPassword(passwordEncoder.encode(password));
	}

	/**
	 * This method controls the update of user's phone by calling the method
	 * setPhone that set this parameter of user's data.
	 *
	 * @param user
	 * @param phone
	 */
	public void updateUserPhone(User user, String phone) {

		user.setPhone(phone);
	}

	/**
	 * This method creates a new address
	 *
	 * @param newStreet
	 * @param newZipCode
	 * @param newCity
	 * @param newDistrict
	 * @param newCountry
	 */
	public Address createNewAddress(String newStreet, String newZipCode, String newCity, String newDistrict,
			String newCountry) {
		return new Address(newStreet, newZipCode, newCity, newDistrict, newCountry);
	}

	/**
	 * This method controls the add of a new address to user's address list by
	 * calling the method addAddress.
	 *
	 * @param user
	 * @param address
	 */
	public void addNewAddress(User user, Address address) {
		user.addAddress(address);
	}

	/**
	 * This method controls the update of user's street by calling the method
	 * searchUserAddress (that search the specific address and return it) and then
	 * uses the method setStreet to change the parameter street of that specific
	 * address.
	 *
	 * @param user
	 * @param newStreet
	 * @param street
	 */
	public void updateUserStreet(User user, String street, String newStreet) {
		user.searchUserAddress(street).setStreet(newStreet);
	}

	/**
	 * This method controls the update of user's zipCode by calling the method
	 * searchUserAddress (that search the specific address and return it) and then
	 * uses the method setZipCode to change the parameter zipCode of that specific
	 * address.
	 *
	 * @param user
	 * @param street
	 * @param zipCode
	 */
	public void updateUserZipCode(User user, String street, String zipCode) {
		user.searchUserAddress(street).setZipCode(zipCode);
	}

	/**
	 * This method controls the update of user's city by calling the method
	 * searchUserAddress (that search the specific address and return it) and then
	 * uses the method setCity to change the parameter city of that specific
	 * address.
	 *
	 * @param user
	 * @param street
	 * @param city
	 */
	public void updateUserCity(User user, String street, String city) {
		user.searchUserAddress(street).setCity(city);
	}

	/**
	 * This method controls the update of user's district by calling the method
	 * searchUserAddress (that search the specific address and return it) and then
	 * uses the method setDistrict to change the parameter district of that specific
	 * address.
	 *
	 * @param user
	 * @param street
	 * @param district
	 */
	public void updateUserDistrict(User user, String street, String district) {
		user.searchUserAddress(street).setDistrict(district);
	}

	/**
	 * This method controls the update of user's country by calling the method
	 * searchUserAddress (that search the specific address and return it) and then
	 * uses the method setCountry to change the parameter country of that specific
	 * address.
	 *
	 * @param user
	 * @param street
	 * @param country
	 */
	public void updateUserCountry(User user, String street, String country) {
		user.searchUserAddress(street).setCountry(country);
	}

	/**
	 * This method returns a list of all the addresses from a user
	 *
	 * @param user
	 */
	public List<Address> getAllAddresses(User user) {
		return user.getAddressList();
	}

	/**
	 * Returns the name of the user.
	 *
	 * @return the name of the user
	 */
	public String getName(User user) {
		return user.getName();
	}

	/**
	 *
	 * Returns the user Email
	 *
	 * @return the user Email.
	 */
	public String getEmail(User user) {
		return user.getEmail();
	}

	/**
	 * Returns the ID number of the user
	 *
	 * @return the ID number of the user.
	 */
	public String getIdNumber(User user) {
		return user.getIdNumber();
	}

	/**
	 * Returns the function of the user
	 *
	 * @return Returns the user function.
	 */
	public String getFunction(User user) {
		return user.getFunction();
	}

	/**
	 * Returns the phone number of a certain user
	 *
	 * @return the phone number of a certain user
	 */
	public String getPhone(User user) {
		return user.getPhone();
	}

	/**
	 * this method search one specific address in user address list, if find one
	 * match, the method return this specific address object.
	 *
	 * @param user
	 * @param street
	 * @return address
	 */
	public Address searchUserAddress(User user, String street) {
		for (Address other : this.getAllAddresses(user)) {
			if (other.getStreet().equals(street))
				return other;
		}
		return null;
	}

	/**
	 *
	 * Gets the street
	 *
	 * @return Street
	 */
	public String getStreet(Address address) {
		return address.getStreet();
	}

	/**
	 * Gets the City
	 *
	 * @return city City
	 */
	public String getCity(Address address) {
		return address.getCity();
	}

	/**
	 * Gets the Country
	 *
	 * @return country Country
	 */
	public String getCountry(Address address) {
		return address.getCountry();
	}

	/**
	 * Gets the District
	 *
	 * @return district District
	 */
	public String getDistrict(Address address) {
		return address.getDistrict();
	}

	/**
	 * Gets the zipCode
	 *
	 * @return zipCode zipCode
	 */
	public String getZipCode(Address address) {
		return address.getZipCode();
	}

	public void alterUser(User user) {
		this.userContainer.addUserToUserRepositoryX(user);
	}

}
