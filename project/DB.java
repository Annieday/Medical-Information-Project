import java.io.*;
import java.sql.*;


public class DB {
	
	private Connector connector;
	
	//Class constructor: initializes the DB connection using the specified Oracle DB. 
	public DB () 
	{
		System.out.println("Opening connection to Oracle DB...");
		connector = new Connector("dbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS", "jzhao9", "!ZzJj21518860317");
		System.out.println("Connection has been opened successfully!");
	}
	
	//Closes the connection
	public void close () 
	{
		connector.closeConnection();
		System.out.println("Connection Closed!");
	}

	
	public ResultSet executeQuery (String sql)
	{
		// Get ResultSet objects
		ResultSet rset = connector.executeQuery(sql);
		return rset;
	}
	
	// Check type_id
	public int checkTypeId (String name) throws SQLException
	{
		int type_id = -1;
		String sql = "select type_id from test_type where test_name = '" +name+"'";
		ResultSet rset = connector.executeQuery(sql);
		while (rset.next())
		{
			type_id = rset.getInt("type_id");
		}
		return type_id;
	}
	
	// Update test record table
	public void updateTest_Record(int test_id, int type_id, int patient_no, int employee_no) throws SQLException 
	{
		String sql_1 = "alter SESSION set NLS_DATE_FORMAT = 'MM/DD/YYYY'";
		connector.executeNonQuery(sql_1);
		String sql_2 = "insert into test_record values("+test_id+","+type_id+","+patient_no+","+employee_no+",NULL,NULL,sysdate,NULL)";
		connector.executeNonQuery(sql_2);
		
		ResultSet rset = connector.executeQuery("select * from test_record where test_id = "+test_id+"");
		System.out.println ("New precription is created successfully.");
		System.out.println ("Test id "+" |"+" Type id"+" |"+" Patient no"+" |"+"Employee no"+" |"+"Prescribe date");
		while (rset.next())
		{
			
			System.out.println (rset.getInt("test_id")+ "       |"+rset.getInt("type_id")+ "       |"+ rset.getInt("patient_no")+ "       |"+ rset.getInt("employee_no") + "         |"+ rset.getString("prescribe_date"));
		}
	
	}
	
	// Check employee no
	public boolean checkEmp_no(int emp_no) throws SQLException 
	{
		//String sql = "select employee_no from doctor where employee_no = "+emp_no";
		int row_num =0;
		ResultSet rset = connector.executeQuery("select employee_no from doctor where employee_no = "+emp_no+"");
		while (rset.next())
		{
			row_num++;
		}
		if (row_num == 0)
			return false;
		else
			return true; 
	}

	// Check health care no
	public boolean checkHCN(int health_care_no) throws SQLException {
		int row_num = 0;
		ResultSet rset = connector.executeQuery("select health_care_no from patient where health_care_no = "+health_care_no+"");
		while (rset.next())
		{
			row_num++;
		}
		if (row_num == 0)
			return false;
		else
			return true; 
	}

	// Display patient information
	public void displayPaitentInfor(int health_care_no) throws SQLException 
	{
		String value = "";
		int i =1;
		ResultSet rset = connector.executeQuery("select * from patient where health_care_no = "+health_care_no+"");
		System.out.println ("health_care_no"+"\t"+"name"+"\t"+"address"+"\t\t\t"+"birth_day"+"\t"+"phone");
		System.out.println ("----------------------------------------------------------------------------");
		while (rset.next())
		{
			value +=rset.getString("health_care_no") +" | "+ rset.getString("name")+ " | "+ rset.getString("address")+ " | "+ rset.getString("birth_day") + " | " +rset.getString("phone");
		}
		System.out.println (value);
		
	}
	
	// Update patient table
	public void updatePatient (int option,int hcn, String update)
	{
		String sql = "";
		if (option == 1)
		{
			sql = "update patient set name = '" +update+"' where health_care_no = "+hcn+"";
			connector.executeNonQuery(sql);
		}
		else if (option ==2)
		{
			sql = "update patient set address = '" +update+"' where health_care_no = "+hcn+"";
			connector.executeNonQuery(sql);
		}
		else if (option ==3)
		{
			sql = "update patient set birth_day = to_date ('"+update+"','MM/DD/YYYY') where health_care_no = "+hcn+"";
			connector.executeNonQuery(sql);
		}
		else if (option ==4)
		{
			sql = "update patient set phone = '"+update+"'  where health_care_no = "+hcn+"";
			connector.executeNonQuery(sql);
		}
	}
	
	// Insert new patient table
	public void insertPatient (int hcn, String name, String address, String bd, String phone) throws SQLException
	{
		String sql;
		String sql_1 = "alter SESSION set NLS_DATE_FORMAT = 'MM/DD/YYYY'";
		connector.executeNonQuery(sql_1);
		if (bd == null)
		{
			sql = "insert into patient values ("+hcn+",'"+name+"','"+address+"',null,'"+phone+"')";
		}
		else
		{
			sql = "insert into patient values ("+hcn+",'"+name+"','"+address+"',to_date('"+bd+"','MM/DD/YYYY'),'"+phone+"')";
		}
		
		connector.executeNonQuery(sql);
		this.displayPaitentInfor(hcn);
	}

	// Insert not allowed table
	public void insertNot_allowed(int health_care_no, int type_id)
	{
		
		String sql = "insert into not_allowed values ("+health_care_no+", "+type_id+")";
		connector.executeNonQuery(sql);
		
	}
	
	// Check if the entered test is in not_allowed table
	public boolean check_patient_in_NotAllowed (int hco) throws SQLException
	{
		String sql = "select health_care_no from not_allowed";
		ResultSet rset = connector.executeQuery(sql);
		while (rset.next())
		{
			if (rset.getInt("health_care_no") == hco)
				return true;
		}
		return false;
	}

	// Insert a new row into not allowed table
	public void updateNot_allowed(int health_care_no, int update_type_id)
	{
		String sql = "update not_allowed set test_id = "+update_type_id+" where health_care_no = "+health_care_no+" ";
		connector.executeNonQuery(sql);
	}

	public int get_emp_no(String doc_name) throws SQLException 
	{
		String sql = "select doctor.employee_no, patient.name from doctor join patient on doctor.health_care_no = patient.health_care_no";
		//db.executeQuery(sql);
		int repeat = 0;
		int emp_num = -1;
		ResultSet rset = connector.executeQuery(sql);
		System.out.println ("");
		
		while (rset.next())
		{
			if (rset.getString("name").equalsIgnoreCase(doc_name))
			{
				repeat ++;
				if (repeat == 1)
				{
					System.out.println ("Employee number: ");
					emp_num = rset.getInt("employee_no");
				}
				if (repeat>=1)
				{
					
					System.out.println (rset.getInt("employee_no"));
				}
			}
		}
		if (repeat == 1)
		{
			return emp_num;
		}
		else if (repeat >1)
		{
			System.out.println ("There are repeated doctor name <"+doc_name +">in the system, please choose the right employee number");
			return -2;
		}
	return -1;
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////ANNI//////////////////////////////////////////////////////////////

	public boolean CheckExistHealthCareNo(int healthcareno) throws SQLException {
		boolean bcheck = false;

		String sql = "select * from patient where health_care_no="
				+ healthcareno + "";

		ResultSet rs = null;

		try {
			rs = connector.executeQuery(sql);

			while (rs.next()) {
				bcheck = true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		return bcheck;
	}
	
	public String GetTestRecordbyHealthcareno(int healthcareno)
			throws SQLException {
		String testrecord = "";
		String sql = "select distinct p.health_care_no,p.name,t.test_name,r.test_date,r.result "
				+ "from patient p,test_type t,test_record r "
				+ "where r.patient_no="
				+ healthcareno
				+ " "
				+ "and t.type_id=r.type_id and r.patient_no=p.health_care_no";

		ResultSet rs = null;

		try {
			rs = connector.executeQuery(sql);

			while (rs.next()) {
				testrecord += "	";
				testrecord += rs.getInt(1);
				testrecord += "		";
				testrecord += rs.getString(2);
				testrecord += "		";
				testrecord += rs.getString(3);
				testrecord += "		";
				testrecord += rs.getDate(4);
				testrecord += "		";
				testrecord += rs.getString(5);
				testrecord += "\n";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		return testrecord;
	}
	
	// gety patient id by patient name
		public int CheckPatientName(String name) throws SQLException {

			String sql = "select name, health_care_no from patient where name='"
					+ name + "'";
			String value = "";
			ResultSet rs = null;
			int check = 0;
			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					check++;
					value += rs.getString("name") + "	|	"
							+ rs.getString("health_care_no");
				}

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if (check != 0) {
				System.out.println("Name		|health_care_no	");
				System.out.println(value);
			}
			return check;
		}
		
	
		public boolean CheckEmployeeNo(int employeeno) throws SQLException {
			boolean bcheck = false;
			String value = "";
			String sql = "select p.name, d.employee_no, d.clinic_address from patient p, doctor d where employee_no= "
					+ employeeno + "and p.health_care_no = d.health_care_no";
			ResultSet rs = null;
			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					bcheck = true;
					value += rs.getString("name") + "		" + rs.getInt("employee_no")
							+ "		" + rs.getString("clinic_address") + "\n";
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			if (bcheck) {
				System.out.println("Name		Doctor Number		Clinic Address");
				System.out.println(value);
			}
			return bcheck;
		}
		
		/*
		 * This part by executing sql query to find all tests prescribed by a given
		 * doctor's employee number
		 */
		public String GetTestPrescribesByNo(int employeeno, String startdate,
				String enddate) throws SQLException {
			String prescribes = "";
			ResultSet rs = null;

			try {

				String sql = "select distinct p.health_care_no,p.name,t.test_name,"
						+ "r.prescribe_date from patient p, test_type t, test_record r "
						+ " where r.employee_no="
						+ employeeno
						+ " and r.prescribe_date>=to_date('"
						+ startdate
						+ "','MM/dd/yyyy')"
						+ " and r.prescribe_date<=to_date('"
						+ enddate
						+ "','MM/dd/yyyy')"
						+ " and r.patient_no =p.health_care_no and t.type_id = r.type_id";

				rs = connector.executeQuery(sql);

				while (rs.next()) {
					prescribes += "	";
					prescribes += rs.getInt(1);
					prescribes += "		";
					prescribes += rs.getString(2);
					prescribes += "		";
					prescribes += rs.getString(3);
					prescribes += "		";
					prescribes += rs.getDate(4);
					prescribes += "\n";
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			return prescribes;
		}
		
		public int CheckEmployeeName(String name) throws SQLException {
			int check = 0;
			String value = "";
			String sql = "select p.name, d.employee_no, d.clinic_address from patient p, doctor d where p.name = '"
					+ name + "' and p.health_care_no = d.health_care_no";
			ResultSet rs = null;
			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					check++;
					value += rs.getString("name") + "		" + rs.getInt("employee_no")
							+ "		" + rs.getString("clinic_address") + "\n";
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			if (check != 0) {
				System.out.println("Name		Doctor Number		Clinic Address");
				System.out.println(value);
			}
			return check;
		}
		
		// get test_id by test_name
		public int GetTypeIdbyName(String name) throws SQLException {
			int Type_Id = -1;

			String sql = "select * from test_type where test_name='" + name + "'";
			ResultSet rs = null;
			String strid;

			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					strid = rs.getString(1);
					if (strid != null) {
						Type_Id = Integer.parseInt(strid);
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			return Type_Id;
		}
		
		public boolean CheckTypeId(int typeid) throws SQLException {

			String sql = "select * from test_type where type_id=" + typeid + "";

			ResultSet rs = null;
			boolean bcheck = false;
			try {
				rs = connector.executeQuery(sql);

				while (rs.next())
					bcheck = true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {

					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			return bcheck;
		}
		
		public String GetTestNamebyTypeId(int typeid) throws SQLException {
			String name = null;
			String sql = "select * from test_type where type_id=" + typeid + "";
			ResultSet rs = null;

			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					name = rs.getString(2);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			return name;
		}
		
		/*
		 * This part by executing sql query to find people who have reached the
		 * alarming age of the given test type but have never taken a test of that
		 * type by requesting the test type name.
		 */
		public String GetAlarmAgePatient(int typeid) throws SQLException {
			ResultSet rs = null;
			int i = 0;
			String ListPatient = "";
			try {
				String sql1 = " SELECT DISTINCT p.health_care_no,p.name,p.address,p.phone,m.alarming_age FROM patient p,"
						+ "(SELECT c1.type_id medical_type,min(c1.age) alarming_age "
						+ "FROM (SELECT t1.type_id, count(distinct t1.patient_no)/count(distinct t2.patient_no) ab_rate "
						+ "FROM test_record t1, test_record t2 "
						+ "WHERE t1.result <> 'normal' AND t1.type_id = t2.type_id AND t1.result IS NOT NULL "
						+ "AND t1.medical_lab IS NOT NULL AND t1.test_date IS NOT NULL "
						+ "AND t2.result IS NOT NULL AND t2.medical_lab IS NOT NULL AND t2.test_date IS NOT NULL "
						+ "GROUP BY t1.type_id "
						+ ") r, "
						+ "(SELECT t1.type_id,age,COUNT(distinct p1.health_care_no) AS ab_cnt "
						+ "FROM     patient p1,test_record t1, "
						+ "(SELECT DISTINCT trunc(months_between(sysdate,p1.birth_day)/12) AS age FROM patient p1) "
						+ "WHERE trunc(months_between(sysdate,p1.birth_day)/12)>=age "
						+ "AND p1.health_care_no=t1.patient_no "
						+ "AND t1.result<>'normal' AND  t1.result IS NOT NULL AND t1.medical_lab IS NOT NULL AND t1.test_date IS NOT NULL "
						+ "GROUP BY age,t1.type_id "
						+ ") c1, "
						+ "(SELECT  t1.type_id,age,COUNT(distinct p1.health_care_no) AS cnt "
						+ "FROM patient p1, test_record t1, "
						+ "(SELECT DISTINCT trunc(months_between(sysdate,p1.birth_day)/12) AS age FROM patient p1) "
						+ "WHERE trunc(months_between(sysdate,p1.birth_day)/12)>=age "
						+ "AND p1.health_care_no=t1.patient_no AND t1.type_id = "
						+ typeid
						+ " AND t1.result IS NOT NULL "
						+ "AND t1.medical_lab IS NOT NULL AND t1.test_date IS NOT NULL "
						+ "GROUP BY age,t1.type_id "
						+ ") c2 "
						+ "WHERE  c1.age = c2.age AND c1.type_id = c2.type_id AND c1.type_id = r.type_id "
						+ "AND c1.ab_cnt/c2.cnt>=2*r.ab_rate "
						+ "GROUP BY c1.type_id,ab_rate) m "
						+ "WHERE trunc(months_between(sysdate,p.birth_day)/12) >= m.alarming_age "
						+ "AND p.health_care_no NOT IN (SELECT patient_no "
						+ "FROM  test_record t WHERE m.medical_type = t.type_id AND t.type_id ="
						+ typeid + ")";


				rs = connector.executeQuery(sql1);

				while (rs.next()) {
					ListPatient += "	";

					for (i = 1; i < 5; i++) {
						ListPatient += rs.getString(i);
						ListPatient += "	";
					}

					ListPatient += "\n";
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} 
			return ListPatient;
		}
		

public boolean UpdateTestResult(int testid, String medicallab,String result, String testdate) throws SQLException
{
			boolean bsuccess = false;

			ResultSet rs = null;

			try {

				String sql = "update test_record set medical_lab= '" + medicallab
						+ "',result='" + result + "',test_date = to_date('"
						+ testdate + "','MM/dd/yyyy')" + " where test_id = "
						+ testid + "";
				rs = connector.executeQuery(sql);

				bsuccess = true;
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			return bsuccess;

}
public boolean CheckCanConduct(int typeid, String labname) 
{
			boolean bcheck = false;
			String sql = "select * from can_conduct where type_id =" + typeid
					+ " and lab_name ='" + labname + "'";
			ResultSet rs = null;
			try {
				rs = connector.executeQuery(sql);

				while (rs.next()) {
					bcheck = true;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			return bcheck;
}

public boolean CheckLabbyName(String labname) throws SQLException {
	boolean bcheck = false;
	String sql = "select * from medical_lab where lab_name='" + labname
			+ "'";
	ResultSet rs = null;

	try {
		rs = connector.executeQuery(sql);

		while (rs.next()) {
			bcheck = true;
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	return bcheck;
}

public boolean CheckIfTested(int testid) throws SQLException {
	boolean bcheck = false;
	String sql = "select result from test_record where test_id=" + testid
			+ " and result is NULL";
	ResultSet rs = null;

	try {
		rs = connector.executeQuery(sql);

		while (rs.next()) {
			bcheck = true;
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	return bcheck;
}

public int GetEmployeeNobyName(String name) throws SQLException {
	int employeeno = -1;
	String sql = "select d.employee_no from patient p, doctor d where p.name = '"
			+ name + "' and p.health_care_no = d.health_care_no";
	ResultSet rs = null;
	String strid;
	try {
		rs = connector.executeQuery(sql);
		while (rs.next()) {
			strid = rs.getString(1);
			if (strid != null) {
				employeeno = Integer.parseInt(strid);
			}
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	return employeeno;
}

/*Get health care number by given patient's name*/
public int GetHealthcarenobyName(String name) throws SQLException {
	int healthcareno = -1;

	String sql = "select health_care_no from patient where name='" + name+ "'";
			

	ResultSet rs = null;

	try {
		rs = connector.executeQuery(sql);

		while (rs.next()) {
			healthcareno = rs.getInt(1);
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	return healthcareno;
}

public boolean CheckPrescriptedTest(int employeeno, int healthcareno,
		int typeid, int testid) throws SQLException {
	boolean bcheck = false;
	String sql = "select * from test_record where test_id=" + testid
			+ " and type_id=" + typeid + " and patient_no =" + healthcareno
			+ " and employee_no =" + employeeno + "";
	ResultSet rs = null;

	try {
		rs = connector.executeQuery(sql);

		while (rs.next()) {
			bcheck = true;
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	return bcheck;
}

public int get_hco_by_patient_name(String patient_name) throws SQLException 
{
	String sql = "select health_care_no, name from patient";
	//db.executeQuery(sql);
	int repeat = 0;
	int emp_num = -1;
	ResultSet rset = connector.executeQuery(sql);
	System.out.println ("");
	
	while (rset.next())
	{
		if (rset.getString("name").equalsIgnoreCase(patient_name))
		{
			repeat ++;
			if (repeat == 1)
			{
				System.out.println ("Health care number: ");
				emp_num = rset.getInt("health_care_no");
			}
			if (repeat>=1)
			{
				
				System.out.println (rset.getInt("health_care_no"));
			}
		}
	}
	if (repeat == 1)
	{
		return emp_num;
	}
	else if (repeat >1)
	{
		System.out.println ("There are repeated patient name <"+patient_name +">in the system, please choose the right health care number");
		return -2;
	}
return -1;	

}

}
