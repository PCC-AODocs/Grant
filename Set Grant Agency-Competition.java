import java.util.List;
import java.util.Arrays;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public void update(ReadableDocument document) throws Exception {

  //------------------------------------------------------------------------
  //DESCRIPTION
  //Creates the category entries if needed, based on entries in the Grant
  // for Agency/Competition and Agency
  // These categories are built as Grants are added, and then used to filter
  // items in the views
  //
  //USAGE: Grant
  //-------------------------------------------------------------------------

  //--------------------------------------------------------------------------------------------------------
  //Pull in the three fields from grant needed for the category: agency, competition, Title
  // Agency is pulled from the Agency/Division category, but just pulling the first in the hierarchy, Agency
  //--------------------------------------------------------------------------------------------------------
  String agency = (String)document.getHierarchicalCategory("Agency").get(0);
  String competition = (String)document.getHierarchicalCategory("Competition").get(0);
  String title = (String)document.getTitle();

  //set defaults of whether to add new entry or not to "true"
  Boolean createAgency = true;
  Boolean createCompetition = true;
  Boolean createTitle = true;

  //retrieve the categories we will want to check to see if need new entries
  List<String> items = getCategoryService().listValues("Agency/Competition");
  List<String> agencyOnly = getCategoryService().listValues("Agency");

  //-----------------------------------------------------------
  //Loop through Agency/Competition and see if new entry needed
  //-----------------------------------------------------------

  if(items.contains(agency)){
    createAgency = false;
    List<string> competitions = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency));
    if(competitions.contains(competition)){
       createCompetition = false;
       List<string> titles = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency, competition));
       createTitle = !titles.contains(title);
    }
  }

  //-----------------------------------------------------
  // Create any additional Agency/Competition entries needed
  //-----------------------------------------------------
  if(createAgency){
     getCategoryService().createCategoryValue("Agency/Competition", agency, "");
  }
  if(createCompetition){
     getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency), competition, "");
  }
  if(createTitle){
     getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency, competition), title, "");
  }

  // -------------------------------------------------------
  // Check if entry needed for Agency
  //--------------------------------------------------------
  if(!agencyOnly.contains(agency)){
   getCategoryService().createCategoryValue("Agency", agency, "");
  }


  //--------------------------------------------------------------
  //Assign the Agency/Compeition entry to Grant
  //-------------------------------------------------------------
  Document grant = getDocumentService().lockDocument(document);
  grant.setHierarchicalCategory("Agency/Competition", Arrays.asList(agency, competition, title));


}
