import com.altirnao.aodocs.custom.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public void update(ReadableDocument document) throws Exception {

DocumentService documentService = getDocumentService();
ReadableDocument grant = document;

//-----------------------------------
//List of person fields in grant
//-----------------------------------
List roles = new ArrayList<String>();
List emails = new ArrayList<String>();

String fieldName = "Grants Office Staff";
if(document.getField(fieldName) != null && ((String)grant.getField(fieldName )).length() > 0){
  emails.add((String)document.getField(fieldName));
  roles.add(fieldName);
}

fieldName = "PCC Director / Lead";
if(document.getField(fieldName) != null && ((String)grant.getField(fieldName )).length() > 0){
  emails.add((String)document.getField(fieldName));
  roles.add(fieldName);
}

fieldName = "CGA Accountant";
if(document.getField(fieldName) != null && ((String)grant.getField(fieldName )).length() > 0){
  emails.add((String)document.getField(fieldName));
  roles.add(fieldName);
}


//-------------------------------------------------------
//Iterate through roles, and see if need to add a new one
//-------------------------------------------------------
String emailToSearch;
String roleToSearch;
ReadableDocument priorRole = null;
for(int j = 0; j<emails.size(); j++){
   emailToSearch = (String)emails.get(j);
   roleToSearch = (String)roles.get(j);

  Boolean found = false;
  //get list of existing roles entries
  List roleHistoryIds = grant.getToRelatedDocumentIds("Grant to Person Roles");

  //see if current entries exist
  for(int i = 0; i<roleHistoryIds.size(); i++){
    ReadableDocument roleEntry = documentService.loadReadableDocument((String)roleHistoryIds.get(i));
    if(roleEntry.getField("Role").equals(roleToSearch) && roleEntry.getField("End Date") == null){
        priorRole = roleEntry;
    }
    if(roleEntry.getField("Email").equals(emailToSearch) && roleEntry.getField("Role").equals(roleToSearch)){
       found = true;
       break;
    }
  }

  //create entry if not found
  if(!found){
    Document newRole = documentService.createDocument("Role History", roleToSearch + " " + emailToSearch);
    newRole.addFromRelatedDocument("Grant to Person Roles", grant);
    newRole.setField("Email",emailToSearch );
    newRole.setField("Role", roleToSearch);
    newRole.setField("Begin Date", new Date());
    newRole.setField("Grant Id", grant.getField("Grant Id"));

    documentService.store(newRole);
     
    if(priorRole != null){
      Document priorRoleDoc = documentService.lockDocument(priorRole);
      priorRoleDoc.setField("End Date", new Date());
      documentService.store(priorRoleDoc);
    }
  }

}

}