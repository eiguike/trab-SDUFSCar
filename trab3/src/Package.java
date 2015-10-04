
import java.io.Serializable;
import java.util.ArrayList;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author thiago
 */
public class Package implements Serializable {

	// clock atual do nó que remetente
	private Integer clockSource;

	// pid do remetente
	private Integer pidSource;
	
	// pid do destinatário
	private Integer pidTarget;

	// comandos que serão enviados pela mensagem
	private Integer Ok;

	// 1 = ok, 2 = eleicao, 3 = definir clock, 4 = parar thread 
	// 5 = iam alive
	private Integer cmd;

	public Package(Integer clock, Integer pidTarget, Integer pidActual, Integer cmd) {
		this.clockSource= clock;
		this.pidSource= pidActual;
		this.pidTarget = pidTarget;
		this.cmd = cmd;
	}

	public Integer getClockSource() {
		return clockSource;
	}

	public void setClockSource(Integer clockSource) {
		this.clockSource = clockSource;
	}

	public Integer getPidSource() {
		return pidSource;
	}

	public void setPidSource(Integer pidSource) {
		this.pidSource = pidSource;
	}

	public Integer getPidTarget() {
		return pidTarget;
	}

	public void setPidTarget(Integer pidTarget) {
		this.pidTarget = pidTarget;
	}

	public Integer getOk() {
		return Ok;
	}

	public void setOk(Integer Ok) {
		this.Ok = Ok;
	}

	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}


}
