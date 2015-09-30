package org.aiwolf.client.ui.log;

import java.awt.Image;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.util.BidiMap;

public class ContestResource extends JapaneseResource implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5033734829293769603L;

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

	
	
	public ContestResource() {
		agentResourceList = Arrays.asList(agentResourceAry);
		long seed = 30;
		Collections.shuffle(agentResourceList, new Random(seed));

		bidiMap = new BidiMap<>();
	}
	
	public void setName(int i, String name){
		agentResourceList.get(i)[0] = name;
	}


	@Override
	public String convert(Agent agent) {
		String name = agentResourceList.get(agent.getAgentIdx())[0];
		bidiMap.put(agent, name);
		System.out.println(agent+"\t"+name);
		return name;
	}

}
