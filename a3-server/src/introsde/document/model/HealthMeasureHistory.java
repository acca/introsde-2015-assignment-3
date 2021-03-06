package introsde.document.model;

import introsde.document.dao.LifeCoachDao;
import introsde.document.model.Person;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name="HealthMeasureHistory")
@NamedQueries({
	@NamedQuery(name="HealthMeasureHistory.findAll", query="SELECT h FROM HealthMeasureHistory h"),
	@NamedQuery(
			name="HealthMeasureHistory.findPersonHistoryMeasuresWithType", 
			query="SELECT h FROM HealthMeasureHistory h WHERE h.person = :person AND h.measureDefinition = :measureDefinition"),
	@NamedQuery(
			name="HealthMeasureHistory.findPersonHistoryMeasuresWithTypeFilterByDate", 
			query="SELECT h FROM HealthMeasureHistory h WHERE h.person = :person AND h.measureDefinition = :measureDefinition AND h.timestamp > :dateAfter AND h.timestamp < :dateBefore")
})
//@XmlRootElement
public class HealthMeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="HealthMeasureHistory")
	@Column(name="idMeasureHistory")
	private int idMeasureHistory;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timestamp")
	private Date timestamp;

	@Column(name="value")
	private String value;

	@ManyToOne
	@JoinColumn(name = "idMeasureDefinition", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne	
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public HealthMeasureHistory() {
	}

	public int getIdMeasureHistory() {
		return this.idMeasureHistory;
	}

	public void setIdMeasureHistory(int idMeasureHistory) {
		this.idMeasureHistory = idMeasureHistory;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
	    return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
	    this.measureDefinition = param;
	}
	
	@XmlTransient
	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person param) {
	    this.person = param;
	}

	// database operations
	public static HealthMeasureHistory getHealthMeasureHistoryById(int id) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		HealthMeasureHistory p = em.find(HealthMeasureHistory.class, id);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<HealthMeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static HealthMeasureHistory saveHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static HealthMeasureHistory updateHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
	
	public static List<HealthMeasureHistory> getPersonHealthMeasureHistoryByMeasureDef(Person p, MeasureDefinition md) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();		
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findPersonHistoryMeasuresWithType", HealthMeasureHistory.class)
	    		.setParameter("person", p)
	    		.setParameter("measureDefinition", md)
	    		.getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	@Override
	public String toString() {
		return "Measure id: " + idMeasureHistory +
				" Timestam: " + timestamp +
				" Value: " + value +
				" Person " + person;				
	}

	public static List<HealthMeasureHistory> getPersonHealthMeasureHistoryByMeasureDefFilterByDate(Person p, MeasureDefinition md, Date dateBefore, Date dateAfter) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();		
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findPersonHistoryMeasuresWithTypeFilterByDate", HealthMeasureHistory.class)
	    		.setParameter("person", p)
	    		.setParameter("measureDefinition", md)
	    		.setParameter("dateBefore", dateBefore)
	    		.setParameter("dateAfter", dateAfter)
	    		.getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
}
