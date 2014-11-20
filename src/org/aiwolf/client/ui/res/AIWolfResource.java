package org.aiwolf.client.ui.res;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.util.Counter;

/**
 * Resources for AIWolf Human Agent
 * @author tori
 *
 */
public interface AIWolfResource {

	/**
	 * convert agent to name
	 * @param agent
	 * @return
	 */
	String convert(Agent agent);

	/**
	 * convert agent to name
	 * @param agent
	 * @return
	 */
	Agent convertToAgent(String name);

	
	/**
	 * convert talk to Natural Language
	 * @param talk
	 * @return
	 */
	String convertTalk(Talk talk);
	
	/**
	 * convert whisper to Natural Language
	 * @param whisper
	 * @return
	 */
	String convertWhisper(Talk whisper);
	
	/**
	 * 
	 * @param vote
	 * @return
	 */
	String convertVote(Vote vote);
	
	/**
	 * 
	 * @param vote
	 * @return
	 */
	String convertAttackVote(Vote vote);

	/**
	 * 
	 * @param mediumResult
	 * @return
	 */
	String convertMedium(Judge mediumResult);

	/**
	 * 
	 * @param divineResult
	 * @return
	 */
	String convertDivined(Judge divineResult);

	/**
	 * 
	 * @param guardedAgent
	 * @return
	 */
	String convertGuarded(Agent guardedAgent);

	/**
	 * 
	 * @param attackedAgent
	 * @return
	 */
	String convertAttacked(Agent attackedAgent);

	/**
	 * 
	 * @param executedAgent
	 * @return
	 */
	String convertExecuted(Agent executedAgent);

	/**
	 * 
	 * @param role
	 * @return
	 */
	String convert(Role role);

	/**
	 * 
	 * @param species
	 * @return
	 */
	String convert(Species species);

	/**
	 * 
	 * @param team
	 * @return
	 */
	String convert(Team team);

	/**
	 * Information of alival agents num
	 * @param size
	 * @return
	 */
	String aliveRemain(int agents);
	
	/**
	 * 
	 * @param topic
	 * @return
	 */
	String convert(Topic topic);

	/**
	 * get ImageIcon of Agent
	 * @return
	 */
	ImageIcon getImageIcon(Agent agent);

	/**
	 * get win text
	 * @param winner
	 * @return
	 */
	String convertWinner(Team winner);

	/**
	 * 
	 * @param agent
	 * @param role
	 * @return
	 */
	String getFirstText(Agent agent, Role role);

	/**
	 * 
	 * @param counter
	 * @return
	 */
	String getRoleInformation(Map<Role, Integer> roleCounter);

	/**
	 * 
	 * @param talkType
	 * @return
	 */
	String convert(TalkType talkType);

	/**
	 * 
	 * @param day
	 * @return
	 */
	String convertAttackedDay(int day);

	/**
	 * 
	 * @param day
	 * @return
	 */
	String convertExecutedDay(int day);

	
}
