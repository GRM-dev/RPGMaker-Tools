package pl.grm.rvpacker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import org.ini4j.*;
import org.ini4j.Profile.Section;

public class FileOperation {

	private static Logger logger;

	public static String getCurrentJarPath(Class<?> classHandler) throws UnsupportedEncodingException {
		String jarFileLoc = "";
		jarFileLoc = URLDecoder.decode(classHandler.getProtectionDomain().getCodeSource().getLocation().getPath(),
				"UTF-8");
		jarFileLoc = jarFileLoc.replace("file:/", "");
		int index = 100;
		if (jarFileLoc.contains("!")) {
			index = jarFileLoc.indexOf("!");
			jarFileLoc = jarFileLoc.substring(0, index);
		}
		if (jarFileLoc.contains("/")) {
			index = jarFileLoc.indexOf("/");
			if (index == 0) {
				jarFileLoc = jarFileLoc.substring(1, jarFileLoc.length());
			}
		}
		return jarFileLoc;
	}

	public static Logger setupLogger(String fileName) throws IllegalArgumentException {
		logger = Logger.getLogger(fileName);
		try {
			FileHandler fileHandler = new FileHandler(MainPacker.EXECUTABLE_PATH + fileName, 1048476, 1, true);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		}
		catch (SecurityException e) {}
		catch (IOException e) {}
		logger.info("Execution Location: " + MainPacker.EXECUTABLE_PATH);
		return logger;
	}

	public static boolean configExists() {
		File file = new File(MainPacker.CONFIG_FILE_NAME);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static void saveConfig(HashMap<ConfigId, String> config) {
		File file = new File(MainPacker.CONFIG_FILE_NAME);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			Ini ini = new Wini(file);
			for (Iterator<ConfigId> it = config.keySet().iterator(); it.hasNext();) {
				ConfigId key = it.next();
				String value = config.get(key);
				ini.add("configuration", key.toString(), value);
			}
			ini.store();
		}
		catch (IOException e) {
			logger.log(Level.CONFIG, "cannot save config file", e);
			e.printStackTrace();
		}
	}

	public static HashMap<ConfigId, String> readConfig() {
		HashMap<ConfigId, String> fConfig = new HashMap<>();
		File file = new File(MainPacker.CONFIG_FILE_NAME);
		try {
			Ini ini = new Wini(file);
			Section section = ini.get("configuration");
			if (section == null) { throw new IOException("Cannot find configuration section"); }
			for (String key : section.keySet()) {
				try {
					ConfigId keyE = ConfigId.getFromString(key);
					fConfig.put(keyE, section.get(key));
				}
				catch (IllegalArgumentException e) {
					logger.log(Level.CONFIG, "Config with name " + key + " not exists", e);
				}
			}
		}
		catch (IOException e) {
			logger.log(Level.CONFIG, "cannot load config file", e);
			e.printStackTrace();
		}
		return fConfig;
	}

	public static File getFile(String fileName) throws FileNotFoundException {
		ClassLoader classLoader = FileOperation.class.getClassLoader();
		URL resFile = classLoader.getResource(fileName);
		File file;
		if (resFile != null) {
			file = new File(resFile.getFile());
		} else {
			file = new File(fileName);
		}
		if (file == null || !file.exists()) { throw new FileNotFoundException("Nie znaleziono pliku o nazwie "
				+ fileName); }
		return file;
	}
}
