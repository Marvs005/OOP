import static java.lang.Character.getName;

public class Employee {
    private String name;
    private int employeeId;

    private String birthday;

    private String status;

    private String address;

    private String phone_no;

    private String sss_no;

    private String pagibig_no;

    private String philhealth_no;

    private String tin_no;

    private int positionID;

    private String immediate_supervisor;



    public Employee(String name, String birthday, String status, String address, String phone_no, String sss_no, String pagibig_no, String philhealth_no, String tin_no, String immediate_supervisor) {
        this.name = name;
        this.employeeId = employeeId;
        this.birthday = birthday;
        this.status = status;
        this.address = address;
        this.phone_no = phone_no;
        this.sss_no = sss_no;
        this.pagibig_no = pagibig_no;
        this.philhealth_no = philhealth_no;
        this.tin_no = tin_no;
        this.positionID = positionID;
    }

    public Employee(String name, int employeeId) {
    }

    public String getName() {
        return name;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getSss_no() {
        return sss_no;
    }

    public String getPagibig_no() {
        return pagibig_no;
    }

    public String getPhilhealth_no() {
        return philhealth_no;
    }

    public String getTin_no() {
        return tin_no;
    }

    public int getPositionID() {
        return positionID;
    }

    public String getImmediate_supervisor() {
        return immediate_supervisor;
    }

     public void calculateSalary() {
        System.out.println("Employee Calculations: " + name);
    }

    public void printPayslip() {
        System.out.println("Employee Payslip: " + name);
    }

}

class FullTimeEmployee extends Employee {
    private double monthlyRate;
    private double dailyRate;
    private int daysWorked;
    private double overtimeRate;
    private double grossPay;
    private int diductionID;
    private int allowance;
    private double netPay;


    public FullTimeEmployee(String name, int employeeId, double monthlyRate, int dailyRate, int daysWorked, double overtimeRate, double grossPay, int diductionID, int allowance, double netPay) {
        super(name, employeeId);
        this.monthlyRate = monthlyRate;
        this.dailyRate = dailyRate;
        this.daysWorked = daysWorked;
        this.overtimeRate = overtimeRate;
        this.grossPay = grossPay;
        this.diductionID = diductionID;
        this.allowance = allowance;
        this.netPay = netPay;
    }


    public void calculateSalary() {
        System.out.println("Monthly Salary Calculation of the Full-time Employee: " + getName());
    }

    public void printPayslip() {
        System.out.println("Printing payslip for full-time employee: " + getName());
    }

}

class ContractEmployee extends Employee {
    private int contractAmount;

    public ContractEmployee(String name, int employeeId, int contractAmount) {
        super(name, employeeId);
        this.contractAmount = contractAmount;
    }

    @Override
    public void calculateSalary() {
        System.out.println("Calculating salary for contract employee: " + getName());
    }


}

public class Main {

    public static void main(String[] args) {
        System.out.println(" Name of the Employee.: "+ getName() "");


        fullTimeEmployee.calculateSalary();
        contractEmployee.calculateSalary();

        fullTimeEmployee.printPayslip();
        contractEmployee.printPayslip();
    }

    public static String getName() {
        return name;
    }

    }
