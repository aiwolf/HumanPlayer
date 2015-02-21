package org.aiwolf.client.ui.bin;

import java.io.IOException;

import org.aiwolf.client.base.smpl.SampleRoleAssignPlayer;
import org.aiwolf.client.ui.HumanPlayer;
import org.aiwolf.client.ui.res.DefaultResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Role;
import org.aiwolf.server.bin.RoleRequestStarter;

/**
 * クライアントを指定して直接シミュレーションを実行する
 * @author tori
 *
 */
public class HumanPlayerStarter {

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
//		Role role = null;
		Role role = Role.WEREWOLF;
//		HumanPlayer humanPlayer = new HumanPlayer(new JapaneseResource());
		HumanPlayer humanPlayer = new HumanPlayer(new DefaultResource());
		String defaultClassName = SampleRoleAssignPlayer.class.getName();
//		String defaultClassName = InabaPlayer.class.getName();
//		String logDir = null;
		String logDir = "./log/";
		int agentNum = 12;
		
		for(int i = 0; i < args.length; i++){
			if(args[i].startsWith("-")){
				if(args[i].equals("-r")){
					i++;
					role = Role.valueOf(args[i]);
				}
				else if(args[i].equals("-n")){
					i++;
					agentNum = Integer.parseInt(args[i]);
				}
				else if(args[i].equals("-d")){
					i++;
					defaultClassName = args[i];
				}
				else if(args[i].equals("-l")){
					i++;
					logDir = args[i];
				}
			}
		}
		
		
		RoleRequestStarter.start(humanPlayer, role, agentNum, defaultClassName, logDir);
//		start(humanPlayer, role, agentNum, defaultClassName, logDir);
	}
	/**
	 * 一人のRoleを指定してDirectに実行
	 * @param player
	 * @param role
	 * @param playerNum
	 * @param defaultClsName
	 * @param logDir
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
//	public static void start(Player player, Role role, int playerNum, String defaultClsName, String logDir) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
//		Map<Player, Role> playerMap = new HashMap<Player, Role>();
//
//		playerMap.put(player, role);
//		while(playerMap.size() < playerNum){
//			playerMap.put((Player) Class.forName(defaultClsName).newInstance(), null);
//		}
//		start(playerNum, playerMap, logDir);
//	}
//	
//	public static void start(int playerNum, Map<Player, Role> playerMap, String logDir) throws IOException {
//		String timeString = CalendarTools.toDateTime(System.currentTimeMillis()).replaceAll("[\\s-/:]", "");
//		File logFile = new File(String.format("%s/aiwolfGame%s.log", logDir, timeString));
//		
//		DirectConnectServer gameServer = new DirectConnectServer(playerMap);
//		GameSettingEntity gameSetting = GameSettingEntity.getDefaultGame(playerNum);
//		AIWolfGame game = new AIWolfGame(gameSetting, gameServer);
//		if(logDir != null){
//			game.setLogFile(logFile);
//		}
//		game.setRand(new Random());
//		game.start();
//	}

}
