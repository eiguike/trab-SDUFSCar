/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.calculator;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author ricke
 */
@WebService(serviceName = "Calculadora")
public class Calculadora {


	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "add")
	public int add(@WebParam(name = "i") int i, @WebParam(name = "j") int j) {
		//TODO write your implementation code here:
		int k = i + j;
		return k;
	}
}
