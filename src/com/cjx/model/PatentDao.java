package com.cjx.model;

import java.sql.Date;

public class PatentDao {

	private String applyNum;
	private Date applyDate;
	private String pttName;
	private String pttNum;
	private Date pttDate;
	private String pttMainClassNum;
	private String pttClassNum;
	private String proposer;
	private String proposerAddress;
	private String inventor;
	private String pttAgencyOrg;
	private String pttAgencyPerson;
	private String pttAbstract;
	private String classNumG06Q;
	private String internationalApply;
	private String internationalPublication;
	private Date intoDate;
	private String pttType;
	private String content;
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getPttType() {
		return pttType;
	}
	public void setPttType(String pttType) {
		this.pttType = pttType;
	}
	public String getInternationalApply() {
		return internationalApply;
	}
	public void setInternationalApply(String internationalApply) {
		this.internationalApply = internationalApply;
	}
	public String getInternationalPublication() {
		return internationalPublication;
	}
	public void setInternationalPublication(String internationalPublication) {
		this.internationalPublication = internationalPublication;
	}
	public Date getIntoDate() {
		return intoDate;
	}
	public void setIntoDate(Date intoDate) {
		this.intoDate = intoDate;
	}
	public String getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getPttName() {
		return pttName;
	}
	public void setPttName(String pttName) {
		this.pttName = pttName;
	}
	public String getPttNum() {
		return pttNum;
	}
	public void setPttNum(String pttNum) {
		this.pttNum = pttNum;
	}
	public Date getPttDate() {
		return pttDate;
	}
	public void setPttDate(Date pttDate) {
		this.pttDate = pttDate;
	}
	public String getPttMainClassNum() {
		return pttMainClassNum;
	}
	public void setPttMainClassNum(String pttMainClassNum) {
		this.pttMainClassNum = pttMainClassNum;
	}
	public String getPttClassNum() {
		return pttClassNum;
	}
	public void setPttClassNum(String pttClassNum) {
		this.pttClassNum = pttClassNum;
		
		this.classNumG06Q = getG06QClass(pttClassNum);
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public String getProposerAddress() {
		return proposerAddress;
	}
	public void setProposerAddress(String proposerAddress) {
		this.proposerAddress = proposerAddress;
	}
	public String getInventor() {
		return inventor;
	}
	public void setInventor(String inventor) {
		this.inventor = inventor;
	}
	public String getPttAgencyOrg() {
		return pttAgencyOrg;
	}
	public void setPttAgencyOrg(String pttAgencyOrg) {
		this.pttAgencyOrg = pttAgencyOrg;
	}
	public String getPttAgencyPerson() {
		return pttAgencyPerson;
	}
	public void setPttAgencyPerson(String pttAgencyPerson) {
		this.pttAgencyPerson = pttAgencyPerson;
	}
	public String getPttAbstract() {
		return pttAbstract;
	}
	public void setPttAbstract(String pttAbstract) {
		this.pttAbstract = pttAbstract;
	}
	public String getClassNumG06Q() {
		return classNumG06Q;
	}
	public void setClassNumG06Q(String classNumG06Q) {
		this.classNumG06Q = classNumG06Q;
	}
	
	private String getG06QClass(String classStr)
	{
		String[] ss = classStr.split(";");
		
		int len = ss.length;
		for(int i=0; i<len; i++)
		{
			if(ss[i].indexOf("G06Q") != -1)
			{
				return ss[i];
			}
		}
		
		return "";
	}
	
}
