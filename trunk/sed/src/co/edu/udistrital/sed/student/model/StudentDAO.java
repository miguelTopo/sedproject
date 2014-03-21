package co.edu.udistrital.sed.student.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.sed.model.Student;

public class StudentDAO extends HibernateDAO{

	public Student loadStudent(Long idStudent) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
		hql.append(" SELECT s.id AS id, "); 
		hql.append(" s.identification AS identification, "); 
		hql.append(" s.name AS name  ");
		hql.append(" FROM Student s ");
		hql.append(" WHERE s.id = :idloquesea ");
		hql.append(" AND s.state = :state ");
		
		qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
		qo.setParameter("idloquesea", idStudent);
		qo.setParameter("state", IState.ACTIVE);
		qo.setMaxResults(1);	
		
		return (Student) qo.uniqueResult();
		} catch (Exception e) {
			throw e;
		}finally{
			hql = null;
			qo = null;
		}
	}
	
	public List<Student> loadListStudentByCourse (Long idCourse) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
			
			
			hql.append("select s.id AS id,"); 
			hql.append("s.identification AS identificacion,"); 
			hql.append("s.name AS name");
			hql.append("s.idCourse AS idCourse");
			hql.append("FROM student s");
			hql.append("WHERE s.idCourse=:idCourse");
			hql.append("AND s.state=:paramState");
			
		qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
		qo.setParameter("paramState", IState.ACTIVE);
		qo.setParameter("idCourse", idCourse);
			
		
		return qo.list();
		} catch (Exception e) {
			throw e;
		}finally{
			hql = null;
			qo = null;
		}
	}
	
	
	
	public List<Student> loadListStudentByGrade (Long idGrade) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {

			hql.append("select s.id AS id,");
			hql.append("s.identification AS identificacion,");
			hql.append("s.name AS name,");
			hql.append("s.idCourse as idCourse,");
			hql.append("c.idgrade as idgrade");
			hql.append("FROM student s JOIN course c");
			hql.append("ON s.idCourse=:c.id");
			hql.append("WHERE c.idgrade=:idGrade");
			hql.append("AND s.state=:1;");
			
		qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
		qo.setParameter("paramState", IState.ACTIVE);
		qo.setParameter("idGrade", idGrade);
			
		
		return qo.list();
		} catch (Exception e) {
			throw e;
		}finally{
			hql = null;
			qo = null;
		}
	}
	

	public boolean deleteStudent(long idStudent) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
		hql.append(" UPDATE Student"); 
		hql.append(" SET state=0"); 
		hql.append(" WHERE id=:idStudent ");
		hql.append(" AND s.state=:state ");
		
		qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
		qo.setParameter("state", IState.ACTIVE);
		qo.setParameter("paramId", idStudent);
		
		qo.executeUpdate();
		return true;
		
		} catch (Exception e) {
			throw e;
		}finally{
			hql = null;
			qo = null;
		}
	}

	/*public boolean createStudent() throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
		hql.append(" UPDATE Student"); 
		hql.append(" SET state=0"); 
		hql.append(" WHERE id:paramId ");
		hql.append(" AND s.state = :paramState ");
		
		qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
		qo.setParameter("state", IState.ACTIVE);
//		qo.setParameter("paramId", idStudent);
		
		qo.executeUpdate();
		return true;
		
		} catch (Exception e) {
			throw e;
		}finally{
			hql = null;
			qo = null;
		}
	}*/
		public boolean updateStudent(Long idStudent, String identificacionStudent, String nameStudent, Long idCourse) throws Exception {
			StringBuilder hql = null;
			Query qo = null;
			try {
				
			hql.append(" UPDATE Student s"); 
			hql.append(" SET s.identification:identificacionStudent,"); 
			hql.append("s.name:nameStudent, ");
			hql.append("s.idCourse:idCourse");
			hql.append(" WHERE s.id:idStudent");
			hql.append(" AND s.state:state ");
			
			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("identificacionStudent", identificacionStudent);
			qo.setParameter("nameStudent", nameStudent);
			qo.setParameter("idCourse", idCourse);
			qo.setParameter("state", IState.ACTIVE);

			
			qo.executeUpdate();
			return true;
			
			} catch (Exception e) {
				throw e;
			}finally{
				hql = null;
				qo = null;
			}
	
		}}
	