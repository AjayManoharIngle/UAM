package org.rebit.auth.disha.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_TEAM_DETAILS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_TEAM_MEMBER_ID")
	private Long teamMemberId;
	
	@Column(name = "N_TEAM_MEMBER_NAME")
	private String teamMemberName;
	
	@Column(name = "N_TEAM_MEMBER_ROLE")
	private String teamMemberRole;
	
	@Column(name = "N_TEAM_MEMBER_EMAIL")
	private String teamMemberEmail;
	
	@Column(name = "N_TEAM_MEMBER_CONTACT_NO")
	private String teamMemberContactNo;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="N_PROJECT_ID")
	private ProjectRegistration projectRegistration;
}
