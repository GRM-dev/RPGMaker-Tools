package pl.grmdev.rpgmaker.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.Path;

import org.apache.logging.log4j.core.LoggerContext;

@Path("logs")
public class CLogger {

	private static Logger fileLogger;
	private static org.apache.logging.log4j.Logger dbLogger;
	private static String logFileName = "info.log";
	private static String locPath = "logs\\";

	public static void initLogger() {
		fileLogger = Logger.getLogger(logFileName);
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
		fileLogger.addHandler(fHandler);
		dbLogger = LoggerContext.getContext(true).getLogger("sql");
	}

	public static void info(String msg) {
		if (fileLogger != null) {
			fileLogger.info(msg);
		}
		if (dbLogger != null) {
			try {
				dbLogger.info(msg);
			}
			catch (Exception ex) {
				fileLogger.log(Level.SEVERE, ex.toString());
			}
		}
	}

	public static void warn(String msg) {
		if (fileLogger != null) {
			fileLogger.log(Level.WARNING, msg);
		}
		if (dbLogger != null) {
			try {
				dbLogger.warn(msg);
			}
			catch (Exception ex) {
				fileLogger.log(Level.SEVERE, ex.toString());
			}
		}
	}
	
	public static void log(Level level, String msg, Throwable thrown) {
		if (fileLogger != null) {
			fileLogger.log(level, msg, thrown);
		}
		if (dbLogger != null) {
			try {
			if (level == Level.SEVERE) {
				dbLogger.error(msg, thrown);
			} else if (level == Level.WARNING) {
				dbLogger.warn(msg, thrown);
			} else if (level == Level.INFO) {
				dbLogger.info(msg, thrown);
			} else {
				dbLogger.info("ULC: " + msg, thrown);
			}
		}
			catch (Exception ex) {
				fileLogger.log(Level.SEVERE, ex.toString());
			}
		}
	}

	public static void logException(Exception e) {
		log(Level.SEVERE, e.toString(), e);
	}

	public static void setFileLogger(Logger logger) {
		fileLogger = logger;
	}

	public static void closeLoggers() {
		Handler handler = fileLogger.getHandlers()[0];
		fileLogger.removeHandler(handler);
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
