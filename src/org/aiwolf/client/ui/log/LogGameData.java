package org.aiwolf.client.ui.log;

import java.util.HashMap;
import java.util.Map;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.server.GameData;

class LogGameData extends GameData {

	public LogGameData(GameSetting gameSetting, Map<Agent, Role> agentRoleMap) {
		super(gameSetting);
		
		this.agentRoleMap = new HashMap<Agent, Role>(agentRoleMap);
		this.agentStatusMap = new HashMap<Agent, Status>();
		for(Agent agent:agentRoleMap.keySet()){
			agentStatusMap.put(agent, Status.ALIVE);
		}
	}

	public GameData nextDay(){
		LogGameData gameData = new LogGameData(gameSetting, agentRoleMap);
		
		gameData.day = this.day+1;
		gameData.agentStatusMap = new HashMap<Agent, Status>(agentStatusMap);
		if(executed != null){
			gameData.agentStatusMap.put(executed, Status.DEAD);
		}
		if(attacked != null){
			gameData.agentStatusMap.put(attacked, Status.DEAD);
		}
		gameData.agentRoleMap = new HashMap<Agent, Role>(agentRoleMap);
		
		gameData.dayBefore = this;
		
		return gameData;
	}
	
}