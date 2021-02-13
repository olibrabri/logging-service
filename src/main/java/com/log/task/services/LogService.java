package com.log.task.services;

import com.log.task.models.Server;

public interface LogService {

	public String getServerLog(String server);

	public String getAllServerLogs();

	public void startServer(Server server);

	public void completeServerJob(Server server);

	public void terminateServer(Server server);

}
