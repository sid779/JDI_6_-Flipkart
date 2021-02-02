package com.flipkart.client;

import com.flipkart.bean.*;
import com.flipkart.constants.UserType;
import com.flipkart.exception.UserCRSException;
import com.flipkart.service.AdminInterface;
import com.flipkart.service.AdminOperation;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AdminCRSClient {

	Scanner inputObj = new Scanner(System.in);

	private static Logger logger = Logger.getLogger(AdminCRSClient.class);

	AdminInterface adminInterfaceReference = AdminOperation.getInstance();

	/**
	 * Menu Handler for Admin
	 * @param admin entity
	 */
	public void adminMenuHandler(Admin admin) {
		try {
			int choice;
			boolean adminLogin = true;
			do {
				getAdminMenu();
				choice = inputObj.nextInt();
				switch (choice) {
				case 1:
					boolean updateStatus = updateCourseInCatalogue();
					if (!updateStatus) {
						String msg = "Exception while updating course by admin";
						throw new UserCRSException(msg, admin.getUserId());
					} 
					break;
				case 2:
					boolean addStatus = addCourseInCatalogue();
					if (!addStatus) {
						String msg = "Unable to add course to the Database, please try again.";
						throw new UserCRSException(msg, admin.getUserId());
					} 
					break;
				case 3: 
					boolean deleteStatus = deleteCourseInCatalogue();
					if(!deleteStatus) {
						String msg= "Unable to delete the course, Try again ";
						throw new UserCRSException(msg, admin.getUserId());
					}
					break;
				case 4:
					boolean approvalStatus=approveStudent();
					if(!approvalStatus) {
						String msg="Approval for Student failed, Try again";
						throw new UserCRSException(msg, admin.getUserId());
					}
					break;
				case 5:
					boolean addUserStatus = addUser();
					if(!addUserStatus) {
						String msg = "Unable to add user";
						throw new UserCRSException(msg, admin.getUserId());
					}
					break;
				case 6:
					boolean reportStatus = generateReport();
					if(!reportStatus) {
						String msg = "Unable to generate report";
						throw new UserCRSException(msg, admin.getUserId());
					}
					break;
				case 7:
					boolean removeUserStatus = removeUser();
					if(!removeUserStatus) {
						String msg ="Unable to delete user ";
						throw new UserCRSException(msg, admin.getUserId());
					}
					break;
				case 8:
					viewCourseCatalogue();
					break;
				case 9:
					viewStudentsToApprove();
					break;
				case 0:
					adminLogin = false;
					break;
				}
			} while (adminLogin);
		}
		catch (UserCRSException ex) {
			logger.error(ex.getMsg() + " for adminID: " + ex.getUserId());
			adminMenuHandler(admin);
		} finally {
			inputObj.close();
			return;
		}
	}

	/**
	 * Helper class to display the available operations for Admin.
	 */
	public void getAdminMenu() {
		logger.info("-----------------------------------------");
		logger.info("Enter your choice:");
		logger.info("1. Update Course in Catalogue");
		logger.info("2. Add Course in Catalogue");
		logger.info("3. Delete Course in Catalogue");
		logger.info("4. Approve Students");
		logger.info("5. Add User");
		logger.info("6. Generate Report");
		logger.info("7. Remove User");
		logger.info("8. View Course Catalogue.");
		logger.info("9. View Students to be approved.");
		logger.info("0. logout");
		logger.info("-----------------------------------------");
	}

	/**
	 * Helper class which interacts with user to get studentId for approval
	 */
	private boolean approveStudent() {
		logger.info("-----------------------------------------");
		logger.info("Enter Student ID: ");
		int studentId = inputObj.nextInt();
		Student student = new Student();
		student.setUserId(studentId);
		if(adminInterfaceReference.approveStudents(student)) {
			logger.info("Student with ID: "+studentId+"  approved.");
			return true;
		}
		else {
			logger.info("Approval for Student with ID: "+ studentId + " failed. Try again");
		}
		return false;
	}

	/**
	 * Helper class to view the list of studentId and name to approve from DB
	 */
	public void viewStudentsToApprove() {
		logger.info("-----------------------------------------");
		List<Student> studentList = adminInterfaceReference.viewStudentsToApprove();
		if(studentList.size()==0)
			logger.info("No Students to approve.");
		else {
			logger.info("List of Students to approve :");
			for (Student student : studentList) {
				logger.info("ID: " + student.getUserId() + "   Name: " + student.getUsername());
			}
		}
		return;
	}

	/**
	 * Helper function to generate report for student
	 */
	public boolean generateReport() {
		logger.info("-----------------------------------------");
		logger.info("Enter Student ID: ");
		int studentId = inputObj.nextInt();
		Student student = new Student();
		student.setUserId(studentId);
		if(adminInterfaceReference.generateReport(student)) {
			logger.info("Report generated Successfully!");
			return true;
		}
		else {
			logger.info("Unable to generate report for student : "+ studentId);
		}
		return false;
	}

	/**
	 * Helper function to view the course catalogue
	 */
	private void viewCourseCatalogue() {
		logger.info("-----------------------------------------");
		logger.info("Admin is viewing the course catalogue.");
		ArrayList<Course> courseCatalogue = adminInterfaceReference.viewCourseCatalogue();
		logger.info("-------------------------------------------------------------------------");
		String header= String.format("%10s%15s%12s%15s%20s", "Course ID", "Name", "Professor", "Description", "Fee");
		logger.info(header);
		logger.info("-------------------------------------------------------------------------");
		for (Course course : courseCatalogue) {
			String output= String.format("%10s%15s%12s%15s%20f", course.getCourseId(), course.getCourseName(), course.getCourseProfessor(), course.getCourseDescription(), course.getCourseFee());
			logger.info(output);
		}
		logger.info("-------------------------------------------------------------------------");
		logger.info("");
		return;
	}

	/**
	 * Helper function to add courses in the course catalogue by the Admin
	 */
	private boolean addCourseInCatalogue() {
		logger.info("-----------------------------------------");
		logger.info("Admin is adding courses to the Course Catalogue.");

		logger.info("Enter Course Id: ");
		String courseId = inputObj.next();
		courseId = courseId.toLowerCase();

		logger.info("Enter Course Name: ");
		String courseName = inputObj.next();
		courseName = courseName.toLowerCase();

		logger.info("Enter Course Professor Name: ");
		String courseProfessor = inputObj.next();
		courseProfessor = courseProfessor.toLowerCase();

		logger.info("Enter Course Fee: ");
		Double courseFee = inputObj.nextDouble();

		logger.info("Enter Catalogue ID: ");
		String catalogueId = inputObj.next();

		logger.info("Enter Course Description: ");
		String courseDescription = inputObj.next();

		logger.info("Enter Professor ID: ");
		int professorID = inputObj.nextInt();

		Course course = new Course(courseId, courseName, courseProfessor, courseFee, catalogueId, courseDescription, professorID);
		if(adminInterfaceReference.addCourseInCatalogue(course)) {
			logger.info("Course " + courseId + " successfully added to Database.");
			return true;
		}
		else {
			logger.info("Unable to add course to the Database, please try again.");
		}
		return false;
	}

	/**
	 * Helper function to delete courses from the Course Catalogue
	 */
	private boolean deleteCourseInCatalogue() {
		logger.info("-----------------------------------------");
		logger.info("Enter Course Id: ");
		String courseId = inputObj.next();
		Course course = new Course();
		course.setCourseId(courseId);
		if(adminInterfaceReference.deleteCourseInCatalogue(course)) {
			logger.info("Course with course ID: "+ courseId+ " deleted successfully");
			return true;
		}
		else {
			logger.info("Unable to delete the course with ID: "+ courseId);
		}
		return false;
	}

	/**
	 * Helper function to update the course catalogue.
	 */
	private boolean updateCourseInCatalogue() {
		logger.info("-----------------------------------------");
		logger.info("Enter Course Id: ");
		String courseId = inputObj.next();
		courseId = courseId.toLowerCase();

		logger.info("Enter Course Name: ");
		String courseName = inputObj.next();
		courseName = courseName.toLowerCase();

		logger.info("Enter Course Professor Name: ");
		String courseProfessor = inputObj.next();
		courseProfessor = courseProfessor.toLowerCase();

		logger.info("Enter Course Fee: ");
		Double courseFee = inputObj.nextDouble();

		logger.info("Enter Catalogue ID: ");
		String catalogueId = inputObj.next();

		logger.info("Enter Course Description: ");
		String courseDescription = inputObj.next();

		logger.info("Enter Professor ID: ");
		int professorID = inputObj.nextInt();

		Course course = new Course(courseId, courseName, courseProfessor, courseFee, catalogueId, courseDescription, professorID);
		if(adminInterfaceReference.updateCourseInCatalogue(course)) {
			logger.info("Course with course ID: "+ courseId+ " updated successfully");
			logger.info("Updated course entries: ");
			logger.info(course.toString());
			return true;
		}
		else {
			logger.info("Unable to update course with ID: "+ courseId);
		}
		return false;
	}

	/**
	 * Helper function to add new user to Database by Admin
	 */
	private boolean addUser()
	{
		logger.info("-----------------------------------------");
		logger.info("Enter Username: ");
		String username = inputObj.next();
		username = username.toLowerCase();
		
		logger.info("Enter Email ID: " );
		String emailId = inputObj.next();
		emailId = emailId.toLowerCase();
		
		logger.info("Enter Password: " );
		String password = inputObj.next();
		password = password.toLowerCase();
		
		logger.info("Select Usertype: \n1. Admin\n2. Professor\n3. Student\n");
		
		logger.info("Enter Usertype choice: " );
		int userTypeInput = inputObj.nextInt();

		boolean addStatus = false;
		User user = new User();
		switch(userTypeInput)
		{
			case 1:
				user = new User(username, 0,  emailId, password, UserType.ADMIN);
				Admin admin = new Admin(user, 0);
				addStatus = adminInterfaceReference.addUser(admin);
				break;
			case 2:
				user = new User(username, 0, emailId, password, UserType.PROFESSOR);
				Professor professor = addProfessor(user);
				addStatus = adminInterfaceReference.addUser(professor);
				break;
			case 3:
				user = new User(username, 0, emailId, password, UserType.STUDENT);
				Student student = addStudent(user);
				addStatus = adminInterfaceReference.addUser(student);
				break;
		}
		if(addStatus){
			logger.info("User: "+ username+ " added successfully.");
		}
		else {
			logger.info("Problem occurred while adding User: "+ username+ ". Try again.");
		}
		return addStatus;
	}

	/**
	 * Helper function to fetch additional details for professor
	 * @param user user details
	 * @return professor
	 */
	private Professor addProfessor(User user){
		logger.info("-----------------------------------------");
		logger.info("Enter additional details for Professor: ");
		logger.info("Enter Gender (m/f): ");
		String gender = inputObj.next();
		gender = gender.toLowerCase();

		logger.info("Enter Department: ");
		String department = inputObj.next();
		department = department.toLowerCase();

		Professor professor = new Professor(user, department, gender);
		return professor;
	}

	/**
	 * Helper function to fetch additional details for student
	 * @param user user details
	 * @return student
	 */
	private Student addStudent(User user){
		logger.info("-----------------------------------------");
		logger.info("Enter additional details for Student: ");
		logger.info("Enter Branch: ");
		String branch = inputObj.next();
		branch = branch.toLowerCase();

		logger.info("Enter Gender (m/f): ");
		String gender = inputObj.next();
		gender = gender.toLowerCase();

		logger.info("Enter Address: ");
		String address = inputObj.next();
		address = address.toLowerCase();

		logger.info("Enter if the student has Scholarship (t/f): ");
		String input = inputObj.next();
		boolean hasScholarship = false;
		if(input.equals("t"))
			hasScholarship = true;
		Student student = new Student(user, branch, gender, hasScholarship, address);
		return student;
	}

	/**
	 * Helper function to remove user from the DB
	 */
	private boolean removeUser() {
		logger.info("-----------------------------------------");
		logger.info("Enter email ID for user that you want to remove: ");
		String email = inputObj.next();
		email = email.toLowerCase();
		
		User user = new User();
		user.setEmailId(email);
		
		if(adminInterfaceReference.removeUser(user)) {
			logger.info("User with email ID: "+ email+ " removed successfully");
			return true;
		}
		else {
			logger.info("Unable to delete user with email ID: "+ email);
		}
		return false;
	}
}
