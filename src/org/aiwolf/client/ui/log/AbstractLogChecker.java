package org.aiwolf.client.ui.log;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Guard;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.BidiMap;
import org.aiwolf.server.AIWolfGame;
import org.aiwolf.server.GameData;

abstract public class AbstractLogChecker {

	protected File logFile;
	protected LogGameData gameData;
	protected ExGame game;
	protected BidiMap<Agent, String> agentNameMap;
	protected boolean hasResult;

	public AbstractLogChecker(File logFile) throws IOException {
		super();
		this.logFile = logFile;

		Map<Agent, Role> agentRoleMap = new LinkedHashMap<Agent, Role>();
		agentNameMap = new BidiMap<>();
		
		Exception ex = null;
		boolean skip = false;
		for(String line:LineReader.read(logFile, "UTF-8")){
			if(skip){
				continue;
			}
			try{
				String[] data = line.split(",");
				if(!data[1].equals("status")){
					skip = true;
					continue;
				}
				Agent agent = Agent.getAgent(Integer.parseInt(data[2]));
				Role role = Role.valueOf(data[3]);
				String name = data[5];
				agentRoleMap.put(agent, role);
	//			resource.setName(agent.getAgentIdx(), name);
				agentNameMap.put(agent, name);
			}catch(Exception e){
				ex = e;
				skip = true;
			}
		}
		if(ex != null){
			throw new RuntimeException(ex);
		}
		
		GameSetting gameSetting = GameSetting.getDefaultGame(agentRoleMap.size());
		gameData = new LogGameData(gameSetting, agentRoleMap);
		game = new ExGame(gameSetting, gameData);
		
	}

	abstract public void dailyCheck(GameData gameData);
	abstract public void lineCheck(String line, GameData gameData);
	abstract public void finishCheck(GameData gameData);
	
	public void start() throws NumberFormatException, IOException {
		hasResult = false;
		
		Exception ex = null;
		boolean skip = false;
		
		for(String line:LineReader.read(logFile)){
			if(skip){
				continue;
			}
			try{
				lineCheck(line, gameData);
				String[] data = line.split(",");
	
				if(Integer.parseInt(data[0]) != gameData.getDay()){
					dailyCheck(gameData);
					gameData = (LogGameData)gameData.nextDay();
					game.setGameData(gameData);
					
				}
				if(data[1].equals("status")){
					continue;
				}
				else if(data[1].equals("talk")){
					Talk talk = toTalk(data);
					gameData.addTalk(talk.getAgent(), talk);
				}
				else if(data[1].equals("whisper")){
					Talk talk = toTalk(data);
					gameData.addWisper(talk.getAgent(), talk);
				}
				else if(data[1].equals("divine")){
					Judge divine = toJudge(data);
					gameData.addDivine(divine);
				}
				else if(data[1].equals("guard")){
					Guard guard = toGuard(data);
					gameData.addGuard(guard);
				}
				else if(data[1].equals("vote")){
					Vote vote = toVote(data);
					gameData.addVote(vote);
				}
				else if(data[1].equals("attackVote")){
					Vote vote = toVote(data);
					gameData.addAttack(vote);
				}
				else if(data[1].equals("execute")){
					Agent target = Agent.getAgent(Integer.parseInt(data[2]));
					gameData.setExecuteTarget(target);
				}
				else if(data[1].equals("attack")){
					if(data[3].equals("true")){
						Agent target = Agent.getAgent(Integer.parseInt(data[2]));
						gameData.setAttackedTarget(target);
					}
				}
				else if(data[1].equals("result")){
					hasResult = true;
	//				gameLogger.log(line);
	//				gameLogger.close();
	//				break;
				}
			}catch(Exception e){
				ex = e;
				skip = true;
			}
		}
		
		if(ex != null){
			throw new RuntimeException(ex);
		}
		
		finishCheck(gameData);
	}

	protected Vote toVote(String[] data) {
		Agent agent = Agent.getAgent(Integer.parseInt(data[2]));
		Agent target = Agent.getAgent(Integer.parseInt(data[3]));
		Vote vote = new Vote(Integer.parseInt(data[2]), agent, target);
		return vote;
	}

	protected Talk toTalk(String[] data) {
		Talk talk = new Talk(Integer.parseInt(data[2]), Integer.parseInt(data[0]), Agent.getAgent(Integer.parseInt(data[3])), data[4]);
		return talk;
	}

	protected Judge toJudge(String[] data) {
		Agent target = Agent.getAgent(Integer.parseInt(data[3]));
		Judge judge = new Judge(Integer.parseInt(data[0]), Agent.getAgent(Integer.parseInt(data[2])), target, gameData.getRole(target).getSpecies());
		return judge;
	}

	protected Guard toGuard(String[] data) {
		Agent agent = Agent.getAgent(Integer.parseInt(data[2]));
		Agent target = Agent.getAgent(Integer.parseInt(data[3]));
		Guard guard = new Guard(Integer.parseInt(data[0]), agent, target);
		return guard;
	}

	/**
	 * @return logFile
	 */
	public File getLogFile() {
		return logFile;
	}

	/**
	 * @return hasResult
	 */
	public boolean isHasResult() {
		return hasResult;
	}

	/**
	 * @return gameData
	 */
	public GameData getGameData() {
		return gameData;
	}

	/**
	 * @return game
	 */
	public AIWolfGame getGame() {
		return game;
	}

}