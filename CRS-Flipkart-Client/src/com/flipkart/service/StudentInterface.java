package com.flipkart.service;

import com.flipkart.bean.Course;
import com.flipkart.bean.Student;
import com.flipkart.constants.Grades;

import java.util.List;
import java.util.Map;

public interface StudentInterface extends UserInterface
{
	/**
	 * To register courses added by student
	 * @param student studentId
	 * @param courseList<Course> courselist of courses student wants to register for
	 * @return boolean for successful/unsuccessful registration
	 */
	public List<Course> registerCourses(Student student, List<Course> courseList);

	/**
	 * To view the grades obtained by student during in the semester
	 * @param student studentId
	 * @return Map of course and the respective grades
	 */
	public Map<Course, Grades> viewGrades(Student student);

	/**
	 * Make payment for student
	 * @param student studentId
	 * @return boolean for successful/unsuccessful payment
	 */
	public boolean makeFeePayment(Student student);

	/**
	 * To add courses that the student wants to register
	 * @param course course to be added
	 * @param student student to which course will be added
	 * @return	boolean for successful/unsuccessful addition
	 */
	public boolean addCourses(Course course, Student student);

	/**
	 * To drop a course by Student
	 * @param course course to be added
	 * @param student student to which course will be added
	 * @return boolean for successful/unsuccessful drop
	 */
	public boolean dropCourses(Course course, Student student);


	/**
	 * To check whether the student has already registered for courses or not
	 * @param student student for which course registration will checked
	 * @return return status
	 */
	public boolean isAllowed(Student student);

	/**
	 * To check whether the student has any space to add course in the courseList or not
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	public boolean canAdd(Student student);


	/**
	 * To check whether the student has exact 6 courses while registration
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	public boolean isCourseListValid(Student student);

	/**
	 * To check whether the student has exact 6 courses while registration
	 * @param student student for which courseList has to be checked
	 * @return return status
	 */
	public boolean hasThisCourse(Student student, Course course);

}
