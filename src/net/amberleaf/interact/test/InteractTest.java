/**
 * 
 */
package net.amberleaf.interact.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.*;
import com.unicacorp.interact.api.*;
import com.unicacorp.interact.api.jsoverhttp.InteractAPI;

/**
 * Testing mechanism
 * @author zbeth
 *
 */
public class InteractTest {

	private static final Logger log = LogManager.getLogger(InteractTest.class);
	
	public InteractTest()
	{
		try{
			configFile = new Properties();
			configFile.load(this.getClass().getClassLoader().getResourceAsStream("InteractUT.properties"));
			interactUrl = String.valueOf(configFile.getProperty("interact.url"));
			log.debug("Executing test against: " + interactUrl);
		} catch(IOException io){
			log.error("IO error occurred in loading properties file.");
			io.printStackTrace();
		}
	}
	
	public InteractTest(String environ)
	{
		try{
			configFile = new Properties();
			configFile.load(this.getClass().getClassLoader().getResourceAsStream("InteractUT.properties"));
			if(environ.equalsIgnoreCase("TEST"))
				interactUrl = String.valueOf(configFile.getProperty("interact.testurl"));
			else
				interactUrl = String.valueOf(configFile.getProperty("interact.url"));
			log.debug("Executing test against: " + interactUrl);
		} catch(IOException io){
			log.error("IO error occurred in loading properties file.");
			io.printStackTrace();
		}
	}
	
	private Properties configFile;
	private String interactUrl;
	
	public List<String> getOfferNames(InteractUnit iu)
	{
		List<String> result = new ArrayList<String>();
		log.debug("Retrieving offer for unit test: " + iu.getTestName() + " for IP: " + iu.getInteractionPoint());
		
		try{
			InteractAPI client = InteractAPI.getInstance(interactUrl);
			Response startResult = client.startSession(iu.getSessionId(), false, false, iu.getInteractionChannel(), iu.getAudience(), iu.getAudienceLevel(), iu.getSessionParams());
			
			if(startResult.getStatusCode()==Response.STATUS_SUCCESS)
			{
				Response offerResult = client.getOffers(iu.getSessionId(), iu.getInteractionPoint(), 10);
				if(offerResult.getOfferList()!=null && offerResult.getOfferList().getRecommendedOffers()!=null)
				{
					String oName;
					for(Offer o : offerResult.getOfferList().getRecommendedOffers() )
					{
						log.trace("Offer: " + o.getOfferName());
						NameValuePair[] extAttribs = o.getAdditionalAttributes();
						Date startDate = null;
						Date endDate = null;
						for(int i=0;i<extAttribs.length;i++)
						{
							if(extAttribs[i].getName().equalsIgnoreCase("ExpirationDate"))
								endDate = extAttribs[i].getValueAsDate();
							if(extAttribs[i].getName().equalsIgnoreCase("prod_effective"))
								startDate = extAttribs[i].getValueAsDate();
						}
						SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
						oName = String.format("%s\t%s\t%s\t%s", iu.getInteractionPoint(), o.getOfferName(), startDate!=null?df.format(startDate):"", endDate!=null?df.format(endDate):"");
						result.add(oName);
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean executeUnitTest(InteractUnit iu)
	{
		log.info("Executing unit test: " + iu.getTestName() + " for IP: " + iu.getInteractionPoint());
		if(iu.getTestDescription()!=null)
			log.info("\tValidating: " + iu.getTestDescription());
		boolean foundOffer=false;
		try {
			InteractAPI client = InteractAPI.getInstance(interactUrl);
			Response startResult = client.startSession(iu.getSessionId(), false, false, iu.getInteractionChannel(), iu.getAudience(), iu.getAudienceLevel(), iu.getSessionParams());
			
			if(startResult.getStatusCode()==Response.STATUS_SUCCESS)
			{
				log.info("Retrieving first "+iu.getOfferCount()+" highest ranked offers.");
				Response offerResult = client.getOffers(iu.getSessionId(), iu.getInteractionPoint(), iu.getOfferCount());
				if(offerResult.getStatusCode()==Response.STATUS_SUCCESS)
				{
					log.debug("Looking for offer identified by: " + iu.getOfferId());
					
					if(offerResult.getOfferList()!=null && offerResult.getOfferList().getRecommendedOffers()!=null) 
					{
						for(Offer o  : offerResult.getOfferList().getRecommendedOffers() )
						{
							
							if(o.getOfferName().contains(iu.getOfferId()))
							{
								log.info("Offer: " + o.getOfferName());

								foundOffer= true;
								
								
//								NameValuePair[] extAttributes = o.getAdditionalAttributes();
//								for(int i=0;i<extAttributes.length;i++)
//								{
////									if(extAttributes[i].getName().equalsIgnoreCase("contentid"))
////										log.info(extAttributes[i].getName() + " : " + extAttributes[i].getValueAsString());
////									if(extAttributes[i].getName().equalsIgnoreCase("modifier2"))
////										log.info(extAttributes[i].getName() + " : " + extAttributes[i].getValueAsString());
//									if(extAttributes[i].getName().equalsIgnoreCase("ExpirationDate"))
//										log.info("Expires: " + extAttributes[i].getValueAsDate().toString()); 
//								}
							}
							
						}
					}
					
					if(iu.assertExist!=null && iu.assertExist!=foundOffer)
						log.error("Assertion ["+iu.assertExist+"] in disagreement with offer eligibility ["+foundOffer+"]");
					else
						log.info("Assertion ["+iu.assertExist+"] confirmed.");
					
				} else {
					log.error("Retrieval of offers status: " + offerResult.getStatusCode());
					for(AdvisoryMessage a : offerResult.getAdvisoryMessages())
						log.error(a.getMessage() + a.getDetailMessage());
				}
				
				
			} else {
				log.error("StartSession status: " + startResult.getStatusCode());
				for(AdvisoryMessage a : startResult.getAdvisoryMessages())
				{
					log.error(a.getMessage() + a.getDetailMessage());
				}
			}
			
			client.endSession(iu.getSessionId());
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 

		return foundOffer;
	}

	public boolean executeHomepageRank(InteractUnit iu) {
		log.info("Executing unit test: " + iu.getTestName() + " for IP: " + iu.getInteractionPoint());
		if(iu.getTestDescription()!=null)
			log.info("\tValidating: " + iu.getTestDescription());
		boolean foundOffer=false;
		try {
			InteractAPI client = InteractAPI.getInstance(interactUrl);
			Response startResult = client.startSession(iu.getSessionId(), false, false, iu.getInteractionChannel(), iu.getAudience(), iu.getAudienceLevel(), iu.getSessionParams());
			
			if(startResult.getStatusCode()==startResult.STATUS_SUCCESS)
			{
				Response offerResult = client.getOffers(iu.getSessionId(), iu.getInteractionPoint(), iu.getOfferCount());
				if(offerResult.getStatusCode()==offerResult.STATUS_SUCCESS)
				{
					log.debug("Need offer identified by: " + iu.getOfferId());
					
					if(offerResult.getOfferList()!=null && offerResult.getOfferList().getRecommendedOffers()!=null) 
					{
						int jRank=1;
						for(Offer o  : offerResult.getOfferList().getRecommendedOffers() )
						{
							log.info("Ranking: " + jRank + "; " +o.getOfferName());
							jRank++;
						}
					}
				}
			}
			client.endSession(iu.getSessionId());
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		return true;
	}
}
