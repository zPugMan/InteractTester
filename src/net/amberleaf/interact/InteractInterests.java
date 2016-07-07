/**
 * 
 */
package net.amberleaf.interact;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.log4j.*;

import net.amberleaf.interact.test.*;

/**
 * @author zbeth
 *
 */
public class InteractInterests {

	private static final Logger log = LogManager.getLogger(InteractInterests.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String relPath = args[1];
		
		File procFolder = new File(relPath);
		File[] procFiles = procFolder.listFiles();
		UnitTestLoader ud = null;
		InteractUnit uTest = null;
		InteractTest tester = new InteractTest(args[0]);
		log.debug("Searching for files to process");
		for( File process : procFiles)
		{
			log.debug("Reviewing file: " + process.getName());
			if(process.exists() && process.getName().contains(".xml"))
			{
				//TODO make offer request
				try{
					log.debug("Load request executed for: " + process.getName());
					ud=new UnitTestLoader(process);
					if(ud.getTestCount()==0){
						log.error("Test definition invalid, check XML: " + process.getName());
						ud=null;
					}
					else if(ud.getTestCount()>1)
						log.warn("Only the first unit test will be processed");
					else
						log.debug("Processing first unit test; file: " + process.getName());
					
				} catch(Exception e) {
					log.warn(e);
				}
				
				if(ud!=null)
				{
					uTest = new InteractUnit(ud.getUnitDefinition(), 0); 
					List<String> out = tester.getOfferNames(uTest);
					
					for(String oline : out)
					{
						log.debug("Offer line item: " + oline);
						System.out.println(oline);
					}
				}
			}
		}
	}

}
