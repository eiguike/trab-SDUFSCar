/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

/**
 *
 * @author rick
 */
public class main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		System.out.println(main.hello("meunom é heuheu"));
	}

	private static String hello(java.lang.String name) {
		newpackage.Test_Service service = new newpackage.Test_Service();
		newpackage.Test port = service.getTestPort();
		return port.hello(name);
	}
	
}
