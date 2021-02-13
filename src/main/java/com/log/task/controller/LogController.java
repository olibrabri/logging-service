package com.log.task.controller;

import com.log.task.models.Server;
import com.log.task.services.impl.LogServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/server/logs")
public class LogController {

	@Autowired
	private LogServiceImpl logService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

	@GetMapping(value = "/retrieve/{server}/log")
	public ResponseEntity<String> retrieveServerLogs(@PathVariable String server) {
		LOGGER.info("***** Retrieving server log *****");

		String serverLog = logService.getServerLog(server);
		return new ResponseEntity<>(serverLog, HttpStatus.OK);
	}

	@GetMapping(value = "/retrieve/all")
	public ResponseEntity<String> retrieveAllLogs() {
		LOGGER.info("***** Retrieving all server logs *****");

		String serverLogs = logService.getAllServerLogs();
		return new ResponseEntity<>(serverLogs, HttpStatus.OK);
	}

	@PostMapping(value = "/start")
	public ResponseEntity<String> startServer(@RequestBody Server server) {
		LOGGER.info("***** Starting server *****");

		logService.startServer(server);
		return new ResponseEntity<>(server.getServerName() + " started successfully!", HttpStatus.OK);
	}

	@PostMapping(value = "/complete")
	public ResponseEntity<String> completeServerJob(@RequestBody Server server) {
		LOGGER.info("***** Completing server job *****");

		logService.completeServerJob(server);
		return new ResponseEntity<>(server.getServerName() + " completed job successfully!", HttpStatus.OK);
	}

	@PostMapping(value = "/terminate")
	public ResponseEntity<String> terminateServer(@RequestBody Server server) {
		LOGGER.info("***Terminating server *****");

		logService.terminateServer(server);
		return new ResponseEntity<>(server.getServerName() + " terminated successfully!", HttpStatus.OK);
	}

}
