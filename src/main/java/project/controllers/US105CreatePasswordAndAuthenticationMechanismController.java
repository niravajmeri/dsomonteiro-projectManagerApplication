package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import project.model.CodeGenerator;
import project.model.User;
import project.model.sendcode.AnswerValidation;
import project.model.sendcode.SendCodeFactory;
import project.model.sendcode.ValidationMethod;
import project.services.UserService;

import javax.mail.MessagingException;
import java.io.IOException;

@Controller
public class US105CreatePasswordAndAuthenticationMechanismController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private String stringToSend;

    CodeGenerator codeGenerator = new CodeGenerator();

    SendCodeFactory factory = new SendCodeFactory();

    ValidationMethod validation;
    public US105CreatePasswordAndAuthenticationMechanismController() {
        //Empty constructor created for JPA integration tests

    }

    /**
     * This method set a new password to user and change the variable firstLogin to false and
     * save the changes in DB
     *
     * @param user
     * @param newPassword
     */
    public void setUserPassword(User user, String newPassword) {

        user.setPassword(passwordEncoder.encode(newPassword));

        userService.updateUser(user);
    }

    /**
     * Method to find and return the question associated with a specific user
     * @param user user whose question we are searching for
     * @return the question of the user
     */

    /**
     * Sends  a stringToSend to the phone number provided with the validation stringToSend
     *
     * @param option phone number to which to send the stringToSend
     */

    public String performAuthentication(String userPhone, String userEmail, String userQuestion, String option) throws IOException, MessagingException {

        String code = codeGenerator.generateCode();
        this.stringToSend = code;

        validation = factory.getCodeSenderType(option).orElse(null);
        if (validation != null) {
            return validation.performValidationMethod(userPhone, userEmail, userQuestion, code);
        } else {
            return "Invalid method selected. Please choose a valid one.";
        }


    }


    /**
     * checks if the stringToSend provided by the user is the same as the stringToSend sent by the application
     *
     * @param code stringToSend provided by the user
     * @return true if both codes are the same, false if they aren't
     */
    public boolean isCodeValid(String code, User user) {
        if (validation instanceof AnswerValidation) {
            return validation.checkRightAnswer(code, user.getQuestion());
        } else {
            return validation.checkRightAnswer(code, this.stringToSend);
        }


    }

    public ValidationMethod getValidation() {
        return validation;
    }


}
