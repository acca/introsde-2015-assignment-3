package introsde.assignment.soap;
import introsde.document.exceptions.ResourceNotFound;
import introsde.document.model.HealthMeasureHistory;
import introsde.document.model.LifeStatus;
import introsde.document.model.MeasureDefinition;
import introsde.document.model.Person;

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
        Person.savePerson(person);
        return person.getIdPerson();
    }

    @Override
    public int updatePerson(Person person) {
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
	public LifeStatus readPersonMeasurement(int id, String measureName, int mid) {		
		if (mid != 0) {
			return LifeStatus.getLifeStatusById(mid);	
		}
		else {
			Person p = Person.getPersonById(id);
			MeasureDefinition md = MeasureDefinition.getMeasureDefinitionByName(measureName);
			return LifeStatus.getPersonMeasureByMeasureDef(p, md);
		}
		
	}

	@Override
	public int savePersonMeasurement(int id, LifeStatus m) {
		Person p = Person.getPersonById(id);
		LifeStatus ls = LifeStatus.getPersonMeasureByMeasureDef(p, m.getMeasureDefinition());
		if (ls == null) {
			// Create person measure
        	ls = new LifeStatus();
        	ls.setMeasureDefinition(m.getMeasureDefinition());
        	ls.setPerson(p);
        	ls.setValue(m.getValue());
        	ls = LifeStatus.saveLifeStatus(ls);
            // Insert old measure in history
        	int newId = udpateHistory(ls);
            return newId;
		}
		else if (ls.getPerson().getIdPerson() == id) {
            // Update person measure
        	ls.setValue(m.getValue());
        	LifeStatus.updateLifeStatus(ls);
            // Insert old measure in history
        	int newId = udpateHistory(ls);
            return newId;
        } else {
            return -1;
        }		
	}

	private int udpateHistory(LifeStatus ls) {
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