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
