package org.ie.kafka;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApilotConsumer {

    String timeStamp;
	String	av_id;
	Integer	speed;  //reaction
	String	takeRest; //reaction
	String	weatherStatus; //road condition
	Integer	tractionWheelsLevel; //reaction
	String location;
	String municipalitySubdivision;

	final String camunda = "http://ec2-44-204-149-72.compute-1.amazonaws.com:8080/avaas";

	final Logger logger = Logger.getLogger(ApilotConsumer.class);

	@Incoming("apilot-functionality")
	public void receiveDecision(Record<String, String> record) {

		logger.infof("Consumed an APilot: Id = " + record.key() + " Value = " + record.value() + "\n"); 
        
        processAvEventMessage(record.value());

        Mediator mediator = new Mediator(timeStamp, av_id, speed,takeRest,weatherStatus, tractionWheelsLevel, location);

		AVResultProducer.createMessage(mediationToIQEQAQ());
		
		System.out.println ("----------------------------------------\n");
	}

	private void processAvEventMessage(String msg) {

		String[] msgArray = msg.replace("\"Mediator\" [", "").replace("]", "").split(";");

		timeStamp = msgArray[0].split("=")[1];
		av_id = msgArray[1].split("=")[1];
		speed = Integer.parseInt(msgArray[2].split("=")[1]);  //reaction
		takeRest = msgArray[3].split("=")[1]; //reaction
		weatherStatus = msgArray[4].split("=")[1]; //road condition
		tractionWheelsLevel = Integer.parseInt(msgArray[5].split("=")[1]); //reaction
		location = msgArray[6].split("=")[1]; //Map API

	}

	//sends the info to camunda and waits for its recomendations.
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String mediationToIQEQAQ() {
				
		String message = "No IQ EQ AQ recommendation";

		getLocationFromTomTom();
		
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String responseback = "empty response!";
		try
	{
		HttpPost postRequest = new HttpPost(camunda);
		postRequest.addHeader("content-type","application/json");
		postRequest.addHeader("accept","application/json");
		String query = "{'weatherStatus': weatherStatus,'takeRest': takeRest,'tractionWheelsLevel': tractionWheelsLevel,'speed': speed,'municipalitySubdivision': municipalitySubdivision}";
		query= query.replace("'", "\"");                      
		StringEntity Entity = new StringEntity(query);
		postRequest.setEntity(Entity);

		CloseableHttpResponse  response = httpclient.execute(postRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		responseback = "Finished with HTTP error code : " + statusCode + "\n" + response.toString();
		HttpEntity responseEntity = response.getEntity();

		if (responseEntity!=null){
				responseback += "response body = "  + EntityUtils.toString(responseEntity);
				message = responseback.split("=")[1];
			}
			httpclient.close();
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace(); 
			} 
		catch (IOException e) { 
			e.printStackTrace();    
		
		}
		return message;
	}

	private void getLocationFromTomTom(){

		 String url = "https://api.tomtom.com/search/2/reverseGeocode/"+location+".json?key=jJ1hVh4kwApk1yjmXG4leP21AJukItrH&radius=100";

        try {
            URL apiUrl = new URL(url);

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                String jsonResponse = response.toString();

                // Extract values from JSON
                try {
                    JSONObject responseObj = new JSONObject(jsonResponse);

                    JSONArray addresses = responseObj.getJSONArray("addresses");

                    if (addresses.length() > 0) {
                        JSONObject address = addresses.getJSONObject(0);

                        municipalitySubdivision = address.getJSONObject("address").getString("municipalitySubdivision");

                    } else {
                        municipalitySubdivision = "Unkown";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } 
			else {
                System.out.println("HTTP GET request failed with response code: " + responseCode);
            }

            // Close connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
 
