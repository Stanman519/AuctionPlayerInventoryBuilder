package com.stanfan.PlayerInventoryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;





@Component
public class InventoryManager {

	private final String url = "http://fantasy.espn.com/apis/v3/games/ffl/seasons/2020/players?scoringPeriodId=0&view=players_wl";
	private final String mflUrl = "https://ryan-passion-project.apps.vn01.pcf.dcsg.com/Mfl/currentFreeAgents/2020";
	private PlayerDAO playerDAO;
	RestTemplate restTemplate = new RestTemplate();
	Map<Integer, String> posMap = new HashMap<Integer, String>();
	
	public InventoryManager(DataSource dataSource){
		playerDAO = new JDBCPlayerDAO(dataSource);
	}
	
	@PostConstruct
	public void buildInventory() {
	
	
		MflPlayer[] freeAgents = null;
		try {
			freeAgents = getAllFreeAgents();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Player> auctionInventory = enterFreeAgentInventory(freeAgents);
	
	}
	
	public MflPlayer[] getAllFreeAgents() {
		
	   ResponseEntity<MflPlayer[]> response = restTemplate.getForEntity(mflUrl, MflPlayer[].class);
	   MflPlayer[] playerInventory = response.getBody();
	   return playerInventory;  
	}
	
	
	public EspnPlayer[] getAllPlayers() throws Exception{
		
		   ResponseEntity<EspnPlayer[]> response = restTemplate.getForEntity(url, EspnPlayer[].class);
		   EspnPlayer[] playerInventory = response.getBody();
		   return playerInventory;  
		}
	
	public List<Player> enterFreeAgentInventory(MflPlayer[] freeAgents) {
		List<Player> ourPlayers = new ArrayList<Player>();
		
		for (MflPlayer player : freeAgents) {

			String firstName = player.getName();
			String lastName = "";
			int espnId = Integer.parseInt(player.getId());
			String position = player.getPosition();
			Player thisPlayer = new Player(espnId, firstName, lastName, position);
			if(!playerDAO.playerAlreadyListed(thisPlayer.getEspnId())) {
				thisPlayer = playerDAO.insertPlayer(thisPlayer);
				System.out.println("made a player..." + thisPlayer.getFirstName() + " " + thisPlayer.getLastName() + " " + thisPlayer.getPosition());
				ourPlayers.add(thisPlayer);
			}
			
			
		}
		System.out.println("done building inventory.");
		return ourPlayers;
	}
	
//	public List<Player> makePlayerInventory(EspnPlayer[] espnPlayers) {
//		List<Player> ourPlayers = new ArrayList<Player>();
//		
//		posMap.put(1, "QB");
//		posMap.put(2, "RB");
//		posMap.put(3, "WR");
//		posMap.put(4, "TE");
//		
//		for (EspnPlayer player : espnPlayers) {
//			if (player.getDefaultPositionId() < 5) {
//				String firstName = player.getFirstName();
//				String lastName = player.getLastName();
//				int espnId = player.getId();
//				String position = posToString(player.getDefaultPositionId());
//				Player thisPlayer = new Player(espnId, firstName, lastName, position);
//				if(!playerDAO.playerAlreadyListed(thisPlayer.getEspnId())) {
//					thisPlayer = playerDAO.insertPlayer(thisPlayer);
//					System.out.println("made a player..." + thisPlayer.getFirstName() + " " + thisPlayer.getLastName() + " " + thisPlayer.getPosition());
//					ourPlayers.add(thisPlayer);
//				}
//				
//			}
//		}
//		System.out.println("done building inventory.");
//		return ourPlayers;
//	}

	public String posToString(int posId) {
		return posMap.get(posId);
	}
	
}
