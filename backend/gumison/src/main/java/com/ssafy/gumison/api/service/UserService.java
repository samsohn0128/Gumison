package com.ssafy.gumison.api.service;

import com.ssafy.gumison.api.response.UserSearchRes;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
  
  UserSearchRes getUserList(String nickname, int pageNumber);
  
}
