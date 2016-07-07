/**
 * 
 */
package net.amberleaf.interact.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.*;
import com.unicacorp.interact.api.*;
/**
 * A single Interact Test definition, which will be processed through the API
 * @author zbeth
 *
 */
public class InteractUnit {

	private static final Logger log = LogManager.getLogger(InteractUnit.class);
	
	public InteractUnit(){}
	
	public InteractUnit(UnitDefinition ud, int unitTestIndex){
		this.unitTest=unitTestIndex;
		this.testDefinition = ud;
		setInteractParameters();
	}
	
	private UnitDefinition testDefinition;
	private NameValuePair[] audience;
	private String interactionChannel;
	private String sessionId;
	private String audienceLevel;
	private String interactionPoint;
	private int offerCount;
	private NameValuePair[] sessionParams;
	private int unitTest;
	private String offerId;
	private String testDescription;
	public Boolean assertExist;
	
	private void setInteractParameters()
	{
		log.debug("Setting interaction parameters for unit test #: "+unitTest);
		this.interactionChannel=testDefinition.getInteractionChannel();
		this.sessionId = testDefinition.getSessionId();
		log.debug("sessionId defined: " + this.sessionId);
		this.audience = new NameValuePair[] { testDefinition.getAudience().getAudiences().get(0).getNameValue() };
		this.audienceLevel = testDefinition.getAudienceName();
		this.interactionPoint = testDefinition.getTests().getUnitTests().get(unitTest).getInteractionPoint();
		this.testDescription = testDefinition.getTests().getUnitTests().get(unitTest).getDescription();
		this.offerId = testDefinition.getOfferId();
		this.assertExist = testDefinition.getTests().getUnitTests().get(unitTest).getAssertOffer();
		
		log.debug("Retrieving all configured parameters for session");
		int paramCount=testDefinition.getTests().getUnitTests().get(unitTest).getParms().getParamList().size();
		sessionParams = new NameValuePair[paramCount];
		for(int i=0;i<paramCount;i++ )
		{
			sessionParams[i] = testDefinition.getTests().getUnitTests().get(unitTest).getParms().getParamList().get(i).getNameValue();
		}
	}
	
	public NameValuePair[] getAudience()
	{
		return audience;
	}
	public String getInteractionChannel()
	{
		return interactionChannel;
	}
	public String getSessionId()
	{
		return sessionId;
	}
	public String getAudienceLevel()
	{
		return audienceLevel;
	}
	public String getInteractionPoint()
	{
		return interactionPoint;
	}
	public int getOfferCount()
	{
		if(this.offerCount==0)
			return 20;
		else
			return offerCount;
	}
	public NameValuePair[] getSessionParams(){
		return sessionParams;
	}
	
	public String getOfferId()
	{
		return offerId;
	}
	
	public String getTestName()
	{
		return testDefinition.getOfferId() + "[" + unitTest + "]";
	}
	
	public String getTestDescription()
	{
		return testDescription;
	}
	
	
}
