package co.edu.udistrital.sed.teacher.controller;

import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.CourseDAO;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationDAO;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.SubjectDAO;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class TeacherQualificationController extends Controller {

	/** @author MTorres 31/7/2014 22:37:23 */
	public List<Course> loadCourseListByTeacher(Long idSedUser) throws Exception {
		CourseDAO dao = new CourseDAO();
		try {
			return dao.loadCourseListByTeacher(idSedUser);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 31/7/2014 23:12:27 */
	public List<Subject> loadSubjectListByTeacherCourse(Long idSedUser, Long idCourse) throws Exception {
		SubjectDAO dao = new SubjectDAO();
		try {
			return dao.loadSubjectListByTeacherCourse(idSedUser, idCourse);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 4/8/2014 22:46:04 */
	public List<Student> loadStudentListByCourse(Long idCourse) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		try {
			return dao.loadStudentListByCourse(idCourse);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 7/8/2014 15:29:50 */
	public List<Qualification> loadQualificationByCourseSubject(List<Long> idStudentList, Long idSubject) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		try {
			return dao.loadQualificationByCourseSubject(idStudentList, idSubject);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 10/08/2014 11:13:52 a. m. */
	public boolean saveQualificationList(List<Student> studentList, Long idSedUser, Long idSubject, String user) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();

			int count = 0;

			for (Student s : studentList) {

				for (Qualification q : s.getQualificationList()) {
					q.initialize(q.getId() != null ? false : true, user);
					q.setIdSubject(idSubject);
					q.setIdStudentCourse(s.getIdStudentCourse());
					q.setState(IState.ACTIVE);

					dao.getSession().saveOrUpdate(q);

					if (count++ % 20 == 0)
						dao.getSession().flush();
				}

			}
			tx.commit();
			return true;
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}
}
