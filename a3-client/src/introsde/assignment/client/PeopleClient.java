package introsde.assignment.client;

import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Holder;

import introsde.assignment.soap.PeopleService;
import introsde.assignment.soap.HealthMeasureHistory;
import introsde.assignment.soap.Measure;
import introsde.assignment.soap.MeasureDefinition;
import introsde.assignment.soap.People;
import introsde.assignment.soap.Person;
import introsde.assignment.soap.Person.CurrenHealth;

public class PeopleClient{
	public static void main(String[] args) throws Exception {
		PeopleService service = new PeopleService();
		People people = service.getPeopleImplPort();

		//        Person p = people.readPerson(1);
		//        List<Person> pList = people.readPersonList();
		//        System.out.println("Result ==> "+p);
		//        System.out.println("Result ==> "+pList);
		//        System.out.println("First Person in the list ==> "+pList.get(0).getName());


		// Method #1
		pl("Method #1");        
		JAXBContext jc = JAXBContext.newInstance(Person.class);
		List<Person> pl = people.readPersonList();
		Iterator<Person> pi = pl.iterator();
		while(pi.hasNext()) {
			pl(asString(jc,pi.next()));			
		}
				
		// Method #2
		pl("Method #2");
		//jc = JAXBContext.newInstance(Person.class);
		Person p = people.readPerson(3341);
		pl(asString(jc,p));
		
		// Method #3 ---> return person
		pl("Method #3");
		//jc = JAXBContext.newInstance(Person.class);
		p.setName("XXXXXXXXX");
		//p.setBirthdate(new Date());		
		//Person newP = people.updatePerson(p);
		//pl(asString(jc,newP));
		
		// Method #4 ---> return person
		pl("Method #4");
		//jc = JAXBContext.newInstance(Person.class);
		Person p4 = new Person();
		p4.setName("Person method 4");
		p4.setLastname("Surname");		
		people.createPerson(p);		
		
		// Method #5
		pl("Method #5");
		people.deletePerson(new Holder<Integer>(1));
		
		// Method #6
		pl("Method #6");
		JAXBContext mc = JAXBContext.newInstance(HealthMeasureHistory.class);
		List<HealthMeasureHistory> ml = people.readPersonHistory(1,"weight");		
		Iterator<HealthMeasureHistory> mi = ml.iterator();
		while(mi.hasNext()) {
			pl(asString(mc,pi.next()));			
		}
		
		// Method #7
		pl("Method #7");
		Measure m = people.readPersonMeasurement(3341, "weight", 1656);
		m.toString();
		
		// Method #8
		pl("Method #8");		
		MeasureDefinition md = new MeasureDefinition();
		md.setIdMeasureDef(3);
		md.setMeasureName("steps");
		md.setMeasureType("integer");
		m = new Measure();
		m.setMeasureDefinition(md);
		m.setValue("9999");
		int id = people.savePersonMeasurement(3341, m);
		pl("new measure ID is " + id);
		pl("---> Printing person:");
		p = people.readPerson(3341);
		pl(asString(jc,p));
		
		// Method #9
		pl("Method #9");
		List<MeasureDefinition> mdl = people.readMeasureTypes();
		Iterator<MeasureDefinition> mdli = mdl.iterator();
		while(mdli.hasNext()) {
			mdli.next().toString();
		}
		

		// Extra #1
		pl("Extra #1");

		// Extra #2 Method #10
		pl("Extra #2 - Method #10");

		// Extra #3 Method #11
		pl("Extra #3 - Method #11");

		// Extra #4 Method #12
		pl("Extra #4 - Method #12");
	}

	private static String asString(JAXBContext pContext, Object pObject) throws JAXBException {
		java.io.StringWriter sw = new StringWriter();
		Marshaller marshaller = pContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		//JAXBElement<MyClass> a = new JAXBElement<MyClass>(new QName("http://soap.assignment.introsde/","MyClassNameXML"), MyClass.class, md);
		marshaller.marshal(pObject, sw);
		return sw.toString();
	}

	private static void pl (String s) {
		System.out.println(s);
	}

	private static void p (String s) {
		System.out.print(s);
	}
}