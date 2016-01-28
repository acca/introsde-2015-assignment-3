package introsde.assignment.soap;
import introsde.document.exceptions.ResourceNotFound;
import introsde.document.model.HealthMeasureHistory;
import introsde.document.model.Measure;
import introsde.document.model.MeasureDefinition;
import introsde.document.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.People",
    serviceName="PeopleService")
public class PeopleImpl implements People {

    @Override
    public Person readPerson(int id) {
        System.out.println("---> Reading Person by id = "+id);
        Person p = Person.getPersonById(id);
        if (p!=null) {
            System.out.println("---> Found Person by id = "+id+" => "+p.getName());
        } else {
            System.out.println("---> Didn't find any Person with  id = "+id);
        }
        return p;
    }

    @Override
    public List<Person> readPersonList() {
        return Person.getAll();
    }

    @Override
    public int createPerson(Person person) {
    	Person newPerson = new Person();
    	// Update person informations
    	newPerson.setName(person.getName());
    	newPerson.setUsername(person.getUsername());
    	newPerson.setLastname(person.getLastname());
    	newPerson.setEmail(person.getEmail());
    	newPerson.setBirthdate(person.getBirthdate());    	    	    
    	
    	// If present update person healthProfile
    	if (person.getLifeStatus() != null) {
    		List<Measure> newMeasureList = new ArrayList<Measure>();    	
        	Iterator<Measure> i = person.getLifeStatus().iterator();
        	while(i.hasNext()){
        		Measure m = i.next();
        		Measure newMeasure = new Measure();
        		newMeasure.setMeasureDefinition(m.getMeasureDefinition());
        		newMeasure.setPerson(newPerson);
        		newMeasure.setValue(m.getValue());
        		newMeasureList.add(newMeasure);
        	}    	    	
        	newPerson.setLifeStatus(newMeasureList);	
    	}    	
    	Person dbPerson = Person.savePerson(newPerson);
        return dbPerson.getIdPerson();        
    }

    @Override
    public int updatePerson(Person person) {
    	// Does not update the healthProfile information 
    	person.setLifeStatus(null);
        Person.updatePerson(person);
        return person.getIdPerson();
    }

    @Override
    public int deletePerson(int id) {    	
        Person p = Person.getPersonById(id);
        if (p!=null) {
            Person.removePerson(p);
            return 0;
        } else {
            return -1;
        }
    }

	@Override
	public List<HealthMeasureHistory> readPersonHistory(int id, String measureName) {
		Person p = Person.getPersonById(id);
		MeasureDefinition md = MeasureDefinition.getMeasureDefinitionByName(measureName);		
		return HealthMeasureHistory.getPersonHealthMeasureHistoryByMeasureDef(p, md);
	}

	@Override
	public Measure readPersonMeasurement(int id, String measureName, int mid) {		
		if (mid != 0) {
			return Measure.getLifeStatusById(mid);	
		}
		else {
			Person p = Person.getPersonById(id);
			MeasureDefinition md = MeasureDefinition.getMeasureDefinitionByName(measureName);
			return Measure.getPersonMeasureByMeasureDef(p, md);
		}
		
	}

	@Override
	public int savePersonMeasurement(int id, Measure m) {
		Person p = Person.getPersonById(id);
		Measure ls = Measure.getPersonMeasureByMeasureDef(p, m.getMeasureDefinition());
		if (ls == null) {
			// Create person measure
        	ls = new Measure();
        	ls.setMeasureDefinition(m.getMeasureDefinition());
        	ls.setPerson(p);
        	ls.setValue(m.getValue());
        	ls = Measure.saveLifeStatus(ls);
            // Insert old measure in history
        	int newId = udpateHistory(ls);
            return newId;
		}
		else if (ls.getPerson().getIdPerson() == id) {
            // Update person measure
        	ls.setValue(m.getValue());
        	Measure.updateLifeStatus(ls);
            // Insert old measure in history
        	int newId = udpateHistory(ls);
            return newId;
        } else {
            return -1;
        }		
	}

	private int udpateHistory(Measure ls) {
		HealthMeasureHistory historyMeasure = new HealthMeasureHistory();
    	historyMeasure.setMeasureDefinition(ls.getMeasureDefinition());
    	historyMeasure.setPerson(ls.getPerson());
    	historyMeasure.setTimestamp(new Date());
    	historyMeasure.setValue(ls.getValue());
        HealthMeasureHistory hm = HealthMeasureHistory.saveHealthMeasureHistory(historyMeasure);
        return hm.getIdMeasureHistory();
	}

	@Override
	public List<MeasureDefinition> readMeasureTypes() {
		return MeasureDefinition.getAll();
	}

}