package project.model.sendcode;

import project.model.SendSMS;


public class SMSSender implements ValidationMethod {


    @Override
    public String performValidationMethod(String receipientPhoneNum, String email, String question, String msg) {

        SendSMS sendSMS = new SendSMS();
        sendSMS.sendMessage("Your verification code for registering in the ProjectManagement App is: " + msg, receipientPhoneNum, false);
        return "SMS sent! Please input the code sent to you:";
    }

    @Override
    public boolean checkRightAnswer(String userInput, String rightAnswer) {
        return userInput.equals(rightAnswer);
    }
}
