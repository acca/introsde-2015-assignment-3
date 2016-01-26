package introsde.assignment.soap;
import introsde.document.model.HealthMeasureHistory;
import introsde.document.model.LifeStatus;
import introsde.document.model.MeasureDefinition;
import introsde.document.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) //optional
public interface People {
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") int id);

    @WebMethod(operationName="getPeopleList")
    @WebResult(name="people") 
    public List<Person> readPersonList();

    @WebMethod(operationName="createPerson")
    @WebResult(name="personId") 
    public int createPerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="updatePerson")
    @WebResult(name="personId") 
    public int updatePerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="deletePerson")
    @WebResult(name="personId") 
    public int deletePerson(@WebParam(name="personId") int id);

    @WebMethod(operationName="updatePersonHealthProfile")
    @WebResult(name="hpId") 
    public int updatePersonHP(@WebParam(name="personId") int id, @WebParam(name="healthProfile") LifeStatus hp);
    
    // Methods from 6 to 9
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="HealthMeasureHistory")
    public List<HealthMeasureHistory> readPersonHistory(@WebParam(name="personId") int id, @WebParam(name="measureType") String measureType);

    @WebMethod(operationName="readPersonMeasurement")
    @WebResult(name="measure")
    public LifeStatus readPersonMeasurement(Long id, String measureType, Long mid);
    
    @WebMethod(operationName="savePersonMeasurement")
    @WebResult(name="measureId")
    public int savePersonMeasurement(Long id, LifeStatus m);
    
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes")
    public List<MeasureDefinition> readMeasureTypes();
    
}