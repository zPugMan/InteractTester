/**
 * 
 */
package net.amberleaf.interact.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 * Internal Java class representing the UnitTest definition. This is the base class
 * that reflects the entire unit test defined via XML
 * @author zbethem@amberleaf.net
 *
 */
@XmlRootElement
public class UnitDefinition {
	
	private String offerId;
	private String interactionChannel;
	private String sessionId;
	private String description;
	private UnitDefinition.Tests tests;
	private UnitDefinition.Audience audience;
	private String audienceName;
	
	public String getOfferId()
	{ 
		return offerId; 
	}
	public void setOfferId(String value)
	{
		this.offerId = value;
	}
	
	public String getInteractionChannel()
	{
		return interactionChannel;
	}
	public void setInteractionChannel(String value)
	{
		this.interactionChannel=value;
	}
	
	public String getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(String value){
		this.sessionId=value;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String value){
		this.description=value;
	}
	
	public String getAudienceName()
	{
		return audienceName;
	}
	public void setAudienceName(String value)
	{
		this.audienceName = value;
	}
	
	public UnitDefinition.Tests getTests()
	{
		return tests;
	}
	@XmlElement(name="tests")
	public void setTests(UnitDefinition.Tests value){
		this.tests = value;
	}
	
	public static class Tests
	{
		@XmlElement(name="unitTest")
		private List<UnitDefinitionTest> unitTests;
		
		public List<UnitDefinitionTest> getUnitTests() {
			if(unitTests == null)
				unitTests = new ArrayList<UnitDefinitionTest>();
			
			return this.unitTests;
		}
	}
	
	public UnitDefinition.Audience getAudience()
	{
		return this.audience;
	}
	@XmlElement(name="audience")
	public void setAudience(UnitDefinition.Audience value){
		this.audience =value;
	}
	
	public static class Audience
	{
		@XmlElement(name="param")
		private List<UnitDefinitionParam> audiences;
		public List<UnitDefinitionParam> getAudiences() {
			if(audiences==null)
				audiences = new ArrayList<UnitDefinitionParam>();
			
			return audiences;
		}
	}
}
