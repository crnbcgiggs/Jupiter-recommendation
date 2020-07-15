package external;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder; 



public class GitHubClient {
	private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
	private static final String DEFAULT_KEYWORD = "manager";
	
	
	public List<Item> search(double lat, double lon, String keyword) {// input data are latitude, longitude, keyword
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");//处理异常字符，转化关键字符，里面可能有空格，空格后面都会被忽略，会影响结果
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String url = String.format(URL_TEMPLATE, keyword, lat, lon);// 这个是Java自带的method，String.format
		
		CloseableHttpClient httpclient = HttpClients.createDefault();

	    // Create a custom response handler
	    ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {

	        @Override
	        public List<Item> handleResponse(
	                final HttpResponse response) throws IOException {
	            if (response.getStatusLine().getStatusCode() != 200) {//这里如果是200 - 299 都是正确代码
	            	return new ArrayList<>();
	            }
	            HttpEntity entity = response.getEntity();
	            if (entity == null) {
	            	//Throw exception is also fine;
	            	
	            	return new ArrayList<>();
	            }
	            String responseBody = EntityUtils.toString(entity);//这个responseBody 就是json string.这里调用Java的method
	            JSONArray array = new JSONArray(responseBody);
	            return getItemList(array);

	        }
	    };
	    
		try {
			return httpclient.execute( new HttpGet(url), responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();

	}
	
	private List<Item> getItemList(JSONArray array) {
		List<Item> itemList = new ArrayList<>();
		
		List<String> descriptionList = new ArrayList<>();
		
		for (int i = 0; i < array.length(); i++) {
			// We need to extract keywords from description since GitHub API
			// doesn't return keywords.
			String description = getStringFieldOrEmpty(array.getJSONObject(i), "description");
			if (description.equals("") || description.equals("\n")) {
				descriptionList.add(getStringFieldOrEmpty(array.getJSONObject(i), "title"));
			} else {
				descriptionList.add(description);
			}	
		}

		// We need to get keywords from multiple text in one request since
		// MonkeyLearnAPI has limitations on request per minute.
		List<List<String>> keywords = MonkeyLearnClient.extractKeywords(descriptionList.toArray(new String[descriptionList.size()]));


		
		
		for (int i = 0; i < array.length(); i ++) {
			JSONObject object = array.getJSONObject(i);
			// object -> item
			ItemBuilder builder = new ItemBuilder();
			builder.setItemId(getStringFieldOrEmpty(object, "id"));
			builder.setName(getStringFieldOrEmpty(object, "title"));
			builder.setAddress(getStringFieldOrEmpty(object, "location"));
			builder.setUrl(getStringFieldOrEmpty(object, "url"));
			builder.setImageUrl(getStringFieldOrEmpty(object, "company_logo"));
			builder.setKeywords(new HashSet<String>(keywords.get(i)));
			
			
			Item item = builder.build();
			itemList.add(item);//把item加到item list里面

			// item -> itemList
		}
		return itemList;
	}
	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? "" : obj.getString(field);//如果key 是null， 进行去除null的操作。避免exception
	}


}
