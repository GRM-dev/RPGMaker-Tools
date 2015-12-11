package pl.grmdev.rpgmaker.utils;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.logging.*;

import javax.persistence.*;
import javax.ws.rs.Path;

@Entity
@Table(name = "rpg_logs")
@Path("logs")
public class CLogger {

	private static Logger logger;
	private static String logFileName = "info.log";
	private static String locPath = "logs\\";
	private static boolean dbEnabled = false;

	public static void initLogger() {
		logger = Logger.getLogger(logFileName);
		FileHandler fHandler = null;
		File mainDir = new File(locPath);
		try {
			if (!mainDir.exists() && !mainDir.mkdir()) {
				throw new IOException("Cannot create main '{app}/logs' folder!");
			}
			fHandler = new FileHandler(locPath + logFileName, 1048476, 1, true);
			SimpleFormatter formatter = new SimpleFormatter();
			fHandler.setFormatter(formatter);
			System.out.println(mainDir.getAbsolutePath());
		}
		catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		logger.addHandler(fHandler);
	}

	public static void info(String msg) {
		if (logger != null) logger.info(msg);
	}

	public static void log(Level level, String msg, Throwable thrown) {
		if (logger != null) logger.log(level, msg, thrown);
	}

	public static void logException(Exception e) {
		log(Level.SEVERE, e.getMessage(), e);
	}

	public static void setLogger(Logger logger) {
		CLogger.logger = logger;
	}

	public static void closeLogger() {
		Handler handler = logger.getHandlers()[0];
		logger.removeHandler(handler);
		handler.close();
	}

	public static void printDebugFieldValue(Object obj, String... stringsFieldNames) {
		Class<?> clazz = obj.getClass();
		if (stringsFieldNames.length == 0) { return; }
		ArrayList<Field> fields = new ArrayList<Field>();
		Field[] fieldsClazz = clazz.getDeclaredFields();
		for (Field field : fieldsClazz) {
			fields.add(field);
		}
		Class<?> sClazz = clazz;
		while (sClazz.getSuperclass() != null && sClazz.getSuperclass() != Object.class) {
			Class<?> superClass = sClazz.getSuperclass();
			fieldsClazz = superClass.getDeclaredFields();
			for (Field field : fieldsClazz) {
				fields.add(field);
			}
			sClazz = superClass;
		}
		System.out.println("ANALYZE OF " + fields.size() + " FIELDS");
		try {
			for (String string : stringsFieldNames) {
				System.out.print("Field ");
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(string)) {
						if (clazz.getMethod("toString") != null) {
							Method method = clazz.getMethod("toString");
							Class<?> fieldClass = field.getDeclaringClass();
							Object objCasted = fieldClass.cast(obj);
							System.out.print(string + ": " + method.invoke(objCasted));
						} else {
							System.out.print(string + ": " + field.toString());
						}
					}
				}
				System.out.println("");
			}
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
