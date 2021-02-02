package com.flipkart.client;

import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.constants.Grades;
import com.flipkart.exception.UserCRSException;
import com.flipkart.service.ProfessorInterface;
import com.flipkart.service.ProfessorOperation;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Scanner;

public class ProfessorCRSClient {

	Scanner inputObj = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(ProfessorCRSClient.class);
	ProfessorInterface professorOperationReference = ProfessorOperation.getInstance();

	/**
	 * driver class for professor menu
	 * @param professor entity
	 */
	public void professorMenuHandler(Professor professor) {
		try {
			int choice;
			boolean professorLogin = true;
			do {
				getProfessorMenu();
				choice = inputObj.nextInt();
				switch (choice) {
				case 1:
					ArrayList<Course> courseList = professorOperationReference.viewCourses(professor);
					printCourseList(courseList);
					break;
				case 2:
					printEnrolledStudents(professor);
					break;
				case 3:
					boolean flag = gradeStudent(professor);
					if (flag) {
						logger.info("Student Graded");
					} else {
						String msg = "Exception while grading student";
						throw new UserCRSException(msg, professor.getUserId());
					}
					break;
				case 4:
					printCourseCatalogue();
					break;
				case 0:
					professorLogin = false;
					break;

				}
			} while (professorLogin == true);
		} catch (UserCRSException ex) {
			logger.error(ex.getMsg() + " for professorID: " + ex.getUserId());
			professorMenuHandler(professor);
		}
		catch(IllegalArgumentException ex)
		{
			logger.error(ex.getMessage());
			professorMenuHandler(professor);
		}
		finally {
			inputObj.close();
		}
	}

	/**
	 * Helper class to display menu.
	 */
	private void getProfessorMenu() {
		logger.info("-----------------------------------------");
		logger.info("Enter your choice:");
		logger.info("1. View Courses");
		logger.info("2. View Enrolled Students");
		logger.info("3. Grade a Student");
		logger.info("4. View Course Catalogue");
		logger.info("0. logout");
		logger.info("-----------------------------------------");
	}

	/**
	 * Helper class to print the course catalogue
	 */
	private void printCourseCatalogue() {
		logger.info("Displaying the course catalogue");
		ArrayList<Course> courseCatalogue = professorOperationReference.viewCourseCatalogue();
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
	 * Helper class to print course list.
	 * @param courseList entity
	 */
	private void printCourseList(ArrayList<Course> courseList) {
		for (Course obj : courseList) {
			logger.info(obj.getCourseId() + " - " + obj.getCourseName());
		}
	}

	/**
	 * Helper class to print enrolled students
	 */
	private void printEnrolledStudents(Professor professor) {
		logger.info("Enter the course ID: ");
		String courseId = inputObj.next();
		courseId= courseId.toLowerCase();
		Course enrolledStudentOfCourse = new Course();
		enrolledStudentOfCourse.setCourseId(courseId);
		ArrayList<Student> enrolledStudents = professorOperationReference.viewEnrolledStudents(professor,enrolledStudentOfCourse);
		logger.info(courseId);
		
		if(enrolledStudents == null)
			logger.info("Invalid Course Access!");
		else if(enrolledStudents.size()==0)
			logger.info("No students are registered in the course ID: "+ courseId);
		else
		{
			for (Student obj : enrolledStudents) {
				logger.info("Student Name: "+ obj.getUsername()+ " Student ID: "+ obj.getUserId());
			}
		}
	}

	/**
	 * Helper class to grade the student
	 * @return boolean
	 */
	private boolean gradeStudent(Professor professor) {
		Student studentToGrade = new Student(); // fix this
		logger.info("Enter the Student Id to Grade : ");
		studentToGrade.setUserId(inputObj.nextInt());
		Course courseToGrade = new Course();
		logger.info("Enter Course Id to Grade : ");
		courseToGrade.setCourseId(inputObj.next());
		logger.info("Enter the Grades : ");
		String gradeValue = inputObj.next();
		Grades grade = Grades.valueOf(gradeValue);
		return professorOperationReference.gradeStudents(professor,studentToGrade, courseToGrade, grade);
	}
}
