package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Accoppiamento;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs(Map<Integer, Nerc> idMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				idMap.put(n.getId(), n);
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<Accoppiamento> loadAllEdges() {

		String sql = "SELECT nerc_one, nerc_two FROM nercrelations";
		List<Accoppiamento> result = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Accoppiamento(res.getInt("nerc_one"), res.getInt("nerc_two"), 0.0));		
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
	
	/**
	 * Ottengo i pesi di quell'accoppiamento
	 * @param a
	 */
			public void getAllWeight(Accoppiamento a) {

				final String sql="SELECT COUNT(*) AS cnt " + 
						"FROM poweroutages p1, poweroutages p2 " + 
						"WHERE YEAR(p1.date_event_began)=YEAR(p2.date_event_began) AND MONTH(p1.date_event_began)=MONTH(p2.date_event_began) AND p1.nerc_id =? AND p2.nerc_id =?";
				try {
					
					Connection conn = ConnectDB.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);
					
					//Setto parametri
					st.setInt(1, a.getId1());
					st.setInt(2, a.getId2());
					
					ResultSet res = st.executeQuery();

					while (res.next()) {	
						a.setPeso(res.getDouble("cnt"));
					}

					conn.close();

				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

			}


}
