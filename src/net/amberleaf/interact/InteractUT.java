/**
 * 
 */
package net.amberleaf.interact;

import org.apache.log4j.*;
import net.amberleaf.interact.test.InteractTest;
import net.amberleaf.interact.test.InteractUnit;
import net.amberleaf.interact.test.UnitTestLoader;

/**
 * @author zbethem@amberleaf.net
 *
 */
public class InteractUT {

	private static final Logger log = LogManager.getLogger(InteractUT.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		UnitTestLoader ud = null;
		
		if(validArgs(args))
		{
			log.info("Starting request for unit test: "+ args[1]);
			try {
				ud = new UnitTestLoader(args[1]);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			
			if(ud!=null)
			{
				InteractUnit unitTest;
				InteractTest tester = new InteractTest(args[0]);
				boolean result;
				for(int i=0; i<ud.getTestCount(); i++)
				{
					log.info("");
					log.info("Executing test: " + i);
					unitTest = new InteractUnit(ud.getUnitDefinition(), i);
					result = tester.executeUnitTest(unitTest);
					
					if(result)
						log.info("Offer retrieved and configured.");
					else
						log.warn("Offer not available");
				}
			}
		}
	}
	
	public static boolean validArgs(String[] args)
	{
		boolean result = false;
		
		if(args!=null && args.length==2)
			result = true;
		else if(args!=null && args.length > 2)
		{
			log.warn("Additional unexpected argument provided. This value will be ignored.");
			log.info(getArgSyntax());
			result = true;
		}
		else
		{
			log.info(getArgSyntax());
			result=false;
		}
		
		return result;
	}
	
	public static String getArgSyntax()
	{
		return "Syntax:   InteractUT.java <env> <pif#>";
	}

}
