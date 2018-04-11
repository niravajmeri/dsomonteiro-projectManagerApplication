package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.CodeGenerator;
import project.model.SendEmail;
import project.model.SendSMS;
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
    private UserService userService;

    private String code;

    SendSMS sender = new SendSMS();

    SendEmail emailSender = new SendEmail();

    CodeGenerator codeGenerator = new CodeGenerator();


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

        user.setPassword(newPassword);

        userService.updateUser(user);
    }

    /**
     * Method to find and return the question associated with a specific user
     * @param user user whose question we are searching for
     * @return the question of the user
     */

    /**
     * Method to determine if the answer provided is the right one
     * @param answer the answer provided by the user
     * @param user the user trying to login
     * @return true if it's the right answer, false if it isn't
     */
    public boolean isRightAnswer(String answer, User user){

        return answer.equalsIgnoreCase(user.getAnswer());
    }

    /**
     * Sends  a code to the phone number provided with the validation code
     *
     * @param option phone number to which to send the code
     */

    public String performAuthentication(String userPhone, String userEmail, String userQuestion, String option) throws IOException, MessagingException {

        String code = codeGenerator.generateCode();
        this.code = code;

        SendCodeFactory factory = new SendCodeFactory();

        if (factory.getCodeSenderType(option).isPresent()) {
            validation = factory.getCodeSenderType(option).get();
            return validation.performValidationMethod(userPhone, userEmail, userQuestion, code);
        } else {
            validation = null;
            return "Invalid method selected. Please choose a valid one.";
        }


    }


    /**
     * checks if the code provided by the user is the same as the code sent by the application
     * @param code code provided by the user
     * @return true if both codes are the same, false if they aren't
     */
    public boolean isCodeValid(String code, User user) {
        if (validation instanceof AnswerValidation) {
            return validation.checkRightAnswer(code, user.getQuestion());
        } else {
            return validation.checkRightAnswer(code, this.code);
        }
    }

    public ValidationMethod getValidation() {
        return validation;
    }


}
