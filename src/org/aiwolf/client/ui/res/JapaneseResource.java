package org.aiwolf.client.ui.res;

import java.awt.Image;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.util.BidiMap;

public class JapaneseResource implements AIWolfResource {


	String[][] agentResourceAry = {
			{"麦藁帽のオリバー", "img/00_body.png"}, 
			{"学生デイクル", "img/01_body.png"}, 
			{"変人レノックス", "img/02_body.png"}, 
			{"医者の卵トレイス", "img/03_body.png"}, 
			{"水商売ディラニイ", "img/04_body.png"}, 
			{"パジャマジェリコ", "img/05_body.png"}, 
			{"王女サフィラ", "img/06_body.png"}, 
			{"団長バークレイ", "img/07_body.png"}, 
			{"浮浪者ロデリック", "img/08_body.png"}, 
			{"掃除人モンティ", "img/09_body.png"}, 
			{"元軍人リンシイ", "img/10_body.png"}, 
			{"用心棒ダコタ", "img/11_body.png"}, 
			{"黒ずきんマーシャ", "img/12_body.png"}, 
			{"囚人キプリング", "img/13_body.png"}, 
			{"料理人チャズ", "img/14_body.png"}, 
			{"探偵トーリイ", "img/15_body.png"}, 
			{"不幸なタットン", "img/16_body.png"}, 
			{"眼鏡マリンダ", "img/17_body.png"}, 
			{"御曹司ランダル", "img/18_body.png"}, 
			{"包帯の子ニッキー", "img/19_body.png"}, 
			{"盗賊レギナルド", "img/20_body.png"}, 
			{"怠け者ミッチェル", "img/21_body.png"}, 
			{"詩人ルシアス", "img/22_body.png"}, 
			{"教師ジョアキム", "img/23_body.png"}, 
			{"村娘カサリナ", "img/24_body.png"}, 
			{"堅物クレア", "img/25_body.png"}, 
			{"曲芸師テッド", "img/26_body.png"}, 
			{"ゴロツキガルダ", "img/27_body.png"}, 
			{"牧童クゥ", "img/28_body.png"}, 
			{"モノマネ師ピンク", "img/29_body.png"}, 
			{"男前少年トム", "img/30_body.png"}, 
			{"露天商クリフ", "img/31_body.png"}, 
			{"酔払いアレクセイ", "img/32_body.png"}, 
			{"不機嫌エイミー", "img/33_body.png"}, 
			{"嘘つきジャック", "img/34_body.png"}, 
			{"記者キャサリン", "img/35_body.png"}, 
			{"令嬢ブレンダ", "img/36_body.png"}, 
			{"芸人ピエール", "img/37_body.png"}, 
			{"孤児ショコラ", "img/38_body.png"}, 
			{"旅人ザク", "img/39_body.png"}, 
			{"双子の兄エメット", "img/40_body.png"}, 
			{"双子の弟ミメット", "img/41_body.png"}, 
			{"女学生サーラ", "img/42_body.png"}, 
			{"ボスアンジェラ", "img/43_body.png"}, 
			{"老婆キノ", "img/44_body.png"}, 
			{"看板娘モニカ", "img/45_body.png"}, 
			{"優等生デニース", "img/46_body.png"}, 
			{"教徒カミュ", "img/47_body.png"}, 
			{"修道女ユーリエ", "img/48_body.png"}, 
			{"雑貨屋ノラ", "img/49_body.png"}, 
			{"踊り子バルバラ", "img/50_body.png"}, 
			{"双子のチェルシー", "img/51_body.png"}, 
			{"双子のチェリオ", "img/52_body.png"}, 
			{"猫仙人グエン", "img/53_body.png"}, 
			{"闇商人ライザ", "img/54_body.png"}, 
			{"赤面症ベス", "img/55_body.png"}, 
			{"剣士ウォーカー", "img/56_body.png"}, 
			{"洗濯屋メアリ", "img/57_body.png"}, 
			{"聖者マキリス", "img/58_body.png"}, 
			{"処刑人カナビス", "img/59_body.png"}, 
			{"悪童ハッチ", "img/60_body.png"}, 
			{"植物好きマーブル", "img/61_body.png"}, 
			{"調査員ピース", "img/62_body.png"}, 
			{"天才医師バラキン", "img/63_body.png"}, 
			{"家庭教師メリーズ", "img/64_body.png"}, 
			{"浜辺の少女フラン", "img/65_body.png"}, 
			{"作家ヒグラシ", "img/66_body.png"}, 
			{"村長イワン", "img/67_body.png"}, 
			{"食道楽タオ", "img/68_body.png"}, 
			{"アウトロージーマ", "img/69_body.png"}, 
			{"魔女ターニャ", "img/70_body.png"}, 
			{"遊び人ジョニー", "img/71_body.png"}, 
			{"旅人リンドバーグ", "img/72_body.png"}, 
	};

	
	
	List<String[]> agentResourceList;
	
	BidiMap<Agent, String> bidiMap;
	
	public JapaneseResource() {
		agentResourceList = Arrays.asList(agentResourceAry);
		Collections.shuffle(agentResourceList);

		bidiMap = new BidiMap<>();
	}
	
	@Override
	public String convert(Agent agent) {
//		return agent.toString();
		String name = agentResourceList.get(agent.getAgentIdx())[0];
		bidiMap.put(agent, name);
		return name;
	}
	
	@Override
	public String convert(Role role) {
		switch (role) {
		case BODYGUARD:
			return "狩人";
		case FREEMASON:
			return "共有者";
		case MEDIUM:
			return "霊媒師";
		case POSSESSED:
			return "狂人";
		case SEER:
			return "占い師";
		case VILLAGER:
			return "村人";
		case WEREWOLF:
			return "人狼";
		default:
			return null;
		}
	}
	
	@Override
	public String convert(Species species) {
		switch (species) {
		case HUMAN:
			return "人間";
		case WEREWOLF:
			return "人狼";
		default:
			return null;
		}
	}
	
	@Override
	public String convert(Team team) {
		switch (team) {
		case VILLAGER:
			return "村人側";
		case WEREWOLF:
			return "人狼側";
		default:
			return null;
		}
	}


	@Override
	public String convert(Topic topic) {
		switch (topic){
		case AGREE:
			return "同意";
		case ATTACK:
			return "襲撃";
		case COMINGOUT:
			return "カミングアウト";
		case DISAGREE:
			return "反対";
		case DIVINED:
			return "占い結果";
		case ESTIMATE:
			return "予想";
		case GUARDED:
			return "護衛先";
		case INQUESTED:
			return "霊媒結果";
		case OVER:
			return "発言終了";
		case SKIP:
			return "スキップ";
		case VOTE:
			return "投票先";
		default:
			return "";
		}
		
	}

	@Override
	public String convert(TalkType talkType){
		switch(talkType){
		case TALK:
			return "意見";
		case WHISPER:
			return "囁き";
		}
		return "";
	}
	
	@Override
	public Agent convertToAgent(String name) {
		return bidiMap.getKey(name);
	}
	
	@Override
	public String convertWhisper(Talk whisper) {
		Utterance utterance = new Utterance(whisper.getContent());
		Topic topic = utterance.getTopic();
		if(topic == Topic.AGREE){
			return String.format("%d日の%s(%03d)に賛成する", utterance.getTalkDay(), convert(utterance.getTalkType()), utterance.getTalkID());
		}
		else if(topic == Topic.COMINGOUT){
			return String.format("私は%sと名乗り出る", convert(utterance.getRole()));
		}
		else if(topic == Topic.DISAGREE){
			return String.format("%d日の%s(%03d)に反対する", utterance.getTalkDay(), convert(utterance.getTalkType()), utterance.getTalkID());
		}
		else if(topic == Topic.DIVINED){
			return String.format("%sを占った結果%sだったことにする", convert(utterance.getTarget()), convert(utterance.getResult()));
		}
		else if(topic == Topic.ESTIMATE){
			return String.format("%sは%sだと思う．", convert(utterance.getTarget()), convert(utterance.getRole()));
		}
		else if(topic == Topic.GUARDED){
			return String.format("%sを護衛したことにする", convert(utterance.getTarget()));
		}
		else if(topic == Topic.INQUESTED){
			return String.format("%sは%sだったと言う", convert(utterance.getTarget()), convert(utterance.getResult()));
		}
		else if(topic == Topic.VOTE){
			return String.format("%sに投票する", convert(utterance.getTarget()));
		}
		else if(topic == Topic.ATTACK){
			return String.format("%sを襲撃する", convert(utterance.getTarget()));
		}
		return whisper.getContent();
	}

	@Override
	public String convertTalk(Talk talk) {
		if(talk.isSkip()){
			return "様子を見ている";
		}
		else if(talk.isOver()){
			return "特に話すことはない";
		}
		try{
			Utterance utterance = new Utterance(talk.getContent());
			Topic topic = utterance.getTopic();
			if(topic == Topic.ATTACK){
				return String.format("%sを襲撃する", convert(utterance.getTarget()));
			}
			else if(topic == Topic.AGREE){
				return String.format("%d日の意見(%03d)に同意", utterance.getTalkDay(), utterance.getTalkID());
			}
			else if(topic == Topic.COMINGOUT){
				return String.format("【私は%s】です", convert(utterance.getRole()));
			}
			else if(topic == Topic.DISAGREE){
				return String.format("%d日の意見(%03d)に反対", utterance.getTalkDay(), utterance.getTalkID());
			}
			else if(topic == Topic.DIVINED){
				return String.format("占い結果：【%sは%s】だった．", convert(utterance.getTarget()), convert(utterance.getResult()));
			}
			else if(topic == Topic.ESTIMATE){
				return String.format("%sは%sだと思う．", convert(utterance.getTarget()), convert(utterance.getRole()));
			}
			else if(topic == Topic.GUARDED){
				return String.format("【%sを護衛】した", convert(utterance.getTarget()));
			}
			else if(topic == Topic.INQUESTED){
				return String.format("霊媒結果：【%sは%s】だった", convert(utterance.getTarget()), convert(utterance.getResult()));
			}
			else if(topic == Topic.VOTE){
				return String.format("%sに投票する", convert(utterance.getTarget()));
			}
			return talk.getContent();
		}catch(Exception e){
			return talk.getContent();
		}
	}

	@Override
	public String convertVote(Vote vote) {
		return "投票："+convert(vote.getTarget())+"\t<-"+convert(vote.getAgent());

	}

	@Override
	public String convertAttackVote(Vote vote) {
		return "襲撃："+convert(vote.getTarget())+"\t<-"+convert(vote.getAgent());

	}

	@Override
	public String convertMedium(Judge mediumResult) {
		return String.format("%sは%sだった", convert(mediumResult.getTarget()), convert(mediumResult.getResult()));
	}

	@Override
	public String convertDivined(Judge divineResult) {
		return String.format("%sは%sだった", convert(divineResult.getTarget()), convert(divineResult.getResult()));
	}

	@Override
	public String convertGuarded(Agent guardedAgent) {
		return String.format("%sを守った", convert(guardedAgent));
	}

	@Override
	public String convertAttacked(Agent attackedAgent) {
		if(attackedAgent != null){
			return String.format("%sが遺体となって発見された", convert(attackedAgent));
		}
		else{
			return String.format("誰も死ななかった");
		}
	}

	@Override
	public String convertExecuted(Agent executedAgent) {
		return String.format("%sを処刑した", convert(executedAgent));
	}

	@Override
	public String aliveRemain(int agents) {
		return String.format("%d人生存", agents);
	}

	@Override
	public ImageIcon getImageIcon(Agent agent) {
		String imageUrl = agentResourceList.get(agent.getAgentIdx())[1];
//		System.out.println(imageUrl+"\t"+agentResourceList.get(agent.getAgentIdx())[0]);
		URL url=getClass().getClassLoader().getResource(imageUrl);
		ImageIcon icon = new ImageIcon(url);
		return icon;
	}

	@Override
	public String convertWinner(Team winner) {
		if(winner == Team.VILLAGER){
			return "村に光が差し込んだ！\nすべての人狼を退治することに成功した！\n村人の勝利だ";
		}
		else{
			return "村は深い闇に包まれた．村人はすべて人狼達の胃袋に収まり，人狼はまた新たな犠牲者を求めて村を去って行った．\n人狼の勝利だ";
		}
	}

	@Override
	public String getFirstText(Agent agent, Role role) {
		Team team = role.getTeam();
		return String.format("あなたは %sです．与えられた役割は%sです．\n%sに所属しますので，%sの勝利に向けて行動してください．", convert(agent), convert(role), convert(team), convert(team));
	}


	@Override
	public String getRoleInformation(Map<Role, Integer> roleCounter) {
		StringBuffer buf = new StringBuffer();
		buf.append("この村には\n");
		String separator = "";
		for(Role role:roleCounter.keySet()){
			if(roleCounter.get(role) == 0){
				continue;
			}
			buf.append(separator);
			buf.append(convert(role)+"が"+roleCounter.get(role)+"人");
			separator = "，";
		}
		buf.append("がいるらしい");
		return buf.toString();
	}
	

	@Override
	public String convertAttackedDay(int day) {
		return day+"日目に襲撃";
	}

	@Override
	public String convertExecutedDay(int day) {
		return day+"日目に処刑";
	}
}
