package core;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import parser.ChessParser;

/**
 * Represent a bot.
 * @author Paul Chaignon
 */
public class Bot extends Resource {
	private List<BotSuggestion> moves;
	protected static final String JSON_DEPTH = "depth";
	protected static final String JSON_ENGINE_SCORE = "score";
	
	/**
	 * Constructor
	 * @param uri The URI.
	 * @param name The name.
	 * @param trust The trust in this bot.
	 * @param active True if the resource is active.
	 */
	public Bot(String uri, String name, int trust, boolean active) {
		super(uri, name, trust, active);
		this.moves = new LinkedList<BotSuggestion>();
	}

	@Override
	public List<BotSuggestion> getMoveSuggestions() {
		return this.moves;
	}

	@Override
	protected void clearSuggestions() {
		this.moves.clear();
	}

	@Override
	protected void parseJSONMove(String response, String fen) {
		JSONArray jsonArray = new JSONArray(response);
		for(int i=0 ; i<jsonArray.length() ; i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			String move = json.getString(JSON_MOVE);
			if(!this.san) {
				ChessParser parser = new ChessParser(fen);
				move = parser.convertLANToSAN(move);
			}
			int depth = json.getInt(JSON_DEPTH);
			double engineScore = json.getDouble(JSON_ENGINE_SCORE);
			BotSuggestion suggestion = new BotSuggestion(move, depth, engineScore);
			this.moves.add(suggestion);
		}
	}
}