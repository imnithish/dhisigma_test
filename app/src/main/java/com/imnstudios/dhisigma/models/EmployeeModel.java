package com.imnstudios.dhisigma.models;

import java.io.Serializable;

public class EmployeeModel implements Serializable {
    int employeeCode;
    int employeeCodeDesc;
    String employeeName;
    String employeeImageUrl;

    public EmployeeModel(int employeeCode,int employeeCodeDesc, String employeeName, String employeeImageUrl) {
        this.employeeCode = employeeCode;
        this.employeeCodeDesc = employeeCodeDesc;
        this.employeeName = employeeName;
        this.employeeImageUrl = employeeImageUrl;
    }

    public EmployeeModel() {

    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public int getEmployeeCodeDesc() {
        return employeeCodeDesc;
    }

    public void setEmployeeCodeDesc(int employeeCodeDesc) {
        this.employeeCodeDesc = employeeCodeDesc;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }
}
