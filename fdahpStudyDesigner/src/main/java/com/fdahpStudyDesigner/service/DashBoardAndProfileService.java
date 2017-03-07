package com.fdahpStudyDesigner.service;

import com.fdahpStudyDesigner.bo.UserBO;

public interface DashBoardAndProfileService {

	public String updateProfileDetails(UserBO userBO, int userId);
	
	public String isEmailValid(String email) throws Exception;
}