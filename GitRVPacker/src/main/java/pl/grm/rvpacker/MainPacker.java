/**
 * 
 */
package pl.grm.rvpacker;

import java.awt.EventQueue;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import pl.grm.rvpacker.Runner.RVPAction;

/**
 * @author Levvy055
 *
 */
public class MainPacker {

	public static final String EXECUTABLE_PATH = System.getProperty("user.dir") + "\\";
	public static final String CONFIG_FILE_NAME = "config.ini";
	public static final String LOGGER_FILE_NAME = "yrvPacker.log";
	private Logger logger;
	private Executor executor;
	/** Key - Identificator of config, Value - config Value */
	private HashMap<ConfigId, String> config;
	private HashMap<ArgType, HashMap<String, String>> argsMap;
	private boolean needsSave;
	private AppFrame appFrame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainPacker packer = new MainPacker();
		if (args != null && args.length > 0) {
			try {
				packer.parseArgs(args);
				String rubyPath = packer.tryFindRuby();
				if (packer.verify(rubyPath)) {
					if (rubyPath == null) {
						rubyPath = packer.tryFindRuby();
					}
					if (rubyPath != null && new File(rubyPath).exists()) {
						if (new File(rubyPath + "\\rvpacker.bat").exists()) {
							packer.setExecutor(new Executor(packer));
							int exitValue = packer.execute();
							if (exitValue == 0) {
								System.out.println("Looks like completed.");
							} else {
								System.out.println("Error!\nGot " + exitValue + " exit value.");
								System.exit(exitValue);
							}
						} else {
							System.out.println("Couldn't find rvpacker gem in Ruby directory.");
						}
					} else {
						System.out.println("Wrong Ruby Path");
					}
				} else {
					System.out.println("Bad arguments!\n You need to provide args: "
							+ "project directory(-p), action type[pack,unpack](-a)\n"
							+ "Additionally if u don't have Ruby with rvpacker in environment variables than \n"
							+ " u can provide path to its directory(-r).");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			packer.init();
			packer.start();
		}
	}

	/**
	 * Main private constructor called only from main()
	 */
	private MainPacker() {
		logger = FileOperation.setupLogger(LOGGER_FILE_NAME);
		config = createDefaultConfig();
		argsMap = new HashMap<>();
	}

	/**
	 * Collects args to appropriate list and save the in argsMap
	 * 
	 * @param args
	 */
	private void parseArgs(String[] args) throws IllegalArgumentException {
		HashMap<String, String> argsList = new HashMap<String, String>();
		HashMap<String, String> optsList = new HashMap<String, String>();
		HashMap<String, String> doubleOptsList = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
				case '-' :
					if (args[i].length() < 2) throw new IllegalArgumentException("Not a valid argument: " + args[i]);
					if (args[i].charAt(1) == '-') {
						if (args[i].length() < 3)
							throw new IllegalArgumentException("Not a valid argument: " + args[i]);
						doubleOptsList.put(args[i].substring(2, args[i].length()), "");
					} else {
						if (args.length - 1 == i) throw new IllegalArgumentException("Expected arg after: " + args[i]);
						optsList.put(args[i], args[i + 1]);
						i++;
					}
					break;
				default :
					argsList.put(args[i], "");
					break;
			}
		}
		argsMap.put(ArgType.ARGS, argsList);
		argsMap.put(ArgType.OPTS, optsList);
		argsMap.put(ArgType.DOUBLE_OPTS, doubleOptsList);
	}

	/**
	 * Verifies if argumentns of main method are correct
	 * 
	 * @param rubyPath
	 *            additional path of ruby env path
	 * 
	 */
	private boolean verify(String rubyPath) {
		HashMap<String, String> optsMap = argsMap.get(ArgType.OPTS);
		boolean p = optsMap.containsKey("-p");
		boolean a = optsMap.containsKey("-a");
		boolean r = rubyPath != null || optsMap.containsKey("-r");
		if (p) {
			if (a) {
				if (r) {
					return true;
				} else {
					System.out.println("App couldn't find Ruby path with rvpacker gem installed.\n"
							+ "You can also provide path to it.! \n" + "Use -r parameter.\n");
				}
			} else {
				System.out.println("You haven't specified type of action! \n"
						+ "Use -a parameter.\n Available tasks: [pack, unpack].");
			}
		} else {
			System.out.println("You haven't specified project directory! \n"
					+ "Use -p parameter.\n In directory should be project run file.");
		}
		return false;
	}

	/**
	 * Executes script when running with startup parameters
	 */
	private int execute() {
		Runner runner = getExecutor().getNewRunner();
		try {
			String actionS = argsMap.get(ArgType.OPTS).get("-a");
			RVPAction actionE = RVPAction.valueOf(actionS.toUpperCase());
			String projectS = argsMap.get(ArgType.OPTS).get("-p");
			String rubyS = argsMap.get(ArgType.OPTS).get("-r");
			return runner.runScript(actionE, new File(projectS).getParentFile(), rubyS, false);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Initialize program
	 */
	private void init() {
		if (FileOperation.configExists()) {
			HashMap<ConfigId, String> fileConf = FileOperation.readConfig();
			for (Iterator<ConfigId> it = fileConf.keySet().iterator(); it.hasNext();) {
				ConfigId key = it.next();
				String newValue = fileConf.get(key);
				String oldValue = config.get(key);
				if (!newValue.equals(oldValue)) {
					logger.info("Read " + key + ": " + newValue + " (default: "
							+ (oldValue.isEmpty() ? "\"\"" : oldValue) + ")");
					setConfigValue(key, newValue);
					needsSave = false;
				}
			}
		} else {
			FileOperation.saveConfig(config);
		}
		setExecutor(new Executor(this));
		appFrame = new AppFrame(this);
		String path;
		if ((path = getConfigValue(ConfigId.RUBY_PATH)) == null || !new File(path).exists()
				|| !new File(path + "\\rvpacker.bat").exists()) {
			path = tryFindRuby();
			if (path != null) {
				setConfigValue(ConfigId.RUBY_PATH, path);
			}
			save();
		}
	}

	/**
	 * Looking for Ruby path and rvpacker gem there
	 * 
	 * @return
	 */
	private String tryFindRuby() {
		String path = null;
		boolean rubyPathFound = false;
		boolean rvpackerFound = false;
		Map<String, String> envs = System.getenv();
		if (envs.containsKey("RUBY")) {
			String env = envs.get("RUBY");
			if (rubyPathFound = (path = findRuby(env)) != null) {
				rvpackerFound = findRVPacker(path);
			}
		}
		if (!rvpackerFound && envs.containsKey("RUBYPATH")) {
			String env = envs.get("RUBYPATH");
			if (rubyPathFound = (path = findRuby(env)) != null) {
				rvpackerFound = findRVPacker(path);
			}
		}
		if (!rvpackerFound && (envs.containsKey("PATH") || envs.containsKey("Path"))) {
			String env = envs.get("PATH");
			if (env == null) {
				env = envs.get("Path");
			}
			if (rubyPathFound = (path = findRuby(env)) != null) {
				rvpackerFound = findRVPacker(path);
			}
		}

		if (rubyPathFound && path != null && path.length() > 3) {
			if (rvpackerFound) {
				return path;
			} else {
				JOptionPane.showMessageDialog(appFrame,
						"Can't find gem rvpacker in Ruby path.\n  Set correct path in settings.",
						"Ruby found, but can't find gems", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(appFrame,
					"Wrong Ruby Path or no path in ENV.\n Set correct path in settings.", "Ruby path not found",
					JOptionPane.WARNING_MESSAGE);
		}
		return null;
	}

	/**
	 * @param env
	 *            environmental variable
	 * @return ruby path if found with required gem. Null if not found
	 */
	private String findRuby(String env) {
		String[] paths = env.split(";");
		for (String path : paths) {
			File file;
			if ((file = new File(path)).exists() && file.isDirectory() && new File(path + "\\ruby.exe").exists()) { return path; }
		}
		return null;
	}

	/**
	 * @param path
	 * @return true if found gem rvpacker
	 */
	private boolean findRVPacker(String path) {
		boolean f1 = new File(path).exists();
		boolean f2 = new File(path + "\\rvpacker.bat").exists();
		if (f1 && f2) { return true; }
		return false;
	}

	/**
	 * Start program
	 */
	private void start() {
		EventQueue.invokeLater(() -> {
			try {
				appFrame.applyConfig(config);
				appFrame.setVisible(true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @return default config map
	 */
	private HashMap<ConfigId, String> createDefaultConfig() {
		HashMap<ConfigId, String> configD = new HashMap<>();
		for (ConfigId conf : ConfigId.values()) {
			configD.put(conf, conf.getDefValue());
		}
		return configD;
	}

	/**
	 * Updates config value with new one
	 * 
	 * @param key
	 *            config of which value we want to update
	 * @param value
	 *            new value to override previous one
	 */
	public void setConfigValue(ConfigId key, String value) {
		if (value != null) {
			needsSave = true;
			config.put(key, value);
		}
	}

	/**
	 * Save config and ammends all actions when closing
	 */
	public void save() {
		if (needsSave) {
			FileOperation.saveConfig(config);
		}
	}

	/**
	 * Gets Logger
	 * 
	 * @return logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Gets Executor
	 * 
	 * @return executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @param rubyPath
	 * @return
	 */
	public String getConfigValue(ConfigId key) {
		return config.get(key);
	}

	private void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public enum ArgType {
		ARGS,
		OPTS,
		DOUBLE_OPTS
	}
}
