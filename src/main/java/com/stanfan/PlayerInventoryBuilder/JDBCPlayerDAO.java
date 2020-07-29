package com.stanfan.PlayerInventoryBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;






public class JDBCPlayerDAO implements PlayerDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCPlayerDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override
	public List<Player> getAllPlayers(){
		List<Player> allPlayers = new ArrayList<Player>();
		String sqlGetPlayers = "SELECT playerId, firstName, lastName, position FROM player ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPlayers);
		while(results.next()) {
			Player p = new Player();
			p.setPlayerId(results.getInt("playerId"));
			p.setFirstName(results.getString("firstName"));
			p.setLastName(results.getString("lastName"));
			p.setPosition(results.getString("position"));
			allPlayers.add(p);
		}
		return allPlayers;
	}
	@Override
	public Player getPlayerById(int id) {
		Player p = new Player();
		String getPlayer = "SELECT * FROM player WHERE playerid = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(getPlayer, id);
		while(result.next()) {
		p = mapRowToPlayer(result);
		}
		return p;
	}
	
	@Override
	public Player insertPlayer(Player insertMe) {
		String sqlInsertPlayer = "INSERT INTO player (playerId, espnId, firstName, lastName, position) " +
								 "VALUES (?, ?, ?, ?, ?)";
		insertMe.setPlayerId(getNextPlayerId());
		jdbcTemplate.update(sqlInsertPlayer, insertMe.getPlayerId(), insertMe.getEspnId(), insertMe.getFirstName(), insertMe.getLastName(), insertMe.getPosition());
		return insertMe;
	}
	
	public boolean playerAlreadyListed(int id) {
		String sqlCheckForPlayer = "SELECT espnId FROM player WHERE espnId = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCheckForPlayer, id); 
		 int count = 0;
		 while(result.next()) {
			 count++;
		 }
		return count > 0;
	}
	@Override
	public List<Player> getAllPlayersOnTeam(int ownerId){
		List<Player> playersOnTeam = new ArrayList<Player>();
		String sqlPlayersOnTeam = "SELECT playerId, espnId, ownerId, firstname, lastname, position, salary, length, contractvalue " +
								  "FROM player " +
								  "WHERE ownerId = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlPlayersOnTeam);
		while(results.next()) {
			Player player = mapRowToPlayer(results);
			playersOnTeam.add(player);
		}
		return playersOnTeam;
	}
	@Override
	public void addOwnerToPlayer(int playerId, int ownerId) {
		String sqlSetOwner = "UPDATE player SET ownerId = ? WHERE playerId = ?";
		jdbcTemplate.update(sqlSetOwner, ownerId, playerId);
	}

	@Override
	public List<Player> getAvailablePlayersAtPosition(String position){
		List<Player> availableByPosition = new ArrayList<Player>();
		String sqlGetPlayers = "SELECT playerId, firstName, lastName, position FROM player WHERE position = ? AND ownerId IS NULL ORDER BY lastName;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPlayers, position);
		while(results.next()) {
			Player p = new Player();
			p.setPlayerId(results.getInt("playerId"));
			p.setFirstName(results.getString("firstName"));
			p.setLastName(results.getString("lastName"));
			p.setPosition(results.getString("position"));
			
			availableByPosition.add(p);
		}
		return availableByPosition;
	}
	
	
	public Player mapRowToPlayer(SqlRowSet rs) {
		Player p = new Player();
		p.setPlayerId(rs.getInt("playerid"));
		p.setEspnId(rs.getInt("espnid"));
		p.setOwnerId(rs.getInt("ownerid"));
		p.setFirstName(rs.getString("firstname"));
		p.setLastName(rs.getString("lastname"));
		p.setPosition(rs.getString("position"));
		p.setSalary(rs.getInt("salary"));
		p.setLength(rs.getInt("length"));
		p.setContractValue(rs.getInt("contractvalue"));
		return p;
		
	}
	

	
	private int getNextPlayerId() {
		SqlRowSet nextId = jdbcTemplate.queryForRowSet("SELECT nextval('player_playerid_seq')");
		if(nextId.next()) {
			return nextId.getInt(1);
		} else {
			throw new RuntimeException("Error.  Unable to save your reservation. Please try again later.");
		}
	}
}
