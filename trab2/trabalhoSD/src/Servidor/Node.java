/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.Serializable;

/**
 *
 * @author rick
 */
public class Node implements Serializable{
	private Integer clock;
	private Integer id;
	private boolean ok;
        private boolean allowed;
	private boolean send;
        private boolean wonPosition;

	private Integer idTarget;

	public void printf(){
		System.out.println("-----------------");
		System.out.println("Clock: "+this.clock);
		System.out.println("ID: "+this.id);
		System.out.println("Thank: "+this.ok);
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

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean thank) {
		this.ok = thank;
	}
        
        public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
        
        public boolean wonPosition() {
            return wonPosition;
        }

        public void setWonPosition(boolean wonPosition) {
            this.wonPosition = wonPosition;
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
