package edu.usfca.cs112.project1.my_study_buddy;

import java.io.IOException; 
import java.net.URI; 
import java.net.URISyntaxException; 
import java.net.http.HttpClient; 
import java.net.http.HttpRequest; 
import java.net.http.HttpResponse;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Model {
	private static final Map <String, String> env = System.getenv();
	private static final String key = env.get("OPENAI_API_KEY");
	public String final_content;
	
	public String generate (String question) { 
		  JSONObject payload = new JSONObject(); 
		  payload.put("model", "gpt-4o"); 

		  JSONArray ja = new JSONArray();
		  JSONObject jo = new JSONObject();
		  
		  jo.put("role", "user");
		  jo.put("content", question);
		  
		  ja.put(jo);
		  payload.put("messages", ja);
		  
		  HttpClient client = HttpClient.newHttpClient(); 
		  HttpRequest request; 
		  
		  try { 
		    request = HttpRequest.newBuilder() 
		       .uri(new URI("https://api.openai.com/v1/chat/completions")) 
		       .header("Content-Type", "application/json")
		       .header("Authorization", "Bearer " + key)
		       .POST(HttpRequest.BodyPublishers.ofString(payload.toString())) 
		       .build(); 
		   HttpResponse<String> response = client.send(request, 
		HttpResponse.BodyHandlers.ofString());
		   
		  JSONObject jsonResponse = new JSONObject(response.body());
		  JSONArray jsonMessage = (JSONArray) jsonResponse.getJSONArray("choices");
		  JSONObject jsonMessage_2 = (JSONObject) jsonMessage.getJSONObject(0);
		  JSONObject jsonMessage_3 = (JSONObject) jsonMessage_2.getJSONObject("message");
		  
		  final_content = jsonMessage_3.getString("content");
		   
		  } catch (URISyntaxException e) { 
		   System.out.println("Oops, the url was bad."); 
		  } catch (IOException e) { 
		   System.out.println("Oops, the request was malformed."); 
		  } catch (InterruptedException e) { 
		   System.out.println("Oops, the connection was interrupted."); 
		  } 
		return final_content;
		} 

}
