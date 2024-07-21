package org.rebit.auth.service;

import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.model.ApplicationUser;

public interface ApplicationUserServices {

   public ApplicationUser selectApplicationUserByUsername(String orgUserName,String customizedName,String password);

   public UserMaster getUserDetails(String userName);
   

}