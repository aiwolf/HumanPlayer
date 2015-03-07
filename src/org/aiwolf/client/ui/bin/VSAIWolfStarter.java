package org.aiwolf.client.ui.bin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.aiwolf.client.base.smpl.SampleRoleAssignPlayer;
import org.aiwolf.client.ui.HumanPlayer;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.AIWolfRuntimeException;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.server.bin.RoleRequestStarter;

/**
 * クライアントを指定して直接シミュレーションを実行する
 * @author tori
 *
 */
public class VSAIWolfStarter {

	/**
	 * Start Human Agent Starter
	 * 
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		try{
			String configFile = "./vsAIWolf.properties";
			if(args.length > 0){
				configFile = args[0];
			}
			VSAIWolfStarter starter = new VSAIWolfStarter(configFile);
			starter.init();
		}catch(Throwable e){
			e.printStackTrace();
			StringBuffer buf = new StringBuffer();
			buf.append("Following Exception have been occured.\n");
			buf.append("If you are AIWolf Developer, Please check your AIWolf program.\n");
			buf.append("If you are NOT AIWolf Developer, please contact \nhttp://aiwolf.org\n with following message.\n");
			buf.append("==============================\n");

			buf.append(String.format("Exception in thread \"%s\" ", Thread.currentThread().getName()));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			String stack = baos.toString("UTF-8");
			buf.append(stack);
			JOptionPane.showMessageDialog(null, buf.toString());
//			System.exit(1);
		}
	}
	
	File initFile;
	Properties config;
	private URLClassLoader classLoader;
	public VSAIWolfStarter(String initFile) throws FileNotFoundException, IOException{
		this.initFile = new File(initFile);
		
		this.config = new Properties();
		if(this.initFile.exists()){
			config.load(new FileReader(initFile));
		}
	}
	
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		Role role = null;
		HumanPlayer humanPlayer = new HumanPlayer(new JapaneseResource());
		String logDir = null;
		int agentNum = 12;
		boolean debug = false;

		if(config.containsKey("debug")){
			debug = Boolean.parseBoolean((String)config.get("debug"));
		}
		if(debug){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeText = sdf.format(Calendar.getInstance().getTime());
			File outFile = new File(String.format("./debug/out%s.log", timeText));
			File errFile = new File(String.format("./debug/err%s.log", timeText));
			outFile.getParentFile().mkdirs();
			System.setOut(new PrintStream(outFile));
			System.setErr(new PrintStream(errFile));
		}

		
		if(config.containsKey("role")){
			try{
				role = Role.valueOf(((String)config.get("role")).toUpperCase());
			}
			catch(IllegalArgumentException e){
				showMessage("No such Role as "+config.get("role"));
			}
		}
		if(config.containsKey("players")){
			agentNum = Integer.parseInt((String)config.get("players"));
		}
		if(config.containsKey("log")){
			logDir = (String) config.get("log");
		}

		List<String> agentClassNameList = new ArrayList<>();
		if(config.containsKey("defaultAgent")){
			for(String defaultClassName:((String) config.get("defaultAgent")).split(",")){
				agentClassNameList.add(defaultClassName);
			}
		}
		else{
			agentClassNameList.add(SampleRoleAssignPlayer.class.getName());
		}
		
		List<URL> urlList = new ArrayList<>();
		if(config.containsKey("jars")){
			String[] jars = config.getProperty("jars").split(",");
			for(String jar:jars){
				File file = new File(jar);
				if(file.isDirectory()){
					urlList.addAll(loadJar(file, true));
				}
				else{
					urlList.add(file.toURI().toURL());
				}
			}
		}
		urlList.addAll(loadJar(new File("./"), false));
		urlList.addAll(loadJar(new File("./lib/"), true));
//				System.out.println(urlList);
		classLoader = URLClassLoader.newInstance(urlList.toArray(new URL[0]), ClassLoader.getSystemClassLoader());

		start(humanPlayer, role, agentNum, agentClassNameList, logDir);
	}

	protected void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * 一人のRoleを指定してDirectに実行
	 * @param player
	 * @param role
	 * @param playerNum
	 * @param agentClassNameList
	 * @param logDir
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected void start(Player player, Role role, int playerNum, List<String> agentClassNameList, String logDir) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		Map<Player, Role> playerMap = new LinkedHashMap<Player, Role>();

//		System.out.printf("Deafult Agent:"+defaultClsName);
		playerMap.put(player, role);
		while(playerMap.size() < playerNum){
			Player enemy = getPlaeryer(agentClassNameList);
			if(enemy != null){
				playerMap.put(enemy, null);
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RoleRequestStarter.start(playerMap, logDir);
	}

	static final Random rand = new Random();
	protected Player getPlaeryer(List<String> agentClassNameList) throws InstantiationException, IllegalAccessException {
		String clsName = agentClassNameList.get(rand.nextInt(agentClassNameList.size()));
		try{
			return (Player)classLoader.loadClass(clsName).newInstance();
		}catch(NoClassDefFoundError e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try{
			return (Player)Class.forName(clsName).newInstance();
		}catch(ClassNotFoundException e2){
			e2.printStackTrace();
			showMessage("Can't find class "+agentClassNameList+"\n Use "+SampleRoleAssignPlayer.class.getName());
//			playerMap.put(new SampleRoleAssignPlayer(), null);
//			agentClassNameList = SampleRoleAssignPlayer.class.getName();
			agentClassNameList.remove(clsName);
			if(agentClassNameList.isEmpty()){
				agentClassNameList.add(SampleRoleAssignPlayer.class.getName());
			}
		}

		return null;
	}

	protected static List<URL> loadJar(File currentDir, boolean isReadDir) throws MalformedURLException {
		List<URL> urlList = new ArrayList<>();
		if(!currentDir.exists()){
			return urlList;
		}
				
		for(File file:currentDir.listFiles()){
			if(file.isDirectory() && isReadDir){
				urlList.addAll(loadJar(file, true));
			}
			if(file.getName().endsWith(".jar")){
				urlList.add(file.toURI().toURL());
			}
		}
		return urlList;
	}

}
