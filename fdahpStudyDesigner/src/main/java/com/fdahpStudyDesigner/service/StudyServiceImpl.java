package com.fdahpStudyDesigner.service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fdahpStudyDesigner.bean.StudyListBean;
import com.fdahpStudyDesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpStudyDesigner.bo.ConsentInfoBo;
import com.fdahpStudyDesigner.bo.EligibilityBo;
import com.fdahpStudyDesigner.bo.ReferenceTablesBo;
import com.fdahpStudyDesigner.bo.StudyBo;
import com.fdahpStudyDesigner.bo.StudyPageBo;
import com.fdahpStudyDesigner.bo.StudySequenceBo;
import com.fdahpStudyDesigner.dao.StudyDAO;
import com.fdahpStudyDesigner.util.SessionObject;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerConstants;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerUtil;

/**
 * 
 * @author Ronalin
 *
 */
@Service
public class StudyServiceImpl implements StudyService{

	private static Logger logger = Logger.getLogger(StudyServiceImpl.class);
	private StudyDAO studyDAO;

	/**
	 * Setting DI
	 * @param studyDAO
	 */
    @Autowired
	public void setStudyDAO(StudyDAO studyDAO) {
		this.studyDAO = studyDAO;
	}





    /**
	 * return study List based on user 
	 * @author Ronalin
	 * 
	 * @param userId of the user
	 * @return the Study list
	 * @exception Exception
	 */
	@Override
	public List<StudyListBean> getStudyList(Integer userId) throws Exception {
		logger.info("StudyServiceImpl - getStudyList() - Starts");
		List<StudyListBean> studyBos = null;
		try {
			if(userId!=null && userId!=0){
				studyBos  = studyDAO.getStudyList(userId);
			} 
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudyList() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - getStudyList() - Ends");
		return studyBos;
	}



	/**
	 * return reference List based on category
	 * @author Ronalin
	 * 
	 * @return the reference table List
	 * @exception Exception
	 */
	@Override
	public HashMap<String, List<ReferenceTablesBo>> getreferenceListByCategory() {
		logger.info("StudyServiceImpl - getreferenceListByCategory() - Starts");
		HashMap<String, List<ReferenceTablesBo>> referenceMap = null;
		try {
			referenceMap  = studyDAO.getreferenceListByCategory();
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudyList() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - getreferenceListByCategory() - Ends");
		return referenceMap;
	}




	/**
	 * return Study based on id
	 * @author Ronalin
	 * 
	 * @return StudyBo
	 * @exception Exception
	 */
	@Override
	public StudyBo getStudyById(String studyId) {
		logger.info("StudyServiceImpl - getStudyById() - Starts");
		StudyBo studyBo = null;
		try {
			studyBo  = studyDAO.getStudyById(studyId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudyById() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - getStudyById() - Ends");
		return studyBo;
	}

	/**
	 * @author Ronalin
	 * Add/Update the Study
	 * @param StudyBo , {@link StudyBo}
	 * @return {@link String}
	 */
	@Override
	public String saveOrUpdateStudy(StudyBo studyBo) throws Exception {
		logger.info("StudyServiceImpl - saveOrUpdateStudy() - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		StudySequenceBo studySequenceBo = null;
		try {
			if(StringUtils.isNotEmpty(studyBo.getType())){
				if(studyBo.getType().equalsIgnoreCase(fdahpStudyDesignerConstants.STUDY_TYPE_GT)){
					studyBo.setType(fdahpStudyDesignerConstants.STUDY_TYPE_GT);
				}else if(studyBo.getType().equalsIgnoreCase(fdahpStudyDesignerConstants.STUDY_TYPE_SD)){
					studyBo.setType(fdahpStudyDesignerConstants.STUDY_TYPE_SD);
				}
			}
			if(studyBo.getStudyPermissions()!=null && studyBo.getStudyPermissions().size()>0){
			}
			message = studyDAO.saveOrUpdateStudy(studyBo);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - saveOrUpdateStudy() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - saveOrUpdateStudy() - Ends");
		return message;
	}




	/**
	 * return false or true of deleting record of studyPermission based on studyId and userId
	 * @author Ronalin
	 * 
	 * @return boolean
	 * @exception Exception
	 */
	@Override
	public boolean deleteStudyPermissionById(Integer userId, String studyId) {
		logger.info("StudyServiceImpl - deleteStudyPermissionById() - Starts");
		boolean delFlag = false;
		try {
			delFlag = studyDAO.deleteStudyPermissionById(userId, studyId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - deleteStudyPermissionById() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - deleteStudyPermissionById() - Ends");
		return delFlag;
	}




	/**
	 * return false or true of adding record of studyPermission based on studyId and userId
	 * @author Ronalin
	 * 
	 * @return boolean
	 * @exception Exception
	 */
	@Override
	public boolean addStudyPermissionByuserIds(Integer userId, String studyId, String userIds) throws Exception {
		logger.info("StudyServiceImpl - addStudyPermissionByuserIds() - Starts");
		boolean delFlag = false;
		try {
			delFlag = studyDAO.addStudyPermissionByuserIds(userId, studyId, userIds);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - addStudyPermissionByuserIds() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - addStudyPermissionByuserIds() - Ends");
		return delFlag;
	}

	 /**
		 * return study overview pageList based on studyId 
		 * @author Ronalin
		 * 
		 * @param studyId of the StudyBo
		 * @return the Study list
		 * @exception Exception
	*/
	@Override
	public List<StudyPageBo> getOverviewStudyPagesById(String studyId) throws Exception {
		logger.info("StudyServiceImpl - getOverviewStudyPagesById() - Starts");
		List<StudyPageBo> studyPageBos = null;
		try {
			 studyPageBos = studyDAO.getOverviewStudyPagesById(studyId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getOverviewStudyPagesById() - ERROR " , e);
		}
		return studyPageBos;
	}

	/**
	 * @author Ronalin
	 * delete the Study Overview Page By Page Id
	 * @param studyId ,pageId
	 * @return {@link String}
	 */
	@Override
	public String deleteOverviewStudyPageById(String studyId, String pageId) throws Exception {
		logger.info("StudyServiceImpl - deleteOverviewStudyPageById() - Starts");
		String message = "";
		try {
			message = studyDAO.deleteOverviewStudyPageById(studyId, pageId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - deleteOverviewStudyPageById() - ERROR " , e);
		}
		return message;
	}




	/**
	 * @author Ronalin
	 * save the Study Overview Page By PageId
	 * @param studyId
	 * @return {@link Integer}
	 */
	@Override
	public Integer saveOverviewStudyPageById(String studyId) throws Exception {
		logger.info("StudyServiceImpl - saveOverviewStudyPageById() - Starts");
		Integer pageId = 0;
		try {
			pageId = studyDAO.saveOverviewStudyPageById(studyId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - saveOverviewStudyPageById() - ERROR " , e);
		}
		return pageId;
	}




	/**
	 * @author Ronalin
	 * Add/Update the Study Overview Pages
	 * @param studyId ,pageIds,titles,descs,files {@link StudyBo}
	 * @return {@link String}
	 */
	@Override
	public String saveOrUpdateOverviewStudyPages(String studyId, String pageIds, String titles, String descs,
			List<MultipartFile> files) {
		logger.info("StudyServiceImpl - saveOrUpdateOverviewStudyPages() - Starts");
		String message = "";
		try {
			message = studyDAO.saveOrUpdateOverviewStudyPages(studyId, pageIds, titles, descs, files);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - saveOrUpdateOverviewStudyPages() - ERROR " , e);
		}
		return message;
	}

	/**
	 * @author Ravinder
	 * @param Integer : studyId
	 * @return List :ConsentInfoList
	 *  This method used to get the consent info list of an study
	 */
	@Override
	public List<ConsentInfoBo> getConsentInfoList(Integer studyId) {
		logger.info("StudyServiceImpl - getConsentInfoList() - Starts");
		List<ConsentInfoBo> consentInfoList = null;
		try{
			consentInfoList = studyDAO.getConsentInfoList(studyId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - getConsentInfoList() - Error",e);
		}
		logger.info("StudyServiceImpl - getConsentInfoList() - Ends");
		return consentInfoList;
	}




	/**
	 * @author Ravinder
	 * @param Integer : consentInfoId
	 * @return String :SUCCESS or FAILURE
	 *  TThis method used to get the delete the consent information
	 */
	@Override
	public String deleteConsentInfo(Integer consentInfoId) {
		logger.info("StudyServiceImpl - deleteConsentInfo() - Starts");
		String message = null;
		try{
			message = studyDAO.deleteConsentInfo(consentInfoId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - deleteConsentInfo() - Error",e);
		}
		logger.info("StudyServiceImpl - deleteConsentInfo() - Ends");
		return message;
	}




	/**
	 * @author Ravinder
	 * @param Integer studyId
	 * @param int oldOrderNumber
	 * @param int newOrderNumber
	 * @return String SUCCESS or FAILURE
	 * 
	 * This method is used to update the order of an consent info
	 */
	@Override
	public String reOrderConsentInfoList(Integer studyId, int oldOrderNumber,int newOrderNumber) {
		logger.info("StudyServiceImpl - reOrderConsentInfoList() - Starts");
		String message = fdahpStudyDesignerConstants.SUCCESS;
		try{
			message = studyDAO.reOrderConsentInfoList(studyId, oldOrderNumber, newOrderNumber);
		}catch(Exception e){
			logger.error("StudyServiceImpl - reOrderConsentInfoList() - Error",e);
		}
		logger.info("StudyServiceImpl - reOrderConsentInfoList() - Ends");
		return message;
	}

	/**
	 * @author Ravinder
	 * 
	 * 
	 */
	@Override
	public ConsentInfoBo saveOrUpdateConsentInfo(ConsentInfoBo consentInfoBo,SessionObject sessionObject) {
		logger.info("StudyServiceImpl - saveOrUpdateConsentInfo() - Starts");
		ConsentInfoBo updateConsentInfoBo = null;
		try{
			if(consentInfoBo != null){
				if(consentInfoBo.getId() != null){
					updateConsentInfoBo = studyDAO.getConsentInfoById(consentInfoBo.getId());
					updateConsentInfoBo.setModifiedBy(sessionObject.getUserId());
					updateConsentInfoBo.setModifiedOn(fdahpStudyDesignerUtil.getCurrentDateTime());
				}else{
					updateConsentInfoBo = new ConsentInfoBo();
					updateConsentInfoBo.setCreatedBy(sessionObject.getUserId());
					updateConsentInfoBo.setCreatedOn(fdahpStudyDesignerUtil.getCurrentDateTime());
				}
				if(consentInfoBo.getConsentItemType() != null){
					updateConsentInfoBo.setConsentItemType(consentInfoBo.getConsentItemType());
				}
				if(consentInfoBo.getTitle() != null){
					updateConsentInfoBo.setTitle(consentInfoBo.getTitle());
				}
				if(consentInfoBo.getContentType() != null){
					updateConsentInfoBo.setContentType(consentInfoBo.getContentType());
				}
				if(consentInfoBo.getBriefSummary() != null){
					updateConsentInfoBo.setBriefSummary(consentInfoBo.getBriefSummary());
				}
				if(consentInfoBo.getElaborated() != null){
					updateConsentInfoBo.setElaborated(consentInfoBo.getElaborated());
				}
				if(consentInfoBo.getHtmlContent() != null){
					updateConsentInfoBo.setHtmlContent(consentInfoBo.getHtmlContent());
				}
				if(consentInfoBo.getUrl()!= null){
					updateConsentInfoBo.setUrl(consentInfoBo.getUrl());
				}
				if(consentInfoBo.getVisualStep()!=null){
					updateConsentInfoBo.setVisualStep(consentInfoBo.getVisualStep());
				}
				if(consentInfoBo.getOrder() != null){
					updateConsentInfoBo.setOrder(consentInfoBo.getOrder());
				}
				updateConsentInfoBo = studyDAO.saveOrUpdateConsentInfo(updateConsentInfoBo);
			}
			
		}catch(Exception e){
			logger.error("StudyServiceImpl - saveOrUpdateConsentInfo() - Error",e);
		}
		logger.info("StudyServiceImpl - saveOrUpdateConsentInfo() - Ends");
		return updateConsentInfoBo;
	}




	/**
	 * @author Ravinder
	 * @param Integer :ConsentInfoId
	 * @return Object :ConsentInfoBo
	 * 
	 * This method is used to get the consent info object based on consent info id 
	 */
	@Override
	public ConsentInfoBo getConsentInfoById(Integer consentInfoId) {
		logger.info("StudyServiceImpl - getConsentInfoById() - Starts");
		ConsentInfoBo consentInfoBo = null;
		try{
			consentInfoBo = studyDAO.getConsentInfoById(consentInfoId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - getConsentInfoById() - Error",e);
		}
		logger.info("StudyServiceImpl - getConsentInfoById() - Ends");
		return consentInfoBo;
	}

	/**
	 * @author Ravinder
	 * @param studyId
	 * @return int count
	 * 
	 * This method is used to get the last order of an consent info of an study
	 */
	@Override
	public int consentInfoOrder(Integer studyId) {
		int count = 1;
		logger.info("StudyServiceImpl - consentInfoOrder() - Starts");
		try{
			count = studyDAO.consentInfoOrder(studyId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - consentInfoOrder() - Error",e);
		}
		logger.info("StudyServiceImpl - consentInfoOrder() - Ends");
		return count;
	}
	
	/**
	 * @author Ravinder
	 * @param Integer : studyId
	 * @return List : ComprehensionTestQuestions
	 * 
	 * This method is used to get the ComprehensionTest Questions
	 */
	@Override
	public List<ComprehensionTestQuestionBo> getComprehensionTestQuestionList(Integer studyId) {
		logger.info("StudyServiceImpl - getComprehensionTestQuestionList() - Starts");
		List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = null;
		try{
			comprehensionTestQuestionList = studyDAO.getComprehensionTestQuestionList(studyId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - getComprehensionTestQuestionList() - Error",e);
		}
		logger.info("StudyServiceImpl - getComprehensionTestQuestionList() - Starts");
		return comprehensionTestQuestionList;
	}
	
	/**
	 * @author Ravinder
	 * @param Integer :QuestionId
	 * @return Object : ComprehensionTestQuestionBo
	 * 
	 * This method is used to get the ComprehensionTestQuestion of an study
	 */
	@Override
	public ComprehensionTestQuestionBo getComprehensionTestQuestionById(Integer questionId) {
		logger.info("StudyServiceImpl - getComprehensionTestQuestionById() - Starts");
		ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
		try{
			comprehensionTestQuestionBo = studyDAO.getComprehensionTestQuestionById(questionId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - getComprehensionTestQuestionById() - Error",e);
		}
		logger.info("StudyServiceImpl - getComprehensionTestQuestionById() - Ends");
		return comprehensionTestQuestionBo;
	}
	
	/**
	 * @author Ravinder
	 * @param Integer  :questionId
	 * @return String : SUCCESS or FAILURE
	 * 
	 * This method is used to delete the Comprehension Test Question in a study
	 * 
	 */
	@Override
	public String deleteComprehensionTestQuestion(Integer questionId) {
		logger.info("StudyServiceImpl - deleteComprehensionTestQuestion() - Starts");
		String message = null;
		try{
			message = studyDAO.deleteComprehensionTestQuestion(questionId);
		}catch(Exception e){
			logger.error("StudyServiceImpl - deleteComprehensionTestQuestion() - Error",e);
		}
		logger.info("StudyServiceImpl - deleteComprehensionTestQuestion() - Ends");
		return message;
	}
	/*------------------------------------Added By Vivek Start---------------------------------------------------*/
	
	/**
	 * return  eligibility based on user's Study Id
	 * @author Vivek
	 * 
	 * @param studyId, studyId of the {@link StudyBo}
	 * @return {@link EligibilityBo}
	 * @exception Exception
	 */
	@Override
	public EligibilityBo getStudyEligibiltyByStudyId(String studyId) {
		logger.info("StudyServiceImpl - getStudyEligibiltyByStudyId() - Starts");
		EligibilityBo eligibilityBo = null;
		try {
			eligibilityBo = studyDAO.getStudyEligibiltyByStudyId(studyId);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudyEligibiltyByStudyId() - ERROR ", e);
		}
		logger.info("StudyServiceImpl - getStudyEligibiltyByStudyId() - Ends");
		return eligibilityBo;
	}
	
	/**
	 * return  eligibility based on user's Study Id
	 * @author Vivek
	 * 
	 * @param studyId, studyId of the {@link StudyBo}
	 * @return {@link EligibilityBo}
	 * @exception Exception
	 */
	@Override
	public String saveOrUpdateStudyEligibilty(EligibilityBo eligibilityBo) {
		logger.info("StudyServiceImpl - getStudyEligibiltyByStudyId() - Starts");
		String  result = fdahpStudyDesignerConstants.FAILURE;
		try {
			result = studyDAO.saveOrUpdateStudyEligibilty(eligibilityBo);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudyEligibiltyByStudyId() - ERROR ", e);
		}
		logger.info("StudyServiceImpl - getStudyEligibiltyByStudyId() - Ends");
		return result;
	}
	/*------------------------------------Added By Vivek End---------------------------------------------------*/
	/**
	 * return study list
	 * @author Pradyumn
	 * @return the study list
	 */
	@Override
	public List<StudyBo> getStudies(){
		logger.info("StudyServiceImpl - getStudies() - Starts");
		List<StudyBo> studyBOList = null;
		try {
			studyBOList  = studyDAO.getStudies();
		} catch (Exception e) {
			logger.error("StudyServiceImpl - getStudies() - ERROR " , e);
		}
		logger.info("StudyServiceImpl - getStudies() - Ends");
		return studyBOList;
	}





	





	
	
}
