package org.aiwolf.client.ui.res;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

	String[][] agentResourceAry = {
			{"Oliver", "img/00_body.png"}, 
			{"Deicle", "img/01_body.png"}, 
			{"Lenox", "img/02_body.png"}, 
			{"Traise", "img/03_body.png"}, 
			{"Dylany", "img/04_body.png"}, 
			{"Jerico", "img/05_body.png"}, 
			{"Safira", "img/06_body.png"}, 
			{"Birkley", "img/07_body.png"}, 
			{"Rodelic", "img/08_body.png"}, 
			{"Monty", "img/09_body.png"}, 
			{"Rinsy", "img/10_body.png"}, 
			{"Dacota", "img/11_body.png"}, 
			{"Masha", "img/12_body.png"}, 
			{"Kypling", "img/13_body.png"}, 
			{"Chazz", "img/14_body.png"}, 
			{"Tory", "img/15_body.png"}, 
			{"Tatton", "img/16_body.png"}, 
			{"Marinda", "img/17_body.png"}, 
			{"Randal", "img/18_body.png"}, 
			{"Nikky", "img/19_body.png"}, 
			{"Reginald", "img/20_body.png"}, 
			{"Michael", "img/21_body.png"}, 
			{"Rusias", "img/22_body.png"}, 
			{"Joakim", "img/23_body.png"}, 
			{"Casalina", "img/24_body.png"}, 
			{"Clare", "img/25_body.png"}, 
			{"Ted", "img/26_body.png"}, 
			{"Garda", "img/27_body.png"}, 
			{"Ku", "img/28_body.png"}, 
			{"Pink", "img/29_body.png"}, 
			{"Tom", "img/30_body.png"}, 
			{"Chrif", "img/31_body.png"}, 
			{"Alexey", "img/32_body.png"}, 
			{"Eimy", "img/33_body.png"}, 
			{"Jack", "img/34_body.png"}, 
			{"Catherine", "img/35_body.png"}, 
			{"Blenda", "img/36_body.png"}, 
			{"Pierre", "img/37_body.png"}, 
			{"Chocola", "img/38_body.png"}, 
			{"Zak", "img/39_body.png"}, 
			{"Emet", "img/40_body.png"}, 
			{"Emit", "img/41_body.png"}, 
			{"Sarah", "img/42_body.png"}, 
			{"Boss", "img/43_body.png"}, 
			{"Kino", "img/44_body.png"}, 
			{"Monica", "img/45_body.png"}, 
			{"Denis", "img/46_body.png"}, 
			{"Camus", "img/47_body.png"}, 
			{"Yurie", "img/48_body.png"}, 
			{"Norah", "img/49_body.png"}, 
			{"Barbara", "img/50_body.png"}, 
			{"Chercy", "img/51_body.png"}, 
			{"Cherio", "img/52_body.png"}, 
			{"Guen", "img/53_body.png"}, 
			{"Lyza", "img/54_body.png"}, 
			{"Beth", "img/55_body.png"}, 
			{"Walker", "img/56_body.png"}, 
			{"Mary", "img/57_body.png"}, 
			{"Maxris", "img/58_body.png"}, 
			{"Kanabis", "img/59_body.png"}, 
			{"Hach", "img/60_body.png"}, 
			{"Marble", "img/61_body.png"}, 
			{"Peace", "img/62_body.png"}, 
			{"Doc", "img/63_body.png"}, 
			{"Meries", "img/64_body.png"}, 
			{"Fran", "img/65_body.png"}, 
			{"Higras", "img/66_body.png"}, 
			{"Ivan", "img/67_body.png"}, 
			{"Tao", "img/68_body.png"}, 
			{"Jima", "img/69_body.png"}, 
			{"Tanya", "img/70_body.png"}, 
			{"Jonnie", "img/71_body.png"}, 
			{"Lindberg", "img/72_body.png"}, 
	};
	
	BidiMap<Agent, String> bidiMap = new BidiMap<>();
	List<String[]> agentResourceList;
		
	public DefaultResource(){
		agentResourceList = Arrays.asList(agentResourceAry);
		Collections.shuffle(agentResourceList);

		bidiMap = new BidiMap<>();
	}
	
	@Override
	public String convert(Agent agent) {
		String name = agentResourceList.get(agent.getAgentIdx())[0];
		bidiMap.put(agent, name);
		return name;
		
//		String.format("%s%02d", agent.toString(), agent.getAgentIdx());
//		bidiMap.put(agent, agent.toString());
//		return agent.toString();
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
		return String.format("%s guarded", convert(guardedAgent));
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
		String imageUrl = agentResourceList.get(agent.getAgentIdx())[1];
//		System.out.println(imageUrl+"\t"+agentResourceList.get(agent.getAgentIdx())[0]);
		URL url=getClass().getClassLoader().getResource(imageUrl);
		ImageIcon icon = new ImageIcon(url);
		return icon;
//
//		return new ImageIcon(new BufferedImage(1, 1, Image.SCALE_SMOOTH));
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
