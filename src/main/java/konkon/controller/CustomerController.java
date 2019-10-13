package konkon.controller;

import java.util.List;

import konkon.model.Customer;
import konkon.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CustomerController {
  @Autowired
  private CustomerService customerService;

  //-------------------Retrieve All Customers--------------------------------------------------------
  @RequestMapping(value = "/customers/", method = RequestMethod.GET)
  public ResponseEntity<List<Customer>> listAllCustomers() {
    List<Customer> customers = customerService.findAll();
    if (customers.isEmpty()) {
      return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
    }
    return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
  }
  //-------------------Retrieve Single Customer--------------------------------------------------------
  @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
    System.out.println("Fetching Customer with id");
    Customer customer = customerService.findById(id);
    if(customer == null){
      System.out.println("Customer with id "+id+" Not found");
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }else{
      return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }
  }
  //-------------------Create a Customer--------------------------------------------------------
  @RequestMapping(value = "/customers/", method = RequestMethod.POST)
  public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
    System.out.println("Creating Customer " + customer.getLastName());
    customerService.save(customer);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri());
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
  }
  //---------------------------update a customer-----------------------------------------------
  @RequestMapping(value = "/customers/{id}",method = RequestMethod.PUT )
  public  ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer){
    System.out.println("updating Customer : "+ id );
    Customer currentCustomer = customerService.findById(id);
    if(currentCustomer == null){
      System.out.println("customer with id "+ id+ " not found");
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND  );
    }else{
      currentCustomer.setId(customer.getId());
      currentCustomer.setFirstName(customer.getFirstName());
      currentCustomer.setLastName(customer.getLastName());
      customerService.save(currentCustomer);
      return new ResponseEntity<Customer>(currentCustomer,HttpStatus.OK);
    }
  }
  //------------------------delete a customer------------------------
  @RequestMapping(value ="/customers/{id}" , method = RequestMethod.DELETE )
  public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id){
    System.out.println("Fetching and Delete Customer: "+id);
    Customer customer = customerService.findById(id);
    if(customer == null){
      System.out.println("Customer with id  "+ id+ " not found");
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }else{
      customerService.remove(id);
      return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }
  }
}
