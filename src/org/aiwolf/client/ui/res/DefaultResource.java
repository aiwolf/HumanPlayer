package org.aiwolf.client.ui.res;

import java.awt.Image;
import java.awt.image.BufferedImage;
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
import org.aiwolf.common.util.BidiMap;
import org.aiwolf.common.util.Counter;

/**
 * Default Resources
 * @author tori
 *
 */
public class DefaultResource implements AIWolfResource {

	BidiMap<Agent, String> bidiMap = new BidiMap<>();
	
	@Override
	public String convert(Agent agent) {
		bidiMap.put(agent, agent.toString());
		return agent.toString();
	}

	@Override
	public Agent convertToAgent(String name) {
		return bidiMap.getKey(name);
	}

	
	@Override
	public String convertTalk(Talk talk) {
		return talk.getContent();
	}

	@Override
	public String convertWhisper(Talk whisper) {
		return whisper.getContent();
	}

	@Override
	public String convertVote(Vote vote) {
		return convert(vote.getAgent())+" voted to "+convert(vote.getTarget());
	}

	@Override
	public String convertAttackVote(Vote vote) {
		return convert(vote.getAgent())+" attack to "+convert(vote.getTarget());
	}

	@Override
	public String convertMedium(Judge mediumResult) {
		return String.format("%s was %s", convert(mediumResult.getTarget()), convert(mediumResult.getResult()));
	}

	@Override
	public String convertDivined(Judge divineResult) {
		return String.format("%s was %s", convert(divineResult.getTarget()), convert(divineResult.getResult()));
	}

	@Override
	public String convertGuarded(Agent guardedAgent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String convertAttacked(Agent attackedAgent) {
		return String.format("%s was attacked", convert(attackedAgent));
	}

	@Override
	public String convertExecuted(Agent executedAgent) {
		return String.format("%s was executed", convert(executedAgent));
	}

	@Override
	public String convert(Role role) {
		return role.toString();
	}

	@Override
	public String convert(Species species) {
		return species.toString();
	}

	@Override
	public String convert(Team team) {
		return team.toString();
	}

	@Override
	public String aliveRemain(int agents) {
		return String.format("%d agents alive", agents);
	}

	@Override
	public String convert(Topic topic) {
		return topic.toString();
	}

	@Override
	public ImageIcon getImageIcon(Agent agent) {
		return new ImageIcon(new BufferedImage(1, 1, Image.SCALE_SMOOTH));
	}

	public String convertWinner(Team winner){
		return "Winner:"+convert(winner);
	}

	@Override
	public String getFirstText(Agent agent, Role role) {
		return String.format("You are %s with role %s", convert(agent), convert(role));
	}

	@Override
	public String getRoleInformation(Map<Role, Integer> roleCounter) {
		StringBuffer buf = new StringBuffer();
		for(Role role:roleCounter.keySet()){
			if(roleCounter.get(role) == 0){
				continue;
			}
			buf.append(convert(role)+":"+roleCounter.get(role)+", ");
		}
//		buf.append("\n");
		return buf.toString();
	}

	@Override
	public String convert(TalkType talkType) {
		return talkType.toString();
	}

	@Override
	public String convertAttackedDay(int day) {
		return "Attacked@"+day;
	}

	@Override
	public String convertExecutedDay(int day) {
		return "Executed@"+day;
	}
}
