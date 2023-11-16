package com.ps20652.DATN.dao;



import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.ReviewReply;

@Repository
public interface ReviewReplyDAO extends JpaRepository<ReviewReply, Integer> {

	List<ReviewReply> findByCustomerFeedbackFeedbackId(int replyId);
	
	List<ReviewReply> findByCustomerUsername(String username);
	
}
