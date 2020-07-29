package com.stanfan.PlayerInventoryBuilder;

import java.util.ArrayList;
import java.util.List;



public class Player {

	private String firstName;
	private String lastName;
	private int salary;
	private int length;
	private String highestBidder;
	private String position;
	private int ownerId;
	private int espnId;
	private int playerId;
	private int contractValue;
	
	public Player(int espnId, String firstName, String lastName, String position) {
		this.espnId = espnId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.position = position;
		this.salary = 0;
		this.length = 0;
		this.contractValue = this.salary + (this.length * 5);
	}
	
	public Player() {
//		this.salary = 0;
//		this.length = 0;
//		this.contractValue = this.salary + (this.length * 5);
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getHighestBidder() {
		return highestBidder;
	}

	public void setHighestBidderId(String highestBidder) {
		this.highestBidder = highestBidder;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getEspnId() {
		return espnId;
	}

	public void setEspnId(int espnId) {
		this.espnId = espnId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getContractValue() {
		return contractValue;
	}

	public void setContractValue(int contractValue) {
		this.contractValue = contractValue;
	}
	


	public List<Player> makePlayerInventory(EspnPlayer[] espnPlayers, PlayerDAO playerDAO) {
		List<Player> ourPlayers = new ArrayList<Player>();
		for (EspnPlayer player : espnPlayers) {
			if (player.getDefaultPositionId() < 5) {
				String firstName = player.getFirstName();
				String lastName = player.getLastName();
				int espnId = player.getId();
				String position = posToString(player.getDefaultPositionId());
				Player thisPlayer = new Player(espnId, firstName, lastName, position);
				if(!playerDAO.playerAlreadyListed(thisPlayer.getEspnId())) {
					thisPlayer = playerDAO.insertPlayer(thisPlayer);
					System.out.println("made a player..." + thisPlayer.getFirstName() + " " + thisPlayer.getLastName() + " " + thisPlayer.getPosition());
				}
				ourPlayers.add(thisPlayer);
				
			}
		}
		return ourPlayers;
	}
	
	public String posToString(int posId) {
		if (posId == 1) {
			return "QB";
		}
		if (posId == 2) {
			return "RB";
		}
		if (posId == 3) {
			return "WR";
		}
		if (posId == 4) {
			return "TE";
		} else {
			return "NA";
		}

	}
	
}
