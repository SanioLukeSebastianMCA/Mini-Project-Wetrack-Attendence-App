package com.sanioluke00.wetrack.DataModels;

import com.google.firebase.firestore.DocumentReference;

public class EmployeeModel {
    DocumentReference company_path;
    String emp_name, emp_contact, empCountryCode;
    boolean emp_status;

    public EmployeeModel(DocumentReference company_path, String emp_name, String emp_contact, String empCountryCode, boolean emp_status) {
        this.company_path = company_path;
        this.emp_name = emp_name;
        this.emp_contact = emp_contact;
        this.empCountryCode= empCountryCode;
        this.emp_status = emp_status;
    }

    public String getEmpCountryCode() {
        return empCountryCode;
    }

    public void setEmpCountryCode(String empCountryCode) {
        this.empCountryCode = empCountryCode;
    }

    public DocumentReference getCompany_path() {
        return company_path;
    }

    public void setCompany_path(DocumentReference company_path) {
        this.company_path = company_path;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_contact() {
        return emp_contact;
    }

    public void setEmp_contact(String emp_contact) {
        this.emp_contact = emp_contact;
    }

    public boolean isEmp_status() {
        return emp_status;
    }

    public void setEmp_status(boolean emp_status) {
        this.emp_status = emp_status;
    }
}
