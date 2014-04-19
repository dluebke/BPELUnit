package net.bpelunit.framework.coverage.activevos9;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IProcess;

public class ProcessInstance {

	private IProcess process;
	private String log;
	private long processId;
	private QName processName;

	public ProcessInstance(IProcess process, String log, long processId,
			QName processName) {
		super();
		this.process = process;
		this.log = log;
		this.processId = processId;
		this.processName = processName;
	}


	public IProcess getProcess() {
		return process;
	}

	public void setProcess(IProcess process) {
		this.process = process;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public QName getProcessName() {
		return processName;
	}

	public void setProcessName(QName processName) {
		this.processName = processName;
	}

}
