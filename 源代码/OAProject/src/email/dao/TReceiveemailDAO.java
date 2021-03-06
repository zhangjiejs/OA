package email.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import email.tools.PageSupport;

import pojo.TReceiveemail;
import pojo.TReceiveemailId;

/**
 	* A data access object (DAO) providing persistence and search support for TReceiveemail entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see pojo.TReceiveemail
  * @author MyEclipse Persistence Tools 
 */

public class TReceiveemailDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(TReceiveemailDAO.class);
		//property constants



	protected void initDao() {
		//do nothing
	}
    
    public void save(TReceiveemail transientInstance) {
        log.debug("saving TReceiveemail instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    public void update(TReceiveemail transientInstance) {
        log.debug("saving TReceiveemail instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TReceiveemail persistentInstance) {
        log.debug("deleting TReceiveemail instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public void deleteByEmailId(int emailId) {
        log.debug("deleting TReceiveemail instance");
        try {
            getHibernateTemplate().deleteAll(findByEmailId(emailId));
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	//根据邮件id  获得发件人用户名
	public String getSendUserName(int emailId) {
        log.debug("deleting TReceiveemail instance");
        try {
        	String hql = "select id.username from TReceiveemail where id.email.id=? and issend=?";
        	System.out.println("111111");
        	System.out.println(getHibernateTemplate().find(hql, emailId,true));
        	String name = (String)getHibernateTemplate().find(hql, emailId,true).listIterator().next();
        	System.out.println("22222");
        	log.debug("delete successful");
            return name;
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	public TReceiveemail findById(int emailId,String userName) {
        log.debug("deleting TReceiveemail instance");
        try {
        	String hql = "from TReceiveemail where id.email.id=? and id.username=?";
        	TReceiveemail receiveemail = (TReceiveemail)getHibernateTemplate().find(hql, emailId,userName).listIterator().next();
            log.debug("delete successful");
            return receiveemail;
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	public List<TReceiveemail> findByEmailId(int emailId) {
        log.debug("deleting TReceiveemail instance");
        try {
        	String hql = "from TReceiveemail where id.email.id=?";
        	List<TReceiveemail> instance = getHibernateTemplate().find(hql, emailId);
        	
            log.debug("delete successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
//获取邮件列表列表    
    public List<TReceiveemail> findByUserName(final String userName,int emailmode,int isread,int isdel,int issend,final PageSupport pageSupport) {
        log.debug("getting List<TReceiveemail>  with userName: " + userName);
        try {
        	String sql = "from TReceiveemail where id.username=?";
        	if(emailmode >= 0){
        		sql += " and id.email.emailmode=" + (emailmode==0?false:true);
        	}
        	if(isread >= 0){
        		sql += " and isread=" + (isread==0?false:true);
        	}
        	if(isdel >= 0){
        		sql += " and isdel=" + (isdel==0?false:true);
        	}
        	if(issend >= 0){
        		sql += " and issend=" + (issend==0?false:true);
        	}
        	if(isread == -1 && issend == -1 && emailmode == -1){
        		sql += " and (id.email.emailmode=true or issend=true)";
        	} 
        	sql += " order by id.email.senddate desc";
        	final String hql = sql;
        	pageSupport.setTotalCount(totalCount(userName, emailmode, isread, isdel, issend));
        	List<TReceiveemail> receiveEmails;
        	receiveEmails = getHibernateTemplate().executeFind(new HibernateCallback< List<TReceiveemail>>() {

				@Override
				public  List<TReceiveemail> doInHibernate(Session session) throws HibernateException,
						SQLException {
				       Query query = session.createQuery(hql);
				       query.setParameter(0, userName);
				       query.setFirstResult(pageSupport.getMinRownum());//
				       query.setMaxResults(pageSupport.getPageSize());//
				       List<TReceiveemail> list = query.list();					
					return list;
				}
			});

        	
        	
            return receiveEmails;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    //获取收件总数
    public Integer totalCount(String userName,int emailmode,int isread,int isdel,int issend){
    	log.debug("getting List<TReceiveemail>  with userName: " + userName);
        try {
        	String hql = "select count(*) from TReceiveemail where id.username=?";
        	if(emailmode >= 0){
        		hql += " and id.email.emailmode=" + (emailmode==0?false:true);
        	}
        	if(isread >= 0){
        		hql += " and isread=" + (isread==0?false:true);
        	}
        	if(isdel >= 0){
        		hql += " and isdel=" + (isdel==0?false:true);
        	}
        	if(issend >= 0){
        		hql += " and issend=" + (issend==0?false:true);
        	}
        	if(isread == -1 && issend == -1 && emailmode == -1){
        		hql += " and (id.email.emailmode=true or issend=true)";
        	}
        	
        	int totalCount = ((Long)getHibernateTemplate().find(hql,userName).listIterator().next()).intValue();
            return totalCount;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    	
    }
    
 /*   public void test(){
    		String[] s = new String[]{"111","222"}; 
        	String hql = "from TReceiveemail where id.susername in (";
        	for(int i = 0;i<s.length;i++){
        		if(i != s.length-1){
        			hql += "?,";
        		}else{
        			hql += "?)";
        		}
        
        	}
        	List<TReceiveemail>  list = getHibernateTemplate().find(hql, s);
        	System.out.println(list.size());
     
    }*/
    /*
    
    public List findByExample(TReceiveemail instance) {
        log.debug("finding TReceiveemail instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TReceiveemail instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TReceiveemail as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByNisread(Object nisread
	) {
		return findByProperty(NISREAD, nisread
		);
	}
	
	public List findByNisdel(Object nisdel
	) {
		return findByProperty(NISDEL, nisdel
		);
	}
	

	public List findAll() {
		log.debug("finding all TReceiveemail instances");
		try {
			String queryString = "from TReceiveemail";
		 	return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public TReceiveemail merge(TReceiveemail detachedInstance) {
        log.debug("merging TReceiveemail instance");
        try {
            TReceiveemail result = (TReceiveemail) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TReceiveemail instance) {
        log.debug("attaching dirty TReceiveemail instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TReceiveemail instance) {
        log.debug("attaching clean TReceiveemail instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TReceiveemailDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TReceiveemailDAO) ctx.getBean("TReceiveemailDAO");
	}*/
   /* public static void main(String[] args) {
    	TReceiveemailDAO dao = (TReceiveemailDAO)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("TReceiveemailDAO");
		List<TReceiveemail> list = dao.findByUserName("222",false);
		TReceiveemail receiveemail = list.get(0);
		System.out.println(receiveemail.getId().getSusername() + ":" + receiveemail.getId().getEmail().getSenduser());
    	dao.test();
	}*/
}