package post;


public class testClass {
	private  String username = new String();
	private  String  tweetdate = new String();
	private  String status = new String();
	private  String tweet_topic = new String();
	public testClass (String username, String tweetdate, String status) {
		this.username = username;
		this.tweetdate = tweetdate;
		this.status = status;
	}
	public testClass (String username, String tweetdate, String status, String tweet_topic) {
		this.username = username;
		this.tweetdate = tweetdate;
		this.status = status;
		this.tweet_topic = tweet_topic;
	}
	public String getUser () {
		return this.username;
	}
	public String getDate() {
		return this.tweetdate;
	}
	public String getPost() {
		return this.status.replace("@", "#");
	}
	public String getTopic() {
		return this.tweet_topic;
	}
	public void setStatus (String newStat) {
		this.status = newStat;
	}
}
