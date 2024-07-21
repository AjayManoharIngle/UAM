package org.rebit.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name = "TBL_APPLICATION_PROPERTIES")
@Entity
public class ApplicationProperties {
		
		@Id
		@Column(name = "S_KEY")
		private String key;
		
		@Column(name = "S_VALUE")
		private String value;
		
}
