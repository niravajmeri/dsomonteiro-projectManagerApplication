package project.model;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS {




    public void sendMessage(String messageToSend, String numberToSend){

        /*
         Settings taken from twilio.com/user/account

             RegisteredEmail:   isepswitch3@gmail.com
             Password:          Switch_Isep2018

             Check Account_SID and Auth_Token in website's account

         */

        String accountSid = "ACca51735bb18e517813ab9100bfc3cb6a";
        String authToken = "fb2d11d34cf609f035a0d1c981dfd4df";



        Twilio.init(accountSid, authToken);


        /*
        This is the host number provided by Twilio's Service
         */
        String numberProvidedByTwilio = "+18123018184";

        /*
            Test number. Must be a registered number on Twilio's account.
            In this case, the registered number on the Twilio account is +351937429087 number (João Leite's number)
         */


        //HARDCODING NUMBER SO TWILIO DOESNT CRASH APPLICATION
        numberToSend = "+351914187778";

        Message.creator(new PhoneNumber(numberToSend),
                new PhoneNumber(numberProvidedByTwilio),
                messageToSend).create();


    }



}
