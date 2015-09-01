/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Node;

import java.io.Serializable;

/**
 *
 * @author rick
 */
public class Node implements Serializable{
	private Integer clock;
	private Integer id;
	private boolean thank;
	private boolean send;
	private Integer idTarget;

	public void printf(){
		System.out.println("-----------------");
		System.out.println("Clock: "+this.clock);
		System.out.println("ID: "+this.id);
		System.out.println("Thank: "+this.thank);
		System.out.println("Send: "+this.send);
		System.out.println("ID Target: "+this.idTarget);
		System.out.println("-----------------");
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public Integer getIdTarget() {
		return idTarget;
	}

	public void setIdTarget(Integer idTarget) {
		this.idTarget = idTarget;
	}

	public boolean isThank() {
		return thank;
	}

	public void setThank(boolean thank) {
		this.thank = thank;
	}

	public Integer getClock() {
		return clock;
	}

	public void setClock(Integer clock) {
		this.clock = clock;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
