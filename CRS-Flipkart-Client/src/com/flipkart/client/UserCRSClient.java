package com.flipkart.client;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.constants.UserType;
import com.flipkart.exception.UserCRSException;
import com.flipkart.service.AuthenticationService;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class UserCRSClient {

    private static Logger logger = Logger.getLogger(UserCRSClient.class);
    AdminCRSClient adminClient = new AdminCRSClient();
    ProfessorCRSClient professorClient = new ProfessorCRSClient();
    StudentCRSClient studentClient = new StudentCRSClient();
    private static Scanner input = new Scanner(System.in);

    /**
     * Driver class for User Client
     * @throws UserCRSException custom exception
     */
    public void getUserMenu() throws UserCRSException {
        try {
            displayMenu();
            boolean login = true;
            do {
                int choice = Integer.parseInt(input.nextLine());
                boolean flag;
                switch (choice) {
                    case 1:
                        Admin admin = (Admin) credentialHandler(UserType.ADMIN);  //pass admin obj
                        if (admin != null) {
                            adminClient.adminMenuHandler(admin);
                            login = false;
                        } else {
                            logger.info("Try again");
                            getUserMenu();
                        }
                        break;
                    case 2:
                        Professor professor = (Professor) credentialHandler(UserType.PROFESSOR); //pass prof obj
                        if (professor != null) {
                            professorClient.professorMenuHandler(professor);
                            login = false;
                        } else {
                            logger.info("Try again");
                            getUserMenu();
                        }
                        break;
                    case 3:
                        Student student = (Student) credentialHandler(UserType.STUDENT);  // pass student obj
                        if (student != null) {
                            studentClient.studentMenuHandler(student);
                            login = false;
                        } else {
                            logger.info("Try again");
                            getUserMenu();
                        }
                        break;
                    case 4:
                        logger.info("Enter student's details");
                        logger.info("-----------------------------------------");
                        logger.info("Enter email :");
                        String email = input.nextLine();
                        logger.info("Enter name :");
                        String username = input.nextLine();
                        logger.info("Enter password :");
                        String password = input.nextLine();
                        logger.info("Enter branch :");
                        String branch = input.nextLine();
                        logger.info("Enter gender as (m/f) :");
                        String gender = input.nextLine();
                        logger.info("Enter address :");
                        String address = input.nextLine();

                        if (AuthenticationService.registerStudent(username, email, password, branch, gender, address)) {
                            logger.info("Account created!");
                        } else {
                            logger.info("Try again");
                        }
                        getUserMenu();
                        break;
                    case 0:
                        login = false;
                        return;
                }
            }
            while (login);
//        input.close();
        }
        catch (UserCRSException ex){
            logger.error(ex.getMsg() + " for UserID: " + ex.getUserId());
            getUserMenu();
        }
    }

    /**
     * Helper menu to display choices for User.
     */
    private void displayMenu(){
        logger.info("-----------------------------------------");
        logger.info("Select the User Type");
        logger.info("1. Admin");
        logger.info("2. Professor");
        logger.info("3. Student");
        logger.info("4. Student Account Creation");
        logger.info("0. To exit");
        logger.info("-----------------------------------------");
    }

    /**
     * Inout credentials handler fot the email and passwords given by user.
     * @param userType choice
     * @return user entity
     */
    private User credentialHandler(UserType userType){  //Fix this
        logger.info("Enter your credentials");
        logger.info("-----------------------------------------");
        logger.info("Enter email :");
        String email = input.nextLine();
        logger.info("Enter Password :");
        String password = input.nextLine();
        if(userType.equals(UserType.ADMIN)){
            Admin admin = AuthenticationService.authAdmin(email, password);
            return admin;
        }
        else if(userType.equals(UserType.PROFESSOR)){
            Professor professor =  AuthenticationService.authProfessor(email, password);
            return professor;
        }
        else if(userType.equals(UserType.STUDENT)){
            Student student = AuthenticationService.authStudent(email, password);
            return student;
        }
        else{
            return null;
        }
    }

}
