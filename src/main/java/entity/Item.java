package entity;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;


public class Item {
	private String itemId;
	private String name;
	private String address;
	private Set<String> keywords;
	private String imageUrl;
	private String url;
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("item_id", itemId);
		obj.put("name", name);
		obj.put("address", address);
		obj.put("keywords", new JSONArray(keywords));
		obj.put("image_url", imageUrl);
		obj.put("url", url);
		return obj;
	}

	
	//builder form
	//防止使用构造函数的时候参数错填
	public static class ItemBuilder {//这里是static是因为这样不需要new 一个Item就可以new 一个ItemBuilder
		private String itemId;
		private String name;
		private String address;
		private Set<String> keywords;
		private String imageUrl;
		private String url;
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public void setKeywords(Set<String> keywords) {
			this.keywords = keywords;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Item build() {
			return new Item(this);
		}
		
	}
//不设setter防止数据被更改

	public String getItemId() {
		return itemId;
	}


	public String getName() {
		return name;
	}


	public String getAddress() {
		return address;
	}


	public Set<String> getKeywords() {
		return keywords;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public String getUrl() {
		return url;
	}
	
	private Item(ItemBuilder builder) {
		this.itemId = builder.itemId;
		this.name = builder.name;
		this.address = builder.address;
		this.imageUrl = builder.imageUrl;
		this.url = builder.url;
		this.keywords = builder.keywords;
	}
}
//通过itemBuilder来创建item， builder pattern
//把构造函数设计成private，可以避免输入的参数
//ItemBUilder builder = new ItemBuilder();
//builder.setItemId("abcd");
//builder.setName("vicent");
//Item item = builder.build();
