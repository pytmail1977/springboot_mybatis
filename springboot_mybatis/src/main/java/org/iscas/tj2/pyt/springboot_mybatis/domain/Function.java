package org.iscas.tj2.pyt.springboot_mybatis.domain;

public class Function {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.Id_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private Integer idFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.IdReturnType_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private String idreturntypeFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.Name_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private String nameFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.IsInline_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private Integer isinlineFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.IfBreak_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private Integer ifbreakFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.Id_NameSpace_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private Integer idNamespaceFunction;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column Function.Memo_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	private String memoFunction;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Function(Integer idFunction, String idreturntypeFunction, String nameFunction, Integer isinlineFunction,
			Integer ifbreakFunction, Integer idNamespaceFunction, String memoFunction) {
		this.idFunction = idFunction;
		this.idreturntypeFunction = idreturntypeFunction;
		this.nameFunction = nameFunction;
		this.isinlineFunction = isinlineFunction;
		this.ifbreakFunction = ifbreakFunction;
		this.idNamespaceFunction = idNamespaceFunction;
		this.memoFunction = memoFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Function() {
		super();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.Id_Function
	 * @return  the value of Function.Id_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Integer getIdFunction() {
		return idFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.Id_Function
	 * @param idFunction  the value for Function.Id_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setIdFunction(Integer idFunction) {
		this.idFunction = idFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.IdReturnType_Function
	 * @return  the value of Function.IdReturnType_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public String getIdreturntypeFunction() {
		return idreturntypeFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.IdReturnType_Function
	 * @param idreturntypeFunction  the value for Function.IdReturnType_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setIdreturntypeFunction(String idreturntypeFunction) {
		this.idreturntypeFunction = idreturntypeFunction == null ? null : idreturntypeFunction.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.Name_Function
	 * @return  the value of Function.Name_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public String getNameFunction() {
		return nameFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.Name_Function
	 * @param nameFunction  the value for Function.Name_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setNameFunction(String nameFunction) {
		this.nameFunction = nameFunction == null ? null : nameFunction.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.IsInline_Function
	 * @return  the value of Function.IsInline_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Integer getIsinlineFunction() {
		return isinlineFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.IsInline_Function
	 * @param isinlineFunction  the value for Function.IsInline_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setIsinlineFunction(Integer isinlineFunction) {
		this.isinlineFunction = isinlineFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.IfBreak_Function
	 * @return  the value of Function.IfBreak_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Integer getIfbreakFunction() {
		return ifbreakFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.IfBreak_Function
	 * @param ifbreakFunction  the value for Function.IfBreak_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setIfbreakFunction(Integer ifbreakFunction) {
		this.ifbreakFunction = ifbreakFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.Id_NameSpace_Function
	 * @return  the value of Function.Id_NameSpace_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public Integer getIdNamespaceFunction() {
		return idNamespaceFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.Id_NameSpace_Function
	 * @param idNamespaceFunction  the value for Function.Id_NameSpace_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setIdNamespaceFunction(Integer idNamespaceFunction) {
		this.idNamespaceFunction = idNamespaceFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column Function.Memo_Function
	 * @return  the value of Function.Memo_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public String getMemoFunction() {
		return memoFunction;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column Function.Memo_Function
	 * @param memoFunction  the value for Function.Memo_Function
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	public void setMemoFunction(String memoFunction) {
		this.memoFunction = memoFunction == null ? null : memoFunction.trim();
	}
}