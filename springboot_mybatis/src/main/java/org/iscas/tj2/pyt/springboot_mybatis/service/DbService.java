package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.io.IOException;
import java.io.Reader;
/*import java.sql.SQLException;
import java.sql.Types;*/
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.dao.FuncProjectRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.FunctionMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.PermissionMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.ProjectMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.RoleMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.RolePRMSRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.StructItemMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.StructMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.StructUserRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.TypeMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.TypeUserRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserRoleRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.VarMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.VarProjectRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.FuncProjectRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Function;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Permission;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Role;
import org.iscas.tj2.pyt.springboot_mybatis.domain.RolePRMSRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Struct;
import org.iscas.tj2.pyt.springboot_mybatis.domain.StructItem;
import org.iscas.tj2.pyt.springboot_mybatis.domain.StructUserRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Type;
import org.iscas.tj2.pyt.springboot_mybatis.domain.TypeUserRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.domain.UserRoleRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Var;
import org.iscas.tj2.pyt.springboot_mybatis.domain.VarProjectRelation;
import org.springframework.stereotype.Repository;


//@Service("DbService")
@Repository("DbService")
public class DbService {
	private SqlSessionFactory sessionFactory;//
	
    public DbService() {
		super();
		//SqlSessionFactory sessionFactory;
		Reader reader;
		try {
			reader = Resources.getResourceAsReader("conf.xml");
			// 构建sqlSession的工厂
			this.sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    public User getUserInfo(String strWeixinId) {
    	SqlSession session = sessionFactory.openSession();
    	User user = null;
    	UserMapper mapper = session.getMapper(UserMapper.class);
    	try {
    		user = mapper.selectUserByWeixinId(strWeixinId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}
    	return user;
    }	
    
    public List<Project> getProjectsInfo(String strWeixinId) {
    //public Project getProjectsInfo(String strWeixinId) {
    	SqlSession session = sessionFactory.openSession();
    	List<Project> projects = null;
    	//Project projects = null;
    	ProjectMapper mapper = session.getMapper(ProjectMapper.class);
    	try {
    		projects = mapper.selectProjectsByWeixinId(strWeixinId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return projects;
    }
    
    public Role getRoleInfo(Integer intRoleId) {
    	SqlSession session = sessionFactory.openSession();
    	Role role = null;
    	RoleMapper mapper = session.getMapper(RoleMapper.class);
    	try {
    		//role = mapper.selectByPrimaryKey(intRoleId);
    		role = mapper.selectByIdRole(intRoleId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return role;
    }   
    
    public int createProject(int idUser,Project proj) {
    	if (null == proj) {
    		System.out.println("proj is null");
    		return -1;
    	}
    	SqlSession session = sessionFactory.openSession();
    	int idProject = 0;
    	int idPermission = 0;
    	int idRole = 0;
    	int idRolePRMSRelation = 0;
    	int idUserRoleRelation = 0;
    	int rightPermission = 1;
    	ProjectMapper projectMapper = session.getMapper(ProjectMapper.class);
    	PermissionMapper permissionMapper = session.getMapper(PermissionMapper.class);
    	RoleMapper roleMapper = session.getMapper(RoleMapper.class);
    	RolePRMSRelationMapper rolePRMSRelationMapper = session.getMapper(RolePRMSRelationMapper.class);
    	UserRoleRelationMapper userRoleRelationMapper = session.getMapper(UserRoleRelationMapper.class);
    	try {
    		//插入Project表
    		projectMapper.insertSelective(proj);
    		idProject = proj.getIdProject();
    		if (0 == idProject) {
    			return -1;
    		}   		    		
    		//插入 Permission表
    		Permission permission = new Permission();
    		permission.setIdProject(idProject);
    		permission.setRightPermission(rightPermission);
    		permissionMapper.insertSelective(permission);
    		idPermission = permission.getIdPermission();
    		if (0 == idPermission) {
    			session.rollback();
    			return -2;
    		} 
    		//插入 Role表
       		Role role = new Role();
    		role.setNameRole("creatorOfProject_"+idProject);
    		roleMapper.insertSelective(role);
    		idRole = role.getIdRole();
    		if (0 == idRole) {
    			session.rollback();
    			return -3;
    		} 	
    		//插入 RolePRMSRelation表
      		RolePRMSRelation rolePRMSRelation = new RolePRMSRelation();
    		rolePRMSRelation.setIdPermission(idPermission);
    		rolePRMSRelation.setIdRole(idRole);
    		rolePRMSRelationMapper.insertSelective(rolePRMSRelation);
    		idRolePRMSRelation = rolePRMSRelation.getIdRoleprmsrelation();
    		if (0 == idRolePRMSRelation) {
    			session.rollback();
    			return -4;
    		} 
    		//插入UserRoleRelation
      		UserRoleRelation userRoleRelation = new UserRoleRelation();
    		userRoleRelation.setIdRole(idRole);
    		userRoleRelation.setIdUser(idUser);
    		userRoleRelationMapper.insertSelective(userRoleRelation);
    		idUserRoleRelation = userRoleRelation.getIdUserrolerelation();
    		if (0 == idUserRoleRelation) {
    			session.rollback();
    			return -5;
    		} 
    		session.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return 0;
    }   

    /**
     * 插入一个新用户，返回它的Id_User值
     * @param newUser
     * @return
     * 0:插入未成功
     * 其他：新插入记录的Id_User值
     */
    public int insertNewUser(User newUser) {
    	SqlSession session = sessionFactory.openSession();
    	UserMapper mapper = session.getMapper(UserMapper.class);
    	int intId = 0;
    	try {
    		mapper.insertSelective(newUser);
    		intId = newUser.getId_User();
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}
    	return intId;
    }	

    
    public List<Project> getProjectsInfoByUserId(int intUserId) {
        
        	SqlSession session = sessionFactory.openSession();
        	List<Project> projects = null;
        	//Project projects = null;
        	ProjectMapper mapper = session.getMapper(ProjectMapper.class);
        	try {
        		projects = mapper.selectProjectsByUserId(intUserId);
        		session.commit();
        	} catch (Exception e) {
        		e.printStackTrace();
        		session.rollback();
        	}                 
        	return projects;
    }
    
    //2018-11-20 新增
    public List<Type> getTypesInfoByUserId(int intUserId) {
        
        	SqlSession session = sessionFactory.openSession();
        	List<Type> types = null;
        	
        	TypeMapper mapper = session.getMapper(TypeMapper.class);
        	try {
        		types = mapper.selectTypesByUserId(intUserId);
        		session.commit();
        	} catch (Exception e) {
        		e.printStackTrace();
        		session.rollback();
        	}                 
        	return types;
    }
    
    public List<Struct> getStructsInfoByUserId(int intUserId) {
        
    	SqlSession session = sessionFactory.openSession();
    	List<Struct> structs = null;
    	
    	StructMapper mapper = session.getMapper(StructMapper.class);
    	try {
    		structs = mapper.selectStructsByUserId(intUserId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return structs;
    }
    
    public Project getProjectByProjectId(int intProjectId) {
      
        	SqlSession session = sessionFactory.openSession();
        	Project project = null;
        	
        	ProjectMapper mapper = session.getMapper(ProjectMapper.class);
        	try {
        		project = mapper.selectByPrimaryKey(intProjectId);
        		session.commit();
        	} catch (Exception e) {
        		e.printStackTrace();
        		session.rollback();
        	}                 
        	return project;
    }
    
    //2018-12-02 加入
    public Type getTypeByTypeId(int intTypeId) {
        
    	SqlSession session = sessionFactory.openSession();
    	Type type = null;
    	
    	TypeMapper mapper = session.getMapper(TypeMapper.class);
    	try {
    		type = mapper.selectByPrimaryKey(intTypeId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return type;
    }
    
    //2018-12-02加入
    public Struct getStructByStructId(int intStructId) {
        
    	SqlSession session = sessionFactory.openSession();
    	Struct struct = null;
    	
    	StructMapper mapper = session.getMapper(StructMapper.class);
    	try {
    		struct = mapper.selectByPrimaryKey(intStructId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return struct;
    }
    
    //2018-12-02加入
    public int createStruct(int idUser,Struct struct) {
    	SqlSession session = sessionFactory.openSession();
    	int idStruct = 0;
    	int idStructUserRelation = 0;

    	StructMapper structMapper = session.getMapper(StructMapper.class);
    	StructUserRelationMapper structUserRelationMapper = session.getMapper(StructUserRelationMapper.class);
    	try {
    		//插入Struct表
    		structMapper.insertSelective(struct);
    		idStruct = struct.getIdStruct();
    		if (0 == idStruct) {
    			return -1;
    		}   		    		
    		
    		//插入StructUserRelation
      		StructUserRelation structUserRelation = new StructUserRelation();
    		structUserRelation.setIdStruct(idStruct);
    		structUserRelation.setIdUser(idUser);
    		structUserRelationMapper.insertSelective(structUserRelation);
    		idStructUserRelation = structUserRelation.getIdStructuserrelation();
    		if (0 == idStructUserRelation) {
    			session.rollback();
    			return -2;
    		} 
    		session.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return 0;
    }   


    //2018-12-02加入
    public int createType(int idUser,Type type) {
    	SqlSession session = sessionFactory.openSession();
    	int idType = 0;
    	int idTypeUserRelation = 0;

    	TypeMapper typeMapper = session.getMapper(TypeMapper.class);
    	TypeUserRelationMapper typeUserRelationMapper = session.getMapper(TypeUserRelationMapper.class);
    	try {
    		//插入Type表
    		typeMapper.insertSelective(type);
    		idType = type.getIdType();
    		if (0 == idType) {
    			return -1;
    		}   		    		
    		
    		//插入TypeUserRelation
      		TypeUserRelation typeUserRelation = new TypeUserRelation();
    		typeUserRelation.setIdType(idType);
    		typeUserRelation.setIdUser(idUser);
    		typeUserRelationMapper.insertSelective(typeUserRelation);
    		idTypeUserRelation = typeUserRelation.getIdTypeuserrelation();
    		if (0 == idTypeUserRelation) {
    			session.rollback();
    			return -2;
    		} 
    		session.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return 0;
    }       
    
    //2018-12-03 加入
    public int deleteProjectByProjectId(int intProjectId) {
        
    	SqlSession session = sessionFactory.openSession();
    	int ret = 0;
    	
    	ProjectMapper mapper = session.getMapper(ProjectMapper.class);
    	try {
    		ret = mapper.deleteByPrimaryKey(intProjectId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return ret;
    }
    
    public int deleteTypeByTypeId(int intTypeId) {
        
    	SqlSession session = sessionFactory.openSession();
    	int ret = 0;
    	
    	TypeMapper mapper = session.getMapper(TypeMapper.class);
    	try {
    		ret = mapper.deleteByPrimaryKey(intTypeId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return ret;
    }
    
    public int deleteStructByStructId(int intStructId) {
        
    	SqlSession session = sessionFactory.openSession();
    	int ret = 0;
    	
    	StructMapper mapper = session.getMapper(StructMapper.class);
    	try {
    		ret = mapper.deleteByPrimaryKey(intStructId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return ret;
    }
    
    //2018-12-03 新增
    public List<StructItem> getStructItemsInfoByStructId(int intStructId) {
        
        	SqlSession session = sessionFactory.openSession();
        	List<StructItem> structItems = null;
        	
        	StructItemMapper mapper = session.getMapper(StructItemMapper.class);
        	try {
        		structItems = mapper.selectStructItemsByStructId(intStructId);
        		session.commit();
        	} catch (Exception e) {
        		e.printStackTrace();
        		session.rollback();
        	}                 
        	return structItems;
    }
    
    //2018-12-03加入
    public StructItem getStructItemByStructItemId(int intStructItemId) {
        
    	SqlSession session = sessionFactory.openSession();
    	StructItem struct = null;
    	
    	StructItemMapper mapper = session.getMapper(StructItemMapper.class);
    	try {
    		struct = mapper.selectByPrimaryKey(intStructItemId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return struct;
    }
    
    //2018-12-03加入
    public int createStructItem(StructItem struct) {
    	SqlSession session = sessionFactory.openSession();
    	int idStructItem = 0;
    	

    	StructItemMapper structMapper = session.getMapper(StructItemMapper.class);
    	try {
    		//插入StructItem表
    		structMapper.insertSelective(struct);
    		idStructItem = struct.getIdStructitem();
    		if (0 == idStructItem) {
    			return -1;
    		}   		    		
    		
    		session.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return 0;
    }   
    
   public int deleteStructItemByStructItemId(int intStructItemId) {
        
    	SqlSession session = sessionFactory.openSession();
    	int ret = 0;
    	
    	StructItemMapper mapper = session.getMapper(StructItemMapper.class);
    	try {
    		ret = mapper.deleteByPrimaryKey(intStructItemId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return ret;
    }
   
   public List<Function> getFunctionsInfoByProjectId(int intIdProject) {
       
	   	SqlSession session = sessionFactory.openSession();
	   	List<Function> funcstions = null;
	   	
	   	FunctionMapper mapper = session.getMapper(FunctionMapper.class);
	   	try {
	   		funcstions = mapper.selectFunctionsByProjectId(intIdProject);
	   		session.commit();
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		session.rollback();
	   	}                 
	   	return funcstions;
   }
   
   
   public int createFunction(int idProject,Function function) {
   
	if (null == function) {
   		System.out.println("function is null");
   		return -1;
   	}
   	
   	SqlSession session = sessionFactory.openSession();
   	int idFunction = 0;
   	int idFuncProjectRelation = 0;

   	FunctionMapper functionMapper = session.getMapper(FunctionMapper.class);
   	ProjectMapper roleMapper = session.getMapper(ProjectMapper.class);
   	FuncProjectRelationMapper funcProjectRelationMapper = session.getMapper(FuncProjectRelationMapper.class);
   	try {
   		//插入Function表
   		functionMapper.insertSelective(function);
   		idFunction = function.getIdFunction();
   		if (0 == idFunction) {
   			return -1;
   		}   		    		
 
   		//插入 FuncProjectRelation表
     	FuncProjectRelation funcProjectRelation = new FuncProjectRelation();
   		funcProjectRelation.setIdFunction(idFunction);
   		funcProjectRelation.setIdProject(idProject);
   		funcProjectRelationMapper.insertSelective(funcProjectRelation);
   		idFuncProjectRelation = funcProjectRelation.getIdFuncprojectrelation();
   		if (0 == idFuncProjectRelation) {
   			session.rollback();
   			return -2;
   		} 

   		session.commit();
   		
   	} catch (Exception e) {
   		e.printStackTrace();
   		session.rollback();
   	}                 
   	return 0;
   }   
   
   
   
   public int deleteFunctionByFunctionId(int intFunctionId) {
       
   	SqlSession session = sessionFactory.openSession();
   	int ret = 0;
   	
   	FunctionMapper mapper = session.getMapper(FunctionMapper.class);
   	try {
   		ret = mapper.deleteByPrimaryKey(intFunctionId);
   		session.commit();
   	} catch (Exception e) {
   		e.printStackTrace();
   		session.rollback();
   	}                 
   	return ret;
   }
   
      
   public Function getFunctionByFunctionId(int intFunctionId) {
       
   	SqlSession session = sessionFactory.openSession();
   	Function struct = null;
   	
   	FunctionMapper mapper = session.getMapper(FunctionMapper.class);
   	try {
   		struct = mapper.selectByPrimaryKey(intFunctionId);
   		session.commit();
   	} catch (Exception e) {
   		e.printStackTrace();
   		session.rollback();
   	}                 
   	return struct;
   }
   
   
   /////////////////
   public List<Var> getVarsInfoByProjectId(int intIdProject) {
       
	   	SqlSession session = sessionFactory.openSession();
	   	List<Var> funcstions = null;
	   	
	   	VarMapper mapper = session.getMapper(VarMapper.class);
	   	try {
	   		funcstions = mapper.selectVarsByProjectId(intIdProject);
	   		session.commit();
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		session.rollback();
	   	}                 
	   	return funcstions;
  }
  
  
  public int createVar(int idProject,Var var) {
  
	if (null == var) {
  		System.out.println("var is null");
  		return -1;
  	}
  	
  	SqlSession session = sessionFactory.openSession();
  	int idVar = 0;
  	int idVarProjectRelation = 0;

  	VarMapper varMapper = session.getMapper(VarMapper.class);
  	ProjectMapper roleMapper = session.getMapper(ProjectMapper.class);
  	VarProjectRelationMapper varProjectRelationMapper = session.getMapper(VarProjectRelationMapper.class);
  	try {
  		//插入Var表
  		varMapper.insertSelective(var);
  		idVar = var.getIdVar();
  		if (0 == idVar) {
  			return -1;
  		}   		    		

  		//插入 VarProjectRelation表
    	VarProjectRelation varProjectRelation = new VarProjectRelation();
  		varProjectRelation.setIdVar(idVar);
  		varProjectRelation.setIdProject(idProject);
  		varProjectRelationMapper.insertSelective(varProjectRelation);
  		idVarProjectRelation = varProjectRelation.getIdVarprojectrelation();
  		if (0 == idVarProjectRelation) {
  			session.rollback();
  			return -2;
  		} 

  		session.commit();
  		
  	} catch (Exception e) {
  		e.printStackTrace();
  		session.rollback();
  	}                 
  	return 0;
  }   
  
  
  
  public int deleteVarByVarId(int intVarId) {
      
  	SqlSession session = sessionFactory.openSession();
  	int ret = 0;
  	
  	VarMapper mapper = session.getMapper(VarMapper.class);
  	try {
  		ret = mapper.deleteByPrimaryKey(intVarId);
  		session.commit();
  	} catch (Exception e) {
  		e.printStackTrace();
  		session.rollback();
  	}                 
  	return ret;
  }
  
     
  public Var getVarByVarId(int intVarId) {
      
  	SqlSession session = sessionFactory.openSession();
  	Var struct = null;
  	
  	VarMapper mapper = session.getMapper(VarMapper.class);
  	try {
  		struct = mapper.selectByPrimaryKey(intVarId);
  		session.commit();
  	} catch (Exception e) {
  		e.printStackTrace();
  		session.rollback();
  	}                 
  	return struct;
  }
//类结尾    
}

