package net.amberleaf.interact.test;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Internal Java class for a specific UnitTest
 * @author zbethem@amberleaf.net
 *
 */
@XmlRootElement
public class UnitDefinitionTest {
	
	private String interactionPoint;
	private String description;
	private UnitDefinitionTest.Params params;
	private Boolean assertOffer;
	
	@XmlElement(name="interactionPoint")
	public void setInteractionPoint(String value){
		this.interactionPoint=value;
	}
	public String getInteractionPoint(){
		return interactionPoint;
	}
	
	@XmlElement(name="description")
	public void setDescription(String value){
		this.description=value;
	}
	public String getDescription(){
		return description;
	}
	
	@XmlElement(name="params")
	public void setParams(UnitDefinitionTest.Params value){
		this.params = value;
	}
	public UnitDefinitionTest.Params getParms()
	{
		return params;
	}
	
	@XmlElement(name="assertOffer")
	public void setAssertOffer(Boolean value){
		this.assertOffer=value;
	}
	public Boolean getAssertOffer(){
		return assertOffer;
	}
	
	public static class Params
	{
		@XmlElement(name="param")
		private List<UnitDefinitionParam> paramList;
		
		public List<UnitDefinitionParam> getParamList() {
			if(paramList == null)
				paramList = new ArrayList<UnitDefinitionParam>();
			
			return this.paramList;
		}
	}
}
