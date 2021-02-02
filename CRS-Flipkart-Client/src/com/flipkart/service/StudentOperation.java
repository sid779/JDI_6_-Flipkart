/**
 * 
 */
package com.flipkart.service;

import com.flipkart.bean.Course;

import com.flipkart.bean.Student;
import com.flipkart.constants.Grades;
import com.flipkart.dao.CourseDaoImplementation;
import com.flipkart.dao.CourseDaoInterface;
import com.flipkart.dao.StudentDaoImplementation;
import com.flipkart.dao.StudentDaoInterface;

import org.apache.log4j.Logger;
import com.flipkart.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentOperation implements StudentInterface {
	private static volatile StudentOperation instance = null;
	private StudentOperation() {
	}

	public static StudentOperation getInstance() {
		if (instance == null) {
			synchronized (StudentOperation.class) {
				instance = new StudentOperation();
			}
		}
		return instance;
	}
	StudentDaoInterface studentDao = StudentDaoImplementation.getInstance();       //object of StudentDaoInterface
	NotificationSystem notificationSystem = new NotificationSystem();
	
	private static Logger logger = Logger.getLogger(StudentOperation.class);


	/**
	 * To register courses added by student
	 * @param student studentId
	 * @param courseList<Course> courselist of courses student wants to register for
	 * @return boolean for successful/unsuccessful registration
	 */
	@Override
	public synchronized List<Course> registerCourses(Student student, List<Course> courseList) {
		logger.info("Student is registering for courses");
		
		if(courseList.size() != 6 || student.isRegistered()==true)
			return courseList;
		CourseDaoInterface courseDao = CourseDaoImplementation.getInstance();
		List<Course> registrationCourseList = new ArrayList<Course>();
		
		for(Course course: courseList)
		{
			if(courseDao.getSeatCount(course) < 10) {
				registrationCourseList.add(course);
			}
			if(registrationCourseList.size()==4)
				break;
		}
		if(registrationCourseList.size() == 4) {
			List<Course> courses = studentDao.registerCourses(student, registrationCourseList);
			student.setCourseList(courses);
			student.setIsRegistered(true);

			logger.info(notificationSystem.notifyStudent(student,'R'));
			return courses;      		//return course list.

		}
		return courseList;
	}

	/**
	 * To view the grades obtained by student during in the semester
	 * @param student studentId
	 * @return Map of course and the respective grades
	 */
	@Override
	public Map<Course, Grades> viewGrades(Student student){
		logger.info("Student is viewing grades");
		Map<Course, Grades> grades = new HashMap<Course, Grades>();
		grades = studentDao.viewGrades(student);
		return grades;
	}

	/**
	 * Make payment for student
	 * @param student studentId
	 * @return boolean for successful/unsuccessful payment
	 */
	@Override
	public boolean makeFeePayment(Student student){
		// TODO Auto-generated method stub
		logger.info("Student is paying fees");
		boolean flag = studentDao.makeFeePayment(student);
		PaymentService.calculateAmount(student);
		if(flag) {
			logger.info(notificationSystem.notifyStudent(student,'P'));
		}
		else
		{
			logger.info(notificationSystem.notifyStudent(student,'F'));
		}
		
		return flag;//return status of payment.
	}

	/**
	 * To add courses that the student wants to register
	 * @param course course to be added
	 * @param student student to which course will be added
	 * @return	boolean for successful/unsuccessful addition
	 */
	@Override
	public boolean addCourses(Course course, Student student) {
		logger.info("Student is adding courses");
		student.getCourseList().add(course);

		List<Course> courseList = student.getCourseList();
		return true;
	}

	/**
	 * To drop a course by Student
	 * @param course course to be added
	 * @param student student to which course will be added
	 * @return boolean for successful/unsuccessful drop
	 */
	@Override
	public boolean dropCourses(Course course, Student student) {
		logger.info("Student is dropping courses");
		student.getCourseList().remove(course);
		return true;	//return status.
	}

	/**
	 * To check whether the student has already registered for courses or not
	 * @param student student for which course registration will checked
	 * @return return status
	 */
	@Override
	public boolean isAllowed(Student student){
		if(student.isRegistered()){
			return false;
		}
		return true;
	}

	/**
	 * To check whether the student has any space to add course in the courseList or not
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	@Override
	public boolean canAdd(Student student){
		if(student.getCourseList().size() <6){
			return true;
		}
		return false;
	}

	/**
	 * To check whether the student has exact 6 courses while registration
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	@Override
	public boolean isCourseListValid(Student student){
		if(student.getCourseList().size()==6){
			return true;
		}
		return false;
	}

	/**
	 * To check whether the student has exact 6 courses while registration
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	@Override
	public boolean hasThisCourse(Student student, Course course){
		List<Course> courseList = student.getCourseList();
		for(Course crs : courseList){
			if(crs.getCourseId().equals(course.getCourseId())){
				return true;
			}
		}
		return false;
	}
}
