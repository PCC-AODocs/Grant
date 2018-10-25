import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;
import com.google.common.collect.Iterables;
import com.google.common.base.Optional;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.String;
import com.google.common.collect.Lists;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.Cell;
import java.lang.Number;

public void executeAsync(ReadableDocument doc) throws Exception {

	DocumentService documentService = getDocumentService();
	com.altirnao.aodocs.custom.googleintegration.DriveService driveService = getDriveService();
	com.altirnao.aodocs.custom.googleintegration.SpreadsheetService spreadsheet = getSpreadsheetService();

	String bulkDocId = doc.getId();
	String parentId = (String)doc.getField("Grant Id");
	ReadableDocument grantDoc = documentService.loadReadableDocument(parentId);
					
	Document newDoc;
	String spreadsheetId = (String)doc.getField("Spreadsheet Id");
	com.google.gdata.data.spreadsheet.SpreadsheetEntry entries = spreadsheet.openSpreadsheet(spreadsheetId);

	int minRow = ((Long)doc.getField("Min Row")).intValue();
	int maxRow = ((Long)doc.getField("Max Row")).intValue();
	int minCol = 1;
	int maxCol = 7;

	debug("Loading for " + minRow + " to " + maxRow + " for spreadsheet " + spreadsheetId + " grant " + parentId);

	int row;
	int fileSheetIndex = 1; //0 based
	int logSheetIndex = 2;
	int fileIdColIndex = 1;
	int fileNameColIndex = 2;
	int subContractorColIndex = 3;
	int firstDocTypeColIndex = 4;
	int lastDocTypeColIndex = 8;


	for(row = minRow; row <= maxRow; row++){
	  doc = documentService.loadReadableDocument(bulkDocId);
	  if(((Long)doc.getField("Repeat")).intValue() == 0){
		debug("Bulk Load Files Repeat = 0");
		break;
	   }

	  //FILE ID
	  CellEntry ce = (CellEntry)spreadsheet.getCells(entries, fileSheetIndex, row, row, fileIdColIndex, fileIdColIndex).get(0);
	  String fileId = ce.getCell().getValue();

	  //FILE NAME
	  ce = (CellEntry)spreadsheet.getCells(entries, fileSheetIndex, row, row, fileNameColIndex, fileNameColIndex).get(0);
	  String fileName = ce.getCell().getValue();
	  //debug("fileName=" + fileName);

	  spreadsheet.insertCell(entries, logSheetIndex, row, 1, fileName);
	  spreadsheet.insertCell(entries, logSheetIndex, row, 2, "loading...");

	  //SUBCONTRACTOR
	  ce = (CellEntry)spreadsheet.getCells(entries, fileSheetIndex, row, row, subContractorColIndex, subContractorColIndex).get(0);
	  String subContractor = ce.getCell().getValue();
		
	  //DOCUMENT TYPE - FIRST COL
	  List<String> categories = new ArrayList<String>();
	  ce = (CellEntry)spreadsheet.getCells(entries, fileSheetIndex, row, row, firstDocTypeColIndex, firstDocTypeColIndex).get(0);
	  categories.add(ce.getCell().getValue());

	  for(int x=firstDocTypeColIndex+1; x<=lastDocTypeColIndex; x++){
		ce = (CellEntry)spreadsheet.getCells(entries, fileSheetIndex, row, row, x, x).get(0);
		String val = ce.getCell().getValue();
		//debug("category=" + ce.getCell().getValue());
		if(val != null && !val.isEmpty() && !val.equals(fileName)){
			categories.add(ce.getCell().getValue());
		}
	  }

	  try{

		com.google.api.services.drive.model.File f = driveService.fetchMetadata(fileId);
		Attachment at = documentService.createAttachment(f);
		newDoc = documentService.createDocument("File", at.getTitle());
		newDoc = documentService.lockDocument(newDoc);
		newDoc.addAttachment(at);
		newDoc.addFromRelatedDocumentId("Grant to File", parentId);
		newDoc.setHierarchicalCategory("File Type", categories);
		if(subContractor != null && !subContractor.isEmpty()){
		   newDoc.setField("Subcontractor", subContractor);
		}
		newDoc.setField("Agency", grantDoc.getField("Agency"));

		documentService.store(newDoc);
	  
		spreadsheet.insertCell(entries, logSheetIndex, row, 2, "loaded");
	 

	  }catch(Exception e){
		spreadsheet.insertCell(entries, logSheetIndex, row, 2, e.getMessage());

	  }

	  java.util.concurrent.TimeUnit.SECONDS.sleep(2);
	}

	debug("Create Documents from Bulk Load COMPLETED");
}