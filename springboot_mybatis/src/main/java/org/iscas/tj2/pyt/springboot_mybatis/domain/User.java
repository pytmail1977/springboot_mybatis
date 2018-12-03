package org.iscas.tj2.pyt.springboot_mybatis.domain;

public class User extends UserKey {
	
	
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Account_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String account_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Pwd_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String pwd_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Name_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String name_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Phone_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String phone_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Mark_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private Integer mark_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Email_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String email_User;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column User.Memo_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	private String memo_User;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public User(Integer id_User, String weixinId_User, String account_User, String pwd_User, String name_User,
			String phone_User, Integer mark_User, String email_User, String memo_User) {
		super(id_User, weixinId_User);
		this.account_User = account_User;
		this.pwd_User = pwd_User;
		this.name_User = name_User;
		this.phone_User = phone_User;
		this.mark_User = mark_User;
		this.email_User = email_User;
		this.memo_User = memo_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public User() {
		super();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Account_User
	 * @return  the value of User.Account_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getAccount_User() {
		return account_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Account_User
	 * @param account_User  the value for User.Account_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setAccount_User(String account_User) {
		this.account_User = account_User == null ? null : account_User.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Pwd_User
	 * @return  the value of User.Pwd_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getPwd_User() {
		return pwd_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Pwd_User
	 * @param pwd_User  the value for User.Pwd_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setPwd_User(String pwd_User) {
		this.pwd_User = pwd_User == null ? null : pwd_User.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Name_User
	 * @return  the value of User.Name_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getName_User() {
		return name_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Name_User
	 * @param name_User  the value for User.Name_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setName_User(String name_User) {
		this.name_User = name_User == null ? null : name_User.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Phone_User
	 * @return  the value of User.Phone_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getPhone_User() {
		return phone_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Phone_User
	 * @param phone_User  the value for User.Phone_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setPhone_User(String phone_User) {
		this.phone_User = phone_User == null ? null : phone_User.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Mark_User
	 * @return  the value of User.Mark_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public Integer getMark_User() {
		return mark_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Mark_User
	 * @param mark_User  the value for User.Mark_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setMark_User(Integer mark_User) {
		this.mark_User = mark_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Email_User
	 * @return  the value of User.Email_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getEmail_User() {
		return email_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Email_User
	 * @param email_User  the value for User.Email_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setEmail_User(String email_User) {
		this.email_User = email_User == null ? null : email_User.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column User.Memo_User
	 * @return  the value of User.Memo_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public String getMemo_User() {
		return memo_User;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column User.Memo_User
	 * @param memo_User  the value for User.Memo_User
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	public void setMemo_User(String memo_User) {
		this.memo_User = memo_User == null ? null : memo_User.trim();
	}

	/*自己加的只有四个字段的构造函数
	 * 
	 */
	public User(Integer id_User, String weixinId_User, String account_User, String pwd_User) {
		super(id_User, weixinId_User);
		this.account_User = account_User;
		this.pwd_User = pwd_User;
	}
	
	
}