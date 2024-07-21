package org.rebit.auth.disha.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_FILE_UPLOAD")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_FILE_ID")
	private long fileId;
	
	@Column(name = "N_FILE_SIZE")
	private long fileSize;
	
	@Column(name = "N_FILE_NAME")
	private String fileName;
	
	@Column(name = "N_FILE_TYPE")
	private String fileType;
	
	@Lob
	@Column(name = "N_FILE_CONTENT")
	private byte[] fileContent;
	
	@Column(name = "DT_FILE_UPLOAD_TIME")
	private Date fileUploadTime;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="N_PROJECT_ID")
	private ProjectRegistration projectRegistration;
	
}
