package com.log.task.services.impl;

import com.log.task.models.Server;
import com.log.task.services.LogService;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

@Service
public class LogServiceImpl implements LogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);

	@Value("${log.server.path}")
	private String logServerPath;

	private static final String SERVER = "server-";
	private static final String LOG_EXTENSION = ".log";
	private static final String FILE_DOES_NOT_EXIST = "File does not exist";

	@Override
	public String getServerLog(String server) {

		try {
			String serverLog = readFile(server);
			return serverLog;

		} catch (Exception io) {
			io.printStackTrace();
			return io.getMessage();
		}
	}

	@Override
	public String getAllServerLogs() {

		File file = new File(logServerPath);
		File[] files = file.listFiles();
		SortedMap<String, String> fileDate = new TreeMap<>();
		StringBuilder sb = new StringBuilder();
		for (File f : files) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					String dateLog = line.substring(0, line.indexOf(","));
					String contentLog = line.substring(line.indexOf(",") + 1, line.length());

					fileDate.put(dateLog, contentLog);
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				return e.getMessage();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				LOGGER.error(ioe.getMessage());
				return ioe.getMessage();
			}

		}
		for (Entry<String, String> entry : fileDate.entrySet()) {
			String dateMap = entry.getKey();
			String stringMap = entry.getValue();
			sb.append(dateMap + "," + stringMap + "\n");
		}
		return sb.toString();

	}

	@Override
	public void startServer(Server server) {
		if (!server.getServerName().isEmpty()) {
			createCustomServerFile(server.getServerName());
		} else {
			createRandomServerFile();
		}
	}

	@Override
	public void completeServerJob(Server server) {
		if (!server.getServerName().isEmpty()) {
			String logFileName = SERVER + server.getServerName() + LOG_EXTENSION;
			File file = new File(logServerPath + logFileName);
			if (file.exists()) {
				writeToFile(file, ", Server " + server.getServerName() + " completed job.", true);
			} else {
				LOGGER.error("Server name does not exists.");
			}
		} else {
			LOGGER.error("Server name should not be empty.");
		}
	}

	@Override
	public void terminateServer(Server server) {
		String logFileName = SERVER + server.getServerName() + LOG_EXTENSION;
		File file = new File(logServerPath + logFileName);
		if (file.exists()) {
			writeToFile(file, ", Server " + server.getServerName() + " terminated.", true);
		}
	}

	private String readFile(String server) {
		try {
			File file = getFile(server);

			if (file.exists()) {
				String log = new String(Files.readAllBytes(file.toPath()));
				return log;
			} else {
				return FILE_DOES_NOT_EXIST;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	private void createRandomServerFile() {

		String serverName = this.getRandomServerName();
		File file = getFile(serverName);

		try {
			if (file.createNewFile()) {
				LOGGER.info("File has been created.");
				writeToFile(file, ", Server " + serverName + " started.", false);
			} else {
				LOGGER.info("File already exists.");
				writeToFile(file, ", Server " + serverName + " restarted.", true);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

	}

	private void createCustomServerFile(String serverName) {

		String logFileName = SERVER + serverName + LOG_EXTENSION;
		File file = new File(logServerPath + logFileName);

		try {
			if (file.createNewFile()) {
				LOGGER.info("File has been created.");
				writeToFile(file, ", Server " + serverName + " started.", false);

			} else {
				LOGGER.info("File already exists.");
				writeToFile(file, ", Server " + serverName + " started.", true);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

	}

	private void writeToFile(File file, String text, boolean exists) {
		try {
			// ISO 8601 format
			// 2016-12-20T19:00:45Z, Server A completed job, Server A started, Server A
			// terminated
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			dateFormat.setTimeZone(timeZone);

			FileOutputStream outputStream = new FileOutputStream(file, true);
			String date = exists ? "\n" + dateFormat.format(new Date()) : dateFormat.format(new Date());
			byte[] strToBytes = (date + text).getBytes();
			outputStream.write(strToBytes);
			outputStream.close();

		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage());
			ioe.printStackTrace();
		}

	}

	private File getFile(String serverName) {
		String logFileName = SERVER + serverName + LOG_EXTENSION;

		File file = new File(logServerPath + logFileName);
		return file;
	}

	private String getRandomServerName() {
		return RandomStringUtils.random(8, true, true);
	}

}
