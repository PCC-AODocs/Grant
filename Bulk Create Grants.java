import java.util.List;
import java.util.Arrays;
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
import java.text.SimpleDateFormat;

public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {

		DocumentService documentService = getDocumentService();
		com.altirnao.aodocs.custom.googleintegration.SpreadsheetService spreadsheet = getSpreadsheetService();
						
		Document newDoc;
		String spreadsheetId = (String)doc.getField("Spreadsheet Id");
		com.google.gdata.data.spreadsheet.SpreadsheetEntry entries = spreadsheet.openSpreadsheet(spreadsheetId);

		int minRow = 2;
		int maxRow = 37;
		int minCol = 1;
		int maxCol = 27;

		int row = 2;

		int idxDueDate = 1;
		int idxStatus = 2;
		int idxAgency = 3;
		int	idxDivision = 4;
		int	idxCompetition = 5;
		int	idxProjectTitle = 6;
		int	idxProjectDescription = 7;
		int	idxFunderType = 8;
		int	idxLeadApplicant = 9;
		int	idxDuration = 10;
		int	idxFundingReq = 11;
		int	idxFundingRecd = 12;
		int	idxEstNotification = 13;
		int	idxBeginFunding = 14;
		int	idxEndFunding = 15;
		int	idxFundedNotification = 16;
		int	idxGOStaff = 17;
		int	idxPCCDirLead = 18;
		int	idxPrimaryCampus = 19;
		int	idxSecondaryCampus = 20;
		int	idxApplicantCategory = 21;
		int	idxLeadPartner = 22;
		int	idxFundingCategory = 23;
		int	idxNotesInternal = 24;
		int	idxCGAAcct = 25;
		int	idxCGAFund = 26;
		int	idxCGAOrg =27;

		int dataSheetIndex = 0;
		int logSheetIndex = 1;

		String agency = "";
		String title = "";
		String competition = "";

		SimpleDateFormat dtf = new SimpleDateFormat("y-M-d");

		//for(row = 7; row <= 7; row++){
		row = 37;
		 try{

			//Project Title  (#83)
			CellEntry ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxProjectTitle, idxProjectTitle).get(0);
			title = ce.getCell().getValue();
			newDoc = documentService.createDocument("Grant", title);
			newDoc = documentService.lockDocument(newDoc);

			//Agency
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxAgency, idxAgency).get(0);
			List<String> agencyList = new ArrayList<String>();
			agency = ce.getCell().getValue();
			agencyList.add(agency);
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxDivision, idxDivision).get(0);
			if(ce.getCell().getValue() != null){
			   agencyList.add(ce.getCell().getValue());
			}
			newDoc.setHierarchicalCategory("Agency", agencyList);


			//Funder Type
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxFunderType, idxFunderType).get(0);
			newDoc.setField("Funder Type", ce.getCell().getValue());
			
			//Competition 
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxCompetition, idxCompetition).get(0);
			competition = ce.getCell().getValue();
			newDoc.setField("Competition", competition);

			//Status
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxStatus, idxStatus).get(0);
			newDoc.setField("Status", ce.getCell().getValue());

			//Due Date
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxDueDate, idxDueDate).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Due Date", dtf.parse(ce.getCell().getValue()));
			}

			//Description
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxProjectDescription, idxProjectDescription).get(0);
			newDoc.setRichText(ce.getCell().getValue());

			//Lead Applicant
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxLeadApplicant, idxLeadApplicant).get(0);
			newDoc.setField("Lead Applicant / Partner", ce.getCell().getValue());

			//Funding Requested
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxFundingReq, idxFundingReq).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Funding Requested", Double.valueOf(ce.getCell().getValue()).doubleValue());
			}

			//Funding Received
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxFundingRecd, idxFundingRecd).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Funding Received", Double.valueOf(ce.getCell().getValue()).doubleValue());
			}

			//Est Notification
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxEstNotification, idxEstNotification).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Estimated Notification", dtf.parse(ce.getCell().getValue()));
			}

			//Begin Funding
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxBeginFunding, idxBeginFunding).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Begin Funding", dtf.parse(ce.getCell().getValue()));
			}

			//End Funding
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxEndFunding, idxEndFunding).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("End Funding", dtf.parse(ce.getCell().getValue()));
			}

			//Funding Notification
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxFundedNotification, idxFundedNotification).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Funded Notification", dtf.parse(ce.getCell().getValue()));
			}

			//Grants Office Staff
		   ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxGOStaff, idxGOStaff).get(0);
		   if(ce.getCell().getValue() != null){
			  newDoc.setField("Grants Office Staff", ce.getCell().getValue());
		   }

			//PCC Dir/Lead
		   ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxPCCDirLead, idxPCCDirLead).get(0);
		   if(ce.getCell().getValue() != null){
			  newDoc.setField("PCC Director / Lead", ce.getCell().getValue());
		   }


			//Primary Campus
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxPrimaryCampus, idxPrimaryCampus).get(0);
			if(ce.getCell().getValue() != null){
			  String list = (String)ce.getCell().getValue();
			  List<String> campusList = Arrays.asList(list.split(","));
			  for (int i = 0; i < campusList.size(); i++) {
				  campusList.set(i, campusList.get(i).toString().trim());
			  }
			  newDoc.setMultiField("Primary Campus", campusList);
			}

			//Secondary Campus
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxSecondaryCampus, idxSecondaryCampus).get(0);
			if(ce.getCell().getValue() != null){
			  String list = (String)ce.getCell().getValue();
			  List<String> campusList = Arrays.asList(list.split(","));
			  for (int i = 0; i < campusList.size(); i++) {
				  campusList.set(i, campusList.get(i).toString().trim());
			  }
			  newDoc.setMultiField("Secondary Campus", campusList);
			}

			//Applicant Category
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxApplicantCategory, idxApplicantCategory).get(0);
			if(ce.getCell().getValue() != null){
			   newDoc.setField("Applicant Category", ce.getCell().getValue());
			}


			//Funding Category
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxFundingCategory, idxFundingCategory).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("Funding Category", ce.getCell().getValue());
			}

			//CGA Accountant
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxCGAAcct, idxCGAAcct).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("CGA Accountant", ce.getCell().getValue());
			}

			//CGA Fund Number
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxCGAFund, idxCGAFund).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("CGA Fund Number", ce.getCell().getValue());
			}

			//CGA Organization Number
			ce = (CellEntry)spreadsheet.getCells(entries, dataSheetIndex, row, row, idxCGAOrg, idxCGAOrg).get(0);
			if(ce.getCell().getValue() != null){
			  newDoc.setField("CGA Organization Number", ce.getCell().getValue());
			}

			documentService.store(newDoc);
			List logEntry = new ArrayList<String>();
			spreadsheet.insertCell(entries, logSheetIndex, row, 1, agency);
			spreadsheet.insertCell(entries, logSheetIndex, row, 2, competition);
			spreadsheet.insertCell(entries, logSheetIndex, row, 3, title);
			spreadsheet.insertCell(entries, logSheetIndex, row, 4, (String)newDoc.getId());  
		 

		  }catch(Exception e){
			spreadsheet.insertCell(entries, logSheetIndex, row, 1, agency);
			spreadsheet.insertCell(entries, logSheetIndex, row, 2, competition);
			spreadsheet.insertCell(entries, logSheetIndex, row, 3, title);
			spreadsheet.insertCell(entries, logSheetIndex, row, 4, e.getMessage());

		  }

		  java.util.concurrent.TimeUnit.SECONDS.sleep(2);

		//}	

		return getFeedbackHelper().successMessage("Document Created");

} catch (Exception e) {
		handleError(e, doc);
		return getFeedbackHelper().errorMessage(e.getClass() +" "+ e.getMessage());
	}
}