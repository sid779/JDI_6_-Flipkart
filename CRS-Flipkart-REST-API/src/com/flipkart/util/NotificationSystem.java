package com.flipkart.util;

import org.apache.log4j.Logger;
//import com.email.durgesh.Email;
import com.flipkart.bean.Course;
import com.flipkart.bean.Notification;
import com.flipkart.bean.Student;
import com.flipkart.dao.NotificationDaoImplementation;
import com.flipkart.dao.NotificationDaoInterface;
import com.flipkart.service.PaymentService;
// need to add jar files to Referenced libraries

// sends a mail to student after fee payment and registration
public class NotificationSystem {

	private static Logger logger = Logger.getLogger(NotificationSystem.class);
	Notification notification = new Notification();
	NotificationDaoInterface notificationDao = NotificationDaoImplementation.getInstance();


	public String notifyStudent(Student student,char notifyType) {
		
		notification.setNotificationId(notificationDao.noOfEntries()+1);
		notification.setNotificationType(notifyType);
		notification.setUserId(student.getUserId());

		String message;
		if (notifyType == 'P') {
			message = "Notification ID : "+notification.getNotificationId()+"\n"+"Student ID : " + student.getUserId() + "\n" +
									"Transaction ID : "+notificationDao.getTransactionId(notification.getUserId())+"\n"+PaymentService.getTotalFees(student)
					+ " paid successfully \nThank you!";
			// mailSubject = "Fee Payment Successful";
		} else if(notifyType == 'R'){
			message = "Notification ID : "+notification.getNotificationId()+"\n"+"Student ID : "+ student.getUserId() + "\n" + "Registered Courses \n";
			if(student.getCourseList().size() == 4) {
				for (Course course : student.getCourseList()) {
					message = message + course.getCourseId() + "\t" + course.getCourseName() + "\n";
				}
				message += "Registered Successfully";
			}
			else {
				message += "Registration Falied!";
			}
			// mailSubject = "Registration Successful";
		}else {
			message = "Payment Failed!";
		}

		notification.setNotificationMsg(message);
		
		boolean status = notificationDao.addNotification(notification);
		// comments for sending mail, can use it later
		/*
		 * try {
		 * 
		 * // enter email and password from which msg has to be sent Email email = new
		 * Email("xxxx@gmail.com","xxxxxx");
		 * 
		 * //Enter the email address from which mail to be sent
		 * email.setFrom("xxxx@gmail.com", "Notification");
		 * email.setSubject(mailSubject); email.setContent(message, "text/html");
		 * email.addRecipient(student.getEmailId()); email.send();
		 * 
		 * }catch (Exception e) { e.printStackTrace(); }
		 */

		logger.debug("Notification sent");

		return notification.getNotificationMsg();
	}

}