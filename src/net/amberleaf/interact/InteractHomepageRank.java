/**
 * 
 */
package net.amberleaf.interact;

import org.apache.log4j.*;
import net.amberleaf.interact.test.InteractTest;
import net.amberleaf.interact.test.InteractUnit;
import net.amberleaf.interact.test.UnitTestLoader;

/**
 * @author zbeth
 *
 */
public class InteractHomepageRank {

	private static final Logger log = LogManager.getLogger(InteractUT.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.info("Determining rankings for Homepage");
		UnitTestLoader ud = null;
		
		try {
			ud = new UnitTestLoader("4133");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		if(ud!=null)
		{
			InteractUnit unitTest;
			InteractTest tester = new InteractTest();
			boolean homepaged=false;
			for(int i=0; i<ud.getTestCount() && !homepaged; i++)
			{
				if(ud.getUnitDefinition().getTests().getUnitTests().get(i).getInteractionPoint().equalsIgnoreCase("HomepageGrid"))
				{
					homepaged=true;
					unitTest = new InteractUnit(ud.getUnitDefinition(), i);
					tester.executeHomepageRank(unitTest);
					
					
				}
			}
		}
	}
		
}
