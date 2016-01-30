package introsde.assignment.client;

import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
		pl("-----> Method #1");        
		JAXBContext jc = JAXBContext.newInstance(Person.class);
		List<Person> pl = people.readPersonList();
		Iterator<Person> pi = pl.iterator();
		while(pi.hasNext()) {
			pl(asString(jc,pi.next()));			
		}

		// Method #2
		pl("-----> Method #2");
		//jc = JAXBContext.newInstance(Person.class);
		Person p = people.readPerson(3349);
		pl(asString(jc,p));

		// Method #4 ---> return person
		pl("-----> Method #4");
		//jc = JAXBContext.newInstance(Person.class);
		Person p4 = new Person();
		String uuid = UUID.randomUUID().toString();
		p4.setName("Person method 4 " + uuid);
		p4.setLastname("Surname");		
		int pId = people.createPerson(p4);
		pl("--> New person created with id: " + pId);		
		pl(asString(jc,people.readPerson(pId)));

		// Method #3 ---> return person
		pl("-----> Method #3");
		//jc = JAXBContext.newInstance(Person.class);
		p4 = people.readPerson(pId);
		p4.setName(new Date().getTime()+"");
		p4.setEmail(new Date().getTime()+"@unitn.it");
		//p.setBirthdate(new Date());		
		pId = people.updatePerson(p4);
		pl("--> New person updated with id: " + pId);
		pl(asString(jc,people.readPerson(pId)));

		// Method #5
		pl("-----> Method #5");
		people.deletePerson(new Holder<Integer>(pId));
		pl("--> Removed person with "+pId);

		// Method #6
		pl("-----> Method #6");
		pId = 3349;
		JAXBContext mc = JAXBContext.newInstance(HealthMeasureHistory.class);
		List<HealthMeasureHistory> ml = people.readPersonHistory(pId,"height");		
		Iterator<HealthMeasureHistory> mi = ml.iterator();
		while(mi.hasNext()) {
			pl(asString(mc,mi.next()));			
		}

		// Method #7
		pl("-----> Method #7");
		Measure m = people.readPersonMeasurement(pId, "height", 1955);
		pl("ID: " + m.getIdMeasure() + " value: " + m.getValue() + " person Id: " + pId);
		//m.toString();

		// Method #8
		pl("-----> Method #8 - savePersonMeasurement");		
		MeasureDefinition md = new MeasureDefinition();
		List<MeasureDefinition> mdList = people.readMeasureTypes();
		md = mdList.get(0);
		m = new Measure();
		m.setMeasureDefinition(md);
		m.setValue("65" + new Date().getMinutes());
		int id = people.savePersonMeasurement(pId, m);
		pl("new measure ID in history is " + id);
		pl("---> Printing person:");
		p = people.readPerson(pId);
		pl(asString(jc,p));

		// Method #9
		pl("-----> Method #9 - readMeasureTypes");
		mc = JAXBContext.newInstance(MeasureDefinition.class);
		List<MeasureDefinition> mdl = people.readMeasureTypes();
		Iterator<MeasureDefinition> mdli = mdl.iterator();
		while(mdli.hasNext()) {
			pl(asString(mc,mdli.next()));			
		}


		// Extra #1
		pl("Extra #1");
		pl("Database slite used.");

		// Extra #2 Method #10
		pl("Extra #2 - Method #10");
		pl("---> Implemented inside method #8");

		// Extra #3 Method #11
		pl("Extra #3 - Method #11");
		GregorianCalendar now = new GregorianCalendar();
		XMLGregorianCalendar before = DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
		XMLGregorianCalendar after = DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
		after.setHour(after.getHour()-1);
		before.setHour(before.getHour()+1);		
		pl("---> reading  healthprofilehistory from " + after + " to " + before);
		mc = JAXBContext.newInstance(HealthMeasureHistory.class);
		ml = people.readPersonMeasureByDates(3349, "height", before, after);		
		mi = ml.iterator();
		while(mi.hasNext()) {
			pl(asString(mc,mi.next()));			
		}
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