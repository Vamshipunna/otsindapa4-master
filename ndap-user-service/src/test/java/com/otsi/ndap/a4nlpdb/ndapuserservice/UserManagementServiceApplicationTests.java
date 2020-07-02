	package com.otsi.ndap.a4nlpdb.ndapuserservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UserVo;

/*
* @author Praveen
*/

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserManagementServiceApplicationTests {
	/*
	 * @Autowired private MockMvc mockMvc;
	 * 
	 * @Autowired private ObjectMapper objectMapper;
	 * 
	 * @Autowired private UserRepository userRepository;
	 * 
	 * @Test void registrationWorksThroughAllLayers() throws Exception {
	 * 
	 * UserVo user = new UserVo("sai@gmail.com", "local", "saipraveen", 1,
	 * "saipraveen","sai praveen","localhost",true);
	 * mockMvc.perform(post("/api/user/register").contentType("application/json")
	 * .content(objectMapper.writeValueAsString(user))).andExpect(status().
	 * is2xxSuccessful()); User findByUsername =
	 * userRepository.findByUsername("saipraveen"); // UserEntity userEntity =
	 * userRepository.findByName("Zaphod");
	 * System.out.println(findByUsername.getEmail());
	 * assertThat(findByUsername.getEmail()).isEqualTo("sai@gmail.com"); }
	 */

	/*
	 * @Test void deleteByUsername() throws Exception{
	 * 
	 * }
	 */

}
