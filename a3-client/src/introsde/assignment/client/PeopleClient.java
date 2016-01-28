package introsde.assignment.client;

import java.util.List;

import introsde.assignment.soap.PeopleService;
import introsde.assignment.soap.People;
import introsde.assignment.soap.Person;

public class PeopleClient{
    public static void main(String[] args) throws Exception {
        PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();
        Person p = people.readPerson(1);
        List<Person> pList = people.readPersonList();
        System.out.println("Result ==> "+p);
        System.out.println("Result ==> "+pList);
        System.out.println("First Person in the list ==> "+pList.get(0).getName());
    }
}