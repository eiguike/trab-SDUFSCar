/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package learning;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author rick
 */
@WebService(serviceName = "test")
public class test {

	/**
	 * This is a sample web service operation
	 */
	@WebMethod(operationName = "hello")
	public String hello(@WebParam(name = "name") String txt) {
		return "Hello " + txt + " !";
	}

	/**
	 * Operação de Web service
	 */
	@WebMethod(operationName = "operation")
	public String operation() {
		//TODO write your implementation code here:
		return null;
	}
}
