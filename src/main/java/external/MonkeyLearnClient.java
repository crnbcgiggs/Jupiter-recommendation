package external;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.monkeylearn.ExtraParam;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;


public class MonkeyLearnClient {
	private static final String API_KEY = "835e339c69829ca83faae7c5f56835d8febab92b";// make sure change it to your api key.
	
	
	// use a main function to do some test;

	public static void main(String[] args) {
			
			String[] textList = {
					"Jobs and Wozniak co-founded Apple in 1976 to sell Wozniak's Apple I personal computer. "
					+ "Together the duo gained fame and wealth a year later with the Apple II, one of the "
					+ "first highly successful mass-produced microcomputers. Jobs saw the commercial potential "
					+ "of the Xerox Alto in 1979, which was mouse-driven and had a graphical user interface (GUI). "
					+ "This led to the development of the unsuccessful Apple Lisa in 1983, followed by the breakthrough "
					+ "Macintosh in 1984, the first mass-produced computer with a GUI. The Macintosh introduced the desktop "
					+ "publishing industry in 1985 with the addition of the Apple LaserWriter, the first laser printer to "
					+ "feature vector graphics. Jobs was forced out of Apple in 1985 after a long power struggle with the company's "
					+ "board and its then-CEO John Sculley. That same year, Jobs took a few of Apple's members with him to "
					+ "found NeXT, a computer platform development company that specialized in computers for higher-education "
					+ "and business markets. In addition, he helped to develop the visual effects industry when he funded the"
					+ " computer graphics division of George Lucas's Lucasfilm in 1986. The new company was Pixar, which "
					+ "produced the first 3D computer animated film Toy Story (1995).", };
			
			List<List<String>> words = extractKeywords(textList);
			for (List<String> ws : words) {
				for (String w : ws) {
					System.out.println(w);
				}
				System.out.println();
			}
	 }

	
	
	

	
	public static List<List<String>> extractKeywords(String[] text) {
		if (text == null || text.length == 0) {
			return new ArrayList<>();
		}

		// Use the API key from your account
		MonkeyLearn ml = new MonkeyLearn(API_KEY);

		// Use the keyword extractor
		ExtraParam[] extraParams = { new ExtraParam("max_keywords", "3") };//return top 3 keywords
		MonkeyLearnResponse response;
		try {
			response = ml.extractors.extract("ex_YCya9nrn", text, extraParams);//change to your model id
			JSONArray resultArray = response.arrayResult;
			return getKeywords(resultArray);
		} catch (MonkeyLearnException e) {// it’s likely to have an exception
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	//把“front end develop”， “black lives metters” 变成 一个list of list，第二个list放每句话的每个单词；
	private static List<List<String>> getKeywords(JSONArray mlResultArray) {
		List<List<String>> topKeywords = new ArrayList<>();
		// Iterate the result array and convert it to our format.
		for (int i = 0; i < mlResultArray.size(); ++i) {
			List<String> keywords = new ArrayList<>();
			JSONArray keywordsArray = (JSONArray) mlResultArray.get(i);
			for (int j = 0; j < keywordsArray.size(); ++j) {
				JSONObject keywordObject = (JSONObject) keywordsArray.get(j);
				// We just need the keyword, excluding other fields.
				String keyword = (String) keywordObject.get("keyword");
				keywords.add(keyword);

			}
			topKeywords.add(keywords);
		}
		return topKeywords;
	}


}
