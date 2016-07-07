/**
 * 
 */
package net.amberleaf.interact.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.log4j.*;

/**
 * Unit Test definition(s) as read in from XML source
 * @author zbethem@amberleaf.net
 *
 */
public class UnitTestLoader {

	private static final Logger log = LogManager.getLogger(UnitTestLoader.class);
	
	private Properties configFile;
	private String xmlTestDirectory;
	private String offerTestId;
	private int testCount;
	private UnitDefinition utDef;
	
	/***
	 * Constructor
	 */
	public UnitTestLoader()
	{ }
	
	/***
	 * Constructor
	 * @param offerId offer identifier to process unit test(s)
	 */
	public UnitTestLoader(String offerId) throws Exception
	{
		log.debug("Constructing UnitDefinition..");
		offerTestId = offerId;
		loadProperties();
		
		loadXml();
	}
	
	/**
	 * Constructor
	 * @param xmlFile
	 * @throws Exception
	 */
	public UnitTestLoader(File xmlFile) throws Exception
	{
		log.debug("Constructing UnitDefinition..");
		if(xmlFile.exists())
		{
			loadProperties();
			loadXml(xmlFile);
		} else {
			log.warn("File requested not found: " + xmlFile.getAbsolutePath());
		}
	}
	
	public String getOfferTestId(){
		return offerTestId;
	}
	
	/**
	 * Retrieves number of unit tests defined in loaded file
	 * @return
	 */
	public int getTestCount(){
		return testCount;
	}
	public UnitDefinition getUnitDefinition(){
		return utDef;
	}
	
	/**
	 * Load .properties to retrieve unit test staging directory
	 * @throws Exception
	 */
	private void loadProperties() throws Exception
	{
		try{
			configFile = new Properties();
			configFile.load(this.getClass().getClassLoader().getResourceAsStream("InteractUT.properties"));
			xmlTestDirectory = String.valueOf(configFile.getProperty("xml.sourceDir"));
			log.debug("XML unit tests defined under: " + xmlTestDirectory);
			
		} catch(IOException io){
			log.error("Configuration .properties file cannot be found! " + io.getMessage());
			throw new Exception("Issue has occurred in contructing Unit Test definition.. please review logs.");
		}
	}
	
	/**
	 * Loads XML defining the UnitTest
	 */
	private void loadXml()
	{
		File xml = getXmlFileName();
		if(xml!=null)
		{
			loadXml(xml);
		}
	}
	
	/**
	 * Loads XML defining the UnitTest
	 * @param xml
	 */
	private void loadXml(File xml)
	{
		if(xml.exists())
		{
			try{
				JAXBContext jc = JAXBContext.newInstance(UnitDefinition.class);
				Unmarshaller u = jc.createUnmarshaller();
				
				utDef = (UnitDefinition) u.unmarshal(xml);
				
				testCount = utDef.getTests().getUnitTests().size();
				log.info("UnitTest definition loaded and marshalled for: " + utDef.getOfferId() + ". " + testCount + " tests defined.");
				
				
	        } catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			log.fatal("XML file not found and remains null. Check the unit tests directory!");
		}
	}
	
	/**
	 * Retrieves XML unit test file using filename string (offerTestId) with the assumption
	 * that the file follows the naming convention [offerTestId].xml
	 * @return
	 */
	private File getXmlFileName()
	{
		if(offerTestId!=null && xmlTestDirectory!=null)
		{
			log.debug("Searching unit test directory for desired test: " + offerTestId);
			
			File file = new File(xmlTestDirectory + offerTestId + ".xml");
			if(!file.exists())
				log.error("Unit Test definition not found: " + file.getAbsolutePath());
			else 
			{
				log.debug("Found Unit Test definition: " + file.getAbsolutePath());
				return file;
			}
				
		}
		
		return null;
	}
}
