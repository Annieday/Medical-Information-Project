import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class Main {

	//static 
	
	public static void main(String[] args) throws SQLException {
		

		int app_option=0; // Chose application program
		
		Scanner in = new Scanner(System.in);
		// Get connection and create tables
		//db.createDB();
		//db.insertDB();
		
		// Enter main menu
		while ( app_option !=6 )
		{
			System.out.println ("");
			System.out.println("Welcome to the Alberta Health System, please chose an application program: ");
			System.out.println("1. Prescription");
			System.out.println("2. Medical Test");
			System.out.println("3. Patient Information Update");
			System.out.println("4. Search Engine");
			System.out.println("5. Help");
			System.out.println("6. Exit");
			System.out.println ("----------------------------------------------------------------------------");
			System.out.println("Enter a number: ");
			
			// Read app_option from screen
			app_option = in.nextInt();
			
			if (app_option == 1)
			{
				Prescription ();
				
			}
			
			else if (app_option == 2)
			{
				MedicalTest();
			}
			
			else if (app_option == 3)
			{
				PatientInfoUpdate ();
			}
			
			else if (app_option == 4)
			{
				SearchEngine(); 
			}
			
			else if (app_option == 5)
			{
				Help ();
			}
			else if (app_option == 6)
			{
				System.out.println ("Thanks for using the system, Goodbye!");
				//db.close();
				System.exit(0);
			}
			else
			{
				app_option = 0;
				System.out.println ("XXXXX Wrong opertion, try again. XXXXX");
			}
		}
		
		//db.close();
		
	}
	private static boolean IsNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		if (str.length() == 0) {
			return false;
		}

		return true;
	}
	// medical test program!!
private static void MedicalTest() throws SQLException {
			Scanner scan = new Scanner(System.in);
			String strin = "";
			int choise = 1;
			int employee_no = 0;
			String doctorname = "";
			String testname = "";
			String patientname = "";
			int healthcareno = -1;
			int typeid = -1;
			String labname = "";
			String testdate;
			String testresult = "";
			int testid = 0;

			while (choise != 2) {
				System.out.println("----------------------------------- ");
				System.out.println("Medical Test              ");
				System.out.println("1.Enter Information        ");
				System.out.println("2.Back                    ");
				System.out.println("----------------------------------- ");
				System.out.println("Enter a number: ");
				strin = scan.nextLine();
				if (!IsNumeric(strin)) {
					System.out.println("Please Enter a number.");
				} else {
					choise = Integer.parseInt(strin);
					switch (choise) {
					case 1: {
						DB db= new DB();
						boolean bcheck = false;
						int check = 0;
						System.out.println("Please enter doctor number or name(case sensitive) who prescribed the test:");
						strin = scan.nextLine();
						if (!IsNumeric(strin)) {
							if (strin.length()>100)
							{
								System.out.println (" The entered name is over 100.");
								db.close();
								break;
							}
							else if (strin.equalsIgnoreCase("") || strin.equalsIgnoreCase(" "))
							{
								System.out.println ("Wrong formation");
								db.close();
								break;
							}
							check = db.CheckEmployeeName(strin);
							if ( check == 0 ) {
								System.out
										.println("Docter does not exist in the database.");
								db.close();
								break;
							}
							doctorname = strin;
							if ( check > 0 ) {
								employee_no = db.GetEmployeeNobyName(doctorname);
							}
						} else {
							employee_no = Integer.parseInt(strin);
							// check if the no is in the system
							bcheck = db.CheckEmployeeNo(employee_no);
							if (!bcheck) {
								System.out
										.println("Docter does not exist in the database.");
								db.close();
								break;
							}
						}

						System.out.println("Please enter patient's health care number or name (case sensitive):");
						strin = scan.nextLine();
						// input the patient name
						if (!IsNumeric(strin)) {
							if (strin.length()>100)
								{
									System.out.println (" The entered name is over 100.");
									db.close();
								break;
								}
								else if (strin.equalsIgnoreCase("") || strin.equalsIgnoreCase(" "))
								{
									System.out.println ("Wrong formation.");
									db.close();
									break;
								}
							check = db.CheckPatientName(strin);
							// if check is 0, the name is not in the system
							if (check == 0) {
								System.out.println("The patient does not exist in the database.");
								db.close();
								break;
						} else if (check > 0) {
							// if check is 1, there is one person called this
							// name in the system
							patientname = strin;
							healthcareno = db.GetHealthcarenobyName(patientname);
									
						}
						}else {
							healthcareno = Integer.parseInt(strin);
							// check if the no is in the system
							bcheck = db.CheckExistHealthCareNo(healthcareno);
							if (!bcheck) {
								System.out
										.println("The patient does not exist in the database.");
								db.close();
								break;
							}
						}
						System.out
								.println("Please enter test name or test type id:");
						strin = scan.nextLine();
						if (!IsNumeric(strin)) {
						if (strin.length()>48)
								{
									System.out.println (" The entered test name is over 48.");
									db.close();
									break;
								}
								else if (strin.equalsIgnoreCase("") || strin.equalsIgnoreCase(" "))
								{
									System.out.println ("Wrong formation.");
									db.close();
									break;
								}
							testname = strin;
							// get type_id by the type name.
							typeid = db.GetTypeIdbyName(testname);
							if (typeid == -1) {
								System.out.println("The test name does not exist in the database.");
								db.close();
								break;
							}
						} else {
							typeid = Integer.parseInt(strin);
							// check if the type id is in the system.
							bcheck = db.CheckTypeId(typeid);
							if (!bcheck) {
								System.out.println("Test does not exist in the database.");
								db.close();
								break;
							}
						}

						System.out.println("Please enter test id:");
						strin = scan.nextLine();
						testid = Integer.parseInt(strin);
						// use checkPrescriptedTest() to check if the test has been
						// prescribed with the same information that user entered.
						bcheck = db.CheckPrescriptedTest(employee_no,
								healthcareno, typeid, testid);
						if (!bcheck) {
							testid = -1;
							System.out.println("The prescription does not exist.");
							db.close();
							break;
						}
						// check if the test information have been entered.
						bcheck = db.CheckIfTested(testid);
						if (!bcheck) {
							System.out
									.println("The result has been entered. Please enter other information.");
							db.close();
							break;
						}
						if (testid != -1) {
							System.out.println("Please enter the lab name:");
							labname = scan.nextLine();
							// check if the lab is exist
							bcheck = db.CheckLabbyName(labname);
							if (!bcheck) {
								System.out
										.println("No such lab exsit in the database.");
										db.close();
										break;
							}
							// check if the lab can conduct the type of test.
							bcheck = db.CheckCanConduct(typeid, labname);
							if (bcheck) {
								// if can then update the data.
								System.out
										.println("Please enter the test date(MM/DD/YYYY):");
								testdate = scan.nextLine();
								// to see if the input is type of date
								try {
									Date test_date = new Date();
									DateFormat formatdate = new SimpleDateFormat(
											"MM/dd/yyyy");
									test_date = formatdate.parse(testdate);

								} catch (ParseException e) {
									System.out.println("Input wrong date format.");
									db.close();
									break;
								}
								System.out.println("Please enter the test result:");
								testresult = scan.nextLine();
								// to see if the update works
								bcheck = db.UpdateTestResult(testid,
										labname, testresult, testdate);
								if (bcheck) {
									System.out
											.println("Success to make a medical test.");
								} else {
									System.out
											.println("Fail to make a medical test.");
								}
							} else {
								System.out
										.println("This lab can't conduct this test.");
										db.close();
								break;
							}
						}
						db.close();
					}
						break;
					case 2: {
						System.out.println("Back Home Menu.");
					}
						break;
					default:
						System.out
								.println("Please Enter a number between 1 and 2.");
						break;
					}
				}
			}
		}
	

	
	// The searchEngine. can search data in three ways
			// 1. View Test Record
			// 2. View Prescription
			// 3. View Alarm Age
	private static void SearchEngine() throws SQLException
	{
		Scanner scan = new Scanner(System.in);
		String strin = "";
		int choise = 1;
		boolean bcheck = false;

		// list test record table
		String ListTestRecord = "";
		int healthcareno = -1;
		String patientname = "";

		// list prescription
		String ListPrescribe = "";
		int employeeno = -1;
		String doctorname = "";
		String startingdate = "";
		String endingdate = "";

		// list alarm age
		String ListPatient = "";
		String testtypename = "";
		int testtypeid = 0;

		while (choise != 4) {
			System.out.println("----------------------------------- ");
			System.out.println("Search Engine             ");
			System.out.println("1. View Test Record        ");
			System.out.println("2. View Prescription       ");
			System.out.println("3. View Alarm Age          ");
			System.out.println("4. Back                    ");
			System.out.println("----------------------------------- ");
			System.out.println("Enter a number: ");
			strin = scan.nextLine();
			if (!IsNumeric(strin)) {
				System.out.println("Please Enter a number.");
			} else {
				choise = Integer.parseInt(strin);
				switch (choise) {
				case 1: {
					int testrecordchoise = 1;
					DB db= new DB();
					while (testrecordchoise != 3) {
						// if choose 1, then check by healthcare
						// if choose 2, then check by name
						System.out.println("----------------------------------- ");
						System.out.println("View Test Record          ");
						System.out.println("1. Healthcare Number            ");
						System.out.println("2. Patient Name           ");
						System.out.println("3. Back                    ");
						System.out.println("----------------------------------- ");
						System.out.println("Enter a number: ");

						strin = scan.nextLine();
						if (!IsNumeric(strin)) {
							System.out.println("Please enter a number.");
						} else {
							testrecordchoise = Integer.parseInt(strin);

							switch (testrecordchoise) {
							case 1: {
								System.out.println("Please enter a healthcare number :");
								strin = scan.nextLine();
								if (!IsNumeric(strin)) {
									System.out.println("Please Enter a number.");
									//db.close();
									break;
								}

								healthcareno = Integer.parseInt(strin);
								//check if the health care no exist in the database
								bcheck = db.CheckExistHealthCareNo(healthcareno);
										
								if (!bcheck) {
									System.out.println("Patient dose not exist in the database.");
									//db.close();
									break;
								}
								//use GetTestRecordbyHealthcareno() to get the information.
								ListTestRecord = db.GetTestRecordbyHealthcareno(healthcareno);
										

								System.out.println("The Test Record List is:");
								System.out.println("	health_care_no	name		test_name		test_date		result");
								System.out.println(ListTestRecord);

							}
								break;
							case 2: {
								int check = 0;
								System.out.println("Please enter a patient name:");
								strin = scan.nextLine();
								if (!IsNumeric(strin)) {
							if (strin.length()>100)
								{
									System.out.println (" The entered name is over 100.");
									//db.close();
								break;
								}
								else if (strin.equalsIgnoreCase("") || strin.equalsIgnoreCase(" "))
								{
									System.out.println ("Wrong formation.");
									//db.close();
									break;
								}
							check = db.CheckPatientName(strin);
							// if check is 0, the name is not in the system
							if (check == 0) {
								System.out.println("The patient does not exist in the database.");
								//db.close();
								break;
						} else if (check > 0) {
						    patientname = strin;
							healthcareno = db.GetHealthcarenobyName(patientname);
									
						}
						} else {
							System.out.println("Please enter a name.");
							//db.close();
									break;
						}
								//if the name exist, then get all the information under the same name
								ListTestRecord = db.GetTestRecordbyHealthcareno(healthcareno);
								System.out.println("The Test Record List is:");
								System.out.println("	health_care_no	name		test_name		test_date		result");
								System.out.println(ListTestRecord);
							}
								break;
							case 3:
								System.out.println("Back.");
								break;
							default:
								System.out.println("Please Enter a number between 1 and 3.");
								break;

							}
						}
					}
					db.close();
				}
					break;
				case 2: {//view prescribe
					int prescriptionchoise = 1;
					int check = 0;
					DB db= new DB();
					while (prescriptionchoise != 3) {
						System.out.println("----------------------------------- ");
						System.out.println("View Prescribe          ");
						System.out.println("1. Doctor Number            ");
						System.out.println("2. Doctor Name            ");
						System.out.println("3. Back                    ");
						System.out.println("----------------------------------- ");
						System.out.println("Enter a number: ");

						strin = scan.nextLine();
						if (!IsNumeric(strin)) {
							System.out.println("Please Enter a number");
						} else {
							prescriptionchoise = Integer.parseInt(strin);

							switch (prescriptionchoise) {
							case 1: {
								System.out.println("Please enter a doctor number :");
										
								strin = scan.nextLine();
								if (!IsNumeric(strin)) {
									System.out.println("Please Enter a number.");
									db.close();
									break;
								}
								//check the doctor number to see if the doctor exist.
								employeeno = Integer.parseInt(strin);
								bcheck = db.CheckEmployeeNo(employeeno);
								if (!bcheck) {
									System.out.println("Doctor number does not exist in the database.");
									db.close();
									break;
								}
								System.out.println("Please enter a starting date(MM/DD/YYYY):");
										
								//enter the starting date.
								startingdate = scan.nextLine();
								try {
									//make sure that the data is in type of date.
									Date starting_date = new Date();
									DateFormat formatdate = new SimpleDateFormat("MM/dd/yyyy");
									starting_date = (Date) formatdate.parse(startingdate);

								} catch (ParseException e) {
									System.out.println("Input wrong date format.");
									break;
								}
								System.out.println("Please enter a ending date(MM/DD/YYYY):");
								//enter the end date.
								endingdate = scan.nextLine();
								try {
									//make sure that the date is in type of date
									Date ending_date = new Date();
									DateFormat formatdate = new SimpleDateFormat("MM/dd/yyyy");
									ending_date = (Date) formatdate.parse(endingdate);

								} catch (ParseException e) {
									System.out.println("Input wrong date format.");
									break;
								}
								//get the information
								ListPrescribe = db.GetTestPrescribesByNo(employeeno,startingdate, endingdate);
								System.out.println("The Prescribe List is:");

								System.out.println("	health_care_no	name		test_type_name		prescribe_date ");
								System.out.println(ListPrescribe);

							}
								break;
							case 2: {
								System.out.println("Please enter a doctor name:");
										
								strin = scan.nextLine();
								//check the employee name to see if it is in the database.
								check = db.CheckEmployeeName(strin);
								if (check == 0) {
									System.out.println("Docter does not exist in the database.");								
									db.close();
									break;
								} else {
									doctorname = strin;
									employeeno = db.GetEmployeeNobyName(doctorname);
								}

								System.out.println("Please enter a starting date(MM/DD/YYYY):");
								startingdate = scan.nextLine();
								try {
									//make sure the input is in type of date
									Date starting_date = new Date();
									DateFormat formatdate = new SimpleDateFormat("MM/dd/yyyy");
											
									starting_date = (Date) formatdate.parse(startingdate);
								} catch (ParseException e) {
									System.out.println("Input wrong date format.");
											
									break;
								}
								System.out.println("Please enter a ending date(MM/DD/YYYY):");
										
								endingdate = scan.nextLine();
								try {
									//make sure the input is in type of date
									Date ending_date = new Date();
									DateFormat formatdate = new SimpleDateFormat("MM/dd/yyyy");
									ending_date = (Date) formatdate.parse(endingdate);

								} catch (ParseException e) {
									System.out.println("Input wrong date format.");
											
									break;
								}
								//get the prescribe by name.
								ListPrescribe = db.GetTestPrescribesByNo(employeeno,startingdate, endingdate);
								System.out.println("The Test Record List is:");
								System.out.println("	health_care_no	name		test_type_name		prescribe_date ");
								System.out.println(ListPrescribe);
							}
								break;
							case 3:
								System.out.println("Back.");
								break;
							default:
								System.out.println("Please Enter a number between 1 and 3.");
								break;
							}
						}
					}

					db.close();
				}
					break;
				case 3: {
					DB db= new DB();
					System.out.println("Please enter test name or test type id:");
							
					strin = scan.nextLine();
					if (!IsNumeric(strin)) {
						if (strin.length()>48)
							{
								System.out.println (" The entered test name is over 48.");
								db.close();
								break;
							}
							else if (strin.equalsIgnoreCase("") || strin.equalsIgnoreCase(" "))
							{
								System.out.println ("Wrong formation.");
								db.close();
								break;
							}
						testtypename = strin;
						// get type name and get the type id by the test name
						testtypeid = db.GetTypeIdbyName(testtypename);
						if (testtypeid == -1) {
						//if the type id is -1 then there is no such test type in the system.
							System.out.println("The test name does not exist.");
							db.close();
							break;
						}
					} else {
						testtypeid = Integer.parseInt(strin);
						//if input is number, then check if it is a type in the database.
						bcheck = db.CheckTypeId(testtypeid);
						if (!bcheck) {
							System.out.println(" Test does not exist in the database.");
							db.close();
							break;
						}
						testtypename = db.GetTestNamebyTypeId(testtypeid);
								
					}
					//get the information by GetAlarmAgePatient().
					ListPatient = db.GetAlarmAgePatient(testtypeid);

					System.out.println("The Alarm Age Patients for the Test:");
					System.out.println("health_care_no 	    name  		 address  		  phone");
					System.out.println(ListPatient);
					db.close();
				}
					break;
				case 4: {
					System.out.println("  Back Home Menu.");
				}
					break;
				default:
					System.out.println("Please Enter a number between 1 and 4.");
					break;
				}
			}
		}
		
	}

	

	private static void PatientInfoUpdate() throws SQLException 
	{
		int health_care_no = 0;
		boolean have_health_care_no = false;
		String update_patient_info = "";
		String create_patient_info = "";
		String blank = "";
		boolean mode = true;
		int option = 0;
		String name = "";
		String address = "";
		String phone = "";
		String birth_day = "";
		int lenght = 0;
		String quit = null;
		String not_allowed_test = null;
		int update_type_id = -1;
		boolean exist_in_NotAllowed = false;
		
		System.out.println ("Welcome to Patient Information Update program.");
		Scanner in = new Scanner(System.in);
		DB db= new DB();
		/***************Enter health care number ***********************/
		
		mode = true;
		while (mode == true)
		{
			System.out.println ("Please enter your health care number.");
			//blank = in.nextLine();
			try 
			{
			
				health_care_no = in.nextInt();
				blank = in.nextLine();
				mode = false;
			}
			catch (InputMismatchException ime3)
			{
				System.out.println ("XXXXX Wrong health care number fomation, enter 't' to try again. Or enter 'q' to quit.XXXXX");
				blank = in.nextLine();
				quit = in.nextLine();
				if (quit.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the create process, and go back to the main menu");
					mode = false;
					db.close ();
					return;
				}
				else if (quit.equalsIgnoreCase("t"))
				{
					mode = true;
				}
				else
				{
					System.out.println ("XXXXX Wrong operation, try again.XXXXX ");
					mode = true;
				}
			}
		}
		
		// Check health care no in the system or not
		have_health_care_no = db.checkHCN(health_care_no);
		
		// If in the system,display the information and then ask update or not
		if (have_health_care_no == true)
		{
			db.displayPaitentInfor (health_care_no);
			System.out.println ("System already have your information, Do you want to update? Y/N");
			update_patient_info = in.nextLine();
			
			// -------------If user do not want to update the information, then go back to main menu
			if (update_patient_info.equalsIgnoreCase("n"))
			{
				System.out.println ("User do not want to update, go back to main menu.");
				db.close ();
				return;
			}
				
			// If user want to update the information
			mode = true;
			while (mode == true)
			{
				System.out.println ("");
				System.out.println ("Chose which one to update: ");
				System.out.println ("1.name (100)");
				System.out.println ("2.address (200)");
				System.out.println ("3.birth_day (MM/DD/YYY)");
				System.out.println ("4.phone (10)");
				System.out.println ("5.not allowed test (48)");
				System.out.println ("6.finish update, go back to the main menu");
				option = in.nextInt();
				blank = in.nextLine();
				if (option == 1)
				{
					System.out.println ("Enter the new name: ");
					name = in.nextLine();
					// If oversize
					if (name.length()>100)
					{
						boolean tryAgain = true;
						System.out.println ("XXXXX Over the 'name' length constranit, try again. XXXXX ");
						while (tryAgain == true)
						{
							System.out.println ("Try a new name or type 'q' to quit: ");
							name = in.nextLine();
							if (name.equalsIgnoreCase("q"))
							{
								break;
							}
							if (name.length() >100)
							{
								tryAgain = true;
							}
							else
								tryAgain = false;
						}
					}
					db.updatePatient(option,health_care_no,name);
					System.out.println ("Patient information updated :");
					db.displayPaitentInfor (health_care_no);
					mode = true;
				}
				else if (option ==2)
				{
					System.out.println ("Enter the new address: ");
					address = in.nextLine();
					// If oversize
					if (address.length()>200)
					{
						System.out.println ("Over the 'address' length constranit, try again");
					}
					db.updatePatient(option,health_care_no,address);
					System.out.println ("Patient information updated :");
					db.displayPaitentInfor (health_care_no);
					mode = true;
				}
				else if (option ==3)
				{
					System.out.println ("Enter the new birth day: ");
					birth_day = in.nextLine();
					if (birth_day.length()!=10)
					{
						System.out.println ("XXXXX The birth day formation is wrong, should be MM/DD/YYYY, try again. XXXXX  ");
					}
					else
					{
						try
						{
							db.updatePatient(option,health_care_no,birth_day);
							System.out.println ("Patient information updated :");
							db.displayPaitentInfor (health_care_no);
							mode = true;
						}
						catch (SQLException e)
						{
							System.out.println ("XXXXX The birth day formation is wrong, should be MM/DD/YYYY, try again. XXXXX ");
						}
						
						
					}
					
				}
				else if (option ==4)
				{
					System.out.println ("Enter the new phone: ");
					phone = in.nextLine();
					// If oversize
					if (phone.length() > 10)
					{
						System.out.println ("XXXXX Over the 'name' length constranit, try again. XXXXX ");
					}
					db.updatePatient(option,health_care_no,phone);
					System.out.println ("Patient information updated :");
					db.displayPaitentInfor (health_care_no);
					mode = true;
				}
				// Update not allowed test
				else if (option == 5)
				{
					System.out.println ("Enter the new not allowed test: ");
					not_allowed_test = in.nextLine();
					if (not_allowed_test.length()>48)
					{
						System.out.println ("XXXXX The length of test name is over 48, try again. XXXXX ");
						mode = true;
					}
					else if (not_allowed_test.equalsIgnoreCase("") || not_allowed_test.equalsIgnoreCase(" ") )
					{
						System.out.println ("XXXXX Wrong formation, try again. XXXXX ");
						mode = true;
					}
					else
					{
						// Check if the test is in the system
						update_type_id = db.checkTypeId(not_allowed_test);
						if (update_type_id == -1)
						{
							System.out.println (not_allowed_test + "	is not in the system, try again");
							mode = true;
						}
						// If the test is in the system, then check if the patient is in not_allowed table
						else
						{
							exist_in_NotAllowed = db.check_patient_in_NotAllowed(health_care_no);
							if (exist_in_NotAllowed = true)
							{
								db.updateNot_allowed(health_care_no, update_type_id);
							}
							else
							{
								db.insertNot_allowed(health_care_no,update_type_id);
							}
							System.out.println ("Patient not allowed test " +not_allowed_test +" is updated");
							mode = true;
						}
					}
				}
				else if (option ==6)
				{
					db.close();
					return;
				}
				else
				{
					System.out.println ("XXXXX No this option, try again. XXXXX ");
					mode = true;
				}
			}
		}
		
		//-------- If not in the system, ask create or not
		else if (have_health_care_no == false)
		{
			String createOrNot = null;
			String new_name =null;
			String new_address =null;
			String new_birth_day =null;
			String new_phone =null;
			String new_blank = null;
			boolean skipOrNot = false;
			String quitOrNot = "";
			String new_not_allowed_test = null;
			int type_id = -1;
			
			System.out.println ("No your information in the system, Do you want to create your information? Y/N");
			createOrNot = in.nextLine();
			// User do not want to create 
			if (createOrNot.equalsIgnoreCase("n"))
			{
				System.out.println ("User do not want to create, go back to main menu.");
				db.close ();
				return;
			}
			// User want to create 
			////////////////////////////////////////////////////////////////
			/************Enter new health care number****************************/
			
			skipOrNot = false ;
			while (skipOrNot == false)
			{
				System.out.println ("");
				System.out.println ("You must enter your health care number (integer):");
				System.out.println ("You already entered one health care number: "+health_care_no);
				System.out.println ("s. Save the previously entered health care number");
				System.out.println ("q. Quit the create process");
				
				createOrNot = in.nextLine();
				// Check if user want to save the previous one or change to a new one
				if (createOrNot.equalsIgnoreCase("s"))
				{
					System.out.println ("Save the health care number: "+ health_care_no);
					skipOrNot = true;
				}
                else if (createOrNot.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the create process, and go back to the main menu");
					db.close ();
					return;
				}
				else
				{
					System.out.println ("XXXXX Wrong operation, try again! XXXXX ");
					skipOrNot = false;
				}
				
			}
			
			/************Enter new name****************************/
			
			
			skipOrNot = false ;
			while (skipOrNot == false )
			{
				System.out.println ("Enter your name (100) or enter  'q' to quit the create process and go back to the main menu.");
				new_name = in.nextLine();
				// If quit, go back to the main menu
				if (new_name.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the create process, and go back to the main menu");
					new_name = null;
					skipOrNot = true;
					db.close ();
					return;
				}
				else if (new_name.equalsIgnoreCase("") || new_name.equalsIgnoreCase(" ") || (new_name==null))
				{
					System.out.println ("XXXXX Wrong formation, try again. XXXXX ");
					skipOrNot = false;
				}
				else
				{
					// Check length of new name
						// If over 100, then try agian
					if (new_name.length() >100)
					{
						System.out.println ("XXXXX The length of name is over 100, try again. XXXXX ");
						skipOrNot = false;
					}
						// If not over 100, then save it
					else
					{
						System.out.println ("Save the new name: " + new_name);
						skipOrNot = true;
					}
				}
				
			}
			
			/************Enter new address****************************/

			System.out.println ("Enter your address (200) or enter 'k' to skip:");
			skipOrNot = false ;
			while (skipOrNot == false)
			{
				new_address = in.nextLine();
				if (new_address.equalsIgnoreCase(" ") || new_address.equalsIgnoreCase(""))
				{
					System.out.println ("XXXXX Wrong operation, try again.XXXXX ");
					skipOrNot = false;
				}
				// If not skip enter new address
				else if (! new_address.equalsIgnoreCase("k"))
				{
					// Check the length of new address
					// If >10, enter again or enter 'k' to skip
					if (new_address.length()>200)
					{
						System.out.println ("XXXXX The length of new address is over the limit 200, try again or enter 'k' to skip. XXXXX ");
						skipOrNot = false;
					}
					// If <= 200, save the new address, and continue 
					else 
					{
						
						System.out.println ("Save the new address: " + new_address);
						skipOrNot = true;
					}
				}
				else if (new_address.equalsIgnoreCase("k"))
				{
					skipOrNot = true;
					new_phone = null;
				}
				
			}
			
			/************Enter new birth day****************************/
			
			System.out.println ("Enter your bith day (MM/DD/YYYY) or enter 'k' to skip:");
			skipOrNot = false ;;
			while (skipOrNot == false )
			{
				new_birth_day = in.nextLine();
				if (new_birth_day.equalsIgnoreCase(" ") || new_birth_day.equalsIgnoreCase(""))
				{
					System.out.println ("XXXXX Wrong operation, try again.XXXXX ");
					skipOrNot = false;
				}
				// If not skip enter new birth day
				else if (! new_birth_day.equalsIgnoreCase("k"))
				{
					System.out.println ("Save the new birth day: "+new_birth_day);
					skipOrNot = true;
				}
				// If skip, continue, and new birth day = null
				else if (new_birth_day.equalsIgnoreCase("k"))
				{
					skipOrNot = true;
					new_birth_day = null;
				}
				
			}
			
			/************Enter new phone number****************************/
			
			System.out.println ("Enter your phone number (10) or enter 'k' to skip:");
			skipOrNot = false ;
			while (skipOrNot == false)
			{
				new_phone = in.nextLine();
				
				if (new_phone.equalsIgnoreCase(" ") || new_phone.equalsIgnoreCase(""))
				{
					System.out.println ("XXXXX Wrong operation, try again. XXXXX ");
					skipOrNot = false;
				}
				// If not skip enter new address
				else if (! new_phone.equalsIgnoreCase("k"))
				{
					// Check the length of new address
					// If >10, enter again or enter 'k' to skip
					if (new_phone.length()>10)
					{
						System.out.println ("XXXXX The length of new phone is over the limit 10, try again or enter 'k' to skip. XXXXX ");
						skipOrNot = false;
					}
					// If <= 200, save the new address, and continue 
					else 
					{
						System.out.println ("Save the new phone is " + new_phone);
						skipOrNot = true;
					}
				}
				// If skip, continue, and new phone = null;
				else if (new_phone.equalsIgnoreCase("k"))
				{
					skipOrNot = true;
					new_phone = null;
				}
				
			}
			
			/************Enter not allowed test****************************/
			
			System.out.println ("Enter a not allowed test or enter 'k' to skip");
			skipOrNot = false ;
			while (skipOrNot == false)
			{
				new_not_allowed_test = in.nextLine();
				if (new_not_allowed_test.equalsIgnoreCase("k"))
				{
					skipOrNot = true;
					new_not_allowed_test = null;
				}
				else if (new_not_allowed_test.equalsIgnoreCase("") || new_not_allowed_test.equalsIgnoreCase(" ") )
				{
					System.out.println ("XXXXX Wrong formation, try again. XXXXX ");
					skipOrNot = false;
				}
				else if (new_not_allowed_test.length()>48)
				{
					System.out.println ("XXXXX The length of test name is over 48, try again. XXXXX ");
					skipOrNot = false;
				}
				// Check if the not allowed test is in the system
				else
				{
					type_id = db.checkTypeId(new_not_allowed_test);
					// If not in the system
					if (type_id == -1)
					{
						System.out.println ("XXXXX The entered test is not in the system, try again. XXXXX ");
						skipOrNot = false;
					}
					// If in the system
					else
					{
						// Insert into not_allowed table
						System.out.println ("Save the not allowed test: "+new_not_allowed_test);
						skipOrNot = true;
						
					}
				}
			}

			
			// After enter create information, insert into table patient
			System.out.println ("Finsih creating process.");
			System.out.println ("Health care number : "+ health_care_no);
			System.out.println ("Name : "+ new_name);
			System.out.println ("Address : "+ new_address);
			System.out.println ("Birth day : "+ new_birth_day);
			System.out.println ("Phone : "+ new_phone);
			db.insertPatient(health_care_no, new_name, new_address, new_birth_day, new_phone);
			db.insertNot_allowed (health_care_no,type_id);
			System.out.println ("Patient information created successfully.");
			
		}
		
		db.close ();
	}

	private static void Prescription() throws SQLException {
		
		String test_name = "";
		int health_care_no = 0;
		int emp_no = -1;
		boolean have_emp_no = false;
		boolean have_health_care_no = false;
		boolean mode=true;
		String blank = "";
		int test_id = 0;
		int type_id =0;
		String doc_name = null;
		String patient_name = null;
		
		Scanner in = new Scanner(System.in);
		DB db= new DB();
		/**************** Enter the employee_no within Integer type, or try again*******************/
		System.out.println ("Welcome to the Prescription program");
		while (mode == true)
		{
			System.out.println ("Please enter the employee_no or name (case insensitive ) of the doctor who prescribes the test: ");
			try
			{
				emp_no = in.nextInt();
				blank=in.nextLine();
			
				// Check emp_no in the db or not?
				have_emp_no = db.checkEmp_no (emp_no);
				if (have_emp_no == false)
				{	
					System.out.println ("XXXXX No this employee no in the system, try again,or press 'q' to quit. XXXXX ");
					mode = true;
				}
				else
				{
					mode = false;
				}
		
			}
			catch (InputMismatchException ime1)
			{
				doc_name = in.nextLine();
				if (doc_name.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the prescription process, go back to main menu");
					db.close ();
					return;
				}
				else if (doc_name.length()>100)
				{
					System.out.println ("XXXXX The entered name is over 100, try again,or press 'q' to quit. XXXXX");
					mode = true;
				}
				else if (doc_name.equalsIgnoreCase("") || doc_name.equalsIgnoreCase(" "))
				{
					System.out.println ("XXXXX Wrong formation, try again or press 'q' to quit. XXXXX");
					mode = true;
				}
				else
				{
					//////////////////////////////
					
					emp_no = db.get_emp_no (doc_name);
					// No this employee
					if (emp_no == -1)
					{
						System.out.println ("XXXXX No this employee no in the system, try again,or press 'q' to quit. XXXXX ");
						mode = true;
					}
					// Multiple employees have this name
					else if (emp_no == -2)
					{
					
						mode = true;
					}
					// Only one employee have this name 
					else
					{
						mode = false;
					}
					
				}
			}
		}
		
		/**************** Enter the test name (case sensitive)******************************************/
		
		mode = true;
		while (mode == true)
		{
			System.out.println ("Please enter the test name (case sensitive): ");
			try
			{
				test_name = in.nextLine();
				if (test_name.length()>48)
				{
					System.out.println ("XXXXX The length of test name is over 48, try again, or press 'q' to quit. XXXXX");
					mode = true;
				}
				else if(test_name.equalsIgnoreCase(" ") || test_name.equalsIgnoreCase("") )
				{
					System.out.println ("XXXXX Wrong formation, try again.Or press 'q' to quit. XXXXX");
					mode  = true;
				}
				else if (test_name.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the precription process, and go back to main menu");
					db.close ();
					return;
				}
				else
				{
					//Check if test type has this test based on test name, if no ,return to main menu
					type_id = db.checkTypeId (test_name);
					// if no that test
					if (type_id == -1)
					{
						System.out.println ("XXXXX Sorry no that test in the system, try again, or enter 'q' to quit. XXXXX ");
						mode = true;
					}
					else
					{
						mode = false;
					}
				}
			}
			catch (InputMismatchException ime5)
			{
				System.out.println ("XXXXX Wrong formation, try again.Or press 'q' to quit. XXXXX");
				mode = true;
			}
			
		}
		
		/***************** Enter the health care no within Integer type or patient name********************/
		mode = true;
		while (mode == true)
		{
			System.out.println ("Please enter the health_care_no or name (case insensitive) of the patient: ");
			
			try
			{
				health_care_no = in.nextInt();
				//mode = false;
				// Check health_care_no in the system
				have_health_care_no = db.checkHCN (health_care_no);
				blank = in.nextLine();
				if (have_health_care_no == false)
				{
					System.out.println ("Sorry, no this health care no in the system, try again or press 'q' to quit. ");
					mode = true;
				}
				else
				{
					mode = false;
				}
			}
			catch (InputMismatchException ime7)
			{
				patient_name = in.nextLine();
				if (patient_name.equalsIgnoreCase("q"))
				{
					System.out.println ("Quit the precription process, and go back to the main menu");
					db.close ();
					return;
				}
				else if  (patient_name.length() >48)
				{
					System.out.println ("XXXXX The length of patient name is over 48, try again, or press 'q' to quit.");
					mode = true;
				}
				else if (patient_name.equalsIgnoreCase("") || patient_name.equalsIgnoreCase(" "))
				{
					System.out.println ("XXXXX Wrong formation, try again.Or enter 'q' to quit XXXXX  ");
					mode = true;
				}
				else 
				{
					health_care_no = db.get_hco_by_patient_name(patient_name);
					// No this patient in system
					if (health_care_no == -1)
					{
						System.out.println ("XXXXX Sorry, no this patient in the system, try again, or press 'q' to quit");
						mode = true;
					}
					// Same name, chose which health care no
					else if (health_care_no == -2)
					{
						mode = true;
					}
					else
					{
						mode = false;
					}
				}
				
			}
		}
		
		

		/**************** Join not_allowed and test_type tables**************************/
		
		String sql_2 = "select distinct health_care_no, test_name from not_allowed join test_type on not_allowed.type_id = test_type.type_id order by 1";
		ResultSet join_rs = db.executeQuery (sql_2);
		
		/**************** Scan the result set to check health_care_no and test_name*******************/
		
		while (join_rs.next())
		{
			int rs_health_care_no = join_rs.getInt("health_care_no");
			String rs_test_name = join_rs.getString("test_name");
			
			if (health_care_no == rs_health_care_no)
			{
				if (test_name.equals(rs_test_name))
				{
					// If both match, return to the main menu
					System.out.println ("Sorry, you can not do the test: "+ test_name);
					break;				
				}
				else
				{
					// If no match, then add it to test_record
					// Get a test_id;	
					String sql_3 = "select max(test_id) from test_record";
					boolean result = false;
					ResultSet max_test_id_rs = db.executeQuery(sql_3);
					while (max_test_id_rs.next())
					{
						test_id = max_test_id_rs.getInt("max(test_id)")+1;
					}
					
					/**************** Insert values to test_record***********************/
					db.updateTest_Record(test_id, type_id, health_care_no, emp_no);
				}
			}
			
		}
		
		
		db.close ();
	}
	
	private static void Help() 
	{
		System.out.println("Prescription");
		System.out.println("");
		System.out.println("This application program is used by a doctor to prescribe a medical test to");
		System.out.println("a patient. The program will allow a user to enter the detailed information ");
		System.out.println("about the prescription, including the employee_no or name of the doctor who ");
		System.out.println("prescribes the test, the test name, and the name and/or the health_care_no of");
		System.out.println("the patient. As expected, all the prescriptions conflict to the not_allowed");
		System.out.println("constraints  will be rejected.");

		System.out.println("");
		System.out.println("Medical Test");
		System.out.println("The program allow the user to enter all necessary information to ");
		System.out.println("update test result, includingthe lab name, test date, the test result");
		System.out.println("No lab can conduct a test on a patient without a proper prescription.");
		System.out.println("Therefore, the program will first ask the user to enter the necessary ,");
		System.out.println("information: the name or health care number of the patient,");
		System.out.println("the name or employee number of the doctor who prescribes the test");
		System.out.println("and the test type name or id and the test id o search the database");

		System.out.println("");
		System.out.println("Patient Information Update");
		System.out.println("This component is used to enter the information of a new patient or to update");
		System.out.println("the information of an existing patient. All the information about a patient, ");
		System.out.println("except the health_care_no for an existing patient, may be updated.");

		System.out.println("");
		System.out.println("Search Engine");
		System.out.println("1.List the health_care_no, patient name, test type name, testing date, and test");
		System.out.println("	result of all test records by inputting either a health_care_no or a patient name ");
		System.out.println("	from the user.");
		System.out.println("2.List the health_care_no, patient name, test type name, prescribing date of all");
		System.out.println("	tests prescribed by a given doctor during a specified time period. The user needs to");
		System.out.println("	enter the name or employee_no of the doctor, and the starting and ending dates between");
		System.out.println("	which tests are prescribed.");
		System.out.println("3.Display the health_care_no, name, address, and phone number of all patients, who");
		System.out.println("	have reached the alarming age of the given test type but have never taken a test of");
		System.out.println("	that type by requesting the test type name.");
	}

	
}
