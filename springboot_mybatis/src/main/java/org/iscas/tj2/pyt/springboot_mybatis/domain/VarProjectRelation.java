package org.iscas.tj2.pyt.springboot_mybatis.domain;

public class VarProjectRelation {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column VarProjectRelation.Id_VarProjectRelation
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	private Integer idVarprojectrelation;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column VarProjectRelation.Id_Var
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	private Integer idVar;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column VarProjectRelation.Id_Project
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	private Integer idProject;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarProjectRelation
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public VarProjectRelation(Integer idVarprojectrelation, Integer idVar, Integer idProject) {
		this.idVarprojectrelation = idVarprojectrelation;
		this.idVar = idVar;
		this.idProject = idProject;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarProjectRelation
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public VarProjectRelation() {
		super();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column VarProjectRelation.Id_VarProjectRelation
	 * @return  the value of VarProjectRelation.Id_VarProjectRelation
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public Integer getIdVarprojectrelation() {
		return idVarprojectrelation;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column VarProjectRelation.Id_VarProjectRelation
	 * @param idVarprojectrelation  the value for VarProjectRelation.Id_VarProjectRelation
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public void setIdVarprojectrelation(Integer idVarprojectrelation) {
		this.idVarprojectrelation = idVarprojectrelation;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column VarProjectRelation.Id_Var
	 * @return  the value of VarProjectRelation.Id_Var
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public Integer getIdVar() {
		return idVar;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column VarProjectRelation.Id_Var
	 * @param idVar  the value for VarProjectRelation.Id_Var
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public void setIdVar(Integer idVar) {
		this.idVar = idVar;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column VarProjectRelation.Id_Project
	 * @return  the value of VarProjectRelation.Id_Project
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public Integer getIdProject() {
		return idProject;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column VarProjectRelation.Id_Project
	 * @param idProject  the value for VarProjectRelation.Id_Project
	 * @mbg.generated  Mon Dec 03 18:53:01 CST 2018
	 */
	public void setIdProject(Integer idProject) {
		this.idProject = idProject;
	}
}