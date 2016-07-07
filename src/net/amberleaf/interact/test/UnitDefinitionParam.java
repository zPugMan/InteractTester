package net.amberleaf.interact.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.*;
import com.unicacorp.interact.api.NameValuePair;
import com.unicacorp.interact.api.NameValuePairImpl;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="param")
public class UnitDefinitionParam {
	
	private static final Logger log = LogManager.getLogger(UnitDefinitionParam.class);
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String type;
	
	@XmlAttribute
	private String value;
	
	public void setName(String value)
	{
		this.name=value;
	}
	public String getName()
	{
		return name;
	}
	
	public void setType(String value)
	{
		this.type=value;
	}
	public String getType()
	{
		return type;
	}
	
	public void setValue(String value)
	{
		this.value=value;
	}
	public String getValue()
	{
		return this.value;
	}
	
	public NameValuePair getNameValue()
	{
		NameValuePair nvPair = new NameValuePairImpl(); 
		nvPair.setName(getName());
		nvPair.setValueDataType(getType());
		if(getType().equalsIgnoreCase("string"))
		{
			if(getValue().equals(""))
				nvPair.setValueAsString(null);
			else
				nvPair.setValueAsString(getValue());
		}
		else if(getType().equalsIgnoreCase("numeric"))
		{
			try
			{
				Double dbVal = Double.valueOf(getValue());
				nvPair.setValueAsNumeric(dbVal);
			} catch(Exception e){
				log.error("Problem in converting value: " + getValue() + " to double!");
			}
		} else if(getType().equalsIgnoreCase("date"))
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date dateVal = sdf.parse(getValue());
				nvPair.setValueAsDate(dateVal);
			} catch(Exception e){
				log.error("Problem in date conversion: " + getValue() +" using format: 'MM/dd/yyyy'");
			}
		}	
		
		return nvPair;
	}
}
